package mc.minera.plugins.service;

import mc.minera.plugins.common.PluginService;
import mc.minera.plugins.KitsPlugin;
import mc.minera.plugins.common.PermissionWrapper;
import org.bukkit.command.Command;
import org.bukkit.configuration.Configuration;

import java.util.Objects;

public class ConfigurationService implements PluginService {

    private final KitsPlugin plugin;

    public ConfigurationService(KitsPlugin plugin) {
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

    public int getKitAutosaveMinutes() {
        return config().getInt("kits.autosave-minutes");
    }

    /* ---- Permissions ---- */

    public PermissionWrapper getCommandPermission(Command cmd) {
        return new PermissionWrapper("kits.command." + cmd.getName().toLowerCase());
    }

    public PermissionWrapper getCommandPermission(String cmd) {
        return new PermissionWrapper("kits.command." + cmd.toLowerCase());
    }

    public PermissionWrapper getKitPermission(String kitName) {
        return new PermissionWrapper("kits.kit." + kitName.toLowerCase());
    }
}
