package com.xiii.carbon.playerdata.data.impl;

import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.Data;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.MathUtils;

public class VehicleData implements Data {

    private final Profile profile;

    public VehicleData(final Profile profile) {
        this.profile = profile;
    }

    private long lastRide = 10000L;

    @Override
    public void process(final ClientPlayPacket clientPlayPacket) {

        if (profile.getPlayer().getVehicle() != null) this.lastRide = System.currentTimeMillis();
    }

    @Override
    public void process(final ServerPlayPacket serverPlayPacket) {

    }

    public boolean isRiding(final long delay) {
        return MathUtils.elapsed(lastRide) <= delay;
    }
}