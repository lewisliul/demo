package com.moretv.typeAdapter;

import java.util.Map;

/**
 * Created by hc on 2016/9/24.
 */
public abstract class TypeAdapter<T> {
    public abstract void put(Map map,String key, T value);
}
