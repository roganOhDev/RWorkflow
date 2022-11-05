package com.source.rworkflow.common.util;

import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor
public class Triple<F, S, T extends ArrayList<?>> {
    private Map<F, Map<S, T>> value;

//    public Triple(final F first, final S second, final T third) {
//        final var inner = Map.of(second, third);
//        this.value = Map.of(first, inner);
//    }

    public Map<S, T> get(final F first) {
        return this.value.get(first);
    }

    public T get(final F first, final S second){
        return this.value.get(first).get(second);
    }

    public void put(final F first, final S second, T third){
        if (this.value.containsKey(first)) {
            if (this.value.get(first).containsKey(second)) {
                this.value.get(first).get(second).add(third.get(0));

            } else {
                this.value.get(first).put(second, third);
            }
        } else {
            this.value.put(first, Map.of(second, third));
        }
    }
}
