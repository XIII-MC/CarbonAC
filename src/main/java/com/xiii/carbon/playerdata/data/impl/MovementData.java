package com.xiii.carbon.playerdata.data.impl;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPosition;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import com.xiii.carbon.Carbon;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.nms.NmsInstance;
import com.xiii.carbon.playerdata.data.Data;
import com.xiii.carbon.playerdata.processor.impl.SetbackProcessor;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.CollisionUtils;
import com.xiii.carbon.utils.MoveUtils;
import com.xiii.carbon.utils.custom.CustomLocation;
import com.xiii.carbon.utils.custom.Equipment;
import com.xiii.carbon.utils.fastmath.FastMath;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MovementData implements Data {

    private final Profile profile;

    private final Equipment equipment;

    private final SetbackProcessor setbackProcessor;

    private double deltaX, lastDeltaX, deltaZ, lastDeltaZ, deltaY, lastDeltaY, deltaXZ, lastDeltaXZ,
            accelXZ, lastAccelXZ, accelY, lastAccelY;

    private float fallDistance, lastFallDistance,
            baseGroundSpeed, baseAirSpeed,
            frictionFactor = MoveUtils.FRICTION_FACTOR, lastFrictionFactor = MoveUtils.FRICTION_FACTOR;

    private CustomLocation location;
    private CustomLocation lastLocation;

    private List<Material> nearbyBlocks = null, aboveBlocks = null, middleBlocks = null, belowBlocks = null, footBlocks = new ArrayList<>();

    private boolean onGround, lastOnGround, serverGround, lastServerGround, isBlockAbove, lastBlockAbove, isBlockMiddle, isBlockBelow, isBlockUnder, isBlockFoot;

    private int flyTicks, serverGroundTicks, lastServerGroundTicks, nearGroundTicks, lastNearGroundTicks, jumpedTicks,
            lastUnloadedChunkTicks = 100,
            clientGroundTicks, lastNearWallTicks, airTicks, serverAirTicks,
            lastFrictionFactorUpdateTicks, lastNearEdgeTicks,
            lastFlyingAbility = 10000,
            blockAboveTicks, lastBlockAboveTicks, blockMiddleTicks, blockBelowTicks, blockUnderTicks, blockFootTicks;

    public MovementData(final Profile profile) {
        this.profile = profile;

        this.equipment = new Equipment();
        this.setbackProcessor = new SetbackProcessor(profile);

        /*
        Initialize the current location.
         */
        this.location = this.lastLocation = new CustomLocation(profile.getPlayer().getLocation());
    }

    @Override
    public void process(final ClientPlayPacket clientPlayPacket) {

        final World world = profile.getPlayer().getWorld();

        final long currentTime = clientPlayPacket.getTimeStamp();

        switch (clientPlayPacket.getType()) {

            case PLAYER_POSITION:

                final WrapperPlayClientPlayerPosition move = clientPlayPacket.getPositionWrapper();

                this.lastOnGround = this.onGround;
                this.onGround = move.isOnGround();

                this.flyTicks = this.onGround ? 0 : this.flyTicks + 1;
                this.clientGroundTicks = this.onGround ? this.clientGroundTicks + 1 : 0;

                this.lastLocation = this.location;
                this.location = new CustomLocation(
                        world,
                        move.getLocation().getX(), move.getLocation().getY(), move.getLocation().getZ(),
                        this.location.getYaw(), this.location.getPitch(),
                        currentTime
                );

                processLocationData();

                break;

            case PLAYER_POSITION_AND_ROTATION:

                //1.17+
                if (profile.getActionData().getLastDuplicateOnePointSeventeenPacketTicks() == 0) break;

                final WrapperPlayClientPlayerPositionAndRotation posLook = clientPlayPacket.getPositionLookWrapper();

                this.lastOnGround = this.onGround;
                this.onGround = posLook.isOnGround();

                this.flyTicks = this.onGround ? 0 : this.flyTicks + 1;
                this.clientGroundTicks = this.onGround ? this.clientGroundTicks + 1 : 0;

                this.lastLocation = this.location;
                this.location = new CustomLocation(
                        world,
                        posLook.getLocation().getX(), posLook.getLocation().getY(), posLook.getLocation().getZ(),
                        posLook.getYaw(), posLook.getPitch(),
                        currentTime
                );

                processLocationData();

                break;

            case PLAYER_ROTATION:

                final WrapperPlayClientPlayerRotation look = clientPlayPacket.getLookWrapper();

                this.lastOnGround = this.onGround;
                this.onGround = look.isOnGround();

                this.flyTicks = this.onGround ? 0 : this.flyTicks + 1;
                this.clientGroundTicks = this.onGround ? this.clientGroundTicks + 1 : 0;

                this.lastLocation = this.location;
                this.location = new CustomLocation(
                        world,
                        this.location.getX(), this.location.getY(), this.location.getZ(),
                        look.getYaw(), look.getPitch(),
                        currentTime
                );

                processLocationData();

                break;
        }
    }

    @Override
    public void process(final ServerPlayPacket serverPlayPacket) {}

    private void processLocationData() {

        final double lastDeltaX = this.deltaX;
        final double deltaX = this.location.getX() - this.lastLocation.getX();

        this.lastDeltaX = lastDeltaX;
        this.deltaX = deltaX;

        final double lastDeltaY = this.deltaY;
        final double deltaY = this.location.getY() - this.lastLocation.getY();

        this.lastDeltaY = lastDeltaY;
        this.deltaY = deltaY;

        final double lastAccelY = this.accelY;
        final double accelY = Math.abs(lastDeltaY - deltaY);

        this.lastAccelY = lastAccelY;
        this.accelY = accelY;

        final double lastDeltaZ = this.deltaZ;
        final double deltaZ = this.location.getZ() - this.lastLocation.getZ();

        this.lastDeltaZ = lastDeltaZ;
        this.deltaZ = deltaZ;

        final double lastDeltaXZ = this.deltaXZ;
        final double deltaXZ = FastMath.hypot(deltaX, deltaZ);

        this.lastDeltaXZ = lastDeltaXZ;
        this.deltaXZ = deltaXZ;

        final double lastAccelXZ = this.accelXZ;
        final double accelXZ = Math.abs(lastDeltaXZ - deltaXZ);

        this.lastAccelXZ = lastAccelXZ;
        this.accelXZ = accelXZ;

        //Process data
        processPlayerData();
    }

    private void handleNearbyBlocks() {

        /*
        Get the nearby block result from the current location.
         */
        final CollisionUtils.NearbyBlocksResult nearbyBlocksResult = CollisionUtils.getNearbyBlocks(this.location, false);

        /*
        Get the block below the player.
         */
        this.isBlockUnder = CollisionUtils.hasBlockUnder(this.location, new CustomLocation(this.location.getWorld(), this.location.getX(), this.location.getY() - 0.1, this.location.getZ()));

        /*
        Handle collisions
        NOTE: You should ALWAYS use NMS if you plan on supporting 1.9+
        For a production server, DO NOT use spigot's api. It's slow. (Especially for Blocks, Chunks, Materials)
         */

        this.nearbyBlocks = nearbyBlocksResult.getBlockTypes();

        this.aboveBlocks = nearbyBlocksResult.getBlockAboveTypes();


        this.middleBlocks = nearbyBlocksResult.getBlockMiddleTypes();

        this.belowBlocks = nearbyBlocksResult.getBlockBelowTypes();

        this.footBlocks = nearbyBlocksResult.getBlockFootTypes();

        this.isBlockAbove = nearbyBlocksResult.getBlockAboveTypes().size() > 1 || !nearbyBlocksResult.getBlockAboveTypes().contains(Material.AIR);

        this.isBlockMiddle = nearbyBlocksResult.getBlockMiddleTypes().size() > 1 || !nearbyBlocksResult.getBlockMiddleTypes().contains(Material.AIR);

        this.isBlockBelow = nearbyBlocksResult.getBlockBelowTypes().size() > 1 || !nearbyBlocksResult.getBlockBelowTypes().contains(Material.AIR);

        this.isBlockFoot = nearbyBlocksResult.getBlockFootTypes().size() > 1  || !nearbyBlocksResult.getBlockFootTypes().contains(Material.AIR);

        this.lastBlockAbove = this.isBlockAbove;

        this.blockAboveTicks = this.isBlockAbove ? 0 : this.blockAboveTicks + 1;

        this.lastBlockAboveTicks = this.lastBlockAbove ? 0 : this.lastBlockAboveTicks + 1;

        this.blockMiddleTicks = this.isBlockMiddle ? 0 : this.blockMiddleTicks + 1;

        this.blockBelowTicks = this.isBlockBelow ? 0 : this.blockBelowTicks + 1;

        this.blockFootTicks = this.isBlockFoot ? 0 : this.blockFootTicks + 1;
    }

    private void processPlayerData() {

        final Player p = profile.getPlayer();

        final NmsInstance nms = Carbon.getInstance().getNmsManager().getNmsInstance();

        //Chunk

        if ((this.lastUnloadedChunkTicks = nms.isChunkLoaded(
                this.location.getWorld(), this.location.getBlockX(), this.location.getBlockZ())
                ? this.lastUnloadedChunkTicks + 1 : 0) > 10) {

            //Nearby Entities

            //this.nearbyEntityProcessor.process();

            //Nearby Blocks

            handleNearbyBlocks();

            //Friction Factor

            this.frictionFactor = CollisionUtils.getBlockSlipperiness(
                    nms.getType(this.location.clone().subtract(0D, .825D, 0D).getBlock())
            );

            this.lastFrictionFactorUpdateTicks = this.frictionFactor != this.lastFrictionFactor ? 0 : this.lastFrictionFactorUpdateTicks + 1;

            this.lastFrictionFactor = this.frictionFactor;
        }

        //Flying

        this.lastFlyingAbility = nms.isFlying(p) ? 0 : this.lastFlyingAbility + 1;

        //Effects

        //this.effectsProcessor.process();

        //Custom Speed

        //this.customSpeedProcessor.process();

        //Near Ground

        this.nearGroundTicks = !this.nearbyBlocks.get(0).equals(Material.AIR) ? this.nearGroundTicks + 1 : 0;

        //Setbacks

        if (this.nearGroundTicks > 1) this.setbackProcessor.process();

        //Near Wall

        this.lastNearWallTicks = CollisionUtils.isNearWall(this.location) ? 0 : this.lastNearWallTicks + 1;

        //Near Edge

        this.lastNearEdgeTicks = this.lastNearGroundTicks == 0 && CollisionUtils.isNearEdge(this.location) ? 0 : this.lastNearEdgeTicks + 1;

        //Server Ground

        final boolean lastServerGround = this.serverGround;

        final boolean serverGround = CollisionUtils.isServerGround(this.location.getY());

        this.lastServerGround = lastServerGround;

        this.serverGround = serverGround;

        this.serverGroundTicks = serverGround ? this.serverGroundTicks + 1 : 0;

        this.lastServerGroundTicks = serverGround ? 0 : this.serverGroundTicks + 1;

        this.airTicks = onGround ? 0 : this.airTicks + 1;

        this.serverAirTicks = serverGround ? 0 : this.airTicks + 1;

        this.lastNearGroundTicks = this.nearGroundTicks;

        //Equipment

        this.equipment.handle(p);

        //Fall Distance

        this.lastFallDistance = this.fallDistance;

        this.fallDistance = nms.getFallDistance(p);

        //Base Speed

        this.baseGroundSpeed = MoveUtils.getBaseGroundSpeed(profile);

        this.baseAirSpeed = MoveUtils.getBaseAirSpeed(profile);

        //Ghost Blocks

        //this.ghostBlockProcessor.process();

        //Jump

        this.jumpedTicks = ((this.deltaY > 0 && this.lastDeltaY % (1D / 64) == 0 && !this.isOnGround()) && !(this.deltaY % 0.015625 == 0) && (this.lastDeltaY % 0.015625 == 0)) ? 0 : this.jumpedTicks + 1;
    }

    public int getLastNearEdgeTicks() {
        return lastNearEdgeTicks;
    }

    public int getLastFrictionFactorUpdateTicks() {
        return lastFrictionFactorUpdateTicks;
    }

    public float getFrictionFactor() {
        return frictionFactor;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public float getBaseAirSpeed() {
        return baseAirSpeed;
    }

    public float getBaseGroundSpeed() {
        return baseGroundSpeed;
    }

    public int getLastNearWallTicks() {
        return lastNearWallTicks;
    }

    public int getAirTicks() {
        return airTicks;
    }

    public int getServerAirTicks() {
        return serverAirTicks;
    }

    public int getClientGroundTicks() {
        return clientGroundTicks;
    }

    public int getLastUnloadedChunkTicks() {
        return lastUnloadedChunkTicks;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getLastDeltaX() {
        return lastDeltaX;
    }

    public double getDeltaZ() {
        return deltaZ;
    }

    public double getLastDeltaZ() {
        return lastDeltaZ;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getLastDeltaY() {
        return lastDeltaY;
    }

    public double getDeltaXZ() {
        return deltaXZ;
    }

    public double getLastDeltaXZ() {
        return lastDeltaXZ;
    }

    public double getAccelXZ() {
        return accelXZ;
    }

    public double getLastAccelXZ() {
        return lastAccelXZ;
    }

    public double getAccelY() {
        return accelY;
    }

    public double getLastAccelY() {
        return lastAccelY;
    }

    public float getFallDistance() {
        return fallDistance;
    }

    public float getLastFallDistance() {
        return lastFallDistance;
    }

    public CustomLocation getLocation() {
        return location;
    }

    public CustomLocation getLastLocation() {
        return lastLocation;
    }

    public SetbackProcessor getSetbackProcessor() {
        return setbackProcessor;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isServerGround() {
        return serverGround;
    }

    public int getFlyTicks() {
        return flyTicks;
    }

    public int getLastFlyingAbility() {
        return lastFlyingAbility;
    }

    public int getLastServerGroundTicks() {
        return lastServerGroundTicks;
    }

    public int getServerGroundTicks() {
        return serverGroundTicks;
    }

    public boolean isLastOnGround() {
        return lastOnGround;
    }

    public boolean isLastServerGround() {
        return lastServerGround;
    }

    public int getNearGroundTicks() {
        return nearGroundTicks;
    }

    public int getLastNearGroundTicks() {
        return lastNearGroundTicks;
    }

    public boolean isJumpedTicks(final int ticks) {
        return jumpedTicks <= ticks;
    }

    public int getJumpedTicks() {
        return jumpedTicks;
    }

    public List<Material> getNearbyBlocks() {
        return nearbyBlocks;
    }

    public List<Material> getAboveBlocks() {
        return aboveBlocks;
    }

    public List<Material> getMiddleBlocks() {
        return middleBlocks;
    }

    public List<Material> getBelowBlocks() {
        return belowBlocks;
    }

    public List<Material> getFootBlocks() {
        return footBlocks;
    }

    public boolean isBlockAbove(final int ticks) {
        return blockAboveTicks <= ticks;
    }

    public boolean lastBlockAbove(final int ticks) {
        return lastBlockAboveTicks <= ticks;
    }

    public boolean isBlockMiddle(final int ticks) {
        return blockMiddleTicks <= ticks;
    }

    public boolean isBlockBelow(final int ticks) {
        return blockBelowTicks <= ticks;
    }

    public boolean isBlockUnder(final int ticks) {
        return blockUnderTicks <= ticks;
    }

    public boolean isBlockFoot(final int ticks) {
        return blockFootTicks <= ticks;
    }

    public int getBlockAboveTicks() {
        return blockAboveTicks;
    }

    public int getLastBlockAboveTicks() {
        return lastBlockAboveTicks;
    }

    public int getBlockMiddleTicks() {
        return blockMiddleTicks;
    }

    public int getBlockBelowTicks() {
        return blockBelowTicks;
    }

    public int getBlockFootTicks() {
        return blockFootTicks;
    }
}
