package com.pdev.drduels.api;

import java.util.UUID;

public class DuelResult {
    private boolean fromKill;
    private UUID loser;

    public DuelResult(boolean fromKill, UUID loser) {
        this.fromKill = fromKill;
        this.loser = loser;
    }

    public boolean fromKill() {
        return fromKill;
    }

    public UUID getLoser() {
        return loser;
    }
}
