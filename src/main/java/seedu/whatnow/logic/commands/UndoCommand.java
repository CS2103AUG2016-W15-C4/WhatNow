//@@author A0139128A
package seedu.whatnow.logic.commands;

import seedu.whatnow.model.task.ReadOnlyTask;
import seedu.whatnow.model.task.Task;
import seedu.whatnow.model.task.UniqueTaskList;
import seedu.whatnow.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.whatnow.model.task.UniqueTaskList.TaskNotFoundException;

public class UndoCommand extends Command{

	public static final String COMMAND_WORD = "undo";

	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undo previous task in WhatNow "
			+ "Parameters: No parameters"
			+ "Example: " + COMMAND_WORD;

	public static final String MESSAGE_SUCCESS = "Undo Successfully";
	public static final String MESSAGE_FAIL = "Undo failure due to unexisting previous commands";

	public static final String UNKNOWN_COMMAND_FOUND = "Unknown Command Found in Undo";

	public static final String MESSAGE_LIST_NOT_ENTERED = " No previous list command was entered";

	public final String ADD_COMMAND = "add";
	public final String DELETE_COMMAND = "delete";
	public final String LIST_COMMAND = "list";
	public final String MARKDONE_COMMAND = "done";
	public final String MARKUNDONE_COMMAND = "undone";
	public final String UPDATE_COMMAND = "update";
	public final String CLEAR_COMMAND = "clear";

	@Override
	public CommandResult execute() throws DuplicateTaskException, TaskNotFoundException {
		assert model != null;
		if(model.getUndoStack().isEmpty()) {
			return new CommandResult(MESSAGE_FAIL);
		}else {
			String reqCommand = model.getUndoStack().pop();
			model.getRedoStack().push(reqCommand);
			return performReqUndo(reqCommand);
		}
	}
	private CommandResult performReqUndo(String reqCommand) throws TaskNotFoundException {
		if(reqCommand.equals(ADD_COMMAND)) {
			return performUndoAdd();
		}else if(reqCommand.equals(DELETE_COMMAND)) {
			return performUndoDelete();
		}else if(reqCommand.equals(LIST_COMMAND)) {
			return performUndoList();
		}else if(reqCommand.equals(MARKDONE_COMMAND)) {
			return performUndoMarkDone();
		}else if(reqCommand.equals(MARKUNDONE_COMMAND)) {
			return performUndoMarkUnDone();
		}else if(reqCommand.equals(UPDATE_COMMAND)) {
			return performUndoUpdate();
		}else if(reqCommand.equals(CLEAR_COMMAND)) {
			return performUndoClear();
		}else
			return new CommandResult(UNKNOWN_COMMAND_FOUND);
	}

	private CommandResult performUndoAdd() {
		assert model != null;
		if(model.getDeletedStackOfTasksAdd().isEmpty()) {
			return new CommandResult(String.format(UndoCommand.MESSAGE_FAIL));
		}else {
			try {
				ReadOnlyTask reqTask = model.getDeletedStackOfTasksAdd().pop();
				model.getDeletedStackOfTasksAddRedo().push(reqTask);
				model.deleteTask(reqTask);
			} catch (TaskNotFoundException tnfe) {
				return new CommandResult(String.format(UndoCommand.MESSAGE_FAIL));
			} 
			return new CommandResult(String.format(UndoCommand.MESSAGE_SUCCESS));
		}
	}

	private CommandResult performUndoDelete() {
		if(model.getDeletedStackOfTasks().isEmpty() || model.getDeletedStackOfTasksIndex().isEmpty()) {
			return new CommandResult(String.format(UndoCommand.MESSAGE_FAIL));
		}
		ReadOnlyTask taskToReAdd = model.getDeletedStackOfTasks().pop(); /**Gets the required task to reAdd */
		model.getDeletedStackOfTasksRedo().push(taskToReAdd);			/**Stores the required task for redoCommand if needed */
		int idxToReAdd = model.getDeletedStackOfTasksIndex().pop();		/**Gets the required task index to reAdd */
		try {
			model.addTaskSpecific((Task) taskToReAdd, idxToReAdd);
			model.getDeletedStackOfTasksIndexRedo().push(idxToReAdd);
		} catch (DuplicateTaskException e) {
			return new CommandResult(String.format(UndoCommand.MESSAGE_FAIL));
		}
		return new CommandResult(String.format(UndoCommand.MESSAGE_SUCCESS));
	}

