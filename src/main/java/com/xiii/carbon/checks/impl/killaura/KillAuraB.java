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

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (!clientPlayPacket.isFlying()) return;

        final long lastFlying = System.currentTimeMillis() - clientPlayPacket.getLastAttack();

        if (clientPlayPacket.isAttack() && lastFlying > 12) fail("lF: §c" + lastFlying);
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}
