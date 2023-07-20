package com.xiii.carbon.processors.packet;

import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.*;

public class ClientPlayPacket {

    private final PacketType.Play.Client type;
    private final long timeStamp;

    /*
    Use entity cache
     */
    private WrapperPlayClientInteractEntity interactEntityWrapper;
    private long lastAttack;
    private boolean attack;

    /*
    Animation cache
     */
    private WrapperPlayClientAnimation animation;
    private long lastAnimation;

    /*
    Block Dig/Place cache
     */
    private WrapperPlayClientPlayerDigging playerDiggingWrapper;
    private WrapperPlayClientPlayerBlockPlacement playerBlockPlacementWrapper;

    /*
    Window Click cache
     */
    private WrapperPlayClientClickWindow clickWindowWrapper;

    /*
    Entity Action cache
     */
    private WrapperPlayClientEntityAction entityActionWrapper;

    /*
    Chat cache
     */
    private WrapperPlayClientChatMessage chatWrapper;

    /*
    Movement - Flying cache
     */
    private WrapperPlayClientPlayerPosition positionWrapper;
    private WrapperPlayClientPlayerPositionAndRotation positionLookWrapper;
    private WrapperPlayClientPlayerRotation lookWrapper;
    private boolean movement, rotation, flying;

    public ClientPlayPacket(final PacketType.Play.Client type, final PacketPlayReceiveEvent packet, final long timeStamp) {
        this.timeStamp = timeStamp;

        switch (this.type = type) {

            case INTERACT_ENTITY:

                this.interactEntityWrapper = new WrapperPlayClientInteractEntity(packet);

                this.attack = this.interactEntityWrapper.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK;

                if (this.attack) this.lastAttack = this.timeStamp;

                break;

            case PLAYER_DIGGING:

                this.playerDiggingWrapper = new WrapperPlayClientPlayerDigging(packet);

                break;

            case PLAYER_BLOCK_PLACEMENT:

                this.playerBlockPlacementWrapper = new WrapperPlayClientPlayerBlockPlacement(packet);

                break;

            case CLICK_WINDOW:

                this.clickWindowWrapper = new WrapperPlayClientClickWindow(packet);

                break;

            case ENTITY_ACTION:

                this.entityActionWrapper = new WrapperPlayClientEntityAction(packet);

                break;

            case CHAT_MESSAGE:

                this.chatWrapper = new WrapperPlayClientChatMessage(packet);

                break;

            case PLAYER_FLYING:

                this.attack = false;
            case PLAYER_POSITION:

                this.positionWrapper = new WrapperPlayClientPlayerPosition(packet);

                this.flying = this.movement = true;

                break;

            case PLAYER_POSITION_AND_ROTATION:

                this.positionLookWrapper = new WrapperPlayClientPlayerPositionAndRotation(packet);

                this.flying = this.movement = this.rotation = true;

                break;

            case PLAYER_ROTATION:

                this.lookWrapper = new WrapperPlayClientPlayerRotation(packet);

                this.flying = this.rotation = true;

                break;

            case ANIMATION:

                this.animation = new WrapperPlayClientAnimation(packet);

                this.lastAnimation = this.timeStamp;
        }
    }

    public boolean isAttack() {
        return attack;
    }

    public boolean isMovement() {
        return movement;
    }

    public boolean isRotation() {
        return rotation;
    }

    public boolean isFlying() {
        return flying;
    }

    public WrapperPlayClientInteractEntity getInteractEntityWrapper() {
        return interactEntityWrapper;
    }

    public long getLastAttack() {
        return lastAttack;
    }

    public WrapperPlayClientPlayerDigging getPlayerDiggingWrapper() {
        return playerDiggingWrapper;
    }

    public WrapperPlayClientPlayerBlockPlacement getPlayerBlockPlacementWrapper() {
        return playerBlockPlacementWrapper;
    }

    public WrapperPlayClientClickWindow getClickWindowWrapper() {
        return clickWindowWrapper;
    }

    public WrapperPlayClientEntityAction getEntityActionWrapper() {
        return entityActionWrapper;
    }

    public WrapperPlayClientChatMessage getChatWrapper() {
        return chatWrapper;
    }

    public WrapperPlayClientPlayerPosition getPositionWrapper() {
        return positionWrapper;
    }

    public WrapperPlayClientPlayerPositionAndRotation getPositionLookWrapper() {
        return positionLookWrapper;
    }

    public WrapperPlayClientPlayerRotation getLookWrapper() {
        return lookWrapper;
    }

    public WrapperPlayClientAnimation getAnimation() {
        return animation;
    }

    public long getLastAnimation() {
        return lastAnimation;
    }

    public boolean is(PacketType.Play.Client type) {
        return this.type == type;
    }

    public PacketType.Play.Client getType() {
        return type;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
