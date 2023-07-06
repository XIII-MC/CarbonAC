package com.xiii.carbon.checks.enums;

public enum CheckType {

    FLY("Fly", CheckCategory.MOVEMENT),
    FASTCLIMB("FastClimb", CheckCategory.MOVEMENT),
    SPEED("Speed", CheckCategory.MOVEMENT),
    KILLAURA("KillAura", CheckCategory.COMBAT),
    MOTION("Motion", CheckCategory.MOVEMENT);

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
