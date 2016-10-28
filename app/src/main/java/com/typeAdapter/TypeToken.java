package com.typeAdapter;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by hc on 2016/9/24.
 */
public class TypeToken<T> {
    final Class<? super T> rawType;
    final Type type;

    public  TypeToken(){
        this.type = getSuperclassTypeParameter(getClass());
        this.rawType = (Class<? super T>) Types.getRawType(this.type);
    }

    public TypeToken(Type type) {
        this.type = Types.canonicalize(checkNotNull(type));
        this.rawType = (Class<? super T>) Types.getRawType(this.type);
    }

    public static <T> TypeToken<T> get(Class<T> type) {
        return new TypeToken<T>(type);
    }

    public static TypeToken<?> get(Type type) {
        return new TypeToken<Object>(type);
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public final Class<? super T> getRawType() {
        return rawType;
    }

    public final Type getType() {
        return type;
    }

    @Override
    public final String toString() {
        return type instanceof Class ? ((Class<?>) type).getName() : type.toString();
    }

    public static <T> T checkNotNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }
}
