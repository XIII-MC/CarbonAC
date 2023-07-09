package com.xiii.carbon.checks.impl.timer;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.BetterStream;
import com.xiii.carbon.utils.SampleList;

@Experimental
public class TimerA extends Check {

    private long lastFlying = System.currentTimeMillis(), sinceWasZero = System.currentTimeMillis();
    private SampleList<Double> ratioList = new SampleList<>(20, false);
    private boolean wasZero;
    private double balance;


    public TimerA(Profile profile) {
        super(profile, CheckType.TIMER, "A", "Checks if a player sends more packets.");
    }

    @Override
    public void handle(ClientPlayPacket clientPlayPacket) {
        if (clientPlayPacket.isFlying()) {

            final boolean exempt = profile.isExempt().isJoined(2000);
            final long now = System.currentTimeMillis();
            final double ratio = now - lastFlying;

            balance += 50 - ratio;

            if (balance < -100) ratioList.add(ratio);
            else ratioList.clear();

            if (ratio <= 20) {

                wasZero = true;
                sinceWasZero = now;

            } else {

                if (wasZero && balance < -300 && now - sinceWasZero > 5000) {
                    balance += 100;
                    wasZero = false;
                }
            }

            if (ratioList.isCollected()) {

                final double average = BetterStream.getAverageDouble(ratioList);

                if (balance < -100 && average < 50) {
                    balance = -50;
                }
            }

            if (balance > 10 && !exempt) {

                if (increaseBufferBy(1) > 1)
                    fail("b=" + balance);

                balance = -2;

            } else decreaseBufferBy(0.05);

            lastFlying = now;
        }
    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {
        if (serverPlayPacket.is(PacketType.Play.Server.ENTITY_TELEPORT) || serverPlayPacket.is(PacketType.Play.Server.PLAYER_POSITION_AND_LOOK)) {
            balance -= 50;
        }
    }
}
