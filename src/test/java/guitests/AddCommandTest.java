package guitests;

import guitests.guihandles.TaskCardHandle;
import org.junit.Test;

import seedu.whatnow.commons.core.Messages;
import seedu.whatnow.logic.commands.AddCommand;
import seedu.whatnow.testutil.TestTask;
import seedu.whatnow.testutil.TestUtil;

import static org.junit.Assert.assertTrue;

public class AddCommandTest extends WhatNowGuiTest {

    @Test
    public void add() {
        //add one task
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = td.verbena;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add another task
        taskToAdd = td.drashti;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add duplicate task
        commandBox.runCommand(td.verbena.getAddCommand());
        assertResultMessage(AddCommand.MESSAGE_DUPLICATE_TASK);
        assertTrue(taskListPanel.isListMatching(currentList));

        //add to empty list
        commandBox.runCommand("clear");
        assertAddSuccess(td.egg);

        //invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertAddSuccess(TestTask taskToAdd, TestTask... currentList) {
        commandBox.runCommand(taskToAdd.getAddCommand());

        //confirm the new card contains the right data
        TaskCardHandle addedCard = taskListPanel.navigateToTask(taskToAdd.getName().fullName);
        assertMatching(taskToAdd, addedCard);

        //confirm the list now contains all previous tasks plus the new task
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, taskToAdd);
        assertTrue(taskListPanel.isListMatching(expectedList));
    }

}
