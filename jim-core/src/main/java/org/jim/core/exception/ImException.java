package org.jim.core.exception;

/**
 * @ClassName ImException
 * @Description Im异常类
 * @Author WChao
 * @Date 2019/6/13 3:28
 * @Version 1.0
 **/
public class ImException extends Exception{

    /**
     * @Author WChao
     * @Description //TODO
     * @param
     * @return
     **/
    public ImException() {
    }

    /**
     * @Author WChao
     * @Description //TODO
     * @param message
     * @return
     **/
    public ImException(String message) {
        super(message);

    }

    /**
     * @Author WChao
     * @Description //TODO
     * @param message, cause
     * @return
     **/
    public ImException(String message, Throwable cause) {
        super(message, cause);

    }

    /**
     * @Author WChao
     * @Description //TODO
     * @param message, cause, enableSuppression, writableStackTrace
     * @return
     **/
    public ImException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

    /**
     * @Author WChao
     * @Description //TODO
     * @param cause
     * @return
     **/
    public ImException(Throwable cause) {
        super(cause);

    }
}
