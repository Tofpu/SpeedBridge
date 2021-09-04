package me.tofpu.speedbridge.data.file.config.type;

public class ReturnType {
    private Object input;

    public static ReturnType of(final Object input) {
        return new ReturnType().put(input);
    }

    public ReturnType put(final Object input) {
        this.input = input;
        return this;
    }

    public Object accept() {
        return input;
    }
}
