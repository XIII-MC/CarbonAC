package com.xiii.carbon.listener;

import com.xiii.carbon.Carbon;
import com.xiii.carbon.enums.MsgType;
import com.xiii.carbon.enums.Permissions;
import com.xiii.carbon.files.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListener implements Listener {

    private final Carbon plugin;

    public ProfileListener(final Carbon plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent e) {

        final Player player = e.getPlayer();

        this.plugin.getProfileManager().createProfile(player);

        if (Config.Setting.TOGGLE_ALERTS_ON_JOIN.getBoolean() && player.hasPermission(Permissions.AUTO_ALERTS.getPermission())) {

            this.plugin.getAlertManager().addPlayerToAlerts(player.getUniqueId());
            player.sendMessage(MsgType.PREFIX.getMessage() + " Alerts output §aenabled §7(Automatic)");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerQuitEvent e) {
        this.plugin.getProfileManager().removeProfile(e.getPlayer());
    }
}
