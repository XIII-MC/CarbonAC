package com.xiii.carbon.utils;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;

public final class PacketUtils {

    private static long lastPacket, lastFlying;
    private static boolean sent;
    private static double buffer;

    public static boolean isPost(final PacketType.Play.Client packet, final PacketType.Play.Client postPacket) {

        if (packet == PacketType.Play.Client.PLAYER_POSITION || packet == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION || packet == PacketType.Play.Client.PLAYER_ROTATION) {

            final long now = System.currentTimeMillis();
            final long delay = now - lastPacket;

            if (sent) {
                if (delay > 40L && delay < 100L) {
                    buffer += 0.25;

                    if (buffer > 0.5) {
                        return true;
                    }
                } else {
                    buffer = Math.max(buffer - 0.025, 0);
                }

                sent = false;
            }

            lastFlying = now;
        } else if (packet == postPacket) {

            final long now = System.currentTimeMillis();
            final long delay = now - lastFlying;

            if (delay < 10L) {
                lastPacket = now;
                sent = true;
            } else {
                buffer = Math.max(buffer - 0.025, 0.0);
            }
        }

        return false;
    }
}
