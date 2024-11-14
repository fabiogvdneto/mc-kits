package mc.minera.plugins.module;

import mc.minera.plugins.KitsModule;
import mc.minera.plugins.KitsPlugin;
import mc.minera.plugins.common.i18n.PluginTranslator;

import java.util.Objects;

public class TranslationModule implements KitsModule {

    private final KitsPlugin plugin;
    private final PluginTranslator translator = new PluginTranslator();

    public TranslationModule(KitsPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
    }

    @Override
    public void enable() {
        translator.loadTranslations(plugin, plugin.getSettings().getLanguage(), "en");
    }

    @Override
    public void disable() {
        translator.clearTranslations();
    }


}
