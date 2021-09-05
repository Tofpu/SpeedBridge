package me.tofpu.speedbridge.user.properties;

import me.tofpu.speedbridge.api.user.UserProperties;

public class UserPropertiesFactory {
    public static UserProperties of(){
        return new UserPropertiesImpl();
    }
}
