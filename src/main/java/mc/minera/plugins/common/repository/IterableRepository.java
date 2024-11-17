package mc.minera.plugins.common.repository;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

public interface IterableRepository<K, V> extends Repository<Iterator<V>>, Iterable<V> {

    void storeOne(V data);

    V fetchOne(K key);

    void deleteOne(K key);

    Collection<K> fetchKeys();

    @Override
    @NotNull
    default Iterator<V> iterator() {
        return fetch();
    }
}
