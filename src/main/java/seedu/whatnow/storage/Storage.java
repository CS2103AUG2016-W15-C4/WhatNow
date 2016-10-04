package seedu.whatnow.storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import seedu.whatnow.commons.events.model.WhatNowChangedEvent;
import seedu.whatnow.commons.events.storage.DataSavingExceptionEvent;
import seedu.whatnow.commons.exceptions.DataConversionException;
import seedu.whatnow.model.ReadOnlyWhatNow;
import seedu.whatnow.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends WhatNowStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    String getWhatNowFilePath();

    @Override
    Optional<ReadOnlyWhatNow> readWhatNow() throws DataConversionException, IOException;

    @Override
    void saveWhatNow(ReadOnlyWhatNow WhatNow) throws IOException;

    /**
     * Saves the current version of WhatNow to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleWhatNowChangedEvent(WhatNowChangedEvent abce);
}
