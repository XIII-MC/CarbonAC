package com.xiii.carbon.files;

import com.xiii.carbon.Carbon;
import com.xiii.carbon.enums.MsgType;
import com.xiii.carbon.files.commentedfiles.CommentedFileConfiguration;
import com.xiii.carbon.managers.Initializer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Checks implements Initializer {

    private static final String[] HEADER = new String[]{
            "This is the configuration file for Carbon b0001 (checks.yml)",
            "To reset this file, delete it from the Carbon's folder and restart your server."
    };

    private final JavaPlugin plugin;
    private CommentedFileConfiguration configuration;
    private static boolean exists;

    public Checks(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * @return the config.yml as a CommentedFileConfiguration
     */
    public CommentedFileConfiguration getConfig() {
        return this.configuration;
    }

    @Override
    public void initialize() {

        File configFile = new File(this.plugin.getDataFolder(), "checks.yml");

        exists = configFile.exists();

        boolean setHeaderFooter = !exists;

        boolean changed = setHeaderFooter;

        this.configuration = CommentedFileConfiguration.loadConfiguration(this.plugin, configFile);

        if (setHeaderFooter) this.configuration.addComments(HEADER);

        for (Setting setting : Setting.values()) {

            setting.reset();

            changed |= setting.setIfNotExists(this.configuration);
        }

        if (changed) this.configuration.save();
    }

    @Override
    public void shutdown() {
        for (Setting setting : Setting.values()) setting.reset();
    }

    public enum Setting {
        /**
         * Max VLs explanation and usage. (MVC: Max Violation Count)
         * - MVC 0 : PERFECT INSANE UNFALSABLE CHECK. This CANNOT false, if you mess up your fucked. (Example: Invalid sensitivity)
         * - MVC 3 : Check stable, very hard/impossible to false (example: BadPacket)
         * - MVC 6 : Good check, rarely/barely falses but can still for some very rare occurrences (example: Fly)
         * - MVC 12 : Passable check, can be falsed if you really try to/falsable by a lot of different factors (example: Speed)
         * - MVC 24 : Okay-ish check, either falses randomly or can get a few falses without trying (example: Timer)
         * MVC only doubles (3, 6, 12, 24). If you need a higher MVC use 48, 96, ect...
         */

        FLY("fly", "", "Fly Check"),
        FLY_A("fly.a", true, "Should we enable this module?"),
        FLY_B("fly.b", true, "Should we enable this module?"),
        FLY_C("fly.c", true, "Should we enable this module?"),
        FLY_MAX_VL("fly.max_vl", 6, "The maximum violation amount a player needs to reach in order to get punished"),
        FLY_COMMANDS("fly.commands", Collections.singletonList("kick %player% " + MsgType.PREFIX.getMessage() +  " Unfair Advantage"), "The commands that will get executed once a player reaches the maximum violation amount"),

        SPEED("speed", "", "Speed Check"),
        SPEED_A("speed.a", true, "Should we enable this module?"),
        SPEED_B("speed.b", true, "Should we enable this module?"),
        SPEED_C("speed.c", true, "Should we enable this module?"),
        SPEED_MAX_VL("speed.max_vl", 12, "The maximum violation amount a player needs to reach in order to get punished"),
        SPEED_COMMANDS("speed.commands", Collections.singletonList("kick %player% " + MsgType.PREFIX.getMessage() +  " Unfair Advantage"), "The commands that will get executed once a player reaches the maximum violation amount"),

        TIMER("timer", "", "Timer Check"),
        TIMER_A("timer.a", true, "Should we enable this module?"),
        TIMER_MAX_VL("timer.max_vl", 24, "The maximum violation amount a player needs to reach in order to get punished"),
        TIMER_COMMANDS("timer.commands", Collections.singletonList("kick %player% " + MsgType.PREFIX.getMessage() +  " Unfair Advantage"), "The commands that will get executed once a player reaches the maximum violation amount"),

        KILLAURA("killaura", "", "KillAura Check"),
        KILLAURA_A("killaura.a", true, "Should we enable this module?"),
        KILLAURA_B("killaura.b", true, "Should we enable this module?"),
        KILLAURA_C("killaura.c", true, "Should we enable this module?"),
        KILLAURA_MAX_VL("killaura.max_vl", 3, "The maximum violation amount a player needs to reach in order to get punished"),
        KILLAURA_COMMANDS("killaura.commands", Collections.singletonList("kick %player% " + MsgType.PREFIX.getMessage() +  " Unfair Advantage"), "The commands that will get executed once a player reaches the maximum violation amount"),

        VELOCITY("velocity", "", "Velocity Check"),
        VELOCITY_A("velocity.a", true, "Should we enable this module?"),
        VELOCITY_MAX_VL("velocity.max_vl", 12, "The maximum violation amount a player needs to reach in order to get punished"),
        VELOCITY_COMMANDS("velocity.commands", Collections.singletonList("kick %player% " + MsgType.PREFIX.getMessage() +  " Unfair Advantage"), "The commands that will get executed once a player reaches the maximum violation amount"),

        INVALID("invalid", "", "Velocity Check"),
        INVALID_A("invalid.a", true, "Should we enable this module?"),
        INVALID_B("invalid.b", true, "Should we enable this module?"),
        INVALID_MAX_VL("invalid.max_vl", 3, "The maximum violation amount a player needs to reach in order to get punished"),
        INVALID_COMMANDS("invalid.commands", Collections.singletonList("kick %player% " + MsgType.PREFIX.getMessage() +  " Unfair Advantage"), "The commands that will get executed once a player reaches the maximum violation amount"),

        FASTCLIMB("fastclimb", "", "FastClimb Check"),
        FASTCLIMB_A("fastclimb.a", true, "Should we enable this module?"),
        FASTCLIMB_MAX_VL("fastclimb.max_vl", 6, "The maximum violation amount a player needs to reach in order tp get punished"),
        FASTCLIMB_COMMANDS("fastclimb.commands", Collections.singletonList("kick %player% " + MsgType.PREFIX.getMessage() +  " Unfair Advantage"), "The commands that will get executed once a player reaches the maximum violation amount"),

        PACKET("packet", "", "Test Check"),
        PACKET_A("packet.a", true, "Should we enable this module?"),
        PACKET_B("packet.b", true, "Should we enable this module?"),
        PACKET_MAX_VL("packet.max_vl", 3, "The maximum violation amount a player needs to reach in order to get punished"),
        PACKET_COMMANDS("packet.commands", Collections.singletonList("kick %player% " + MsgType.PREFIX.getMessage() +  " Unfair Advantage"), "The commands that will get executed once a player reaches the maximum violation amount"),

        GROUND("ground", "", "Ground Check"),
        GROUND_A("ground.a", true, "Should we enable this module?"),
        GROUND_MAX_VL("ground.max_vl", 6, "The maximum violation amount a player needs to reach in order tp get punished"),
        GROUND_COMMANDS("ground.commands", Collections.singletonList("kick %player% " + MsgType.PREFIX.getMessage() +  " Unfair Advantage"), "The commands that will get executed once a player reaches the maximum violation amount"),

        AIMASSIST("aimassist", "", "Aim Check"),
        AIMASSIST_A("aimassist.a", true, "Should we enable this module?"),
        AIMASSIST_MAX_VL("aimassist.max_vl", 6, "The maximum violation amount a player needs to reach in order tp get punished"),
        AIMASSIST_COMMANDS("aimassist.commands", Collections.singletonList("kick %player% " + MsgType.PREFIX.getMessage() +  " Unfair Advantage"), "The commands that will get executed once a player reaches the maximum violation amount"),

        TEST("test", "", "Test Check"),
        TEST_A("test.a", true, "Should we enable this module?"),
        TEST_MAX_VL("test.max_vl", 999999999, "The maximum violation amount a player needs to reach in order to get punished"),
        TEST_COMMANDS("test.commands", Collections.singletonList("kick %player% " + MsgType.PREFIX.getMessage() +  " Unfair Advantage"), "The commands that will get executed once a player reaches the maximum violation amount");

        private final String key;
        private final Object defaultValue;
        private boolean excluded;
        private final String[] comments;
        private Object value = null;

        Setting(String key, Object defaultValue, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
        }

        Setting(String key, Object defaultValue, boolean excluded, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
            this.excluded = excluded;
        }

        /**
         * Gets the setting as a boolean
         *
         * @return The setting as a boolean
         */
        public boolean getBoolean() {
            this.loadValue();
            return (boolean) this.value;
        }

        public String getKey() {
            return this.key;
        }

        /**
         * @return the setting as an int
         */
        public int getInt() {
            this.loadValue();
            return (int) this.getNumber();
        }

        /**
         * @return the setting as a long
         */
        public long getLong() {
            this.loadValue();
            return (long) this.getNumber();
        }

        /**
         * @return the setting as a double
         */
        public double getDouble() {
            this.loadValue();
            return this.getNumber();
        }

        /**
         * @return the setting as a float
         */
        public float getFloat() {
            this.loadValue();
            return (float) this.getNumber();
        }

        /**
         * @return the setting as a String
         */
        public String getString() {
            this.loadValue();
            return String.valueOf(this.value);
        }

        private double getNumber() {
            if (this.value instanceof Integer) {
                return (int) this.value;
            } else if (this.value instanceof Short) {
                return (short) this.value;
            } else if (this.value instanceof Byte) {
                return (byte) this.value;
            } else if (this.value instanceof Float) {
                return (float) this.value;
            }

            return (double) this.value;
        }

        /**
         * @return the setting as a string list
         */
        @SuppressWarnings("unchecked")
        public List<String> getStringList() {
            this.loadValue();
            return (List<String>) this.value;
        }

        private boolean setIfNotExists(CommentedFileConfiguration fileConfiguration) {
            this.loadValue();

            if (exists && this.excluded) return false;

            if (fileConfiguration.get(this.key) == null) {
                List<String> comments = Stream.of(this.comments).collect(Collectors.toList());
                if (this.defaultValue != null) {
                    fileConfiguration.set(this.key, this.defaultValue, comments.toArray(new String[0]));
                } else {
                    fileConfiguration.addComments(comments.toArray(new String[0]));
                }

                return true;
            }

            return false;
        }

        /**
         * Resets the cached value
         */
        public void reset() {
            this.value = null;
        }

        /**
         * @return true if this setting is only a section and doesn't contain an actual value
         */
        public boolean isSection() {
            return this.defaultValue == null;
        }

        /**
         * Loads the value from the config and caches it if it isn't set yet
         */
        private void loadValue() {
            if (this.value != null) return;
            this.value = Carbon.getInstance().getConfiguration().get(this.key);
        }
    }
}
