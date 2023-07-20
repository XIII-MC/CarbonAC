package com.xiii.carbon.checks.impl.killaura;

import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

@Experimental
public class KillAuraC extends Check {
    public KillAuraC(final Profile profile) {
        super(profile, CheckType.KILLAURA, "C", "Checks the player's packet timing.");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (clientPlayPacket.isFlying() && clientPlayPacket.isAttack()) {

            final long delay = System.currentTimeMillis() - clientPlayPacket.getLastAnimation();

            if (delay > 40) fail("delay: §c" + delay);
        }
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}
