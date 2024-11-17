package mc.minera.plugins.common.repository;

public interface Repository<V> {

    void store(V data);

    V fetch();

    void delete();

}
