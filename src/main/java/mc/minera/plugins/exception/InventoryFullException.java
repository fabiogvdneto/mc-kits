package mc.minera.plugins.exception;

public class InventoryFullException extends RuntimeException {

    private final int spaceRequired;
    private final int spaceAvailable;

    public InventoryFullException(int spaceRequired, int spaceAvailable) {
        this.spaceRequired = spaceRequired;
        this.spaceAvailable = spaceAvailable;
    }

    public int getSpaceRequired() {
        return spaceRequired;
    }

    public int getSpaceAvailable() {
        return spaceAvailable;
    }
}
