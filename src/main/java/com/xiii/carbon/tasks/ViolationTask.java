package com.xiii.carbon.tasks;

import com.xiii.carbon.Carbon;
import com.xiii.carbon.checks.types.Check;
import org.bukkit.scheduler.BukkitRunnable;

public class ViolationTask extends BukkitRunnable {

    private final Carbon plugin;

    public ViolationTask(Carbon plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.plugin.getProfileManager().getProfileMap().values().forEach(profile -> {
            for (Check check : profile.getCheckHolder().getChecks()) check.resetVl();
        });
    }

}
