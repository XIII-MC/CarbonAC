package com.xiii.carbon.playerdata.data.impl;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerPositionAndRotation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerRotation;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.Data;
import com.xiii.carbon.playerdata.processor.impl.CinematicProcessor;
import com.xiii.carbon.playerdata.processor.impl.SensitivityProcessor;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.tasks.TickTask;
import com.xiii.carbon.utils.ChatUtils;
import com.xiii.carbon.utils.MathUtils;

import java.util.logging.Level;

public class RotationData implements Data {

    private final Profile profile;

    private final SensitivityProcessor sensitivityProcessor;
    private final CinematicProcessor cinematicProcessor;

    private float yaw, lastYaw, pitch, lastPitch, deltaYaw, lastDeltaYaw,
            deltaPitch, lastDeltaPitch, yawAccel, lastYawAccel, pitchAccel, lastPitchAccel;

    private int rotationsAfterTeleport, lastRotationTicks;

    public RotationData(Profile profile) {
        this.profile = profile;
        this.sensitivityProcessor = new SensitivityProcessor(profile);
        this.cinematicProcessor = new CinematicProcessor(profile);
    }

    private int invalidSnapThreshold;

    @Override
    public void process(final ClientPlayPacket clientPlayPacket) {

        switch (clientPlayPacket.getType()) {

            case PLAYER_POSITION_AND_ROTATION:

                final WrapperPlayClientPlayerPositionAndRotation posLookWrapper = clientPlayPacket.getPositionLookWrapper();

                processRotation(posLookWrapper.getYaw(), posLookWrapper.getPitch());

                break;

            case PLAYER_ROTATION:

                final WrapperPlayClientPlayerRotation lookWrapper = clientPlayPacket.getLookWrapper();

                processRotation(lookWrapper.getYaw(), lookWrapper.getPitch());

                break;

            case TELEPORT_CONFIRM:

                this.rotationsAfterTeleport = 0;

                break;
        }
    }

    @Override
    public void process(final ServerPlayPacket serverPlayPacket) {}

    public int getRotationsAfterTeleport() {
        return rotationsAfterTeleport;
    }

    private void processRotation(float yaw, float pitch) {

        //Duplicate rotation packet (1.17+)
        if (profile.getVersion().isNewerThanOrEquals(ClientVersion.V_1_17)
                && profile.getTeleportData().getTeleportTicks() > 1
                && yaw == this.yaw
                && pitch == this.pitch
                && profile.getActionData().getLastRidingTicks() > 1) return;

        final float lastYaw = this.yaw;

        this.lastYaw = lastYaw;
        this.yaw = yaw;

        final float lastPitch = this.pitch;

        this.lastPitch = lastPitch;
        this.pitch = pitch;

        final float lastDeltaYaw = this.deltaYaw;

        /*
        Clamp the deltaYaw to similarize the behavior between 1.8 -> latest versions of minecraft.
        (In 1.9 the packet's data is sent differently)
         */
        final float deltaYaw = Math.abs(MathUtils.clamp180(Math.abs(yaw - lastYaw)));

        this.lastDeltaYaw = lastDeltaYaw;
        this.deltaYaw = deltaYaw;

        final float lastDeltaPitch = this.deltaPitch;
        final float deltaPitch = Math.abs(pitch - lastPitch);

        this.lastDeltaPitch = lastDeltaPitch;
        this.deltaPitch = deltaPitch;

        final float lastYawAccel = this.yawAccel;
        final float yawAccel = Math.abs(deltaYaw - lastDeltaYaw);

        this.lastYawAccel = lastYawAccel;
        this.yawAccel = yawAccel;

        final float lastPitchAccel = this.pitchAccel;
        final float pitchAccel = Math.abs(deltaPitch - lastDeltaPitch);

        this.lastPitchAccel = lastPitchAccel;
        this.pitchAccel = pitchAccel;

        //Process sensitivity
        this.sensitivityProcessor.process();

        //Process cinematic
        this.cinematicProcessor.process();

        this.rotationsAfterTeleport++;

        this.lastRotationTicks = TickTask.getCurrentTick();

        /*
        This fixes the infamous bug which gets triggered when moving your client to the side in a small window
        And makes you snap around insanely fast, This will not affect legitimate players who snap around at any
        Sensitivity or DPI since it's almost impossible for your deltaYaw to be the same as the rotation constant
        While the acceleration is also zero for 10 times.

        This bug is very problematic since after it gets triggered all of your rotations will have
        An identical pattern, Which can lead to exploits, bugs or even falses.
        (Yes this is more problematic than you think, Due to the way sensitivity works in the minecraft client with the GUI)
         */
        if (this.deltaYaw > 10F
                && this.deltaPitch == 0F
                && this.yawAccel == 0F
                && this.deltaYaw == this.sensitivityProcessor.getConstantYaw()
                && this.rotationsAfterTeleport > 5) {

            if (this.invalidSnapThreshold++ > 10) {

                ChatUtils.log(Level.SEVERE, "Kicking " + profile.getPlayer().getName() + " for triggering the snap bug.");

                profile.kickPlayer("Invalid Rotation Packet");

                this.invalidSnapThreshold = 0;
            }

        } else this.invalidSnapThreshold = 0;
    }

    public SensitivityProcessor getSensitivityProcessor() {
        return sensitivityProcessor;
    }

    public CinematicProcessor getCinematicProcessor() {
        return cinematicProcessor;
    }

    public float getYaw() {
        return yaw;
    }

    public float getLastYaw() {
        return lastYaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getLastPitch() {
        return lastPitch;
    }

    public float getDeltaYaw() {
        return deltaYaw;
    }

    public float getLastDeltaYaw() {
        return lastDeltaYaw;
    }

    public float getDeltaPitch() {
        return deltaPitch;
    }

    public float getLastDeltaPitch() {
        return lastDeltaPitch;
    }

    public float getYawAccel() {
        return yawAccel;
    }

    public float getLastYawAccel() {
        return lastYawAccel;
    }

    public float getPitchAccel() {
        return pitchAccel;
    }

    public float getLastPitchAccel() {
        return lastPitchAccel;
    }

    public int getLastRotationTicks() {
        return MathUtils.elapsedTicks(this.lastRotationTicks);
    }
}
