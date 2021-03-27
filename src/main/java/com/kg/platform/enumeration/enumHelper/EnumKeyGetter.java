package com.kg.platform.enumeration.enumHelper;

public interface EnumKeyGetter<T extends Enum<T>, k> {

    k getKey(T value);

}
