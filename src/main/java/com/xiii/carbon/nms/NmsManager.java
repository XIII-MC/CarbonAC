package com.xiii.carbon.nms;

import com.xiii.carbon.utils.versionutils.VersionUtils;

/**
 * A simple NMS Manager class
 * <p>
 * NOTE: Obviously this is not done, You should implement every single nms version yourself
 * Inside the me.nik.anticheatbase.manager.managers.nms.impl package.
 * <p>
 * NMS Can improve perfomance by a LOT even when calling simple methods such as p.getAllowFlight();
 * YourKit profiler doesn't lie!
 */
public class NmsManager {

    private final NmsInstance nmsInstance;

    public NmsManager() {

        switch (VersionUtils.getServerVersion()) {

            default:
                this.nmsInstance = new InstanceDefault();
                break;

            /*
            Add more versions here.
             */
        }
    }

    public NmsInstance getNmsInstance() {
        return nmsInstance;
    }
}
