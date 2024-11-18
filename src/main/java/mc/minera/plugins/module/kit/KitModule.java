package mc.minera.plugins.module.kit;

import mc.minera.plugins.KitsModule;
import mc.minera.plugins.KitsPlugin;
import mc.minera.plugins.common.Plugins;
import mc.minera.plugins.exception.KitAlreadyExistsException;
import mc.minera.plugins.exception.KitNotFoundException;
import mc.minera.plugins.model.Kit;
import mc.minera.plugins.model.KitManager;
import mc.minera.plugins.repository.KitRepository;
import mc.minera.plugins.repository.data.KitData;
import mc.minera.plugins.repository.java.JavaKitRepository;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class KitModule implements KitManager, KitsModule {

    private final KitsPlugin plugin;
    private final KitRepository repository;
    private final Map<String, Kit> cache = new HashMap<>();

    private BukkitTask autosave;

    public KitModule(KitsPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
        this.repository = new JavaKitRepository(plugin.getDataPath().resolve("kits"));
    }

    @Override
    public Kit create(String name) throws KitAlreadyExistsException {
        if (exists(name))
            throw new KitAlreadyExistsException(name);

        Kit kit = new StandardKit(name);
        cache.put(key(name), kit);
        return kit;
    }

    @Override
    public Kit get(String name) throws KitNotFoundException {
        Kit kit = cache.get(key(name));

        if (kit == null)
            throw new KitNotFoundException(name);

        return kit;
    }

    @Override
    public Kit delete(String name) throws KitNotFoundException {
        // Replace the value by null instead of removing it,
        // so that it can be deleted from the repository later.
        Kit kit = cache.put(key(name), null);

        if (kit == null)
            throw new KitNotFoundException(name);

        return kit;
    }

    @Override
    public boolean exists(String name) {
        return cache.get(key(name)) != null;
    }

    @Override
    public void enable() {
        disable();
        load();

        long autosaveMinutes = (long) plugin.getSettings().getKitAutosaveMinutes() * 60 * 20;
        this.autosave = Plugins.sync(plugin, this::saveAsync, autosaveMinutes, autosaveMinutes);
    }

    @Override
    public void disable() {
        if (autosave != null) {
            autosave.cancel();
            autosave = null;
            save();
            cache.clear();
        }
    }

    private String key(String name) {
        return name.toLowerCase();
    }

    private String key(Kit kit) {
        return key(kit.getName());
    }

    /* ---- Repository Operations ---- */

    private void load() {
        try {
            plugin.getLogger().info("Loading kits...");
            for (String key : repository.fetchKeys()) {
                KitData data = repository.fetchOne(key);

                if (data != null) {
                    StandardKit kit = new StandardKit(data);
                    cache.put(key(kit), kit);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning("An error occurred while trying to load kits.");
            plugin.getLogger().warning(e.getMessage());
        }
    }

    private void save() {
        plugin.getLogger().info("Saving kits...");

        Iterator<Map.Entry<String, Kit>> it = cache.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Kit> entry = it.next();

            if (entry.getValue() == null) {
                try {
                    repository.deleteOne(entry.getKey());
                } catch (Exception e) {
                    plugin.getLogger().warning("An error occurred while trying to delete a kit.");
                    plugin.getLogger().warning(e.getMessage());
                }
                it.remove();
                continue;
            }

            StandardKit kit = (StandardKit) entry.getValue();
            kit.purge();
            try {
                repository.storeOne(kit.memento());
            } catch (Exception e) {
                plugin.getLogger().warning("An error occurred while trying to save a kit.");
                plugin.getLogger().warning(e.getMessage());
            }
        }
    }

    private void saveAsync() {
        plugin.getLogger().info("Saving kits...");

        Map<String, Kit> data = Map.copyOf(cache);

        Plugins.async(plugin, () -> {
            List<String> deleted = new LinkedList<>();

            for (Map.Entry<String, Kit> entry : data.entrySet()) {
                if (entry.getValue() == null) {
                    try {
                        repository.deleteOne(entry.getKey());
                    } catch (Exception e) {
                        plugin.getLogger().warning("An error occurred while trying to delete a kit.");
                        plugin.getLogger().warning(e.getMessage());
                    }
                    deleted.add(entry.getKey());
                    continue;
                }

                StandardKit kit = (StandardKit) entry.getValue();
                kit.purge();
                try {
                    repository.storeOne(kit.memento());
                } catch (Exception e) {
                    plugin.getLogger().warning("An error occurred while trying to save a kit.");
                    plugin.getLogger().warning(e.getMessage());
                }
            }

            Plugins.sync(plugin, () -> deleted.forEach(cache::remove));
        });
    }
}
