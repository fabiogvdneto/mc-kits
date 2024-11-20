package mc.minera.plugins.command;

import mc.minera.plugins.KitsPlugin;
import mc.minera.plugins.common.command.CommandHandler;
import mc.minera.plugins.common.exception.PermissionRequiredException;
import mc.minera.plugins.model.Kit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class CommandKits extends CommandHandler<KitsPlugin> {

    public CommandKits(KitsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            plugin.getSettings().getCommandPermission(cmd.getName()).require(sender);

            Collection<String> kits = plugin.getKits().getAll().stream().map(Kit::getName)
                    .filter(name -> plugin.getSettings().getKitPermission(name).test(sender))
                    .toList();

            if (kits.isEmpty()) {
                plugin.getMessages().kitListEmpty(sender);
                return;
            }

            plugin.getMessages().kitList(sender, kits);
        } catch (PermissionRequiredException e) {
            plugin.getMessages().permissionRequired(sender);
        }
    }
}
