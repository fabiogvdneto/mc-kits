package mc.minera.plugins.exception;

import mc.minera.plugins.model.KitUnit;

public class KitCooldownException extends RuntimeException {

    private final KitUnit unit;

    /**
     *
     * @param unit the kit unit that was redeemed and is still on cooldown.
     */
    public KitCooldownException(KitUnit unit) {
        super("can't redeem a kit while on cooldown");
        this.unit = unit;
    }

    public KitUnit getUnit() {
        return unit;
    }
}
