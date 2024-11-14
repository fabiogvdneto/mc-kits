package mc.minera.plugins.module.kit;

import mc.minera.plugins.exception.KitCooldownException;
import mc.minera.plugins.model.Kit;
import mc.minera.plugins.model.KitUnit;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.time.Duration;
import java.util.*;

public class StandardKit implements Kit {

    private final Map<UUID, KitUnit> units;
    private final String name;
    private Duration cooldown;
    private BigInteger cost;
    private ItemStack[] contents;

    public StandardKit(String name) {
        this.name = Objects.requireNonNull(name);
        this.cooldown = Duration.ZERO;
        this.cost = BigInteger.ZERO;
        this.contents = new ItemStack[0];
        this.units = new HashMap<>();
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
    public Collection<KitUnit> getAgreements() {
        return units.values();
    }

    @Override
    public KitUnit redeemOrThrow(UUID recipient) throws KitCooldownException {
        KitUnit redeemed = units.get(recipient);

        if (redeemed != null && !redeemed.isDisposable())
            throw new KitCooldownException(redeemed.whenDisposable());

        redeemed = new StandardUnit(this, recipient);
        units.put(recipient, redeemed);
        return redeemed;
    }

    @Override
    public KitUnit redeemOrGet(UUID recipient) {
        return units.compute(recipient, (key, value) ->
            (value == null) || value.isDisposable() ? new StandardUnit(this, key) : value
        );
    }

    @Override
    public KitUnit forceRedeem(UUID recipient) {
        return units.put(recipient, new StandardUnit(this, recipient));
    }
}
