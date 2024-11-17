package mc.minera.plugins.model;

import mc.minera.plugins.exception.InventoryFullException;
import mc.minera.plugins.exception.KitCooldownException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;

public interface Kit {

    String getName();

    Duration getCooldown();

    long getPrice();

    ItemStack[] getContents();

    void setCooldown(Duration cooldown);

    void setPrice(long price);

    void setContents(ItemStack[] contents);

    void redeem(Player recipient) throws KitCooldownException, InventoryFullException;

    void redeemNow(Player recipient) throws InventoryFullException;

    void collect(Inventory target) throws InventoryFullException;

}
