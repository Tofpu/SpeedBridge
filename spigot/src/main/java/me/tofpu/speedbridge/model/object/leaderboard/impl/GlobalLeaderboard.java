package me.tofpu.speedbridge.model.object.leaderboard.impl;

import me.tofpu.speedbridge.api.lobby.BoardUser;
import me.tofpu.speedbridge.data.file.path.Path;
import me.tofpu.speedbridge.model.object.leaderboard.AbstractLeaderboard;
import me.tofpu.speedbridge.util.Util;

public class GlobalLeaderboard extends AbstractLeaderboard {
    public GlobalLeaderboard(final int capacity) {
        super("Global", capacity);
    }

    @Override
    public String print() {
        final StringBuilder builder = new StringBuilder();
        builder.append(Path.LEADERBOARD_HEADER.getValue());

        for (int i = 0; i < positions().size(); i++) {
            if (builder.capacity() != 1) builder.append("\n");
            builder.append(parse(i));
        }

        return Util.colorize(builder.toString());
    }

    @Override
    public String parse(final int slot) {
        if (positions().size() <= slot) return "N/A";
        final int position = slot + 1;
        final BoardUser user = positions().get(slot);

        return user == null ? "N/A" : Util.colorize(Util.WordReplacer.replace(Path.LEADERBOARD_STYLE.getValue(), new String[]{"{position}", "{name}", "{score}"}, position + "", user.name(), user.score() + ""));
    }
}
