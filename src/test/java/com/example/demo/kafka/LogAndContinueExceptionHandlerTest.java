package com.example.demo.kafka;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

public class LogAndContinueExceptionHandlerTest extends BaseExceptionHandlerTest {

    private final KafkaExceptionHandler.OnValidRecordListener onValidRecordListener = Mockito.mock(KafkaExceptionHandler.OnValidRecordListener.class);
    private final KafkaExceptionHandler.OnSkippedRecordListener onSkippedRecordListener = Mockito.mock(KafkaExceptionHandler.OnSkippedRecordListener.class);
    private final KafkaExceptionHandler.OnFatalErrorListener onFatalErrorListener = Mockito.mock(KafkaExceptionHandler.OnFatalErrorListener.class);

    public LogAndContinueExceptionHandlerTest() {
        setExceptionHandler(new LogAndContinueExceptionHandler());
    }


    @Test
    @Override
    public void messageWithKeyAndValueIsValid() {
        setupMessageWithKeyAndValueIsValid(onValidRecordListener, onSkippedRecordListener, onFatalErrorListener);
        verify(onValidRecordListener).onValidRecordEvent();
        verify(onSkippedRecordListener, Mockito.never()).onSkippedRecordEvent(Mockito.any(), Mockito.any());
        verify(onFatalErrorListener, Mockito.never()).onFatalErrorEvent(Mockito.any(), Mockito.any());
    }

    @Test
    @Override
    public void messageWithoutKeyIsValid() {
        setupMessageWithoutKeyIsValid(onValidRecordListener, onSkippedRecordListener, onFatalErrorListener);
        verify(onValidRecordListener).onValidRecordEvent();
        verify(onSkippedRecordListener, Mockito.never()).onSkippedRecordEvent(Mockito.any(), Mockito.any());
        verify(onFatalErrorListener, Mockito.never()).onFatalErrorEvent(Mockito.any(), Mockito.any());
    }

    @Test
    @Override
    public void tombstoneIsValid() {
        setupTombstoneIsValid(onValidRecordListener, onSkippedRecordListener, onFatalErrorListener);
        verify(onValidRecordListener).onValidRecordEvent();
        verify(onSkippedRecordListener, Mockito.never()).onSkippedRecordEvent(Mockito.any(), Mockito.any());
        verify(onFatalErrorListener, Mockito.never()).onFatalErrorEvent(Mockito.any(), Mockito.any());
    }

    @Test
    @Override
    public void serializationErrorOnKey() {
        setupSerializationErrorOnKey(onValidRecordListener, onSkippedRecordListener, onFatalErrorListener);
        verify(onValidRecordListener, Mockito.never()).onValidRecordEvent();
        verify(onSkippedRecordListener).onSkippedRecordEvent(Mockito.any(), Mockito.eq(KafkaExceptionHandler.ErrorType.DESERIALIZATION_ERROR));
        verify(onFatalErrorListener, Mockito.never()).onFatalErrorEvent(Mockito.any(), Mockito.any());
    }


    @Test
    @Override
    public void deserializationErrorOnValue() {
        setupDeserializationErrorOnValue(onValidRecordListener, onSkippedRecordListener, onFatalErrorListener);
        verify(onValidRecordListener, Mockito.never()).onValidRecordEvent();
        verify(onSkippedRecordListener).onSkippedRecordEvent(Mockito.any(), Mockito.eq(KafkaExceptionHandler.ErrorType.DESERIALIZATION_ERROR));
        verify(onFatalErrorListener, Mockito.never()).onFatalErrorEvent(Mockito.any(), Mockito.any());
    }

    @Test
    @Override
    public void processingError() {
        setupProcessingError(onSkippedRecordListener, onFatalErrorListener);
        verify(onSkippedRecordListener).onSkippedRecordEvent(Mockito.any(), Mockito.eq(KafkaExceptionHandler.ErrorType.PROCESSING_ERROR));
        verify(onFatalErrorListener, Mockito.never()).onFatalErrorEvent(Mockito.any(), Mockito.any());
    }
}