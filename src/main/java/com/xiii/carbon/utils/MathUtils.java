package com.xiii.carbon.utils;

import com.xiii.carbon.tasks.TickTask;
import com.xiii.carbon.utils.fastmath.FastMath;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;

public final class MathUtils {

    private MathUtils() {
    }

    //---------------------------------------------------------------------------------------
    public static final double EXPANDER = 1.6777216E7D;
    public static final long MINIMUM_ROTATION_DIVISOR = 131072L;
    //---------------------------------------------------------------------------------------

    public static int millisToTicks(final long millis) {
        return (int) millis / 50;
    }

    public static long ticksToMillis(final int ticks) {
        return ticks * 50L;
    }

    public static long nanosToMillis(final long nanoseconds) {
        return (nanoseconds / 1000000L);
    }

    public static Vector getDirection(final Location location) {

        Vector vector = new Vector();

        final double x = location.getYaw();
        final double y = location.getPitch();

        final double radiansY = FastMath.toRadians(y);

        vector.setY(-FastMath.sin(radiansY));

        final double xz = FastMath.cos(radiansY);

        final double radiansRotX = FastMath.toRadians(x);

        vector.setX(-xz * FastMath.sin(radiansRotX));
        vector.setZ(xz * FastMath.cos(radiansRotX));

        return vector;
    }

    public static double decimalRound(final double val, int scale) {
        return BigDecimal.valueOf(val).setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
    }

    public static float strictClamp360(float value) {

        while (value > 360.0F) value -= 360.0F;

        while (value < 0.0F) value += 360.0F;

        return value;
    }

    public static double strictClamp360(double value) {

        while (value > 360.0F) value -= 360.0F;

        while (value < 0.0F) value += 360.0F;

        return value;
    }

    public static float clamp180(float value) {

        value %= 360F;

        if (value >= 180.0F) value -= 360.0F;

        if (value < -180.0F) value += 360.0F;

        return value;
    }

    public static long elapsed(final long millis) {
        return System.currentTimeMillis() - millis;
    }

    public static int elapsedTicks(final int ticks) {
        return TickTask.getCurrentTick() - ticks;
    }

    public static long getAbsoluteGcd(final float current, final float last) {

        final long currentExpanded = (long) (current * EXPANDER);

        final long lastExpanded = (long) (last * EXPANDER);

        return gcd(currentExpanded, lastExpanded);
    }

    private static long gcd(final long current, final long last) {
        return (last <= 16384L) ? current : gcd(last, current % last);
    }

    public static double getAbsoluteDelta(final double one, final double two) {
        return Math.abs(Math.abs(one) - Math.abs(two));
    }

    public static int getMode(Collection<? extends Number> list) {
        int mode = (int) list.toArray()[0];
        int maxCount = 0;
        for (Number value : list) {
            int count = 1;
            for (Number value2 : list) {
                if (value2.equals(value))
                    count++;
                if (count > maxCount) {
                    mode = (int) value;
                    maxCount = count;
                }
            }
        }
        return mode;
    }

    public static double getVariance(final Collection<? extends Number> data) {
        int count = 0;
        double sum = 0.0;
        double variance = 0.0;
        double average;
        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;
        }
        average = sum / count;
        for (final Number number : data) {
            variance += Math.pow(number.doubleValue() - average, 2.0);
        }
        return variance;
    }
}
