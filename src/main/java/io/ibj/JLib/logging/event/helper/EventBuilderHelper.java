package io.ibj.JLib.logging.event.helper;

import io.ibj.JLib.logging.event.EventBuilder;

/**
 * Helper allowing to add extra information to the {@link io.ibj.JLib.logging.event.EventBuilder} before creating the
 * {@link io.ibj.JLib.logging.event.Event} itself.
 */
public interface EventBuilderHelper {
    /**
     * Adds extra elements to the {@link io.ibj.JLib.logging.event.EventBuilder} before calling {@link io.ibj.JLib.logging.event.EventBuilder#build()}.
     * <p>
     * EventBuilderHelper are supposed to only add details to the Event before it's built. Calling the
     * {@link io.ibj.JLib.logging.event.EventBuilder#build()} method from the helper will prevent the event from being built properly.
     * </p>
     *
     * @param eventBuilder event builder to enhance before the event is built.
     */
    void helpBuildingEvent(EventBuilder eventBuilder);
}
