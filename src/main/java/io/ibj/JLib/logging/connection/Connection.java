package io.ibj.JLib.logging.connection;

import io.ibj.JLib.logging.event.Event;

import java.io.Closeable;

/**
 * Connection to a Sentry server, allowing to send captured events.
 */
public interface Connection extends Closeable {
    /**
     * Sends an event to the sentry server.
     *
     * @param event captured event to add in Sentry.
     */
    void send(Event event);
}
