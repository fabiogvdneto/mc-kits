package mc.minera.plugins.repository.java;

import mc.minera.plugins.common.repository.java.AbstractJavaIterableRepository;
import mc.minera.plugins.repository.KitRepository;
import mc.minera.plugins.repository.data.KitData;

import java.nio.file.Path;
import java.util.logging.Logger;

public class JavaKitRepository extends AbstractJavaIterableRepository<String, KitData> implements KitRepository {

    public JavaKitRepository(Logger logger, Path directory) {
        super(logger, directory);
    }

    @Override
    protected String dataToKey(KitData data) {
        return data.name().toLowerCase();
    }

    @Override
    public String filenameToKey(String id) {
        return id;
    }
}
