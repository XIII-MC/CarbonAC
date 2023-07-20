package com.xiii.carbon.listener;

import com.xiii.carbon.Carbon;
import com.xiii.carbon.api.events.CarbonViolationEvent;
import com.xiii.carbon.enums.MsgType;
import com.xiii.carbon.files.Config;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.tasks.TickTask;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

public class ViolationListener implements Listener {

    private final Carbon plugin;

    public ViolationListener(final Carbon plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onViolation(final CarbonViolationEvent e) {

        this.plugin.getAlertManager().getAlertExecutor().execute(() -> {

            final Player p = e.getPlayer();

            if (p == null || !p.isOnline()) return;

            final Profile profile = this.plugin.getProfileManager().getProfile(p);

            if (profile == null) return;

            final String tps = String.valueOf(TickTask.getTPS());

            final String checkType = e.getType();

            final String checkName = e.getCheck();

            final String check = (checkType.isEmpty() ? checkName : checkName + " (" + checkType + ")")
                    + (e.isExperimental() ? " (Experimental)" : "");

            final String description = e.getDescription();

            final String information = e.getInformation();

            final String playerName = p.getName();

            final int vl = e.getVl();

            //this.plugin.getLogManager().addLogToQueue(new PlayerLog(
            //        Config.Setting.SERVER_NAME.getString(),
            //        playerName,
            //        p.getUniqueId().toString(),
            //        check,
            //        information
            //));
            // TODO: Log manager (disabled for now not needed)

            alerts:
            {

                final String hoverMessage = MsgType.ALERT_HOVER.getMessage()
                        .replace("%description%", description)
                        .replace("%information%", information)
                        .replace("%tps%", tps);

                final String alertMessage = MsgType.ALERT_MESSAGE.getMessage()
                        .replace("%player%", playerName)
                        .replace("%check%", check)
                        .replace("%vl%", String.valueOf(vl));

                /*
                final JsonBuilder jsonBuilder = new JsonBuilder(alertMessage)
                        .setHoverEvent(JsonBuilder.HoverEventType.SHOW_TEXT, hoverMessage)
                        .setClickEvent(JsonBuilder.ClickEventType.RUN_COMMAND, "/tp " + playerName)
                        .buildText();
                 Can't use since the client doesn't receive the legacy packet (S1.8, C1.8)
                 */

                final TextComponent mainComponent = new TextComponent(alertMessage);
                mainComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage).create()));
                mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + playerName));

                for (final UUID uuid : this.plugin.getAlertManager().getPlayersWithAlerts()) {

                    final Player player = Bukkit.getPlayer(uuid);

                    player.spigot().sendMessage(mainComponent);
                }

                if (!Config.Setting.CONSOLE_ALERT.getBoolean()) break alerts;

                Bukkit.getConsoleSender().sendMessage(alertMessage);
            }
        });
    }
}

