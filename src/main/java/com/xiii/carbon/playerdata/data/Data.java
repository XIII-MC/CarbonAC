package com.xiii.carbon.playerdata.data;

import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

public interface Data {

    void process(final ClientPlayPacket clientPlayPacket);

    void process(final ServerPlayPacket serverPlayPacket);
}
