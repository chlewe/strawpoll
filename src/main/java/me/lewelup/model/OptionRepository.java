package me.lewelup.model;

import org.springframework.data.repository.CrudRepository;

/**
 * Provides methods for saving Options to or loading them from the database.
 *
 * @author Christian Lewe
 * @see Option
 */
public interface OptionRepository extends CrudRepository<Option, Long> {

}
