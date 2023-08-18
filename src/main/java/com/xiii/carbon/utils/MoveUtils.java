package com.xiii.carbon.utils;

import com.xiii.carbon.managers.profile.Profile;

public final class MoveUtils {

    //---------------------------------------------------------------------------------------
    public static final float MAXIMUM_PITCH = 90.0F;
    //---------------------------------------------------------------------------------------
    public static final float FRICTION = .91F;
    public static final float FRICTION_FACTOR = .6F;
    public static final double WATER_FRICTION = .800000011920929D;
    public static final double MOTION_Y_FRICTION = .9800000190734863D;
    public static final double JUMP_MOTION = .41999998688697815D;
    public static final double LAND_GROUND_MOTION = -.07840000152587834D;
    public static final float JUMP_MOVEMENT_FACTOR = 0.026F;
    //---------------------------------------------------------------------------------------
    /**
     * Assuming they're moving forward and no acceleration is applied.
     */
    public static final float BASE_AIR_SPEED = .3565F;

    public static final float NON_ABS_AIR_SPEED = .338F;
    /**
     * Assuming they're moving sideways
     */
    public static final float BASE_GROUND_SPEED = .2867F;

    public static final float NON_ABS_GROUND_SPEED = .2868198F;
    //---------------------------------------------------------------------------------------
    /**
     * 1.9+ Clients last tick motion before it resets to 0 due to .003D
     */
    public static final double RESET_MOTION = .003016261509046103D;
    //---------------------------------------------------------------------------------------

    private MoveUtils() {
    }

    public static float getBaseAirSpeed(final Profile profile) {

        //Your own method here
        return 0F;
    }

    public static float getBaseGroundSpeed(final Profile profile) {

        //Your own method here
        return 0F;
    }

    public static float getCustomSpeed(final Profile profile) {

        //Your own method here
        return 0F;
    }

    public static int getMaxVelocityTicks(final double velocityXZ, final double velocityY) {

        int ticks = 0;

        float horizontal = (float) Math.abs(velocityXZ);

        do {

            horizontal -= .02F; //SpeedInAir Value

            horizontal *= MoveUtils.FRICTION; //Horizontal Friction

            if (ticks++ > 30) break;

        } while (horizontal > 0F);

        float vertical = (float) Math.abs(velocityY);

        do {

            vertical -= .08F; //Falling acceleration

            vertical *= MoveUtils.MOTION_Y_FRICTION; //Vertical Friction

            if (ticks++ > 60) break;

        } while (vertical > 0F);

        return ticks;
    }
}
