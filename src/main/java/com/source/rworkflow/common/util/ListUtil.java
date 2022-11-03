package com.source.rworkflow.common.util;

import com.google.common.collect.Sets;

import java.util.List;

public class ListUtil {
    public static boolean hasDuplicateElement(final List<?> list) {
        final var set = Sets.newHashSet(list);
        return set.size() != list.size();
    }
}
