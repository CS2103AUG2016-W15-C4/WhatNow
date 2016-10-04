package seedu.whatnow.storage;

import javax.xml.bind.JAXBException;

import seedu.whatnow.commons.exceptions.DataConversionException;
import seedu.whatnow.commons.util.XmlUtil;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Stores WhatNow data in an XML file
 */
public class XmlFileStorage {
    /**
     * Saves the given WhatNow data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableWhatNow WhatNow)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, WhatNow);
        } catch (JAXBException e) {
            assert false : "Unexpected exception " + e.getMessage();
        }
    }

    /**
     * Returns WhatNow in the file or an empty WhatNow
     */
    public static XmlSerializableWhatNow loadDataFromSaveFile(File file) throws DataConversionException,
                                                                            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableWhatNow.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
