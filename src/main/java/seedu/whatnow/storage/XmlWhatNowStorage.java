package seedu.whatnow.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.whatnow.commons.core.LogsCenter;
import seedu.whatnow.commons.exceptions.DataConversionException;
import seedu.whatnow.commons.util.FileUtil;
import seedu.whatnow.model.ReadOnlyWhatNow;

/**
 * A class to access WhatNow data stored as an xml file on the hard disk.
 */
public class XmlWhatNowStorage implements WhatNowStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlWhatNowStorage.class);

    private String filePath;

    public XmlWhatNowStorage(String filePath){
        this.filePath = filePath;
    }

    public String getWhatNowFilePath(){
        return filePath;
    }

    /**
     * Similar to {@link #readWhatNow()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyWhatNow> readWhatNow(String filePath) throws DataConversionException, FileNotFoundException {
        assert filePath != null;

        File WhatNowFile = new File(filePath);

        if (!WhatNowFile.exists()) {
            logger.info("WhatNow file "  + WhatNowFile + " not found");
            return Optional.empty();
        }

        ReadOnlyWhatNow WhatNowOptional = XmlFileStorage.loadDataFromSaveFile(new File(filePath));

        return Optional.of(WhatNowOptional);
    }

    /**
     * Similar to {@link #saveWhatNow(ReadOnlyWhatNow)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveWhatNow(ReadOnlyWhatNow WhatNow, String filePath) throws IOException {
        assert WhatNow != null;
        assert filePath != null;

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableWhatNow(WhatNow));
    }

    @Override
    public Optional<ReadOnlyWhatNow> readWhatNow() throws DataConversionException, IOException {
        return readWhatNow(filePath);
    }

    @Override
    public void saveWhatNow(ReadOnlyWhatNow WhatNow) throws IOException {
        saveWhatNow(WhatNow, filePath);
    }
}