	private CommandResult performUndoList() {
		if(model.getStackOfListTypes().isEmpty()) {
			return new CommandResult(MESSAGE_LIST_NOT_ENTERED); 
		}else {
			String prevListCommand = model.getStackOfListTypes().pop();
			model.getStackOfListTypesRedo().push(prevListCommand);
			if(model.getStackOfListTypes().isEmpty()) {
				model.updateFilteredListToShowAllIncomplete();
				model.updateFilteredScheduleListToShowAllIncomplete();
				return new CommandResult(UndoCommand.MESSAGE_SUCCESS);
			}else {
				String reqCommandListType = model.getStackOfListTypes().peek();
				if(reqCommandListType.equals(ListCommand.TASK_STATUS_ALL)) {
					model.updateFilteredListToShowAll();
					model.updateFilteredScheduleListToShowAll();
					return new CommandResult(UndoCommand.MESSAGE_SUCCESS);
				}else if(reqCommandListType.equals(ListCommand.TASK_STATUS_INCOMPLETE)) {
					model.updateFilteredListToShowAllIncomplete();
					model.updateFilteredScheduleListToShowAllIncomplete();
					return new CommandResult(UndoCommand.MESSAGE_SUCCESS);
				}else {
					model.updateFilteredListToShowAllCompleted();
					model.updateFilteredScheduleListToShowAllCompleted();
					return new CommandResult(UndoCommand.MESSAGE_SUCCESS);
				}
			}
		}
	}

	private CommandResult performUndoMarkDone() {
		if(model.getStackOfMarkDoneTask().isEmpty()) {
			return new CommandResult(String.format(UndoCommand.MESSAGE_FAIL));
		}else {
			ReadOnlyTask taskToReAdd = model.getStackOfMarkDoneTask().pop();
			try {
				model.unMarkTask(taskToReAdd);
				model.getStackOfMarkDoneTaskRedo().push(taskToReAdd);
			} catch(TaskNotFoundException tnfe) {
				return new CommandResult(UndoCommand.MESSAGE_FAIL);
			}
			return new CommandResult(String.format(UndoCommand.MESSAGE_SUCCESS));
		}
	}

	private CommandResult performUndoMarkUnDone() {
		if(model.getStackOfMarkUndoneTask().isEmpty()) {
			return new CommandResult(String.format(UndoCommand.MESSAGE_FAIL));
		}else {
			ReadOnlyTask taskToReAdd = model.getStackOfMarkUndoneTask().pop();
			try {
				model.markTask(taskToReAdd);
				model.getStackOfMarkUndoneTaskRedo().pop();
			} catch(TaskNotFoundException tnfe) {
				return new CommandResult(UndoCommand.MESSAGE_FAIL);
			}
			return new CommandResult(String.format(UndoCommand.MESSAGE_SUCCESS));
		}
	}

	private CommandResult performUndoUpdate() throws TaskNotFoundException {
		assert model != null;
		if(model.getOldTask().isEmpty() && model.getNewTask().isEmpty()) {
			return new CommandResult(String.format(UndoCommand.MESSAGE_FAIL));
		}else {
			ReadOnlyTask originalTask = model.getOldTask().pop();
			ReadOnlyTask unwantedTask = model.getNewTask().pop();
			model.getOldTask().push(unwantedTask);
			model.getNewTask().push(originalTask);
			try {
				model.updateTask(unwantedTask, (Task) originalTask);
			} catch(UniqueTaskList.DuplicateTaskException utle) {
				return new CommandResult(UndoCommand.MESSAGE_FAIL);
			}
			return new CommandResult(UndoCommand.MESSAGE_SUCCESS); 
		}
	}

	private CommandResult performUndoClear() {
		assert model != null;
		model.revertData();
		return new CommandResult(UndoCommand.MESSAGE_SUCCESS);
	}
}
