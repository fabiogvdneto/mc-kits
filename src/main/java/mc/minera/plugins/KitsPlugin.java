package mc.minera.plugins;

import mc.minera.plugins.module.SettingsModule;
import mc.minera.plugins.module.TranslationModule;
import mc.minera.plugins.module.kit.KitModule;
import org.bukkit.plugin.java.JavaPlugin;

public class KitsPlugin extends JavaPlugin {

    private final SettingsModule settings = new SettingsModule(this);
    private final TranslationModule translations = new TranslationModule(this);
    private final KitModule kits = new KitModule();

    @Override
    public void onEnable() {
        settings.enable();
        translations.enable();
        kits.enable();
    }

    @Override
    public void onDisable() {
        settings.disable();
        translations.disable();
        kits.disable();
    }

    public SettingsModule getSettings() {
        return settings;
    }

    public TranslationModule getTranslations() {
        return translations;
    }

    public KitModule getKits() {
        return kits;
    }
}
