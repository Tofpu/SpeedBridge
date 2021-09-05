package me.tofpu.speedbridge.user.properties;

public class UserPropertiesFactory {
    public static UserProperties of(){
        return new UserPropertiesImpl();
    }
}
