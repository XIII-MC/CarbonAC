package com.xiii.carbon.checks.impl;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.carbon.checks.annotation.Disabled;
import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.annotation.Testing;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.playerdata.data.impl.RotationData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.BetterStream;
import com.xiii.carbon.utils.MathUtils;
import com.xiii.carbon.utils.MoveUtils;
import com.xiii.carbon.utils.SampleList;
import org.bukkit.Material;

import java.util.List;

@Testing
public class Test extends Check {
    public Test(final Profile profile) {
        super(profile, CheckType.TEST, "A", "Test Check for the Developers.");
    }

    int exemptticks = 0;

  //  @Override
   // public void handle(final ClientPlayPacket clientPlayPacket) {

      //  if (!clientPlayPacket.isMovement()) return;

       // final MovementData data = profile.getMovementData();
       // final List<Material> footBlocks = data.getFootBlocks();

      //  final boolean testBoolean = BetterStream.anyMatch(footBlocks, material -> material.toString().equalsIgnoreCase("AIR"));

     //   if (data.getDeltaXZ() < data.getLastDeltaXZ() && testBoolean && data.getAccelXZ() > 0.08 && data.getAccelXZ() < 0.13 && data.getDeltaXZ() < 0.13 && data.getDeltaXZ() > 0.02) {
     //       if (increaseBufferBy(1) > 3)
     //           debug(data.getDeltaXZ() + " " + data.getAccelXZ());
     //   }else decreaseBufferBy(0.01);

        // predictionXZ + " " + data.getAccelXZ() + " " +

        /*if (!clientPlayPacket.isRotation()) return;

        if (profile.isExempt().isJoined(5000L) || profile.isExempt().isVehicle()) return;

        final RotationData data = profile.getRotationData();

        final double fix = (data.getLastDeltaYaw() + data.getDeltaYaw()) % (data.getDeltaYaw() + data.getLastDeltaYaw() + 1000);
        if (fix < 0.31) exemptticks++;
        else exemptticks = 0;

        double gcd = MathUtils.getAbsoluteGcd(data.getYawAccel() * 3242869, data.getLastYawAccel() * 3242869) % MathUtils.getAbsoluteGcd(data.getDeltaYaw() * 3242869, data.getLastDeltaYaw() * 3242869);

        if (gcd != 0 && !String.valueOf(gcd).contains("E") && exemptticks < 2) {
            if (increaseBufferBy(1) > 10)
                fail("LITTLE MONKEY BITCH GET FUCKED RETARDED BNASTERDATERED NEINNNN FRUNKFURT " + gcd + " et=" + exemptticks);
        } else if(gcd != 0) resetBuffer();

        debug(profile.getPlayer().getName() + " " + gcd + " et=" + exemptticks + " b=" + getBuffer());
        */

 //   }

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
