package com.source.rworkflow.common.util;

import java.util.List;
import java.util.Set;

public class ListUtil {
    public static boolean hasDuplicateElement(final List<?> list) {
        final var set = Set.copyOf(list);
        return set.size() != list.size();
    }

    public static <T> List<T> removeDuplicateElement(final List<T> list) {
        final var set = Set.copyOf(list);
        return List.copyOf(set);
    }
}
