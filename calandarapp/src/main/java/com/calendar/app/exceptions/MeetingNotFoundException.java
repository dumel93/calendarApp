package com.calendar.app.exceptions;

import com.calendar.app.i18n.I18nLocalizedRuntimeException;

public class MeetingNotFoundException extends I18nLocalizedRuntimeException {


    public MeetingNotFoundException() {
        super("meeting.not.found");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
