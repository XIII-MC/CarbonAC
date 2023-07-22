package com.xiii.carbon.checks.impl.packet;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.xiii.carbon.Carbon;
import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.checks.types.Check;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.nms.NmsInstance;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

public class PacketA extends Check {
    public PacketA(final Profile profile) {
        super(profile, CheckType.PACKET, "A", "Sent abilities packet without confirmation");
    }

    @Override
    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (clientPlayPacket.is(PacketType.Play.Client.PLAYER_ABILITIES)) {

            final NmsInstance nmsInstance = Carbon.getInstance().getNmsManager().getNmsInstance();

            if (nmsInstance.getAllowFlight(profile.getPlayer())) fail();
        }

    }

    @Override
    public void handle(final ServerPlayPacket serverPlayPacket) {}
}
