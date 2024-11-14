package mc.minera.plugins.command;

import mc.minera.plugins.KitsPlugin;
import mc.minera.plugins.common.command.CommandHandler;
import mc.minera.plugins.common.exception.CommandArgumentException;
import mc.minera.plugins.common.exception.CommandSenderException;
import mc.minera.plugins.common.exception.PermissionRequiredException;
import mc.minera.plugins.exception.KitAlreadyExistsException;
import mc.minera.plugins.model.Kit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class CreatekitCommand extends CommandHandler<KitsPlugin> {

    public CreatekitCommand(KitsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            requirePlayer(sender);
            plugin.getSettings().getCommandPermission(cmd).require(sender);
            requireArguments(args, 1);

            Kit kit = plugin.getKits().create(args[0]);
            Player player = (Player) sender;

            ItemStack[] contents = Arrays.stream(player.getInventory().getContents())
                    .filter(Objects::nonNull)
                    .filter(item -> !item.getType().isAir())
                    .toArray(ItemStack[]::new);

            kit.setContents(contents);
            plugin.getMessages().kitCreated(sender, kit.getName());
        } catch (CommandSenderException e) {
            plugin.getMessages().commandPlayersOnly(sender);
        } catch (CommandArgumentException e) {
            plugin.getMessages().commandUsageCreatekit(sender);
        } catch (PermissionRequiredException e) {
            plugin.getMessages().permissionRequired(sender);
        } catch (KitAlreadyExistsException e) {
            plugin.getMessages().kitAlreadyExists(sender, args[0]);
        }
    }
}
