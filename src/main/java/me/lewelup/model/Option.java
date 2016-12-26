package me.lewelup.model;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;

/**
 * A representation of an option in the poll.
 *
 * @author Christian Lewe
 */
@Entity
@Table(name = "options")
@EqualsAndHashCode(exclude = {"id", "supporters", "numVotes"})
@NoArgsConstructor
public class Option {
    @Id
    @GeneratedValue(generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "SEQ", allocationSize = 1, initialValue = 1)
    @Getter
    private long id;

    @Getter
    private String name;
    @Getter
    private LinkedHashSet<String> supporters;
    @Getter
    private int numVotes;

    public Option(@NonNull String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("The name of an option mustn't be empty!");
        }

        this.name = name;
        this.supporters = new LinkedHashSet<>();
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
