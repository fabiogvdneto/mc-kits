package mc.minera.plugins.exception;

import java.time.Instant;

public class KitCooldownException extends RuntimeException {

    private final Instant disposableInstant;

    /**
     *
     * @param disposableInstant when the player is able to redeem the kit again.
     */
    public KitCooldownException(Instant disposableInstant) {
        super("can't redeem a kit while on cooldown");
        this.disposableInstant = disposableInstant;
    }

    public Instant whenDisposable() {
        return disposableInstant;
    }
}
