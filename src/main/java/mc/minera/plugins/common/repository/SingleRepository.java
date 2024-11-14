package mc.minera.plugins.common.repository;

import java.io.IOException;

public interface SingleRepository<D> extends Repository {

    D fetch() throws IOException;

    void store(D data) throws IOException;

}
