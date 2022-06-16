package com.analyzer.logs;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ObjectCounter<T> {
    private final T object;
    private int count;

    public ObjectCounter(T object) {
        this.object = object;
        this.count = 0;
    }

    public ObjectCounter(T object, int count) {
        this.object = object;
        this.count = count;
    }

    public int increase() {
        this.count += 1;
        return this.count;
    }
}
