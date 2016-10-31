//@@author A0139772U
package seedu.whatnow.model;

import java.nio.file.Path;
import java.util.Set;
import java.util.Stack;

import seedu.whatnow.commons.core.UnmodifiableObservableList;
import seedu.whatnow.model.task.ReadOnlyTask;
import seedu.whatnow.model.task.Task;
import seedu.whatnow.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.whatnow.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    
    //@@author A0139128A
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyWhatNow newData);

    /** Reverts to the pre-existing backing model and replaces with backup-ed data */
	void revertData();
    
	/** Returns the WhatNow */
    ReadOnlyWhatNow getWhatNow();
	
    //=========== Methods for Task List ===============================================================

    /** Deletes the given task. */
    int deleteTask(ReadOnlyTask target) throws TaskNotFoundException;

    /** Adds the given task */
    void addTask(Task task) throws DuplicateTaskException;
    
    /** Adds the given task at specific index */
    void addTaskSpecific(Task task, int idx) throws DuplicateTaskException;
    
    //@@author A0139772U
    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getCurrentFilteredTaskList();
    
    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();
    
    /** Returns the filtered task list with filter keyword as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList(Set<String> key);

    /** Updates the filter of the filtered task list to show all tasks */
    void updateFilteredListToShowAll();
    
    /** Updates the filter of the filtered task list to show all ongoing tasks */
    void updateFilteredListToShowAllIncomplete();
    
    /** Updates the filter of the filtered task list to show all completed tasks */
    void updateFilteredListToShowAllCompleted();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);
    
    /** Updates the filter of the filtered task list to show only task of a specific status specified by the keyword */
    void updateFilteredListToShowAllByStatus(Set<String> keyword);
    
    /** Update the given task */
    void updateTask(ReadOnlyTask old, Task toUpdate) throws TaskNotFoundException, DuplicateTaskException;

    /** Mark the given task as completed */
    void markTask(ReadOnlyTask target) throws TaskNotFoundException;
   
    /** Mark the given task as incomplete */
	void unMarkTask(ReadOnlyTask target) throws TaskNotFoundException;
	
	//@@author A0139128A
    /**Gets the UndoStack if possible */
    Stack<String> getUndoStack();
    
    /**Gets the redoStack if possible*/
    Stack<String> getRedoStack();
    
    /**Gets the oldTask if possible */
	Stack<ReadOnlyTask> getOldTask();
	
	/**Gets the newTask if possible */
	Stack<ReadOnlyTask> getNewTask();
	
	/** Gets the deletedStackOfTask that corresponds to deleteCommand*/
	Stack<ReadOnlyTask> getDeletedStackOfTasks();

	/** Gets the deletedStackOfTsksIndex that corresponds to deleteCommand */
	Stack<Integer> getDeletedStackOfTasksIndex();
	
	/** Gets the deletedStackOfTaskRedo that corresponds to deleteCommand*/
	Stack<ReadOnlyTask> getDeletedStackOfTasksRedo();
	
	/** Gets the deletedStackOfTasksIndexRedo that corresponds to deleteCommand */
	Stack<Integer> getDeletedStackOfTasksIndexRedo();
	 
	/** Gets the deleted StackOfTasks that corresponds to AddCommand */
	Stack<ReadOnlyTask> getDeletedStackOfTasksAdd();

	/** Gets the deleted StackOfTasksRedo that corresponds to AddCommand */
	Stack<ReadOnlyTask> getDeletedStackOfTasksAddRedo();
	
	/** Gets Stack of Task that were marked */
	Stack<ReadOnlyTask> getStackOfMarkDoneTask();
	
	/** Gets stack of Task that were marked and corresponds to RedoCommand */
	Stack<ReadOnlyTask> getStackOfMarkDoneTaskRedo();
	
	/** Gets stack of Task that were marked and corresponds to UndoneCommand */
	Stack<ReadOnlyTask> getStackOfMarkUndoneTask();
	
	/** Gets stack of Task that were marked and corresponds to RedoCommand */
	Stack<ReadOnlyTask> getStackOfMarkUndoneTaskRedo();
	
	/** Gets a stack of String that corresponds to the list of commands that were executed */
	Stack<String> getStackOfListTypes();
	
	/**Gets a stack of String that corresponds to the list of Commands that were undone */
	Stack<String> getStackOfListTypesRedo();
	
	//@@author A0141021H
	/**Gets a stack of String that corresponds to the list of previous file path */
    Stack<String> getStackOfChangeFileLocationOld();
    
    /**Gets a stack of String that corresponds to the list of new file path */
    Stack<String> getStackOfChangeFileLocationNew();
    
	
	//@@author A0139772U
	//=========== Methods for Schedule List ===============================================================
    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getCurrentFilteredScheduleList();
    
    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredScheduleList(boolean isUndo);
    
    /** Returns the filtered task list with filter keyword as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredScheduleList(Set<String> key);
    
    /** Updates the filter of the filtered task list to show all tasks */
    void updateFilteredScheduleListToShowAll();
    
    /** Updates the filter of the filtered task list to show all ongoing tasks */
    void updateFilteredScheduleListToShowAllIncomplete();
    
    /** Updates the filter of the filtered task list to show all completed tasks */
    void updateFilteredScheduleListToShowAllCompleted();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredScheduleList(Set<String> keywords);
    
    /** Updates the filter of the filtered task list to show only task of a specific status specified by the keyword */
    void updateFilteredScheduleListToShowAllByStatus(Set<String> keyword);

    /** Updates the filter of the filtered task list to display all task types*/
    UnmodifiableObservableList<ReadOnlyTask> getAllTaskTypeList();

    //@@author A0141021H
    void changeLocation(Path destination);
}
