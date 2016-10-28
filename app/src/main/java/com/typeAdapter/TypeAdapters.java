package com.typeAdapter;

import android.text.TextUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by hc on 2016/9/24.
 */
public final class TypeAdapters {

    private static final DateFormat enUsFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);

    public static final TypeAdapter<String> STRING = new TypeAdapter<String>(){
        @Override
        public void put(Map map,String key, String value) {
            if("".equals(value)){
                map.put(getObjectKey(key,value),"\"\"");
            }else{
                map.put(getObjectKey(key,value),value);
            }
        }
    };

    public static final TypeAdapter<Number> INTEGER = new TypeAdapter<Number>() {
        @Override
        public void put(Map map,String key, Number value) {
            map.put(getObjectKey(key,value),String.valueOf(value));
        }
    };

    public static final TypeAdapter<Boolean> BOOLEAN = new TypeAdapter<Boolean>() {
        @Override
        public void put(Map map,String key, Boolean value) {
            map.put(getObjectKey(key,value),String.valueOf(value));
        }
    };

    public static final TypeAdapter<Number> BYTE = new TypeAdapter<Number>() {
        @Override
        public void put(Map map,String key, Number value) {
            map.put(getObjectKey(key,value),String.valueOf(value));
        }
    };

    public static final TypeAdapter<Number> SHORT = new TypeAdapter<Number>() {
        @Override
        public void put(Map map,String key, Number value) {
            map.put(getObjectKey(key,value),String.valueOf(value));
        }
    };

    public static final TypeAdapter<Character> CHARACTER = new TypeAdapter<Character>() {
        @Override
        public void put(Map map,String key, Character value) {
            map.put(getObjectKey(key,value),String.valueOf(value));
        }
    };

    public static final TypeAdapter<Number> LONG = new TypeAdapter<Number>() {
        @Override
        public void put(Map map,String key, Number value) {
            map.put(getObjectKey(key,value),String.valueOf(value));
        }
    };

    public static final TypeAdapter<Number> DOUBLE = new TypeAdapter<Number>() {
        @Override
        public void put(Map map,String key, Number value) {
            map.put(getObjectKey(key,value),String.valueOf(value));
        }
    };

    public static final TypeAdapter<Number> FLOAT = new TypeAdapter<Number>() {
        @Override
        public void put(Map map,String key, Number value) {

            map.put(getObjectKey(key,value),String.valueOf(value));
        }
    };

    public static final TypeAdapter<Date> DATE = new TypeAdapter<Date>(){
        @Override
        public void put(Map map,String key, Date value) {
            if(value == null){
                return;
            }
            String dateFormatAsString = enUsFormat.format(value);
            map.put(getObjectKey(key,value),dateFormatAsString);
        }
    };

    public static final TypeAdapter<Calendar> CALENDAR = new TypeAdapter<Calendar>() {
        @Override
        public void put(Map map,String key,Calendar value) {
            if(value == null){
                return ;
            }
            StringBuffer sb = new StringBuffer();
            sb.append(value.get(Calendar.YEAR) + ":").append(value.get(Calendar.MONTH) + ":")
                    .append(value.get(Calendar.DAY_OF_MONTH) + " ").append(value.get(Calendar.HOUR_OF_DAY) + ":")
                    .append(value.get(Calendar.MINUTE) + ":").append(value.get(Calendar.SECOND));
            map.put(getObjectKey(key,value) , sb.toString());
        }
    };

    private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
        private final Map<String, T> nameToConstant = new HashMap<String, T>();
        private final Map<T, String> constantToName = new HashMap<T, String>();

        public EnumTypeAdapter(Class<T> classOfT) {
            try {
                for (T constant : classOfT.getEnumConstants()) {
                    String name = constant.name();
                    SerializedName annotation = classOfT.getField(name).getAnnotation(SerializedName.class);
                    if (annotation != null) {
                        name = annotation.value();
                        for (String alternate : annotation.alternate()) {
                            nameToConstant.put(alternate, constant);
                        }
                    }
                    nameToConstant.put(name, constant);
                    constantToName.put(constant, name);
                }
            } catch (NoSuchFieldException e) {
                throw new AssertionError(e);
            }
        }

        @Override
        public void put(Map map,String key, T value) {
            String emunValue =  value == null ? "" : constantToName.get(value);
            map.put(getObjectKey(key,value),emunValue);
        }
    };

    private static final class ObjectTypeAdapter<T extends Object> extends TypeAdapter<T>{

        private final Mson mson;
        ObjectTypeAdapter(Mson mson) {
            this.mson = mson;
        }

        @Override
        public void put(Map map,String key,T value) {
            if(value == null){
                return;
            }
            TypeAdapter<Object> typeAdapter = (TypeAdapter<Object>) mson.getAdapter(value.getClass());
            if (typeAdapter instanceof ObjectTypeAdapter) {
                return;
            }

            typeAdapter.put(map,key,value);

        }
    }

    private static final class CollectionAdapter<E> extends TypeAdapter<Collection<E>>{

        @Override
        public void put(Map map, String key, Collection<E> value) {

            if(value == null){
                map.put(key,"null");
                return;
            }
            List list = new ArrayList();
            for(E element : value){
                list.add(element);
            }
            map.put(getObjectKey(key,value),list.toString());
        }
    }

    private static final class MapAdapter<K, V> extends TypeAdapter<Map<K, V>> {
        @Override
        public void put(Map map, String key, Map<K, V> value) {
            if (TextUtils.isEmpty(key)){
                map.putAll(value);
                return ;
            }
            if(value == null){
                map.put(key,"null");
                return;
            }

            map.put(key, value.toString());

        }
    };

    public static final TypeAdapterFactory INTEGER_FACTORY = newFactory(int.class, Integer.class, INTEGER);
    public static final TypeAdapterFactory STRING_FACTORY = newFactory(String.class, STRING);
    public static final TypeAdapterFactory BOOLEAN_FACTORY = newFactory(boolean.class, Boolean.class, BOOLEAN);
    public static final TypeAdapterFactory BYTE_FACTORY = newFactory(byte.class, Byte.class, BYTE);
    public static final TypeAdapterFactory SHORT_FACTORY = newFactory(short.class, Short.class, SHORT);
    public static final TypeAdapterFactory CHARACTER_FACTORY = newFactory(char.class, Character.class, CHARACTER);
    public static final TypeAdapterFactory LONG_FACTORY = newFactory(long.class, Long.class, LONG);
    public static final TypeAdapterFactory DOUBLE_FACTORY = newFactory(double.class, Double.class, DOUBLE);
    public static final TypeAdapterFactory FLOAT_FACTORY = newFactory(float.class, Float.class, FLOAT);
    public static final TypeAdapterFactory DATE_FACTORY = newFactory(Date.class, DATE);
    public static final TypeAdapterFactory CALENDAR_FACTORY = newFactoryForMultipleTypes(Calendar.class, GregorianCalendar.class, CALENDAR);

    public static final TypeAdapterFactory ENUM_FACTORY = new TypeAdapterFactory() {
        @Override
        public <T> TypeAdapter<T> create(Mson mson, TypeToken<T> typeToken) {
            Class<? super T> rawType = typeToken.getRawType();
            if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
                return null;
            }
            if (!rawType.isEnum()) {
                rawType = rawType.getSuperclass(); // handle anonymous subclasses
            }
            return (TypeAdapter<T>) new EnumTypeAdapter(rawType);
        }
    };
    public static final TypeAdapterFactory OBJECT_FACTORY = new TypeAdapterFactory() {
        @Override
        public <T> TypeAdapter<T> create(Mson mson, TypeToken<T> typeToken) {
            if (typeToken.getRawType() == Object.class) {
                return (TypeAdapter<T>) new ObjectTypeAdapter(mson);
            }
            return null;
        }
    };
    public static final TypeAdapterFactory CollectionAdapterFactory = new TypeAdapterFactory() {
        @Override
        public <T> TypeAdapter<T> create(Mson mson, TypeToken<T> typeToken) {
            Class<? super T> rawType = typeToken.getRawType();
            if (!Collection.class.isAssignableFrom(rawType)) {
                return null;
            }

            TypeAdapter<T> result = new CollectionAdapter();

            return  result;
        }
    };
    public static final TypeAdapterFactory MapTypeAdapterFactory = new TypeAdapterFactory(){

        @Override
        public <T> TypeAdapter<T> create(Mson mson, TypeToken<T> typeToken) {

            Class<? super T> rawType = typeToken.getRawType();
            if (!Map.class.isAssignableFrom(rawType)) {
                return null;
            }
            TypeAdapter<T> result = new MapAdapter();

            return result;
        }
    };


    public static <TT> TypeAdapterFactory newFactory(final Class<TT> type, final TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(Mson mson, TypeToken<T> typeToken) {
                return typeToken.getRawType() == type ? (TypeAdapter<T>) typeAdapter : null;
            }

            @Override
            public String toString() {
                return "Factory[type=" + type.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactory(final Class<TT> unboxed, final Class<TT> boxed, final TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory() {
            @Override
            public <T> TypeAdapter<T> create(Mson mson, TypeToken<T> typeToken) {
                Class<? super T> rawType = typeToken.getRawType();
                return (rawType == unboxed || rawType == boxed) ? (TypeAdapter<T>) typeAdapter : null;
            }

            @Override
            public String toString() {
                return "Factory[type=" + boxed.getName()
                        + "+" + unboxed.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(final Class<TT> base, final Class<? extends TT> sub, final TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory(){
            @Override
            public <T> TypeAdapter<T> create(Mson mson, TypeToken<T> typeToken) {
                Class<? super T> rawType = typeToken.getRawType();
                return (rawType == base || rawType == sub) ? (TypeAdapter<T>) typeAdapter : null;
            }

            @Override
            public String toString() {
                return "Factory[type=" + base.getName()
                        + "+" + sub.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }


    /**
     * 根据key和value创建新的key
     * @param key
     * @param value
     * @return
     */
    private static String getObjectKey(String key,Object value){
        if(TextUtils.isEmpty(key)){
            String keyBasic = value.getClass().getName();
            int indexOf  = keyBasic.lastIndexOf(".");
            String keySub = keyBasic.substring(indexOf + 1);
            String keyObject = "bi_" + "(" + keySub + ")";
            return keyObject;
        }
        return key;
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD})
    public @interface SerializedName {
        String value();
        String[] alternate() default {};
    }

    public interface TypeAdapterFactory {
        <T> TypeAdapter<T> create(Mson mson, TypeToken<T> typeToken);
    }
}
