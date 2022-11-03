package com.source.rworkflow.common.util;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Comparer {
    public static <EntityT, RequestT> CompareResult<EntityT, RequestT> compare(List<EntityT> entities, List<RequestT> requests, Comparable<EntityT, RequestT> comparable) {
        final var result = new CompareResult<EntityT, RequestT>();

        result.createRequests.addAll(requests);
        result.deleteRequests.addAll(entities);

        entities.forEach(entity -> {
            requests.forEach(request -> {
                if (comparable.equal(entity, request)) {
                    result.createRequests.remove(request);
                    result.ignoreRequests.add(entity);
                    result.deleteRequests.remove(entity);
                }
            });
        });

        return result;
    }

    @FunctionalInterface
    public interface Comparable<M, R> {
        boolean equal(M model, R request);
    }

    @Getter
    public static class CompareResult<EntityT, RequestT> {
        private final List<RequestT> createRequests = new ArrayList<>();
        private final List<EntityT> ignoreRequests = new ArrayList<>();
        private final List<EntityT> deleteRequests = new ArrayList<>();

        public List<EntityT> execute(final Function<RequestT, EntityT> createFunction, final Consumer<EntityT> deleteFunction) {
            List<EntityT> result = new ArrayList<>();

            result.addAll(createRequests.stream()
                    .map(createFunction)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toUnmodifiableList()));

            result.addAll(ignoreRequests);

            deleteRequests.forEach(deleteFunction);

            return result;
        }
    }

}
