package me.tofpu.speedbridge.model.object.user.properties;

import me.tofpu.speedbridge.api.model.object.user.UserProperties;

public class UserPropertiesFactory {
    public static UserProperties of(){
        return new UserPropertiesImpl();
    }
}
