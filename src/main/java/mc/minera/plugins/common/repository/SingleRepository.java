package mc.minera.plugins.common.repository;

public interface SingleRepository<V> extends Repository {

    void store(V data) throws Exception;

    V fetch() throws Exception;

}
