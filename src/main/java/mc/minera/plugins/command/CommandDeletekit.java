package mc.minera.plugins.command;

import mc.minera.plugins.KitsPlugin;
import mc.minera.plugins.common.command.CommandHandler;
import mc.minera.plugins.common.exception.CommandArgumentException;
import mc.minera.plugins.common.exception.PermissionRequiredException;
import mc.minera.plugins.exception.KitNotFoundException;
import mc.minera.plugins.model.Kit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandDeletekit extends CommandHandler<KitsPlugin> {

    public CommandDeletekit(KitsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            plugin.getSettings().getCommandPermission(cmd).require(sender);
            requireArguments(args, 1);

            Kit kit = plugin.getKits().delete(args[0]);
            plugin.getMessages().kitDeleted(sender, kit.getName());
        } catch (CommandArgumentException e) {
            plugin.getMessages().commandUsageDeletekit(sender);
        } catch (PermissionRequiredException e) {
            plugin.getMessages().permissionRequired(sender);
        } catch (KitNotFoundException e) {
            plugin.getMessages().kitNotFound(sender, args[0]);
        }
    }
}
