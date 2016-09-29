package com.moretv.typeAdapter;

import com.moretv.Mson;

/**
 * Created by hc on 2016/9/24.
 */
public interface TypeAdapterFactory {

    <T> TypeAdapter<T> create(Mson mson, TypeToken<T> typeToken);
}
