package mc.minera.plugins;

import mc.minera.plugins.command.CreatekitCommand;
import mc.minera.plugins.command.DeletekitCommand;
import mc.minera.plugins.command.KitCommand;
import mc.minera.plugins.model.KitManager;
import mc.minera.plugins.module.SettingsModule;
import mc.minera.plugins.module.MessagesModule;
import mc.minera.plugins.module.kit.KitModule;
import org.bukkit.plugin.java.JavaPlugin;

public class KitsPlugin extends JavaPlugin {

    private final SettingsModule settings = new SettingsModule(this);
    private final MessagesModule messages = new MessagesModule(this);
    private final KitModule kits = new KitModule();

    @Override
    public void onEnable() {
        settings.enable();
        messages.enable();
        kits.enable();

        registerCommands();
    }

    private void registerCommands() {
        new CreatekitCommand(this).inject("createkit");
        new DeletekitCommand(this).inject("deletekit");
        new KitCommand(this).inject("kit");
    }

    @Override
    public void onDisable() {
        settings.disable();
        messages.disable();
        kits.disable();
    }

    public SettingsModule getSettings() {
        return settings;
    }

    public MessagesModule getMessages() {
        return messages;
    }

    public KitManager getKits() {
        return kits;
    }
}
