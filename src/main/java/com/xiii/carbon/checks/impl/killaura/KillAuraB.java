package com.xiii.carbon.checks.impl.killaura;

import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MathUtils;

@Experimental
public class KillAuraB extends Check {

    private long UseEntityPackets;

    public KillAuraB(Profile profile) {
        super(profile, CheckType.KILLAURA, "B", "Checks the player's packet timing.");
    }

    @Override
    public void handle(ClientPlayPacket clientPlayPacket) {
        if (clientPlayPacket.isAttack()) {
            final long lastFlying = System.currentTimeMillis() - UseEntityPackets;
            if (lastFlying < 30L) {
                if (increaseBufferBy(1) > 1) fail("t=" + lastFlying);
            } else decreaseBufferBy(1);
        } else if (clientPlayPacket.isFlying()) {
            UseEntityPackets = System.currentTimeMillis();
            decreaseBufferBy(0.1);
        }
    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {

    }
}
