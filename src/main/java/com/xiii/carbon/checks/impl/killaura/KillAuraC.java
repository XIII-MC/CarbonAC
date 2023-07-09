package com.xiii.carbon.checks.impl.killaura;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
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

    private long UseEntityPackets;
    private boolean hit;

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (clientPlayPacket.isAttack()) {

            hit = true;
        } else if (clientPlayPacket.isFlying()) {
            if (hit) {
                final long time = (System.currentTimeMillis() - UseEntityPackets);
                if (time > 40) {
                    fail("t=" + time);
                }
                hit = false;
            }

        } else if (clientPlayPacket.is(PacketType.Play.Client.ANIMATION)) {
            UseEntityPackets = System.currentTimeMillis();
        }
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {

    }
}
