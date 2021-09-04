package me.tofpu.speedbridge.data.file.config.output.impl;

import me.tofpu.speedbridge.data.file.config.output.Output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StringListOutput implements Output<List<String>> {
    public static List<String> of(final Object input) {
        return new StringListOutput().output(input);
    }

    @Override
    public List<String> output(Object output) {
        final List<String> list = new ArrayList<>();
        if (output instanceof List) list.addAll((Collection<? extends String>) output);

        return list;
    }
}
