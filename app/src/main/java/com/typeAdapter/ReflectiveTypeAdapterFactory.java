package com.typeAdapter;

import android.text.TextUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by hc on 2016/9/26.
 */
public class ReflectiveTypeAdapterFactory implements TypeAdapters.TypeAdapterFactory {


    private int modifiers = Modifier.TRANSIENT | Modifier.STATIC;

    /**
     * 工厂方法创建适配对象
     * @param mson
     * @param type
     * @param <T>
     * @return
     */
    @Override
    public <T> TypeAdapter<T> create(Mson mson, TypeToken<T> type) {
        Class<? super T> raw = type.getRawType();
        //基本类型返回null
        if (!Object.class.isAssignableFrom(raw)) {
            return null;
        }

        TypeAdapter<T> adapter = new Adapter<T>(getBoundFields(mson,type,raw));

        return adapter;
    }

    /**
     * 通过对象类型将所有变量存储起来
     * @param context
     * @param type
     * @param raw
     * @return
     */
    private Map<String, BoundField> getBoundFields(final Mson context, TypeToken<?> type, Class<?> raw) {
        Map<String, BoundField> result = new LinkedHashMap<>();
        if (raw.isInterface()) {
            return result;
        }

        Type declaredType = type.getType();
        while (raw != Object.class) {
            Field[] fields = raw.getDeclaredFields();
            for (Field field : fields) {
                boolean serialize = exclude(field);
                if (!serialize) {
                    continue;
                }

                field.setAccessible(true);
                Type fieldType = Types.resolve(type.getType(), raw, field.getGenericType());
                List<String> fieldNames = getFieldNames(field);
                BoundField previous = null;
                for (int i = 0; i < fieldNames.size(); ++i) {
                    String name = fieldNames.get(i);
                    if (i != 0){
                        serialize = false;
                    }
                    BoundField boundField = createBoundField(context, field, name,
                            TypeToken.get(fieldType),serialize);
                    BoundField replaced = result.put(name, boundField);
                    if (previous == null){
                        previous = replaced;
                    }
                }
                if (previous != null) {
                    throw new IllegalArgumentException(declaredType
                            + " declares multiple JSON fields named " + previous.name);
                }
            }

            type = TypeToken.get(Types.resolve(type.getType(), raw, raw.getGenericSuperclass()));
            raw = type.getRawType();
        }
        return  result;
    }

    /**
     * 通过成员变量封装成对象
     * @param context
     * @param field
     * @param name
     * @param fieldType
     * @param serialize
     * @return
     */
    private BoundField   createBoundField(final Mson context,
                                                                     final Field field, final String name,
                                                                     final TypeToken<?> fieldType, boolean serialize){
        JsonAdapter annotation = field.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> mapped = null;
        final boolean jsonAdapterPresent = mapped != null;
        if (mapped == null){
            mapped = context.getAdapter(fieldType);
        }
        final TypeAdapter<?> typeAdapter = mapped;

        return new BoundField(name,serialize){

            @Override
            boolean putField(Object value) throws IllegalAccessException {
                if (!serialized || value == null){
                    return false;
                }
                Object fieldValue = field.get(value);
                return fieldValue != value;
            }

            @Override
            void put(Map map, String name, Object value) throws IllegalAccessException {
                Object fieldValue =  field.get(value);
                TypeAdapter t = jsonAdapterPresent ? typeAdapter
                        : new TypeAdapterRuntimeTypeWrapper(context, typeAdapter, fieldType.getType());
                //运行时获取类型适配的类进行键值的存储
                t.put(map,name,fieldValue);
            }
        };
    }

    /**
     * 获取成员变量的名称
     * @param f
     * @return
     */
    private List<String> getFieldNames(Field f) {
        TypeAdapters.SerializedName annotation = f.getAnnotation(TypeAdapters.SerializedName.class);
        if (annotation == null) {
            String name = f.getName();
            return Collections.singletonList(name);
        }

        String serializedName = annotation.value();
        String[] alternates = annotation.alternate();
        if (alternates.length == 0) {
            return Collections.singletonList(serializedName);
        }

        List<String> fieldNames = new ArrayList<>(alternates.length + 1);
        fieldNames.add(serializedName);
        for (String alternate : alternates) {
            fieldNames.add(alternate);
        }
        return fieldNames;
    }

    /**
     * 自定义类的适配类
     * @param <T>
     */
    public static final class Adapter<T> extends TypeAdapter<T> {

        private final Map<String, BoundField> boundFields;
        Adapter(Map<String, BoundField> boundFields) {
            this.boundFields = boundFields;
        }

        @Override
        public void put(Map map, String key, T value) {

            Map subMap = new HashMap();
            try{
                for(BoundField field : boundFields.values()){
                    if(field.putField(value)){
                        field.put(subMap,field.name,value);
                    }
                }
            }catch (IllegalAccessException e){
                throw new AssertionError(e);
            }

            if(value == null){
                map.put(key,"null");
                return;
            }
            if(TextUtils.isEmpty(key)){
                map.putAll(subMap);
            }else{
                map.put(key,subMap.toString());
            }
        }
    }

    /**
     * 对变量进行简单的封装
     */
    static abstract class BoundField {
        final String name;
        final boolean serialized;
        protected BoundField(String name,boolean serialized) {
            this.name = name;
            this.serialized = serialized;
        }
        abstract boolean putField(Object value) throws IllegalAccessException;
        abstract void put(Map map, String name, Object value) throws IllegalAccessException;

    }


    private boolean exclude(Field f) {
        return !excludeClass(f.getType()) && !excludeField(f);
    }

    private boolean excludeField(Field field) {
        if ((modifiers & field.getModifiers()) != 0) {
            return true;
        }
        if (isAnonymousOrLocal(field.getType())) {
            return true;
        }
        return false;
    }

    public boolean excludeClass(Class<?> clazz) {
        if (isAnonymousOrLocal(clazz)) {
            return true;
        }

        return false;
    }
    private boolean isAnonymousOrLocal(Class<?> clazz) {
        return !Enum.class.isAssignableFrom(clazz)
                && (clazz.isAnonymousClass() || clazz.isLocalClass());
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    public @interface JsonAdapter {
        Class<?> value();
    }


}
