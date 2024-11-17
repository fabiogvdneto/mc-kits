package mc.minera.plugins.model;

import mc.minera.plugins.exception.KitAlreadyExistsException;
import mc.minera.plugins.exception.KitNotFoundException;

public interface KitManager {

    Kit get(String name) throws KitNotFoundException;

    Kit create(String name) throws KitAlreadyExistsException;

    Kit delete(String name) throws KitNotFoundException;

    boolean exists(String name);

}
