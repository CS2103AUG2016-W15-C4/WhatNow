package seedu.whatnow.logic.commands;

import seedu.whatnow.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.whatnow.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0139128A
public abstract class UndoAndRedo extends Command{
	public abstract CommandResult undo() throws DuplicateTaskException, TaskNotFoundException;
	
	public abstract CommandResult redo() throws TaskNotFoundException;
}
