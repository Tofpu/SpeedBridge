package me.tofpu.speedbridge.data.file.config.output.impl;

import me.tofpu.speedbridge.data.file.config.output.Output;

public class StringOutput implements Output<String> {
    public static String of(final Object input) {
        return new StringOutput().output(input);
    }

    @Override
    public String output(final Object output) {
        return String.valueOf(output);
    }
}
