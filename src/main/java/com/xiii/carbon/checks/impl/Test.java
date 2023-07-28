package com.xiii.carbon.checks.impl;

import com.xiii.carbon.checks.annotation.Disabled;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

@Disabled
public class Test extends Check {
    public Test(final Profile profile) {
        super(profile, CheckType.TEST, "A", "Test Check for the Developers.");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {


       //debug("" + movementData.getFallDistance() / movementData.getLastFallDistance() + " "  + (movementData.getFallDistance() - movementData.getLastFallDistance()) + " " + movementData.getDeltaY());
    }

    @Override
    public void handle(ServerPlayPacket serverPlayPacket) {}
}
