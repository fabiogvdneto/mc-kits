package mc.minera.plugins.module.kit;

import mc.minera.plugins.exception.InventoryFullException;
import mc.minera.plugins.exception.KitCooldownException;
import mc.minera.plugins.model.Kit;
import mc.minera.plugins.repository.data.KitData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.IntStream;

class StandardKit implements Kit {

    private final Map<UUID, Instant> availability;
    private final String name;
    private Duration cooldown;
    private long price;
    private ItemStack[] contents;

    StandardKit(String name) {
        this.name = Objects.requireNonNull(name);
        this.cooldown = Duration.ZERO;
        this.contents = new ItemStack[0];
        this.availability = new HashMap<>();
    }

    StandardKit(KitData data) {
        this.name = data.name();
        this.cooldown = data.cooldown();
        this.price = data.price();
        this.contents = ItemStack.deserializeItemsFromBytes(data.contents());
        this.availability = new HashMap<>(data.availability());
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
    public long getPrice() {
        return price;
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
    public void setPrice(long price) {
        this.price = Math.max(0, price);
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

    void purge() {
        availability.values().removeIf(Instant.now()::isAfter);
    }

    KitData memento() {
        byte[] contentsNBT = ItemStack.serializeItemsAsBytes(this.contents);
        return new KitData(name, cooldown, price, contentsNBT, Map.copyOf(availability));
    }
}
