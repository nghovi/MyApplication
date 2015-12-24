package com.nguyenhoangviet.vietnguyen.models;

import com.activeandroid.serializer.TypeSerializer;

import java.util.Date;

/**
 * Created by viet on 11/27/2015.
 */
public class UtilDateSerializer extends TypeSerializer {
    @Override
    public Class<?> getDeserializedType() {
        return Date.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return Long.class;
    }

    @Override
    public Long serialize(Object data) {
        if (data == null) {
            return null;
        }

        return ((Date) data).getTime();
    }

    @Override
    public Date deserialize(Object data) {
        if (data == null) {
            return null;
        }

        return new Date((Long) data);
    }
}
