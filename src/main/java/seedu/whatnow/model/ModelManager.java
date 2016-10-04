package seedu.whatnow.model;

import javafx.collections.transformation.FilteredList;
import seedu.whatnow.commons.core.ComponentManager;
import seedu.whatnow.commons.core.LogsCenter;
import seedu.whatnow.commons.core.UnmodifiableObservableList;
import seedu.whatnow.commons.events.model.WhatNowChangedEvent;
import seedu.whatnow.commons.util.StringUtil;
import seedu.whatnow.model.task.ReadOnlyTask;
import seedu.whatnow.model.task.Task;
import seedu.whatnow.model.task.UniqueTaskList;
import seedu.whatnow.model.task.UniqueTaskList.TaskNotFoundException;

import java.util.Set;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of WhatNow data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final WhatNow WhatNow;
    private final FilteredList<Task> filteredTasks;

    /**
     * Initializes a ModelManager with the given WhatNow
     * WhatNow and its variables should not be null
     */
    public ModelManager(WhatNow src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with WhatNow: " + src + " and user prefs " + userPrefs);

        WhatNow = new WhatNow(src);
        filteredTasks = new FilteredList<>(WhatNow.getTasks());
    }

    public ModelManager() {
        this(new WhatNow(), new UserPrefs());
    }

    public ModelManager(ReadOnlyWhatNow initialData, UserPrefs userPrefs) {
        WhatNow = new WhatNow(initialData);
        filteredTasks = new FilteredList<>(WhatNow.getTasks());
    }

    @Override
    public void resetData(ReadOnlyWhatNow newData) {
        WhatNow.resetData(newData);
        indicateWhatNowChanged();
    }

    @Override
    public ReadOnlyWhatNow getWhatNow() {
        return WhatNow;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateWhatNowChanged() {
        raise(new WhatNowChangedEvent(WhatNow));
    }

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        WhatNow.removeTask(target);
        indicateWhatNowChanged();
    }

    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        WhatNow.addTask(task);
        updateFilteredListToShowAll();
        indicateWhatNowChanged();
    }

    //=========== Filtered Task List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredTasks.setPredicate(null);
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords){
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }

    //========== Inner classes/interfaces used for filtering ==================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask task);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            return qualifier.run(task);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask task);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(task.getName().fullName, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

}
