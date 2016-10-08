package seedu.emeraldo.storage;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyEmeraldo;
import seedu.address.model.UserPrefs;
import seedu.emeraldo.commons.events.model.EmeraldoChangedEvent;
import seedu.emeraldo.commons.events.storage.DataSavingExceptionEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

/**
 * API of the Storage component
 */
public interface Storage extends EmeraldoStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    String getEmeraldoFilePath();

    @Override
    Optional<ReadOnlyEmeraldo> readEmeraldo() throws DataConversionException, IOException;

    @Override
    void saveEmeraldo(ReadOnlyEmeraldo addressBook) throws IOException;

    /**
     * Saves the current version of the Address Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleEmeraldoChangedEvent(EmeraldoChangedEvent abce);
}
