package me.tofpu.speedbridge.data.file.config.path;

public enum Path {
    SETTINGS_MAX_SLOTS("settings.max-slots");
    private final String path;

    Path(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
