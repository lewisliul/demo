package com.moretv.typeAdapter;

import android.text.TextUtils;

import com.moretv.Mson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
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
public class ReflectiveTypeAdapterFactory implements TypeAdapterFactory {

    private final Excluder excluder;

    public ReflectiveTypeAdapterFactory(Excluder excluder){
        this.excluder = excluder;

    }

    @Override
    public <T> TypeAdapter<T> create(Mson mson, TypeToken<T> type) {
        Class<? super T> raw = type.getRawType();
        if (!Object.class.isAssignableFrom(raw)) {
            return null; // it's a primitive!
        }

        return new Adapter<T>(getBoundFields(mson,type,raw));
    }

    private Map<String, BoundField> getBoundFields(final Mson context, TypeToken<?> type, Class<?> raw) {
        Map<String, BoundField> result = new LinkedHashMap<>();
        if (raw.isInterface()) {
            return result;
        }

        Type declaredType = type.getType();
        while (raw != Object.class) {
            Field[] fields = raw.getDeclaredFields();
            for (Field field : fields) {

                boolean serialize = excludeField(field);
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

    private ReflectiveTypeAdapterFactory.BoundField createBoundField(final Mson context,
                                                                     final Field field, final String name,
                                                                     final TypeToken<?> fieldType, boolean serialize){
        JsonAdapter annotation = field.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> mapped = null;
        final boolean jsonAdapterPresent = mapped != null;
        if (mapped == null){
            mapped = context.getAdapter(fieldType);
        }
        final TypeAdapter<?> typeAdapter = mapped;

        return new ReflectiveTypeAdapterFactory.BoundField(name,serialize){

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
                map.put(key,subMap);
            }


        }
    }

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


    public boolean excludeField(Field f) {
        return excludeField(f,excluder);
    }

    static boolean excludeField(Field f, Excluder excluder) {
        return !excluder.excludeClass(f.getType()) && !excluder.excludeField(f);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    public @interface JsonAdapter {

        /** Either a {@link TypeAdapter} or {@link TypeAdapterFactory}. */
        Class<?> value();

    }
}
