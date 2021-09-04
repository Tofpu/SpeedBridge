package me.tofpu.speedbridge.lobby.leaderboard;

import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class BoardUser implements Comparable<BoardUser> {
    private final String name;
    private final UUID uuid;
    private Double score;

    public BoardUser(final String name, final UUID uuid, final Double score) {
        this.name = name;
        this.uuid = uuid;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Double getScore() {
        return score;
    }

    public BoardUser setScore(Double score) {
        this.score = score;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardUser user = (BoardUser) o;
        return uuid.equals(user.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }

    @Override
    public int compareTo(@NotNull BoardUser o) {
        if (getScore().equals(o.getScore())) return 0; // THIS EQUAL THAN O
        else if (getScore() > o.getScore()) return 1; // THIS HIGHER THAN O
        return -1; // THIS LOWER THAN O
    }

    public static class Builder {
        private String name;
        private UUID uuid;
        private Double result;

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public UUID getUuid() {
            return uuid;
        }

        public Builder setUuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Double getResult() {
            return result;
        }

        public Builder setResult(Double result) {
            this.result = result;
            return this;
        }

        public BoardUser build() {
            return new BoardUser(getName(), getUuid(), getResult());
        }
    }
}
