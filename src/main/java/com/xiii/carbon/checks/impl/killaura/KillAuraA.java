package com.xiii.carbon.checks.impl.killaura;

import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

public class KillAuraA extends Check {
    public KillAuraA(final Profile profile) {
        super(profile, CheckType.KILLAURA, "A", "Checks if player hits more entities than possible.");
    }

    private int lastHitEntity;

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (clientPlayPacket.isAttack()) {

            final int currentEntity = clientPlayPacket.getInteractEntityWrapper().getEntityId();

            if (currentEntity != lastHitEntity && increaseBuffer() > 1) fail("buffer: §c" + getBuffer());

            lastHitEntity = currentEntity;
        } else if (clientPlayPacket.isFlying()) decreaseBufferBy(0.25);

    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}
