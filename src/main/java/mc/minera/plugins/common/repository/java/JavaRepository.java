package mc.minera.plugins.common.repository.java;

import mc.minera.plugins.common.repository.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class JavaRepository<V extends Serializable> implements Repository<V> {

    private final Logger logger;
    private final Path file;

    public JavaRepository(Logger logger, Path file) {
        this.logger = logger;
        this.file = file;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V fetch() {
        if (Files.isRegularFile(file)) {
            try (ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(file))) {
                return (V) stream.readObject();
            } catch (ClassNotFoundException e) {
                // This should never happen...
            } catch (IOException e) {
                logger.warning("Could not read object from file " + file);
                logger.warning(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public void store(V data) {
        try (ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(file))) {
            stream.writeObject(data);
        } catch (IOException e) {
            logger.warning("Could not write object to file " + file);
            logger.warning(e.getMessage());
        }
    }

    @Override
    public void delete() {
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            logger.warning("Could not delete file " + file);
            logger.warning(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return """
                type:java-object-serialization
                location:%s
                """.formatted(file);
    }
}
