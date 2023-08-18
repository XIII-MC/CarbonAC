package com.xiii.carbon.checks.impl.fly;

import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.processors.PredictionEngine;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.BetterStream;
import com.xiii.carbon.utils.MathUtils;
import com.xiii.carbon.utils.MoveUtils;

public class FlyA extends Check {
    public FlyA(final Profile profile) {
        super(profile, CheckType.FLY, "A", "Player is not biding to Minecraft's physics");
    }

    public double predictionLimit = 1.9262653090336062E-14;

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (!clientPlayPacket.isMovement()) return;

        final MovementData movementData = profile.getMovementData();

        final boolean exempt = profile.isExempt().isFly() || profile.isExempt().isWater(150L) || profile.isExempt().isLava(150L) || profile.isExempt().isTrapdoor_door() || profile.isExempt().isCobweb(50L) || profile.isExempt().isCake() || profile.getVehicleData().isRiding(150L) || (profile.isExempt().isJoined(2000L) && movementData.isServerGround()) || profile.isExempt().isClimable(50L) || profile.isExempt().tookDamage(50L) || profile.isExempt().getTeleportTicks() <= 2;

        final double deltaY = movementData.getDeltaY();

        final boolean nearGroundExempt = movementData.getNearGroundTicks() <= 4 && MathUtils.decimalRound(deltaY, 8) == -0.07840000;

        if (!movementData.isOnGround()) {

            final boolean blockAbove = (movementData.isBlockAbove(1) || movementData.lastBlockAbove(1)) && Math.abs(0.2000000476837 - MathUtils.decimalRound(deltaY, 13)) < predictionLimit;

            final boolean jumpGlitch = ((movementData.getClientGroundTicks() <= 1 || ((movementData.getClientGroundTicks() <= 3 || movementData.getServerGroundTicks() == 8 || movementData.getServerGroundTicks() == 9 || movementData.getServerGroundTicks() == 10 || movementData.getServerGroundTicks() == 14 || movementData.getServerGroundTicks() == 15) && (movementData.isBlockBelow(1) || movementData.isBlockFoot(1)))) && movementData.getLastNearWallTicks() <= 10 && (MathUtils.decimalRound(deltaY, 2) == 0.40 || MathUtils.decimalRound(deltaY, 2) == 0.33 || MathUtils.decimalRound(deltaY, 2) == -0.09 || MathUtils.decimalRound(deltaY, 2) == -0.15));

            final boolean jumpLowBlock = movementData.getClientGroundTicks() == 1 && deltaY == 0.5 && movementData.isBlockBelow(1);

            final boolean jumped = (!movementData.isOnGround() && movementData.isLastOnGround() && deltaY == MoveUtils.JUMP_MOTION) || jumpGlitch || jumpLowBlock;

            final double prediction = deltaY - (blockAbove ? deltaY : PredictionEngine.getVerticalPrediction(movementData.getLastDeltaY()));

            final boolean fix = !(((movementData.getBelowBlocks().size() > 1 || !BetterStream.anyMatch(movementData.getBelowBlocks(), material -> material.toString().equalsIgnoreCase("AIR"))) && !movementData.getBelowBlocks().isEmpty()));

            if (!nearGroundExempt && !exempt && prediction > predictionLimit && !jumped && fix) {
                fail("prediction: §c" + prediction + System.lineSeparator() + "§rdeltaY: §c" + deltaY + System.lineSeparator() + "§rnearGroundExempt: §c" + nearGroundExempt + " §rnGT: §c" + movementData.getNearGroundTicks() + " §rdRdY: §c" + MathUtils.decimalRound(deltaY, 8) + System.lineSeparator() + "§rblockAbove: §c" + blockAbove + " §rbA: §c" + movementData.getBlockAboveTicks() + " §rlBA: §c" + movementData.getLastBlockAboveTicks() + " §rabs: §c" + Math.abs(0.2000000476837 - MathUtils.decimalRound(deltaY, 13)) + System.lineSeparator() + "§rjumpGlitch: §c" + jumpGlitch + " §rcGT: §c" + movementData.getClientGroundTicks() + " §rcST: §c" + movementData.getServerGroundTicks() + " §rbBT: §c" + movementData.getBlockBelowTicks() + " §rbFT: §c" + movementData.getBlockFootTicks() + " §rlNGT: §c" + movementData.getLastNearWallTicks() + " §rdRdY: §c" + MathUtils.decimalRound(deltaY, 2) + System.lineSeparator() + "§rjumpLowBlock: §c" + movementData.getClientGroundTicks() + " dY: " + deltaY + " bBT: " + movementData.getBlockBelowTicks() + System.lineSeparator() + "§rjumped: §c" + jumped + " §rcGT: §c" + movementData.getClientGroundTicks() + System.lineSeparator() + "§rfix: §c" + fix + " §rbBS: §c" + movementData.getBelowBlocks().size());
                //debug((prediction - movementData.getAccelY()) + " " + movementData.getLastServerGroundTicks() + " " +   + " " + movementData.getBelowBlocks());
               // debug("DR-dy=" + MathUtils.decimalRound(deltaY, 14) + " g=" + movementData.getClientGroundTicks() + " sg=" + movementData.getServerGroundTicks() + " bbT=" + movementData.getBlockBelowTicks() + " fbT=" + movementData.getBlockFootTicks() + " NWT=" + movementData.getLastNearWallTicks());
               // debug("" + blockAbove + " " + prediction + " " + movementData.getAirTicks() + " " + movementData.getAccelY() + " " + movementData.getClientGroundTicks() + " " + movementData.getBlockFootTicks());
            }
        }
    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}
