package com.xiii.carbon.managers.profile;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.xiii.carbon.Carbon;
import com.xiii.carbon.checks.CheckHolder;
import com.xiii.carbon.enums.Permissions;
import com.xiii.carbon.exempt.Exempt;
import com.xiii.carbon.files.Config;
import com.xiii.carbon.managers.threads.ProfileThread;
import com.xiii.carbon.playerdata.data.impl.*;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;
import com.xiii.carbon.utils.TaskUtils;
import com.xiii.carbon.utils.versionutils.VersionUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profile {

    //-------------------------------------------
    private final ActionData actionData;
    private final CombatData combatData;
    private final ConnectionData connectionData;
    private final MovementData movementData;
    private final RotationData rotationData;
    private final TeleportData teleportData;
    private final VelocityData velocityData;
    private final VehicleData vehicleData;
    //-------------------------------------------

    //--------------------------------------
    private final CheckHolder checkHolder;
    //--------------------------------------

    //--------------------------------------
    private final String version;
    private final ClientVersion clientVersion;
    private String clientBrand = "Unknown";
    private final boolean bypass;
    //--------------------------------------

    //------------------------------------------
    private final ProfileThread profileThread;
    private final Player player;
    private final UUID uuid;
    //------------------------------------------

    //---------------------------
    private final Exempt exempt;
    //---------------------------

    public Profile(final Player player) {

        //Player Object
        this.player = player;

        //UUID
        this.uuid = player.getUniqueId();

        //Version
        this.version = VersionUtils.getClientVersionAsString(player);
        this.clientVersion = VersionUtils.getClientVersion(player);

        //Bypass
        this.bypass = !Config.Setting.DISABLE_BYPASS_PERMISSION.getBoolean() && player.hasPermission(Permissions.BYPASS.getPermission());

        //Data
        this.actionData = new ActionData(this);
        this.combatData = new CombatData(this);
        this.connectionData = new ConnectionData();
        this.movementData = new MovementData(this);
        this.rotationData = new RotationData(this);
        this.teleportData = new TeleportData();
        this.velocityData = new VelocityData();
        this.vehicleData = new VehicleData(this);

        //Check Holder
        this.checkHolder = new CheckHolder(this);

        //Exempt
        this.exempt = new Exempt(this);

        //Thread
        this.profileThread = Carbon.getInstance().getThreadManager().getAvailableProfileThread();

        //Initialize Checks
        reloadChecks();
    }

    public boolean isBypassing() {
        return bypass;
    }

    public void handle(final ClientPlayPacket clientPlayPacket) {

        if (this.player == null) return;

        this.connectionData.process(clientPlayPacket);
        this.actionData.process(clientPlayPacket);
        this.combatData.process(clientPlayPacket);
        this.movementData.process(clientPlayPacket);
        this.rotationData.process(clientPlayPacket);
        this.teleportData.process(clientPlayPacket);
        this.velocityData.process(clientPlayPacket);
        this.vehicleData.process(clientPlayPacket);

        this.exempt.handleExempts(clientPlayPacket.getTimeStamp());

        this.checkHolder.runChecks(clientPlayPacket);
    }

    public void handle(final ServerPlayPacket serverPlayPacket) {

        if (this.player == null) return;

        this.connectionData.process(serverPlayPacket);
        this.actionData.process(serverPlayPacket);
        this.combatData.process(serverPlayPacket);
        this.movementData.process(serverPlayPacket);
        this.rotationData.process(serverPlayPacket);
        this.teleportData.process(serverPlayPacket);
        this.velocityData.process(serverPlayPacket);
        this.vehicleData.process(serverPlayPacket);

        this.exempt.handleExempts(serverPlayPacket.getTimeStamp());

        this.checkHolder.runChecks(serverPlayPacket);
    }

    public void kickPlayer(final String reason) {

        if (this.player == null) return;

        TaskUtils.task(() -> this.player.kickPlayer(reason));
    }

    public void handleTick(long currentTime) {
        //Handle the tick here
    }

    public String getVersionAsString() {
        return version;
    }

    public ClientVersion getVersion() {
        return clientVersion;
    }

    public String getClientBrand() {
        return clientBrand;
    }

    public void setClientBrand(final String clientBrand) {
        this.clientBrand = clientBrand;
    }

    public Player getPlayer() {
        return this.player;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void reloadChecks() {
        this.checkHolder.registerAll();
    }

    public TeleportData getTeleportData() { return teleportData; }

    public ActionData getActionData() {
        return actionData;
    }

    public CombatData getCombatData() {
        return combatData;
    }

    public ConnectionData getConnectionData() {
        return connectionData;
    }

    public MovementData getMovementData() {
        return movementData;
    }

    public RotationData getRotationData() {
        return rotationData;
    }

    public VelocityData getVelocityData() {
        return velocityData;
    }

    public VehicleData getVehicleData() {
        return vehicleData;
    }

    public CheckHolder getCheckHolder() {
        return checkHolder;
    }

    public Exempt isExempt() {
        return exempt;
    }

    public ProfileThread getProfileThread() {
        return profileThread;
    }
}
