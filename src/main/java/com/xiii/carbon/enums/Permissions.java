package com.xiii.carbon.enums;

import com.xiii.carbon.Carbon;

public enum Permissions {
    //Commands
    VERSION_COMMAND(Carbon.getInstance().getConfiguration().getString("permissions.carbon-version-command")),
    ALERTS_COMMAND(Carbon.getInstance().getConfiguration().getString("permissions.carbon-alerts-command")),

    //Others
    AUTO_ALERTS(Carbon.getInstance().getConfiguration().getString("permissions.carbon-auto-alerts")),
    BYPASS(Carbon.getInstance().getConfiguration().getString("permissions.carbon-bypass"));

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
