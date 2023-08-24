package com.xiii.carbon.checks.enums;

public enum CheckType {

    FLY("Fly", CheckCategory.MOVEMENT),
    FASTCLIMB("FastClimb", CheckCategory.MOVEMENT),
    VELOCITY("Velocity", CheckCategory.COMBAT),
    SPEED("Speed", CheckCategory.MOVEMENT),
    KILLAURA("KillAura", CheckCategory.COMBAT),
    AIM("Aim", CheckCategory.COMBAT),
    TEST("Test", CheckCategory.MOVEMENT),
    TIMER("Timer", CheckCategory.PACKET),
    INVALID("Invalid", CheckCategory.MOVEMENT),
    PACKET("Packet", CheckCategory.PACKET),
    MOTION("Motion", CheckCategory.MOVEMENT),
    GROUND("Ground", CheckCategory.MOVEMENT);

    private final String checkName;
    private final CheckCategory checkCategory;

    CheckType(String checkName, CheckCategory checkCategory) {
        this.checkName = checkName;
        this.checkCategory = checkCategory;
    }

    public String getCheckName() {
        return checkName;
    }

    public CheckCategory getCheckCategory() {
        return checkCategory;
    }
}
