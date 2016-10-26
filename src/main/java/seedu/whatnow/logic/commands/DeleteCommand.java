package seedu.whatnow.logic.commands;

import seedu.whatnow.commons.core.Messages;
import seedu.whatnow.commons.core.UnmodifiableObservableList;
import seedu.whatnow.model.task.ReadOnlyTask;
import seedu.whatnow.model.task.Task;
import seedu.whatnow.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.whatnow.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Deletes a task identified using it's last displayed index from WhatNow.
 */
public class DeleteCommand extends UndoAndRedo {

	public static final String COMMAND_WORD = "delete";

	public static final String MESSAGE_USAGE = COMMAND_WORD
			+ ": Deletes the task identified by the index number used in the last task listing.\n"
			+ "Parameters: INDEX (must be a positive integer)\n"
			+ "Example: " + COMMAND_WORD + " 1";

	public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted Task: %1$s";

	private static final String TASK_TYPE_FLOATING = "todo";

	private final int targetIndex;
	private final String taskType;

	public DeleteCommand(String taskType, int targetIndex) {
		this.targetIndex = targetIndex;
		this.taskType = taskType;
	}


	@Override
	public CommandResult execute() {

		UnmodifiableObservableList<ReadOnlyTask> lastShownList;

		if (taskType.equals(TASK_TYPE_FLOATING)) {
			lastShownList = model.getCurrentFilteredTaskList();
		} else {
			lastShownList = model.getCurrentFilteredScheduleList();
		}
		if (lastShownList.size() < targetIndex) {
			indicateAttemptToExecuteIncorrectCommand();
			return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
		}
		ReadOnlyTask taskToDelete = lastShownList.get(targetIndex - 1);

		assert model != null;	 
		try {
			model.deleteTask(taskToDelete);
			model.getUndoStack().push(this);
			model.getDeletedStackOfTasks().push(taskToDelete);
		} catch (TaskNotFoundException pnfe) {
			assert false : "The target task cannot be missing";
		}
		return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, taskToDelete));
	}

	//@@author A0139128A
	@Override
	public CommandResult undo() {
		if(model.getDeletedStackOfTasks().isEmpty()) {
			return new CommandResult(String.format(UndoCommand.MESSAGE_FAIL));
		}
		ReadOnlyTask taskToReAdd = model.getDeletedStackOfTasks().pop();
		model.getDeletedStackOfTasksRedo().push(taskToReAdd);
		try {
			model.addTask((Task)taskToReAdd);
		} catch(DuplicateTaskException e) {
			return new CommandResult(String.format(UndoCommand.MESSAGE_FAIL));
		}
		return new CommandResult(String.format(UndoCommand.MESSAGE_SUCCESS));
	}
	
	//@author A0139128A
	@Override
	public CommandResult redo() {
		if(model.getStackOfListTypesRedo().isEmpty()) {
			return new CommandResult(String.format(RedoCommand.MESSAGE_FAIL));
		}
		ReadOnlyTask taskToDelete = model.getDeletedStackOfTasksRedo().pop();
		model.getDeletedStackOfTasks().push(taskToDelete);
		try {
			model.deleteTask((Task) taskToDelete);
		} catch(TaskNotFoundException e) {
			return new CommandResult(String.format(RedoCommand.MESSAGE_FAIL));
		}
		return new CommandResult(String.format(RedoCommand.MESSAGE_SUCCESS));
	}
}
