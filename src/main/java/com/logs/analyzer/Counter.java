package com.logs.analyzer;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Counter<T> {
    private final T value;
    private int count;

    public Counter(T object) {
        this.value = object;
        this.count = 0;
    }

    public Counter(T object, int count) {
        this.value = object;
        this.count = count;
    }

    public int increase() {
        this.count += 1;
        return this.count;
    }
}
