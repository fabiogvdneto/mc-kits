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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class KitModule implements KitManager, KitsModule {

    private final KitsPlugin plugin;
    private final Map<String, Kit> cache = new HashMap<>();

    private KitRepository repository;
    private BukkitTask autosaveTask;

    public KitModule(KitsPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
    }

    @Override
    public Kit create(String name) throws KitAlreadyExistsException {
        String key = getKeyFromName(name);

        if (cache.get(key) != null)
            throw new KitAlreadyExistsException(name);

        Kit kit = new StandardKit(name);
        cache.put(key, kit);
        return kit;
    }

    @Override
    public Kit get(String name) throws KitNotFoundException {
        Kit kit = cache.get(getKeyFromName(name));

        if (kit == null)
            throw new KitNotFoundException(name);

        return kit;
    }

    @Override
    public Kit delete(String name) throws KitNotFoundException {
        // Replace the value by null instead of removing it,
        // so that it can be deleted from the repository later.
        String key = getKeyFromName(name);
        Kit removed = cache.put(key, null);

        if (removed == null)
            throw new KitNotFoundException(name);

        return removed;
    }

    @Override
    public boolean exists(String name) {
        return cache.get(getKeyFromName(name)) != null;
    }

    @Override
    public void enable() {
        disable();
        createRepository();
        load();
        autosave();
    }

    private void createRepository() {
        this.repository = new JavaKitRepository(plugin.getDataPath().resolve("kits"));

        try {
            repository.create();
        } catch (Exception e) {
            plugin.getLogger().warning("Could not create the kits repository.");
            plugin.getLogger().warning(e.getMessage());
        }
    }

    @Override
    public void disable() {
        if (autosaveTask != null) {
            autosaveTask.cancel();
            autosaveTask = null;
            save(memento());
            cache.clear();
        }
    }

    /* ---- Persistence ---- */

    private void load() {
        try {
            for (KitData data : repository.fetchAll()) {
                cache.put(getKey(data), new StandardKit(data));
            }
            plugin.getLogger().info("Loaded " + cache.size() + " kits.");
        } catch (Exception e) {
            plugin.getLogger().warning("An error occurred while trying to load kits.");
            plugin.getLogger().warning(e.getMessage());
        }
    }

    private void save(Map<String, KitData> data) {
        int successCount = 0;
        int errorCount = 0;

        for (Map.Entry<String, KitData> entry : data.entrySet()) {
            if (entry.getValue() == null) {
                // Delete from the repository.
                try {
                    repository.deleteOne(entry.getKey());
                    successCount++;
                } catch (Exception e) {
                    plugin.getLogger().warning("Could not delete a kit.");
                    plugin.getLogger().warning(e.getMessage());
                    errorCount++;
                }
            } else {
                // Store to the repository.
                try {
                    repository.storeOne(entry.getValue());
                    successCount++;
                } catch (Exception e) {
                    plugin.getLogger().warning("Could not delete a kit.");
                    plugin.getLogger().warning(e.getMessage());
                    errorCount++;
                }
            }
        }

        plugin.getLogger().info("Saved " + successCount + " kits with " + errorCount + " errors.");
    }

    private void saveAsync() {
        Map<String, KitData> data = memento();
        Plugins.async(plugin, () -> save(data));
    }

    private void autosave() {
        long autosaveMinutes = (long) plugin.getSettings().getKitAutosaveMinutes() * 60 * 20;
        this.autosaveTask = Plugins.sync(plugin, this::saveAsync, autosaveMinutes, autosaveMinutes);
    }

    /* ---- Utilities ---- */

    private String getKeyFromName(String name) {
        return name.toLowerCase();
    }

    private String getKey(KitData data) {
        return data.name().toLowerCase();
    }

    private Map<String, KitData> memento() {
        Map<String, KitData> data = new HashMap<>();

        for (Map.Entry<String, Kit> entry : cache.entrySet()) {
            if (entry.getValue() == null) {
                data.put(entry.getKey(), null);
            } else {
                data.put(entry.getKey(), ((StandardKit) entry.getValue()).memento());
            }
        }

        return data;
    }
}
