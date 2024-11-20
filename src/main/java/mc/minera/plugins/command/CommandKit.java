package mc.minera.plugins.command;

import mc.minera.plugins.KitsPlugin;
import mc.minera.plugins.common.command.CommandHandler;
import mc.minera.plugins.common.exception.CommandArgumentException;
import mc.minera.plugins.common.exception.CommandSenderException;
import mc.minera.plugins.common.exception.PermissionRequiredException;
import mc.minera.plugins.exception.InventoryFullException;
import mc.minera.plugins.exception.KitCooldownException;
import mc.minera.plugins.exception.KitNotFoundException;
import mc.minera.plugins.model.Kit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CommandKit extends CommandHandler<KitsPlugin> {

    public CommandKit(KitsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            requirePlayer(sender);
            plugin.getSettings().getCommandPermission(cmd).require(sender);
            requireArguments(args, 1);

            Kit kit = plugin.getKits().get(args[0]);

            plugin.getSettings().getKitPermission(kit.getName()).require(sender);

            kit.redeem((Player) sender);

            plugin.getMessages().kitRedeemed(sender, kit.getName());
        } catch (PermissionRequiredException e) {
            plugin.getMessages().permissionRequired(sender);
        } catch (CommandSenderException e) {
            plugin.getMessages().commandPlayersOnly(sender);
        } catch (CommandArgumentException e) {
            plugin.getMessages().commandUsageKit(sender);

            // TODO: list all kits available.
        } catch (KitNotFoundException e) {
            plugin.getMessages().kitNotFound(sender, args[0]);
        } catch (KitCooldownException e) {
            long cooldown = Instant.now().until(e.whenAvailable(), ChronoUnit.MINUTES);
            plugin.getMessages().kitCooldown(sender, Long.toString(cooldown));
        } catch (InventoryFullException e) {
            String required = Integer.toString(e.getSpaceRequired());
            String available = Integer.toString(e.getSpaceAvailable());
            plugin.getMessages().kitInventoryFull(sender, required, available);
        }
    }
}
