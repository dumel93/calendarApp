package com.calendar.app.exceptions;

import com.calendar.app.i18n.I18nLocalizedRuntimeException;

public class LocationNotFoundException extends I18nLocalizedRuntimeException {

    public LocationNotFoundException() {
        super("location.not.found");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
