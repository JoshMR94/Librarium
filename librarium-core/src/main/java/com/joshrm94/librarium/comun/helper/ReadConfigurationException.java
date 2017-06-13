package com.joshrm94.librarium.comun.helper;

/**
 *
 * @author joshmr94
 */

public class ReadConfigurationException extends RuntimeException {

    public ReadConfigurationException() {
        //No hace nada
    }

    public ReadConfigurationException(String message) {
        super(message);
    }

    public ReadConfigurationException(String message,
            Throwable cause) {
        super(message, cause);
    }

    public ReadConfigurationException(Throwable cause) {
        super(cause);
    }

    public ReadConfigurationException(String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
