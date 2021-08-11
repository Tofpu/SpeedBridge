package me.tofpu.speedbridge.lobby.leaderboard.data;

import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class BoardUser implements Comparable<BoardUser> {
    private final String name;
    private final UUID uuid;
    private final double result;

    public BoardUser(final String name, final UUID uuid, final Double result) {
        this.name = name;
        this.uuid = uuid;
        this.result = result == null ? 0 : result;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getResult() {
        return result;
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
        if (this.result == o.result) return 0; // THIS EQUAL THAN O
        else if (this.result < o.result) return 1; // THIS LOWER THAN O
        return -1; // THIS HIGHER THAN O
    }

    public static class Builder {
        private String name;
        private UUID uuid;
        private double result;

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

        public double getResult() {
            return result;
        }

        public Builder setResult(double result) {
            this.result = result;
            return this;
        }

        public BoardUser build(){
            return new BoardUser(getName(), getUuid(), getResult());
        }
    }
}
