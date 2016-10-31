package seedu.whatnow.storage;
//@@author A0141021H
import com.google.common.eventbus.Subscribe;

import seedu.whatnow.commons.core.ComponentManager;
import seedu.whatnow.commons.core.Config;
import seedu.whatnow.commons.core.LogsCenter;
import seedu.whatnow.commons.events.model.ConfigChangedEvent;
import seedu.whatnow.commons.events.model.WhatNowChangedEvent;
import seedu.whatnow.commons.events.storage.DataSavingExceptionEvent;
import seedu.whatnow.commons.exceptions.DataConversionException;
import seedu.whatnow.commons.util.ConfigUtil;
import seedu.whatnow.commons.util.StringUtil;
import seedu.whatnow.model.ReadOnlyWhatNow;
import seedu.whatnow.model.UserPrefs;
import seedu.whatnow.model.WhatNow;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Manages storage of WhatNow data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    public static final String DEFAULT_CONFIG_FILE = "config.json";
    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private Config config;
    private XmlWhatNowStorage xmlWhatNowStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(XmlWhatNowStorage xmlWhatNowStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.xmlWhatNowStorage = xmlWhatNowStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    public StorageManager(String xmlWhatNowFilePath, String userPrefsFilePath) {
        this(new XmlWhatNowStorage(xmlWhatNowFilePath), new JsonUserPrefsStorage(userPrefsFilePath));
    }
    
    public StorageManager(Config config) {
        this(new XmlWhatNowStorage(config.getWhatNowFilePath()), new JsonUserPrefsStorage(config.getUserPrefsFilePath()));
        this.config = config;
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
        return xmlWhatNowStorage.getWhatNowFilePath();
    }
    
    @Override
    public Optional<ReadOnlyWhatNow> readWhatNow() throws DataConversionException, IOException {
        return readWhatNow(xmlWhatNowStorage.getWhatNowFilePath());
    }

    @Override
    public Optional<ReadOnlyWhatNow> readWhatNow(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return xmlWhatNowStorage.readWhatNow(filePath);
    }

    @Override
    public void saveWhatNow(ReadOnlyWhatNow whatNow) throws IOException {
        saveWhatNow(whatNow, xmlWhatNowStorage.getWhatNowFilePath());
    }
    
    @Override
    public void saveConfig(Config config) throws IOException {
        saveConfig(config, xmlWhatNowStorage.getWhatNowFilePath());
    }

    @Override
    public void saveWhatNow(ReadOnlyWhatNow whatNow, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        xmlWhatNowStorage.saveWhatNow(whatNow, filePath);
    }

    @Override
    public void saveConfig(Config config, String filePath) throws IOException {
        logger.fine("Attempting to write to config data file: " + filePath);
        WhatNow whatNow = new WhatNow();
        xmlWhatNowStorage.saveWhatNow(whatNow, filePath);
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
    
    @Override
    @Subscribe
    /**
     * Changes the xml and config file path to the new file path location.
     */
    public void handleFileLocationChangedEvent(ConfigChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local config data changed, saving to file"));
        try {     
            Path source = FileSystems.getDefault().getPath(xmlWhatNowStorage.getWhatNowFilePath());
            xmlWhatNowStorage.setWhatNowFilePath(event.destination.toString());
            Files.move(source, event.destination, StandardCopyOption.REPLACE_EXISTING);
            config.setOldWhatNowFilePath(source.toString());
            config.setWhatNowFilePath(event.destination.toString());
            saveConfig(config);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
        
        String configFilePathUsed = DEFAULT_CONFIG_FILE;
        
        try {
            ConfigUtil.saveConfig(config, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        } 

    }
    // ================ CompletedTask Storage methods ==============================
}
