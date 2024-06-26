package com.xiii.carbon.exempt;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.xiii.carbon.Carbon;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.MovementData;
import com.xiii.carbon.playerdata.data.impl.RotationData;
import com.xiii.carbon.playerdata.data.impl.TeleportData;
import com.xiii.carbon.utils.BetterStream;
import com.xiii.carbon.utils.MathUtils;
import com.xiii.carbon.utils.versionutils.VersionUtils;
import org.bukkit.Material;

import java.util.List;

public class Exempt {

    public Exempt(final Profile profile) {
        this.profile = profile;
    }

    private final Profile profile;

    private boolean fly, water, lava, climable, cobweb, trapdoor_door, cake, cinematic, step;

    private long lastWater, lastLava, lastClimable, lastCobweb, joined = System.currentTimeMillis(), lastStep;

    private int teleportTicks;

    public void handleExempts(long timeStamp) {

        final MovementData movementData = profile.getMovementData();
        final TeleportData teleportData = profile.getTeleportData();
        final RotationData rotationData = profile.getRotationData();

        final List<Material> nearbyBlocks = movementData.getNearbyBlocks();

        //Fly
        this.fly = movementData.getLastFlyingAbility() < (20*5); //5s

        /*
        matchMaterial is not supported on 1.8.
        java.lang.NoSuchMethodError: org.bukkit.Material.matchMaterial(Ljava/lang/String;Z)Lorg/bukkit/Material;
         */

        //Water Liquid
        this.water = BetterStream.anyMatch(nearbyBlocks, mat -> mat.toString().contains("WATER") || (VersionUtils.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_13) && mat.toString().contains("BUBBLE_COLUMN")));

        //Lava Liquid
        this.lava = BetterStream.anyMatch(nearbyBlocks, mat -> mat.toString().contains("LAVA"));

        //Climables
        this.climable = BetterStream.anyMatch(nearbyBlocks, mat -> mat.toString().contains("LADDER") || mat.toString().contains("VINE"));

        //Cobweb
        this.cobweb = BetterStream.anyMatch(nearbyBlocks, mat -> mat.toString().contains("COBWEB"));

        //Trapdoors
        this.trapdoor_door = BetterStream.anyMatch(nearbyBlocks, mat -> mat.toString().contains("DOOR"));

        //Cakes
        this.cake = BetterStream.anyMatch(nearbyBlocks, mat -> mat.toString().contains("CAKE"));

        //Teleport
        this.teleportTicks = teleportData.getTeleportTicks();

        //Cinematic
        this.cinematic = rotationData.getCinematicProcessor().isCinematic();

        //Step
        this.step = BetterStream.anyMatch(nearbyBlocks, mat -> mat.toString().contains("STEP") || mat.toString().contains("STAIR"));

        if (this.water) this.lastWater = timeStamp;

        if (this.lava) this.lastLava = timeStamp;

        if (this.climable) this.lastClimable = timeStamp;

        if (this.cobweb) this.lastCobweb = timeStamp;

        if (this.step) this.lastStep = timeStamp;
    }

    public boolean isFly() {
        return fly;
    }

    public boolean isWater(final long delay) {
        return MathUtils.elapsed(lastWater) <= delay;
    }

    public boolean isWall(final long delay) {
        return profile.getMovementData().getLastNearWallTicks() <= delay; // 0.25s | in ticks 1t=0.05s (5*0.05=0.25)
    }

    public boolean isLava(final long delay) {
        return MathUtils.elapsed(lastLava) <= delay;
    }

    public boolean isJoined(final long delay) {
        return MathUtils.elapsed(joined) <= delay;
    }

    public boolean isClimable(final long delay) {
        return MathUtils.elapsed(lastClimable) <= delay;
    }

    public boolean isCobweb(final long delay) {
        return MathUtils.elapsed(lastCobweb) <= delay;
    }

    public boolean tookDamage(final long delay) {
        return MathUtils.elapsed(profile.getCombatData().getLastDamageTaken()) <= delay;
    }

    public boolean tookHandDamage(final long delay) {
        return MathUtils.elapsed(profile.getCombatData().getLastHandDamageTaken()) <= delay;
    }

    public boolean isTrapdoor_door() {
        return trapdoor_door;
    }

    public boolean isCake() {
        return cake;
    }

    public int getTeleportTicks() {
        return teleportTicks;
    }

    public boolean isCinematic() {
        return cinematic;
    }

    public boolean isVehicle() {
        return Carbon.getInstance().getNmsManager().getNmsInstance().isInsideVehicle(profile.getPlayer());
    }

    public boolean isStep() {
        return step;
    }

    public long getLastStep() {
        return lastStep;
    }
}
