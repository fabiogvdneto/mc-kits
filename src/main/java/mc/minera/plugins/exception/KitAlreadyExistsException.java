package mc.minera.plugins.exception;

public class KitAlreadyExistsException extends RuntimeException {
    public KitAlreadyExistsException(String kitName) {
        super("a kit with the same name (" + kitName + ") already exists");
    }
}
