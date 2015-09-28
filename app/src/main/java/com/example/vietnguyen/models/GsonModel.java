package com.example.vietnguyen.models;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by viet on 9/28/2015.
 * copy from quicker
 */
public class GsonModel<X> implements ParameterizedType {
    private Class<?> wrapped;

    public GsonModel(Class<X> wrapped) {
        this.wrapped = wrapped;
    }

    public Type[] getActualTypeArguments() {
        return new Type[]{this.wrapped};
    }

    public Type getRawType() {
        return List.class;
    }

    public Type getOwnerType() {
        return null;
    }
}
