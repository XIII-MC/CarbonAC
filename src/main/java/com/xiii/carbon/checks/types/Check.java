package com.xiii.carbon.checks.types;

import com.xiii.carbon.checks.enums.CheckType;
import com.xiii.carbon.managers.profile.Profile;
import com.xiii.carbon.processors.packet.ClientPlayPacket;
import com.xiii.carbon.processors.packet.ServerPlayPacket;

public abstract class Check extends AbstractCheck {

    public Check(final Profile profile, final CheckType check, final String type, final String description) {
        super(profile, check, type, description);
    }

    public Check(final Profile profile, final CheckType check, final String description) {
        super(profile, check, "", description);
    }

    public abstract void handle(final ClientPlayPacket clientPlayPacket);

    public abstract void handle(final ServerPlayPacket serverPlayPacket);
}
