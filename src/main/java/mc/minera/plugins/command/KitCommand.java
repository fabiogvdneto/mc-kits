package mc.minera.plugins.command;

import mc.minera.plugins.KitsPlugin;
import mc.minera.plugins.common.command.CommandHandler;
import mc.minera.plugins.common.exception.CommandArgumentException;
import mc.minera.plugins.common.exception.CommandSenderException;
import mc.minera.plugins.common.exception.PermissionRequiredException;
import mc.minera.plugins.exception.KitCooldownException;
import mc.minera.plugins.exception.KitNotFoundException;
import mc.minera.plugins.model.Kit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class KitCommand extends CommandHandler<KitsPlugin> {

    public KitCommand(KitsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            requirePlayer(sender);
            plugin.getSettings().getCommandPermission(cmd).require(sender);
            requireArguments(args, 1);

            Kit kit = plugin.getKits().get(args[0]);

            if (args.length > 1 && checkSubcommand(sender, args, kit)) return;

            UUID recipient = ((Player) sender).getUniqueId();
            int remainder = kit.redeemOrThrow(recipient).collect();

            plugin.getMessages().kitRedeemed(sender, kit.getName());

            if (remainder > 0) {
                plugin.getMessages().kitLeftovers(sender, Integer.toString(remainder));
            }
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
            if (e.getUnit().isEmpty()) {
                long cooldown = Instant.now().until(
                        e.getUnit().whenDisposable(), ChronoUnit.MINUTES
                );
                plugin.getMessages().kitCooldown(sender, Long.toString(cooldown));
                return;
            }

            int remainder = e.getUnit().collect();

            if (remainder > 0) {
                plugin.getMessages().kitLeftovers(sender, Integer.toString(remainder));
            } else {
                plugin.getMessages().kitCollected(sender);
            }
        }
    }

    private boolean checkSubcommand(CommandSender sender, String[] args, Kit kit) {
        switch (args[1]) {
            case "edit":
                // TODO
                return true;
            case "setcooldown":
                // TODO
                return true;
            case "getcooldown":
                // TODO
                return true;
            case "setprice":
                // TODO
                return true;
            case "getprice":
                // TODO
                return true;
            default:
                return false;
        }
    }
}
