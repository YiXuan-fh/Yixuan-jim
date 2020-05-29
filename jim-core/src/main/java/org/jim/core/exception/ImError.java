package org.jim.core.exception;

/**
 * @author WChao
 * @Desc
 * @date 2020-05-01 18:00
 */
public class ImError extends Error {

    public ImError() {
        super();
    }

    public ImError(String message) {
        super(message);
    }

    public ImError(String message, Throwable cause) {
        super(message, cause);
    }

    public ImError(Throwable cause) {
        super(cause);
    }

}
