package com.xiii.carbon.checks.impl.velocity;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.CombatData;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MathUtils;
import com.xiii.carbon.utils.MoveUtils;

@Experimental
public class VelocityA extends Check {

    private Vector3d velocityVector;
    private int ticks = 0;
    private boolean hit = false;

    public VelocityA(final Profile profile) {
        super(profile, CheckType.VELOCITY, "A", "Check's difference between player's and server's velocity.");
    }

    @Override
    public void handle(ClientPlayPacket clientPlayPacket) {
        if (clientPlayPacket.isMovement()) {
            final MovementData movementData = profile.getMovementData();
            //if (!movementData.isBlockAbove()) {
            final boolean jumped = !movementData.isOnGround() && movementData.isLastOnGround();
            if (hit) {
                if(jumped || (ticks++ > (MoveUtils.getMaxVelocityTicks(0, velocityVector.getY()))) ) {
                    final double percentage = movementData.getDeltaY() / velocityVector.getY() * 100;
                    debug("percentage=" + percentage);
                    ticks = 0;
                    hit = false;
                }
            }
        }
    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {
        if (serverPlayPacket.is(PacketType.Play.Server.ENTITY_VELOCITY)) {
            WrapperPlayServerEntityVelocity veloWrapper = serverPlayPacket.getEntityVelocityWrapper();
            if (veloWrapper.getEntityId() == profile.getPlayer().getEntityId()) {
                velocityVector = veloWrapper.getVelocity();
                hit = true;
            }
        }
    }
}
