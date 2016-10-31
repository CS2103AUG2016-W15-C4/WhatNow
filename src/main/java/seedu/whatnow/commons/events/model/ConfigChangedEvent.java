//@@author A0141021H
package seedu.whatnow.commons.events.model;

import java.nio.file.Path;

import seedu.whatnow.commons.events.BaseEvent;

/** Indicates the WhatNow in the model has changed*/
public class ConfigChangedEvent extends BaseEvent {

    public final Path destination;
    
    public static final String DESTINATION = "Destination";
    
    public ConfigChangedEvent(Path destination){
        this.destination = destination;
    }

    @Override
    public String toString(){
        return DESTINATION + " = " + destination;
    }
}
