package mc.minera.plugins.model;

import mc.minera.plugins.exception.PlayerNotFoundException;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

public interface KitUnit {

    Kit getParent();

    UUID getRecipient();

    Instant whenCreated();

    Instant whenDisposable();

    boolean isDisposable();

    /**
     *
     * @return true if the kit has no leftovers, false otherwise
     */
    boolean isEmpty();

    /**
     *
     * @return the items that could not be collected by the player
     */
    Collection<ItemStack> getLeftovers();

    /**
     * Collect the items from this kit unit,
     * keeping those that could not fit in the player's inventory.
     * @return the number of items that could not be collected
     * @throws PlayerNotFoundException if the player is not online
     */
    int collect() throws PlayerNotFoundException;

}
