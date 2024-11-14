package mc.minera.plugins.module;

import mc.minera.plugins.KitsModule;
import mc.minera.plugins.KitsPlugin;
import org.bukkit.configuration.Configuration;

import java.util.Objects;

public class SettingsModule implements KitsModule {

    private final KitsPlugin plugin;

    public SettingsModule(KitsPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
    }

    @Override
    public void enable() {
        plugin.saveDefaultConfig();
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
    }

    @Override
    public void disable() {
        // Nothing here.
    }

    private Configuration config() {
        return plugin.getConfig();
    }

    public String getLanguage() {
        return config().getString("lang");
    }
}
