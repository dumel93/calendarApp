package com.calendar.app.exceptions;

import com.calendar.app.i18n.I18nLocalizedRuntimeException;

public class InvalidInputParameterException extends I18nLocalizedRuntimeException {


    public InvalidInputParameterException(String errorMessage) {
        super(errorMessage);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}