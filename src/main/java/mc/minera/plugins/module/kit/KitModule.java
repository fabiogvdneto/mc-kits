package mc.minera.plugins.module.kit;

import mc.minera.plugins.KitsModule;
import mc.minera.plugins.exception.KitAlreadyExistsException;
import mc.minera.plugins.exception.KitNotFoundException;
import mc.minera.plugins.model.Kit;
import mc.minera.plugins.model.KitManager;

import java.util.HashMap;
import java.util.Map;

public class KitModule implements KitManager, KitsModule {

    private final Map<String, Kit> kitByName = new HashMap<>();

    @Override
    public Kit create(String name) throws KitAlreadyExistsException {
        name = name.toLowerCase();

        if (kitByName.containsKey(name))
            throw new KitAlreadyExistsException();

        Kit kit = new StandardKit(name);
        kitByName.put(name, kit);
        return kit;
    }

    @Override
    public Kit get(String name) throws KitNotFoundException {
        Kit kit = kitByName.get(name.toLowerCase());

        if (kit == null)
            throw new KitNotFoundException();

        return kit;
    }

    @Override
    public void delete(String name) throws KitNotFoundException {
        Kit kit = kitByName.remove(name.toLowerCase());

        if (kit == null)
            throw new KitNotFoundException();
    }

    @Override
    public boolean contains(String name) {
        return kitByName.containsKey(name.toLowerCase());
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }
}
