//@@author A0141021H
package seedu.whatnow.logic.commands;

import static seedu.whatnow.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.whatnow.commons.core.Messages;
import seedu.whatnow.commons.core.UnmodifiableObservableList;
import seedu.whatnow.model.task.ReadOnlyTask;
import seedu.whatnow.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Marks a task identified using it's last displayed index from WhatNow as completed.
 */
public class MarkUndoneCommand extends Command {

    public static final String COMMAND_WORD = "undone";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Mark the task identified by the index number used in the last task listing as incompleted.\n"
            + "Parameters: TODO/SCHEDULE INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " todo 1\n"
            + "Example: " + COMMAND_WORD + " schedule 1";

    public static final String MESSAGE_MARK_TASK_SUCCESS = "Task marked as incompleted: %1$s";
    public static final String MESSAGE_MARK_TASK_FAIL = "Unable to mark task as incomplete";
    private static final String TASK_TYPE_FLOATING = "todo";
    private static final String TASK_TYPE_SCHEDULE = "schedule";

    public final String taskType;
    public final int targetIndex;

    public MarkUndoneCommand(String taskType, int targetIndex) {
        this.taskType = taskType;
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {
        UnmodifiableObservableList<ReadOnlyTask> lastShownList;
        if (taskType.equals(TASK_TYPE_FLOATING)) {
            model.updateFilteredListToShowAllCompleted();
            lastShownList = model.getCurrentFilteredTaskList();
        } else if(taskType.equals(TASK_TYPE_SCHEDULE)){
            model.updateFilteredListToShowAllCompleted();
            lastShownList = model.getCurrentFilteredScheduleList();
        } else {
            return new CommandResult(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkUndoneCommand.MESSAGE_USAGE));
        }
        if (lastShownList.size() < targetIndex) {
            System.out.println("INVALID INDEX");
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask taskToMark = lastShownList.get(targetIndex - 1);

        try {
            model.unMarkTask(taskToMark);
            model.getUndoStack().push(COMMAND_WORD);
            model.getStackOfMarkUndoneTask().push(taskToMark);
        } catch (TaskNotFoundException pnfe) {
            return new CommandResult(String.format(MESSAGE_MARK_TASK_FAIL));
        }

        return new CommandResult(String.format(MESSAGE_MARK_TASK_SUCCESS, taskToMark));
    }
}