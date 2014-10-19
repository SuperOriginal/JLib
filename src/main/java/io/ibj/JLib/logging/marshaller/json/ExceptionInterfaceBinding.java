package io.ibj.JLib.logging.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import io.ibj.JLib.logging.event.interfaces.ExceptionInterface;
import io.ibj.JLib.logging.event.interfaces.SentryException;
import io.ibj.JLib.logging.event.interfaces.StackTraceInterface;

import java.io.IOException;
import java.util.Deque;
import java.util.Iterator;

/**
 * Binding system allowing to convert an {@link io.ibj.JLib.logging.event.interfaces.ExceptionInterface} to a JSON stream.
 */
public class ExceptionInterfaceBinding implements io.ibj.JLib.logging.marshaller.json.InterfaceBinding<ExceptionInterface> {
    private static final String TYPE_PARAMETER = "type";
    private static final String VALUE_PARAMETER = "value";
    private static final String MODULE_PARAMETER = "module";
    private static final String STACKTRACE_PARAMETER = "stacktrace";
    private final io.ibj.JLib.logging.marshaller.json.InterfaceBinding<StackTraceInterface> stackTraceInterfaceBinding;

    /**
     * Creates a Binding system to send a {@link io.ibj.JLib.logging.event.interfaces.ExceptionInterface} on JSON stream.
     * <p>
     * Exceptions may contain StackTraces, this means that the system should also be able to send a
     * {@link io.ibj.JLib.logging.event.interfaces.StackTraceInterface} on the JSON stream.
     * </p>
     *
     * @param stackTraceInterfaceBinding InterfaceBinding allowing to send a {@link io.ibj.JLib.logging.event.interfaces.StackTraceInterface} on the JSON
     *                                   stream.
     */
    public ExceptionInterfaceBinding(io.ibj.JLib.logging.marshaller.json.InterfaceBinding<StackTraceInterface> stackTraceInterfaceBinding) {
        this.stackTraceInterfaceBinding = stackTraceInterfaceBinding;
    }

    @Override
    public void writeInterface(JsonGenerator generator, ExceptionInterface exceptionInterface) throws IOException {
        Deque<SentryException> exceptions = exceptionInterface.getExceptions();

        generator.writeStartArray();
        for (Iterator<SentryException> iterator = exceptions.descendingIterator(); iterator.hasNext(); ) {
            writeException(generator, iterator.next());
        }
        generator.writeEndArray();
    }

    /**
     * Outputs an exception with its StackTrace on a JSon stream.
     *
     * @param generator       JSonGenerator.
     * @param sentryException Sentry exception with its associated {@link io.ibj.JLib.logging.event.interfaces.StackTraceInterface}.
     * @throws java.io.IOException
     */
    private void writeException(JsonGenerator generator, SentryException sentryException) throws IOException {
        generator.writeStartObject();
        generator.writeStringField(TYPE_PARAMETER, sentryException.getExceptionClassName());
        generator.writeStringField(VALUE_PARAMETER, sentryException.getExceptionMessage());
        generator.writeStringField(MODULE_PARAMETER, sentryException.getExceptionPackageName());
        generator.writeFieldName(STACKTRACE_PARAMETER);
        stackTraceInterfaceBinding.writeInterface(generator, sentryException.getStackTraceInterface());
        generator.writeEndObject();
    }

}
