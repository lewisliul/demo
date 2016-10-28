package com.typeAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by hc on 2016/9/24.
 */
public class Mson {
    private static final TypeToken<?> NULL_KEY_SURROGATE = new TypeToken<Object>() {};
    private final Map<TypeToken<?>, TypeAdapter<?>> typeTokenCache = new ConcurrentHashMap<>();
    private final List<TypeAdapters.TypeAdapterFactory> factories;

    public Mson(){
        List<TypeAdapters.TypeAdapterFactory> factories = new ArrayList<>();
        factories.add(TypeAdapters.OBJECT_FACTORY);
        factories.add(TypeAdapters.STRING_FACTORY);
        factories.add(TypeAdapters.INTEGER_FACTORY);
        factories.add(TypeAdapters.BOOLEAN_FACTORY);
        factories.add(TypeAdapters.BYTE_FACTORY);
        factories.add(TypeAdapters.SHORT_FACTORY);
        factories.add(TypeAdapters.CHARACTER_FACTORY);
        factories.add(TypeAdapters.LONG_FACTORY);
        factories.add(TypeAdapters.DOUBLE_FACTORY);
        factories.add(TypeAdapters.FLOAT_FACTORY);
        factories.add(TypeAdapters.DATE_FACTORY);
        factories.add(TypeAdapters.CALENDAR_FACTORY);
        factories.add(TypeAdapters.ENUM_FACTORY);
        factories.add(TypeAdapters.CollectionAdapterFactory);
        factories.add(TypeAdapters.MapTypeAdapterFactory);

        factories.add(new ReflectiveTypeAdapterFactory());

        this.factories = Collections.unmodifiableList(factories);
    }

    public <T> TypeAdapter<T> getAdapter(Class<T> type) {
        return getAdapter(TypeToken.get(type));
    }

    /**
     * 获取适配器
     * @param type
     * @param <T>
     * @return
     */
    public <T> TypeAdapter<T> getAdapter(TypeToken<T> type) {
        TypeAdapter<?> cached = typeTokenCache.get(type == null ? NULL_KEY_SURROGATE : type);
        if (cached != null) {
            return (TypeAdapter<T>) cached;
        }
        try {
            for (TypeAdapters.TypeAdapterFactory factory : factories) {
                TypeAdapter<T> candidate = factory.create(this, type);
                if (candidate != null) {
                    typeTokenCache.put(type, candidate);
                    return candidate;
                }
            }
            throw new IllegalArgumentException("MSON cannot handle " + type);
        } finally {
        }
    }

    /**
     * 对象转MAP
     * @param src
     * @return
     */
    public Map<String,String> beanToMap(Object src){
        Map<String,String> map = new HashMap<>();
        if(src != null){
            toMap(src,src.getClass(),map);
        }
        return map;
    }

    private void toMap(Object src, Type typeOfSrc, Map map){
        TypeAdapter<?> adapter = getAdapter(TypeToken.get(typeOfSrc));

        ((TypeAdapter<Object>) adapter).put(map,"",src);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("factories:{").append(factories)
                .append("}")
                .toString();
    }
}
