package org.shak.bn.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Prefix<T> {
    final T value;
    final Prefix<T> parent;

    Prefix(Prefix<T> parent, T value) {
        this.parent = parent;
        this.value = value;
    }

    // put the whole prefix into given collection
    private <C extends Collection<T>> C addTo(C collection) {
        if (parent != null)
            parent.addTo(collection);
        collection.add(value);
        return collection;
    }

    private static <T, C extends Collection<T>> Stream<C> comb(
            List<? extends Collection<T>> values, int offset, Prefix<T> prefix,
            Supplier<C> supplier) {
        if (offset == values.size() - 1)
            return values.get(offset).stream()
                    .map(e -> new Prefix<>(prefix, e).addTo(supplier.get()));
        return values.get(offset).stream()
                .flatMap(e -> comb(values, offset + 1, new Prefix<>(prefix, e), supplier));
    }

    public static <T, C extends Collection<T>> Stream<C> ofCombinations(
            Collection<? extends Collection<T>> values, Supplier<C> supplier) {
        if (values.isEmpty())
            return Stream.empty();
        return comb(new ArrayList<>(values), 0, null, supplier);
    }
}
