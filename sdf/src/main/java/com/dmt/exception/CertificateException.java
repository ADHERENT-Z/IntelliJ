package com.dmt.exception;

/**
 * 证书助手异常
 *
 * @author zornx5
 */
public class CertificateException extends RuntimeException {

    /**
     * 证书异常
     */
    public CertificateException() {
        super();
    }

    /**
     * 证书异常
     *
     * @param message 异常信息
     */
    public CertificateException(String message) {
        super(message);
    }

    /**
     * 证书异常
     *
     * @param message 异常信息
     * @param cause   异常
     */
    public CertificateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 证书异常
     *
     * @param cause 异常
     */
    public CertificateException(Throwable cause) {
        super(cause);
    }
}
