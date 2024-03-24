package com.xiii.carbon.checks.impl.timer;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.SampleList;

public class TimerB extends Check {

    public TimerB(final Profile profile) {
        super(profile, CheckType.TIMER, "B", "Checks if a player sends more packets.");
    }

    private long lastFlying = System.currentTimeMillis(), sinceWasZero = System.currentTimeMillis();
    private final SampleList<Double> ratioList = new SampleList<>(8, false);
    private boolean wasZero;
    private double balance;

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (clientPlayPacket.isFlying()) {

            final boolean exempt = profile.isExempt().isJoined(2000);
            final long now = System.currentTimeMillis();
            final double ratio = now - lastFlying;
            final MovementData data = profile.getMovementData();
            balance += 50 - ratio;
            if ((50 / ratio) != Double.POSITIVE_INFINITY && (50 / ratio) < 0.7) {
                ratioList.add((50 / ratio));
                if (ratioList.isCollected() && data.getDeltaXZ() > 0) {
                    debug("Rat=" + (50 / ratio) + " Bal=" + balance);
                }
            }

            if (balance > 10) balance = 0;
            lastFlying = now;
        }
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {

        if (serverPlayPacket.is(PacketType.Play.Server.ENTITY_TELEPORT) || serverPlayPacket.is(PacketType.Play.Server.PLAYER_POSITION_AND_LOOK)) {

            balance -= 50;
        }
    }
}
