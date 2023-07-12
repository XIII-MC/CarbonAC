package com.xiii.carbon.playerdata.processor.impl;

import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.playerdata.data.impl.RotationData;
import com.xiii.carbon.playerdata.processor.Processor;
import com.xiii.carbon.utils.MathUtils;

import java.util.ArrayDeque;

/**
 * A sensitivity processor class that we'll be using in order to hold certain data
 * <p>
 * NOTE: This does not include a way to grab the player's sensitivity,
 * Feel free to add your own method since every person does it differently.
 */
public class SensitivityProcessor implements Processor {

    private final Profile profile;

    private double mouseX, mouseY, constantYaw, constantPitch, yawGcd, pitchGcd, testGCD;

    private int sensitivity;

    private ArrayDeque<Integer> sensitivitySamples = new ArrayDeque<>();

    public SensitivityProcessor(final Profile profile) {
        this.profile = profile;
    }

    @Override
    public void process() {

        final RotationData data = profile.getRotationData();

        final float deltaYaw = data.getDeltaYaw();
        final float deltaPitch = data.getDeltaPitch();

        final float lastDeltaYaw = data.getLastDeltaYaw();
        final float lastDeltaPitch = data.getLastDeltaPitch();

        this.yawGcd = MathUtils.getAbsoluteGcd(deltaYaw, lastDeltaYaw);
        this.pitchGcd = MathUtils.getAbsoluteGcd(deltaPitch, lastDeltaPitch);

        this.constantYaw = this.yawGcd / MathUtils.EXPANDER;
        this.constantPitch = this.pitchGcd / MathUtils.EXPANDER;

        this.mouseX = (int) (deltaYaw / this.constantYaw);
        this.mouseY = (int) (deltaPitch / this.constantPitch);

        handleSensitivity();
    }

    private void handleSensitivity() {
        final RotationData data = profile.getRotationData();
        if (data.getDeltaPitch() > 0 && data.getDeltaPitch() < 30) {
            final double modifier = Math.cbrt(0.8333 * (pitchGcd / MathUtils.EXPANDER));
            final double nextStep = (modifier / .6) - 0.3333;
            final double lastStep = nextStep * 200;
            sensitivitySamples.add((int) lastStep);
            if (sensitivitySamples.size() >= 20) {
                sensitivity = MathUtils.getMode(sensitivitySamples);
                final float gcdOne = (sensitivity / 200F) * 0.6F + 0.2F;
                testGCD = gcdOne * gcdOne * gcdOne * 1.2F;
                sensitivitySamples.clear();
            }
        }
        //Your sensitivity processing here
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public double getConstantYaw() {
        return constantYaw;
    }

    public double getConstantPitch() {
        return constantPitch;
    }

    public double getYawGcd() {
        return yawGcd;
    }

    public double getPitchGcd() {
        return pitchGcd;
    }

    public int getSensitivity() {
        return sensitivity;
    }
}
