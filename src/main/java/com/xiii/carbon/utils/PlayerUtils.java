package com.xiii.carbon.utils;

import com.xiii.carbon.managers.profile.Profile;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;

public class PlayerUtils {

    private PlayerUtils() {
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

    public static boolean hasPotionEffect(final Profile profile, final PotionEffectType type) {
        for (PotionEffect potionEffect : profile.getPlayer().getActivePotionEffects()) {
            if(potionEffect.getType().equals(type))
                return true;
        }
        return false;
    }
    public static Optional<PotionEffect> getEffectByType(final Profile profile, final PotionEffectType type) {
        for (PotionEffect potionEffect : profile.getPlayer().getActivePotionEffects()) {
            if(potionEffect.getType().equals(type))
                return Optional.of(potionEffect);
        }
        return Optional.empty();
    }
    public static int getPotionEffectAmplifier(final Profile profile, final PotionEffectType type) {
        for (PotionEffect effect : profile.getPlayer().getActivePotionEffects()) {
            if (effect.getType().getName().equals(type.getName())) {
                return (effect.getAmplifier() + 1);
            }
        }
        return 0;
    }
}
