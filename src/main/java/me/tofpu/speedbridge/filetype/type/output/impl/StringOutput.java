package me.tofpu.speedbridge.filetype.type.output.impl;

import me.tofpu.speedbridge.filetype.type.output.Output;

public class StringOutput implements Output<String> {
    public static String of(final Object input){
        return new StringOutput().output(input);
    }
    
    @Override
    public String output(final Object output) {
        return String.valueOf(output);
    }
}
