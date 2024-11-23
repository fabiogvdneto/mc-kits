package mc.minera.plugins.service;

import mc.minera.plugins.KitsPlugin;
import mc.minera.plugins.common.PluginService;
import mc.minera.plugins.common.i18n.PluginTranslator;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Collection;
import java.util.Objects;

public class MessageService implements PluginService {

    private static final String DEFAULT_LANGUAGE = "en";

    private final KitsPlugin plugin;
    private final PluginTranslator translator = new PluginTranslator();

    public MessageService(KitsPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
    }

    @Override
    public void enable() {
        translator.loadTranslations(plugin, DEFAULT_LANGUAGE);
        translator.loadTranslations(plugin, plugin.getSettings().getLanguage());
    }

    @Override
    public void disable() {
        translator.clearTranslations();
    }

    private Component translate(String key) {
        return MiniMessage.miniMessage().deserialize(translator.get(key));
    }

    private void message(String key, Audience target) {
        target.sendMessage(translate(key));
    }

    private void message(String key, Audience target, TagResolver resolver) {
        target.sendMessage(
                MiniMessage.miniMessage().deserialize(translator.get(key), resolver)
        );
    }

    private void message(String key, Audience target, TagResolver... resolvers) {
        target.sendMessage(
                MiniMessage.miniMessage().deserialize(translator.get(key), resolvers)
        );
    }

    /* ---- Messages ---- */

    public void permissionRequired(Audience target) {
        message("permission-required", target);
    }

    public void commandPlayersOnly(Audience target) {
        message("command.players-only", target);
    }

    public void commandUsageKit(Audience target) {
        message("command.usage.kit", target);
    }

    public void commandUsageCreatekit(Audience target) {
        message("command.usage.createkit", target);
    }

    public void commandUsageDeletekit(Audience target) {
        message("command.usage.deletekit", target);
    }

    public void kitRedeemed(Audience target, String kitName) {
        message("kit.redeemed", target, Placeholder.unparsed("name", kitName));
    }

    public void kitInventoryFull(Audience target, String spaceRequired, String spaceAvailable) {
        message("kit.inventory-full", target,
                Placeholder.unparsed("required", spaceRequired),
                Placeholder.unparsed("available", spaceAvailable));
    }

    public void kitCreated(Audience target, String kitName) {
        message("kit.created", target, Placeholder.unparsed("name", kitName));
    }

    public void kitDeleted(Audience target, String kitName) {
        message("kit.deleted", target, Placeholder.unparsed("name", kitName));
    }

    public void kitNotFound(Audience target, String kitName) {
        message("kit.not-found", target, Placeholder.unparsed("name", kitName));
    }

    public void kitAlreadyExists(Audience target, String kitName) {
        message("kit.already-exists", target, Placeholder.unparsed("name", kitName));
    }

    public void kitCooldown(Audience target, String cooldownMinutes) {
        message("kit.cooldown", target, Placeholder.unparsed("cooldown", cooldownMinutes));
    }

    public void kitInsufficientMoney(Audience target, String cost) {
        message("kit.insufficient-money", target, Placeholder.unparsed("cost", cost));
    }

    public void kitListEmpty(Audience target) {
        message("kit.list.empty", target);
    }

    public void kitList(Audience target, Collection<String> kits) {
        Component separator = translate("kit.list.separator");
        Component list = kits.stream().map(Component::text).collect(Component.toComponent(separator));

        message("kit.list.base", target, Placeholder.component("list", list));
    }
}
