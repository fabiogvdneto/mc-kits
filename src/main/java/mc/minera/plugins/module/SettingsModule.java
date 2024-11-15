package mc.minera.plugins.module;

import mc.minera.plugins.KitsModule;
import mc.minera.plugins.KitsPlugin;
import mc.minera.plugins.common.PermissionWrapper;
import org.bukkit.command.Command;
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
