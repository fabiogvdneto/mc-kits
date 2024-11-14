package mc.minera.plugins.module.kit;

import mc.minera.plugins.exception.PlayerNotFoundException;
import mc.minera.plugins.model.Kit;
import mc.minera.plugins.model.KitUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

public class StandardUnit implements KitUnit {

    private final Kit kit;
    private final UUID recipient;
    private final Instant creationInstant;
    private final Instant disposableInstant;
    private ItemStack[] leftovers;

    public StandardUnit(Kit kit, UUID recipient) {
        this.kit = kit;
        this.recipient = recipient;
        this.creationInstant = Instant.now();
        this.disposableInstant = whenCreated().plus(kit.getCooldown());
        this.leftovers = kit.getContents();
    }

    @Override
    public Kit getParent() {
        return kit;
    }

    @Override
    public UUID getRecipient() {
        return recipient;
    }

    @Override
    public Instant whenCreated() {
        return creationInstant;
    }

    @Override
    public Instant whenDisposable() {
        return disposableInstant;
    }

    @Override
    public boolean isDisposable() {
        return Instant.now().isAfter(disposableInstant);
    }

    @Override
    public boolean isEmpty() {
        return leftovers.length == 0;
    }

    @Override
    public Collection<ItemStack> getLeftovers() {
        return Arrays.asList(leftovers);
    }

    @Override
    public int collect() throws PlayerNotFoundException{
        Player player = Bukkit.getPlayer(recipient);

        if (player == null)
            throw new PlayerNotFoundException();

        this.leftovers = player.getInventory().addItem(leftovers).values().toArray(ItemStack[]::new);
        return leftovers.length;
    }
}
