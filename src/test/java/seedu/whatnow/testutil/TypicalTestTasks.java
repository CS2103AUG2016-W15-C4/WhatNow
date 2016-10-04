package seedu.whatnow.testutil;

import seedu.whatnow.commons.exceptions.IllegalValueException;
import seedu.whatnow.model.WhatNow;
import seedu.whatnow.model.task.*;

/**
 *
 */
public class TypicalTestTasks {

    public static TestTask egg, milk, chocolate, mamtha, marcus, zac, weijie, verbena, drashti;

    public TypicalTestTasks() {
        try {
            egg =  new TaskBuilder().withName("Alice Pauline").build();
            milk = new TaskBuilder().withName("Benson Meier").build();
            chocolate = new TaskBuilder().withName("Carl Kurz").build();
            mamtha =  new TaskBuilder().withName("Alice Pauline").build();
            marcus = new TaskBuilder().withName("Benson Meier").build();
            zac = new TaskBuilder().withName("Carl Kurz").build();
            weijie =  new TaskBuilder().withName("Alice Pauline").build();
            verbena = new TaskBuilder().withName("Benson Meier").build();
            drashti = new TaskBuilder().withName("Carl Kurz").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadWhatNowWithSampleData(WhatNow ab) {

        try {
            ab.addTask(new Task(egg));
            ab.addTask(new Task(milk));
            ab.addTask(new Task(chocolate));
            ab.addTask(new Task(mamtha));
            ab.addTask(new Task(marcus));
            ab.addTask(new Task(zac));
            ab.addTask(new Task(weijie));
            ab.addTask(new Task(verbena));
            ab.addTask(new Task(drashti));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            assert false : "not possible";
        }
    }

    public TestTask[] getTypicalTasks() {
        return new TestTask[]{egg, milk, chocolate, mamtha, marcus, zac, weijie, verbena, drashti};
    }

    public WhatNow getTypicalWhatNow(){
        WhatNow ab = new WhatNow();
        loadWhatNowWithSampleData(ab);
        return ab;
    }
}
