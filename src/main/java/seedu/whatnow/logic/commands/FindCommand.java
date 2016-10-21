package seedu.whatnow.logic.commands;

import java.util.Set;

/**
 * Finds and lists all tasks in WhatNow whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all tasks with"
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " Online quiz";

    private final Set<String> keywords;

    public FindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() {
        return new CommandResult(getMessageForTaskListShownSummary(model.getFilteredTaskList(keywords).size() 
                + model.getFilteredScheduleList(keywords).size()));
    }
}
