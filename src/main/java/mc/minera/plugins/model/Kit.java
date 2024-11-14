package mc.minera.plugins.model;

import mc.minera.plugins.exception.KitCooldownException;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Collection;
import java.util.UUID;

public interface Kit {

    String getName();

    Duration getCooldown();

    BigInteger getCost();

    ItemStack[] getContents();

    void setCooldown(Duration cooldown);

    void setCost(BigInteger cost);

    void setContents(ItemStack[] contents);

    Collection<KitUnit> getAgreements();

    KitUnit redeemOrThrow(UUID recipient) throws KitCooldownException;

    KitUnit redeemOrGet(UUID recipient);

    KitUnit forceRedeem(UUID recipient);

}
