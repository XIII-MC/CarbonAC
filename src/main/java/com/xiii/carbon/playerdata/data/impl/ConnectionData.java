package com.xiii.carbon.playerdata.data.impl;

import com.xiii.carbon.playerdata.data.Data;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

public class ConnectionData implements Data {

    @Override
    public void process(final ClientPlayPacket clientPlayPacket) {}

    @Override
    public void process(final ServerPlayPacket serverPlayPacket) {}
}
