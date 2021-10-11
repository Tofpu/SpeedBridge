package me.tofpu.speedbridge.model.object.leaderboard;

import com.google.common.base.Objects;
import me.tofpu.speedbridge.api.model.object.lobby.BoardUser;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class BoardUserImpl implements Comparable<BoardUser>, BoardUser {
    private final String name;
    private final UUID uuid;
    private Double score;

    public BoardUserImpl(final String name, final UUID uuid, final Double score) {
        this.name = name;
        this.uuid = uuid;
        this.score = score;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public UUID uniqueId() {
        return this.uuid;
    }

    @Override
    public Double score() {
        return this.score;
    }

    @Override
    public BoardUser score(Double score) {
        this.score = score;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardUser user = (BoardUser) o;
        return uuid.equals(user.uniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }

    @Override
    public int compareTo(@NotNull BoardUser o) {
        if (score().equals(o.score())) return 0; // THIS EQUAL THAN O
        else if (score() > o.score()) return 1; // THIS HIGHER THAN O
        return -1; // THIS LOWER THAN O
    }

    public static class Builder {
        private String name;
        private UUID uuid;
        private Double result;

        public String name() {
            return name;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public UUID uniqueId() {
            return uuid;
        }

        public Builder uniqueId(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Double result() {
            return result;
        }

        public Builder result(Double result) {
            this.result = result;
            return this;
        }

        public BoardUser build() {
            return new BoardUserImpl(name(), uniqueId(), result());
        }
    }
}
