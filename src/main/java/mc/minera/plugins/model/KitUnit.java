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

    Collection<ItemStack> getLeftovers();

    void push() throws PlayerNotFoundException;

}
