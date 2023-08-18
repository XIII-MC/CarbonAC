package com.xiii.carbon.checks.impl.invalid;

import com.xiii.carbon.checks.annotation.Experimental;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.BetterStream;
import com.xiii.carbon.utils.MathUtils;
import com.xiii.carbon.utils.MoveUtils;
import com.xiii.carbon.utils.PlayerUtils;
import org.bukkit.potion.PotionEffectType;

@Experimental
public class InvalidB extends Check {
    public InvalidB(final Profile profile) {
        super(profile, CheckType.INVALID, "B", "Invalid Prediction Offset");
    }

    @Override
    public void handle(ClientPlayPacket clientPlayPacket) {
        if (!clientPlayPacket.isMovement()) return;
        final MovementData data = profile.getMovementData();
        double predictionY = (data.getLastDeltaY() - 0.08D) * 0.98f;

        final boolean blockAtFeet = !BetterStream.allMatch(data.getFootBlocks(), material -> material.toString().equalsIgnoreCase("AIR"));
        if (blockAtFeet && data.getDeltaY() == 0) predictionY = 0;
        if (data.isLastOnGround() && !data.isOnGround() && data.getDeltaY() > 0.3) predictionY = MoveUtils.JUMP_MOTION + (PlayerUtils.getEffectByType(profile, PotionEffectType.JUMP).isPresent() ? (PlayerUtils.getPotionEffectAmplifier(profile, PotionEffectType.JUMP) + 1) * 0.1f : 0);
        double offset = MathUtils.decimalRound(predictionY, 8) - MathUtils.decimalRound(data.getDeltaY(), 8);
        if (profile.isExempt().isJoined(5000L) || profile.isExempt().getTeleportTicks() < 2 || BetterStream.anyMatch(data.getFootBlocks(), material -> material.toString().contains("SLIME"))) offset = 0;
        if (Math.abs(offset) > 0.6 && increaseBufferBy(1) > 1)
            fail("o=" + offset);
        else decreaseBufferBy(0.1);
    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {}
}
