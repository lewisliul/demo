package com.moretv.typeAdapter;


import com.moretv.Mson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by hc on 2016/9/27.
 */
public class Excluder implements TypeAdapterFactory {

    private static final double IGNORE_VERSIONS = -1.0d;
    public static final Excluder DEFAULT = new Excluder();

    private double version = IGNORE_VERSIONS;
    private int modifiers = Modifier.TRANSIENT | Modifier.STATIC;
    private boolean serializeInnerClasses = true;
    private boolean requireExpose;

    @Override
    public <T> TypeAdapter<T> create(Mson mson, TypeToken<T> typeToken) {
        return null;
    }

    public boolean excludeField(Field field) {
        if ((modifiers & field.getModifiers()) != 0) {
            return true;
        }

        if (version != Excluder.IGNORE_VERSIONS
                && !isValidVersion(field.getAnnotation(Since.class), field.getAnnotation(Until.class))) {
            return true;
        }

        if (field.isSynthetic()) {
            return true;
        }

        if (!serializeInnerClasses && isInnerClass(field.getType())) {
            return true;
        }

        if (isAnonymousOrLocal(field.getType())) {
            return true;
        }

        return false;
    }

    public boolean excludeClass(Class<?> clazz) {
        if (version != Excluder.IGNORE_VERSIONS
                && !isValidVersion(clazz.getAnnotation(Since.class), clazz.getAnnotation(Until.class))) {
            return true;
        }

        if (!serializeInnerClasses && isInnerClass(clazz)) {
            return true;
        }

        if (isAnonymousOrLocal(clazz)) {
            return true;
        }

        return false;
    }

    private boolean isValidVersion(Since since, Until until) {
        return isValidSince(since) && isValidUntil(until);
    }

    private boolean isValidSince(Since annotation) {
        if (annotation != null) {
            double annotationVersion = annotation.value();
            if (annotationVersion > version) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidUntil(Until annotation) {
        if (annotation != null) {
            double annotationVersion = annotation.value();
            if (annotationVersion <= version) {
                return false;
            }
        }
        return true;
    }

    private boolean isAnonymousOrLocal(Class<?> clazz) {
        return !Enum.class.isAssignableFrom(clazz)
                && (clazz.isAnonymousClass() || clazz.isLocalClass());
    }

    private boolean isInnerClass(Class<?> clazz) {
        return clazz.isMemberClass() && !isStatic(clazz);
    }

    private boolean isStatic(Class<?> clazz) {
        return (clazz.getModifiers() & Modifier.STATIC) != 0;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    public @interface Since {
        double value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    public @interface Until {
        double value();
    }
}
