package mc.minera.plugins.repository.java;

import mc.minera.plugins.common.repository.java.AbstractJavaKeyedRepository;
import mc.minera.plugins.repository.KitRepository;
import mc.minera.plugins.repository.data.KitData;

import java.nio.file.Path;

public class JavaKitRepository extends AbstractJavaKeyedRepository<String, KitData> implements KitRepository {

    public JavaKitRepository(Path directory) {
        super(directory);
    }

    @Override
    protected String getKey(KitData data) {
        return data.name().toLowerCase();
    }

    @Override
    public String getKeyFromString(String id) {
        return id;
    }
}
