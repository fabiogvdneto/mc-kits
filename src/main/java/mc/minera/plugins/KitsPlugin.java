package mc.minera.plugins;

import mc.minera.plugins.command.CommandCreatekit;
import mc.minera.plugins.command.CommandDeletekit;
import mc.minera.plugins.command.CommandKit;
import mc.minera.plugins.command.CommandKits;
import mc.minera.plugins.model.KitManager;
import mc.minera.plugins.service.ConfigurationService;
import mc.minera.plugins.service.MessageService;
import mc.minera.plugins.service.kit.KitService;
import org.bukkit.plugin.java.JavaPlugin;

public class KitsPlugin extends JavaPlugin {

    private final ConfigurationService settings = new ConfigurationService(this);
    private final MessageService messages = new MessageService(this);
    private final KitService kits = new KitService(this);

    @Override
    public void onEnable() {
        settings.enable();
        messages.enable();
        kits.enable();

        registerCommands();
    }

    private void registerCommands() {
        new CommandCreatekit(this).registerAs("createkit");
        new CommandDeletekit(this).registerAs("deletekit");
        new CommandKits(this).registerAs("kits");
        new CommandKit(this).registerAs("kit");
    }

    @Override
    public void onDisable() {
        settings.disable();
        messages.disable();
        kits.disable();
    }

    public ConfigurationService getSettings() {
        return settings;
    }

    public MessageService getMessages() {
        return messages;
    }

    public KitManager getKits() {
        return kits;
    }
}
