package com.xiii.carbon.processors.packet;

import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.*;

public class ServerPlayPacket {

    private final PacketType.Play.Server type;
    private final PacketPlaySendEvent event;
    private final long timeStamp;

    /*
    Entity Velocity cache
     */
    private WrapperPlayServerEntityVelocity entityVelocityWrapper;

    /*
    Arm Animation cache
     */
    private WrapperPlayServerEntityAnimation entityAnimationWrapper;

    /*
    Entity Metadata cache
     */
    private WrapperPlayServerEntityMetadata entityMetadataWrapper;

    /*
    Entity status cache
     */
    private WrapperPlayServerEntityStatus entityStatusWrapper;

    /*
    Block action cache
     */
    private WrapperPlayServerBlockAction blockActionWrapper;

    /*
    Player Position Look cache
     */
    private WrapperPlayServerPlayerPositionAndLook playerPositionAndLookWrapper;

    public ServerPlayPacket(final PacketType.Play.Server type, final PacketPlaySendEvent packet, final long timeStamp) {
        this.timeStamp = timeStamp;
        this.event = packet;

        switch (this.type = type) {

            case ENTITY_VELOCITY:

                this.entityVelocityWrapper = new WrapperPlayServerEntityVelocity(packet);

                break;

            case ENTITY_ANIMATION:

                this.entityAnimationWrapper = new WrapperPlayServerEntityAnimation(packet);

                break;

            case ENTITY_METADATA:

                this.entityMetadataWrapper = new WrapperPlayServerEntityMetadata(packet);

                break;

            case ENTITY_STATUS:

                this.entityStatusWrapper = new WrapperPlayServerEntityStatus(packet);

                break;

            case BLOCK_ACTION:

                this.blockActionWrapper = new WrapperPlayServerBlockAction(packet);

                break;

            case PLAYER_POSITION_AND_LOOK:

                this.playerPositionAndLookWrapper = new WrapperPlayServerPlayerPositionAndLook(packet);

                break;
        }
    }

    public WrapperPlayServerEntityVelocity getEntityVelocityWrapper() {
        return entityVelocityWrapper;
    }

    public WrapperPlayServerEntityAnimation getEntityAnimationWrapper() {
        return entityAnimationWrapper;
    }

    public WrapperPlayServerEntityMetadata getEntityMetadataWrapper() {
        return entityMetadataWrapper;
    }

    public WrapperPlayServerEntityStatus getEntityStatusWrapper() {
        return entityStatusWrapper;
    }

    public WrapperPlayServerBlockAction getBlockActionWrapper() {
        return blockActionWrapper;
    }

    public WrapperPlayServerPlayerPositionAndLook getPlayerPositionAndLookWrapper() {
        return playerPositionAndLookWrapper;
    }

    public boolean is(PacketType.Play.Server type) {
        return this.type == type;
    }

    public PacketType.Play.Server getType() {
        return type;
    }

    public PacketPlaySendEvent getEvent() {
        return event;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
