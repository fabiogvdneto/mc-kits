package mc.minera.plugins.model;

import mc.minera.plugins.exception.KitAlreadyExistsException;
import mc.minera.plugins.exception.KitNotFoundException;

public interface KitManager {

    Kit create(String name) throws KitAlreadyExistsException;

    Kit get(String name) throws KitNotFoundException;

    void delete(String name) throws KitNotFoundException;

    boolean contains(String name);

}
