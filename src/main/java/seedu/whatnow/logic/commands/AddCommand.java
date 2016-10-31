//@@author A0139772U
package seedu.whatnow.logic.commands;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import seedu.whatnow.commons.exceptions.IllegalValueException;
import seedu.whatnow.model.tag.Tag;
import seedu.whatnow.model.tag.UniqueTagList;
import seedu.whatnow.model.task.*;
import seedu.whatnow.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.whatnow.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Adds a task to WhatNow.
 */
public class AddCommand extends Command {

	public static final String COMMAND_WORD = "add";

	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to WhatNow. \n"
			+ "Parameters: \"TASK_NAME\" [t/TAG]...\n"
	        + "Parameters: \"TASK_NAME\" [on/by/from] [today/tomorrow/DATE] [to] [today/tomorrow/DATE] [t/TAG]...\n"
			+ "Parameters: \"TASK_NAME\" [by/at/from] [TIME] [till/to] [TIME] [t/TAG]...\n"
			+ "Example: \n"
			+ COMMAND_WORD + " \"Buy groceries\" on 23/2/2017 t/highPriority\n"
			+ COMMAND_WORD + " \"Buy dinner\" at 6pm t/highPriority\n"
			+ COMMAND_WORD + " \"Lesson\" on 24/2/2017 from 8.30am to 4:30pm t/lowPriority\n"
			+ COMMAND_WORD + " \"Submit homework\" by tomorrow 12pm t/lowPriority\n";

	public static final String MESSAGE_SUCCESS = "New task added: %1$s";
	public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in WhatNow";
	private static final String STATUS_INCOMPLETE = "incomplete";

	private final Task toAdd;
	
	//@@author A0126240W
	public AddCommand(String name, String date, String startDate, String endDate, String time, String startTime, String endTime, Set<String> tags) throws IllegalValueException, ParseException {
	    TaskTime validateTime = null;
	    TaskDate validateDate = null;
	    
	    if (time != null || startTime != null || endTime != null) {
	        validateTime = new TaskTime(time, startTime, endTime, date, startDate, endDate);
	        if (date == null && startDate == null && endDate == null)
	            date = validateTime.getDate();
	    }
	    
	    if (date != null || startDate != null || endDate != null) {
	        validateDate = new TaskDate(date, startDate, endDate);
	        if (date != null) {
	            date = validateDate.getDate();
	        } else if (startDate != null) {
	            startDate = validateDate.getStartDate();
	            endDate = validateDate.getEndDate();
	        }
	    }
	    
	    final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }    
        
        this.toAdd = new Task(new Name(name), date, startDate, endDate, time, startTime, endTime, new UniqueTagList(tagSet), STATUS_INCOMPLETE, null);
	}

	//@@author A0139128A
	@Override
	public CommandResult execute() {
		assert model != null;
		try {
			model.addTask(toAdd);
			model.getUndoStack().push(COMMAND_WORD);
			model.getDeletedStackOfTasksAdd().push(toAdd);
		} catch (UniqueTaskList.DuplicateTaskException e) {
			return new CommandResult(MESSAGE_DUPLICATE_TASK);
		}
		return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
	}
}
