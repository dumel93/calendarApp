package com.calendar.app.exceptions;

import com.calendar.app.i18n.I18nLocalizedRuntimeException;

public class CompanyAlreadyExistsException extends I18nLocalizedRuntimeException {


    public CompanyAlreadyExistsException() {
        super("organization.already.exists");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}

