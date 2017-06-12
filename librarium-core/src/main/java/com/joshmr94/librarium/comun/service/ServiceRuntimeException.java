package com.joshmr94.librarium.comun.service;

/**
 *
 * @author joshmr94
 */
public class ServiceRuntimeException extends RuntimeException {

    public ServiceRuntimeException() {
        //Constructor por defecto
    }

    public ServiceRuntimeException(String message) {
        super(message);
    }

    public ServiceRuntimeException(String message,
            Throwable cause) {
        super(message, cause);
    }

    public ServiceRuntimeException(Throwable cause) {
        super(cause);
    }

    public ServiceRuntimeException(String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
