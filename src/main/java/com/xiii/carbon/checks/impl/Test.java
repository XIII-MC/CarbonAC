package com.xiii.carbon.checks.impl;

import com.xiii.carbon.checks.annotation.Disabled;
import com.xiii.carbon.checks.annotation.Testing;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.playerdata.data.impl.RotationData;
import com.xiii.carbon.playerdata.processor.impl.SensitivityProcessor;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.BetterStream;
import com.xiii.carbon.utils.MathUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

@Testing
public class Test extends Check {
    public Test(final Profile profile) {
        super(profile, CheckType.TEST, "A", "Test Check for the Developers.");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {
       if (!clientPlayPacket.isMovement()) return;
       final MovementData movementData = profile.getMovementData();
       final List<Material> footBlocks = movementData.getFootBlocks();
       final boolean serverGroundFix = !BetterStream.anyMatch(footBlocks, material -> material.toString().equalsIgnoreCase("AIR"));
       final boolean serverGround = movementData.isServerGround(); //  && serverGroundFix
       final boolean clientGround = movementData.isOnGround();
       final boolean fix = !(((movementData.getBelowBlocks().size() > 1 || !BetterStream.anyMatch(movementData.getBelowBlocks(), material -> material.toString().equalsIgnoreCase("AIR"))) && !movementData.getBelowBlocks().isEmpty()));
       debug((clientGround == serverGround ? ChatColor.GREEN : ChatColor.RED) + "c=" + clientGround + " s=" + serverGround  + " fixServerGround=" + serverGroundFix + " footBlocks=" + movementData.getFootBlocks() + " " + footBlocks);
       if (fix && serverGround != clientGround) {

       }

       //debug("" + movementData.getFallDistance() / movementData.getLastFallDistance() + " "  + (movementData.getFallDistance() - movementData.getLastFallDistance()) + " " + movementData.getDeltaY());
    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {}
}
