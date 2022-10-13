package com.calendar.app.i18n;

import java.util.Objects;


public class I18nLocalizedRuntimeException extends RuntimeException {

    private final String messageCode;
    private final transient Object[] errorMessageArguments;

    public I18nLocalizedRuntimeException(String messageCode, Object... errorMessageArguments) {
        this.messageCode = messageCode;
        this.errorMessageArguments = errorMessageArguments;
    }


    public String getMessageCode() {
        return messageCode;
    }
    public Object[] getErrorMessageArguments() {
        return Objects.requireNonNull(errorMessageArguments);
    }
}
