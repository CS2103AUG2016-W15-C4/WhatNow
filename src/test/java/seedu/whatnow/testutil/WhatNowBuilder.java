package seedu.whatnow.testutil;

import seedu.whatnow.commons.exceptions.IllegalValueException;
import seedu.whatnow.model.WhatNow;
import seedu.whatnow.model.tag.Tag;
import seedu.whatnow.model.task.Task;
import seedu.whatnow.model.task.UniqueTaskList;

/**
 * A utility class to help with building WhatNow objects.
 * Example usage: <br>
 *     {@code WhatNow ab = new WhatNowBuilder().withTask("John", "Doe").withTag("Friend").build();}
 */
public class WhatNowBuilder {

    private WhatNow WhatNow;

    public WhatNowBuilder(WhatNow WhatNow){
        this.WhatNow = WhatNow;
    }

    public WhatNowBuilder withTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        WhatNow.addTask(task);
        return this;
    }

    public WhatNow build(){
        return WhatNow;
    }
}
