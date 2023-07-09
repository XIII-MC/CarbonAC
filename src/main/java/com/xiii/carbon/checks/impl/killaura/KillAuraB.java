package com.xiii.carbon.checks.impl.killaura;

import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

@Experimental
public class KillAuraB extends Check {
    public KillAuraB(final Profile profile) {
        super(profile, CheckType.KILLAURA, "B", "Checks the player's packet timing.");
    }

    private long UseEntityPackets;
    private boolean hit;

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (clientPlayPacket.isAttack()) {
            hit = true;
            UseEntityPackets = System.currentTimeMillis();
        } else if (clientPlayPacket.isFlying()) {
            final long lastFlying = System.currentTimeMillis() - UseEntityPackets;
            if (hit) {
                if (lastFlying > 12) {
                    fail("f=" + lastFlying);
                }
            }
            hit = false;
        }
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {

    }
}
