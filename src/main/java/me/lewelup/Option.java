package me.lewelup;

import lombok.Getter;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * A representation of an option in the poll.
 *
 * @author Christian Lewe
 */
public class Option {
    @Getter
    private int id;
    private static int highestId = 0;
    @Getter
    private String name;
    @Getter
    private Set<String> supporters;
    @Getter
    private int numVotes;

    public Option(@NonNull String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("The name of an option mustn't be empty!");
        }

        this.name = name;
        this.supporters = new HashSet<>();
        this.id = highestId++;
    }

    public boolean addSupporter(@NonNull String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty!");
        }

        if (this.supporters.add(name)) {
            this.numVotes++;
            return true;
        }

        return false;
    }

    public boolean removeSupporter(@NonNull String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty!");
        }

        if (this.supporters.remove(name)) {
            this.numVotes--;
            return true;
        }

        return false;
    }
}
