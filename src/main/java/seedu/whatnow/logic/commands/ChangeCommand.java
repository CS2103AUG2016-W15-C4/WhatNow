//@@author A0141021H
package seedu.whatnow.logic.commands;

import java.util.Optional;
import java.util.logging.Logger;

import seedu.whatnow.commons.core.Config;
import seedu.whatnow.commons.core.LogsCenter;
import seedu.whatnow.commons.exceptions.DataConversionException;
import seedu.whatnow.commons.util.ConfigUtil;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import static seedu.whatnow.commons.core.Messages.*;

/**
 * Changes the data file location of WhatNow.
 */
public class ChangeCommand extends Command{

    public static final String COMMAND_WORD = "change";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the WhatNow data file storage location. "
            + "Parameters: PATH\n"
            + "Example: " + COMMAND_WORD + " location to"
            + " PATH: C:"+"\\" + "Users" + "\\" + "Jim"+"\\"+"Dropbox";

    public static final String MESSAGE_SUCCESS = "The data storage location has been successfully changed to: %1$s";

    public static final String MESSAGE_UNDOFAIL = "Unable to undo due to no previous location information";

    public static final String MESSAGE_UNDOSUCCESS = "Successfully able to undo due to %1$s";

    public static final String MESSAGE_REDOSUCCESS = "Successfully able to redo due to %1$s";

    public static final String MESSAGE_REDOFAIL = "Unable to redo due to no previous undo command";

    public static final String WHATNOW_XMLFILE = "whatnow.xml";

    public static final String DOUBLE_BACKSLASH = "\\";

    public static final String SINGLE_FORWARDSLASH = "/";

    public String newPath;

    public Path source; 

    public Path destination;

    private static final Logger logger = LogsCenter.getLogger(ChangeCommand.class);

    public ChangeCommand(String newPath) {
        this.newPath=newPath;
    }

    @Override
    public CommandResult execute() {
        //new path convert from String to Path
        Path path = FileSystems.getDefault().getPath(newPath);
        String configFilePathUsed = Config.DEFAULT_CONFIG_FILE;
        Config config;
        Optional<Config> configOptional;

        try {
            configOptional = ConfigUtil.readConfig(configFilePathUsed);
            config = configOptional.orElse(new Config());
            model.getStackOfChangeFileLocationOld().push(config.getWhatNowFilePath());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. " +
                    "Using default config properties");
            config = new Config();
        }

        if(Files.exists(path)) {
            if (newPath.contains(DOUBLE_BACKSLASH)) {
                newPath = newPath + DOUBLE_BACKSLASH + WHATNOW_XMLFILE;
            } else if (newPath.contains(SINGLE_FORWARDSLASH)) {
                newPath = newPath + SINGLE_FORWARDSLASH + WHATNOW_XMLFILE;
            } else {
                newPath = newPath + SINGLE_FORWARDSLASH + WHATNOW_XMLFILE;
            }
            //update the new Path to add "whatNow.xml" behind
            path = FileSystems.getDefault().getPath(newPath);
            model.changeLocation(path);
            model.getUndoStack().push(COMMAND_WORD); 
            return new CommandResult(String.format(MESSAGE_SUCCESS, newPath));
        } else { 
            return new CommandResult(String.format(MESSAGE_INVALID_PATH, newPath));
        }
    }
}
