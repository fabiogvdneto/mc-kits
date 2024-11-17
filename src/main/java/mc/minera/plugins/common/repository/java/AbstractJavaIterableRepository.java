package mc.minera.plugins.common.repository.java;

import com.google.common.base.Preconditions;
import mc.minera.plugins.common.repository.IterableRepository;
import mc.minera.plugins.common.repository.Repository;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.stream.Stream;

public abstract class AbstractJavaIterableRepository<K, V extends Serializable> implements IterableRepository<K, V> {

    private final Logger logger;
    private final Path directory;

    public AbstractJavaIterableRepository(Logger logger, Path directory) throws IllegalArgumentException {
        Preconditions.checkArgument(!Files.exists(directory) || Files.isDirectory(directory));

        this.logger = logger;
        this.directory = directory;

        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            logger.warning("Could not make directory " + directory);
            logger.warning(e.getMessage());
        }
    }

    protected abstract K dataToKey(V data);

    protected abstract K filenameToKey(String id);

    @Override
    public Collection<K> fetchKeys() {
        try (Stream<Path> files = Files.list(directory)) {
            return files.filter(Files::isRegularFile)
                    .map(file -> file.getFileName().toString())
                    .filter(filename -> filename.endsWith(".ser"))
                    .map(filename -> filename.substring(0, filename.length() - 4))
                    .map(this::filenameToKey)
                    .toList();
        } catch (IOException e) {
            logger.warning("Could not read directory " + directory);
            logger.warning(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public void store(Iterator<V> data) {
        data.forEachRemaining(this::storeOne);
    }

    @Override
    public Iterator<V> fetch() {
        return new Iterator<V>() {
            private final Iterator<K> keys = fetchKeys().iterator();

            @Override
            public boolean hasNext() {
                return keys.hasNext();
            }

            @Override
            public V next() {
                return fetchOne(keys.next());
            }
        };
    }

    @Override
    public void storeOne(V data) {
        select(dataToKey(data)).store(data);
    }

    @Override
    public V fetchOne(K key) {
        return select(key).fetch();
    }

    @Override
    public void deleteOne(K key) {
        select(key).delete();
    }

    private Repository<V> select(K key) {
        return new JavaRepository<V>(logger, directory.resolve(key + ".ser"));
    }

    @Override
    public void delete() {
        try (Stream<Path> stream = Files.walk(directory)) {
            long filesDeleted = stream.takeWhile(path -> {
                try {
                    Files.delete(path);
                    return true;
                } catch (IOException e) {
                    logger.warning("Could not delete file " + path);
                    return false;
                }
            }).count();

            logger.info("Deleted " + filesDeleted + " files inside directory " + directory);
        } catch (IOException e) {
            logger.warning("Could not read directory " + directory);
            logger.warning(e.getMessage());
        }
    }
}
