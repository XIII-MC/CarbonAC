package com.xiii.carbon.utils;

import com.xiii.carbon.Carbon;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public final class TaskUtils {

    private TaskUtils() {
    }

    public static BukkitTask taskTimer(Runnable runnable, long delay, long interval) {
        return Bukkit.getScheduler().runTaskTimer(Carbon.getInstance(), runnable, delay, interval);
    }

    public static BukkitTask taskTimerAsync(Runnable runnable, long delay, long interval) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(Carbon.getInstance(), runnable, delay, interval);
    }

    public static BukkitTask task(Runnable runnable) {
        return Bukkit.getScheduler().runTask(Carbon.getInstance(), runnable);
    }

    public static BukkitTask taskAsync(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(Carbon.getInstance(), runnable);
    }

    public static BukkitTask taskLater(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(Carbon.getInstance(), runnable, delay);
    }

    public static BukkitTask taskLaterAsync(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(Carbon.getInstance(), runnable, delay);
    }
}
