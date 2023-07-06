package com.xiii.carbon.checks.impl.killaura;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import org.bukkit.entity.Entity;

@Experimental
public class KillAuraA extends Check {
    private int hitEntities;
    private int lastHitEntity;

    public KillAuraA(final Profile profile) {
        super(profile, CheckType.KILLAURA, "A", "Checks if player's hit more Entities than possible.");
    }

    @Override
    public void handle(ClientPlayPacket clientPlayPacket) {
        if (clientPlayPacket.isAttack()) {
            final int currentEntity = clientPlayPacket.getInteractEntityWrapper().getEntityId();
            if (currentEntity != lastHitEntity) {
                hitEntities++;
                if (hitEntities > 1) fail("c=" + hitEntities);
            }
            lastHitEntity = currentEntity;
        } else if (clientPlayPacket.isFlying()) {
            hitEntities = 0;
        }

    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {}
}
