package com.xiii.carbon.playerdata.data.impl;

import com.xiii.carbon.playerdata.data.Data;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

public class TeleportData implements Data {

    private int teleportTicks;

    @Override
    public void process(final ClientPlayPacket clientPlayPacket) {}

    @Override
    public void process(final ServerPlayPacket serverPlayPacket) {}

    public int getTeleportTicks() {
        return teleportTicks;
    }
}
