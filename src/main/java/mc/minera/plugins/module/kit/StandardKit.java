package mc.minera.plugins.module.kit;

import mc.minera.plugins.exception.InventoryFullException;
import mc.minera.plugins.exception.KitCooldownException;
import mc.minera.plugins.model.Kit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;

public class StandardKit implements Kit {

    private final Map<UUID, Instant> availability;
    private final String name;
    private Duration cooldown;
    private BigInteger cost;
    private ItemStack[] contents;

    public StandardKit(String name) {
        this.name = Objects.requireNonNull(name);
        this.cooldown = Duration.ZERO;
        this.cost = BigInteger.ZERO;
        this.contents = new ItemStack[0];
        this.availability = new HashMap<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Duration getCooldown() {
        return cooldown;
    }

    @Override
    public BigInteger getCost() {
        return cost;
    }

    @Override
    public ItemStack[] getContents() {
        return contents;
    }

    @Override
    public void setCooldown(Duration cooldown) {
        this.cooldown = Objects.requireNonNull(cooldown);
    }

    @Override
    public void setCost(BigInteger cost) {
        this.cost = Objects.requireNonNull(cost);
    }

    @Override
    public void setContents(ItemStack[] contents) {
        this.contents = Objects.requireNonNull(contents);
    }

    @Override
    public void redeem(Player recipient) throws KitCooldownException, InventoryFullException {
        Instant endOfCooldown = availability.get(recipient.getUniqueId());

        if (endOfCooldown != null && Instant.now().isBefore(endOfCooldown))
            throw new KitCooldownException(endOfCooldown);

        redeemNow(recipient);
    }

    @Override
    public void redeemNow(Player recipient) throws InventoryFullException {
        collect(recipient.getInventory());
        availability.put(recipient.getUniqueId(), Instant.now().plus(cooldown));
    }

    @Override
    public void collect(Inventory target) throws InventoryFullException {
        int[] freeSlots = findFreeSlots(target);

        if (freeSlots.length < contents.length)
            throw new InventoryFullException(contents.length, freeSlots.length);

        for (int i = 0; i < contents.length; i++) {
            target.setItem(freeSlots[i], contents[i]);
        }
    }

    private int[] findFreeSlots(Inventory inv) {
        ItemStack[] storage = inv.getStorageContents();

        return IntStream.range(0, storage.length)
                .filter(i -> storage[i] == null || storage[i].isEmpty())
                .limit(contents.length)
                .toArray();
    }
}
