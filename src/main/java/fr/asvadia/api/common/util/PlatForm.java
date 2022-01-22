package fr.asvadia.api.common.util;

import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public enum PlatForm {


    ALL,
    JAVA,
    BEDROCK;

    public static PlatForm getByUUID(UUID uuid) {
        try {
            if(FloodgateApi.getInstance().isFloodgateId(uuid)) return BEDROCK;
            else return JAVA;
        } catch(NoClassDefFoundError e) {
            return ALL;
        }
    }


}
