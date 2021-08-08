package me.tofpu.speedbridge.filetype.type.output.impl;

import me.tofpu.speedbridge.filetype.type.output.Output;

public class IntegerOutput implements Output<Integer> {
    public static Integer of(final Object input){
        return new IntegerOutput().output(input);
    }

    @Override
    public Integer output(Object output) {
        try {
            return Integer.parseInt(StringOutput.of(output));
        } catch (NumberFormatException exception){
            return null;
        }
    }
}
