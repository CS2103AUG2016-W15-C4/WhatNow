package seedu.whatnow.storage;

import com.google.common.eventbus.Subscribe;

import seedu.whatnow.commons.core.ComponentManager;
import seedu.whatnow.commons.core.LogsCenter;
import seedu.whatnow.commons.events.model.WhatNowChangedEvent;
import seedu.whatnow.commons.events.storage.DataSavingExceptionEvent;
import seedu.whatnow.commons.exceptions.DataConversionException;
import seedu.whatnow.model.ReadOnlyWhatNow;
import seedu.whatnow.model.UserPrefs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Manages storage of WhatNow data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private WhatNowStorage WhatNowStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(WhatNowStorage WhatNowStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.WhatNowStorage = WhatNowStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    public StorageManager(String WhatNowFilePath, String userPrefsFilePath) {
        this(new XmlWhatNowStorage(WhatNowFilePath), new JsonUserPrefsStorage(userPrefsFilePath));
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ WhatNow methods ==============================

    @Override
    public String getWhatNowFilePath() {
        return WhatNowStorage.getWhatNowFilePath();
    }

    @Override
    public Optional<ReadOnlyWhatNow> readWhatNow() throws DataConversionException, IOException {
        return readWhatNow(WhatNowStorage.getWhatNowFilePath());
    }

    @Override
    public Optional<ReadOnlyWhatNow> readWhatNow(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return WhatNowStorage.readWhatNow(filePath);
    }

    @Override
    public void saveWhatNow(ReadOnlyWhatNow WhatNow) throws IOException {
        saveWhatNow(WhatNow, WhatNowStorage.getWhatNowFilePath());
    }

    @Override
    public void saveWhatNow(ReadOnlyWhatNow WhatNow, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        WhatNowStorage.saveWhatNow(WhatNow, filePath);
    }


    @Override
    @Subscribe
    public void handleWhatNowChangedEvent(WhatNowChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveWhatNow(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
