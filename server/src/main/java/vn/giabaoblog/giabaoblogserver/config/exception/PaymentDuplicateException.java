package vn.giabaoblog.giabaoblogserver.config.exception;

public class PaymentDuplicateException extends RuntimeException {
    public PaymentDuplicateException(String message) {
        super(message);
    }

    public PaymentDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentDuplicateException(Throwable cause) {
        super(cause);
    }

    public PaymentDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

