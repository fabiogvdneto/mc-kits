package mc.minera.plugins.model;

import mc.minera.plugins.exception.InventoryFullException;
import mc.minera.plugins.exception.KitCooldownException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.time.Duration;

public interface Kit {

    String getName();

    Duration getCooldown();

    BigInteger getCost();

    ItemStack[] getContents();

    void setCooldown(Duration cooldown);

    void setCost(BigInteger cost);

    void setContents(ItemStack[] contents);

    void redeem(Player recipient) throws KitCooldownException, InventoryFullException;

    void redeemNow(Player recipient) throws InventoryFullException;

    void collect(Inventory target) throws InventoryFullException;

}
