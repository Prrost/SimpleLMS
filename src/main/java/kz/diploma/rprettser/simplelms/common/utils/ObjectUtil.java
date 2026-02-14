package kz.diploma.rprettser.simplelms.common.utils;

import java.util.function.Consumer;

public class ObjectUtil {
    public static <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    public static <T> void ifNullThrow(T value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
