package com.hepexta.jaxrs.bank.ex;

public class TransferException extends RuntimeException {

    private final int errorCode;

    public TransferException(ErrorMessage msg) {
        super(getErrorText(msg.getMessage()));
        this.errorCode = msg.getCode();
    }

    public TransferException(ErrorMessage message, Exception e) {
        super(getErrorText(message.getMessage()), e);
        this.errorCode = message.getCode();
    }

    private TransferException(int errorCode, String errorMessage, Object... params) {
        super(getErrorText(errorMessage, params));
        this.errorCode = errorCode;
    }

    private static String getErrorText( String message, Object...params ){
        return String.format( message, params );
    }

    int getErrorCode() {
        return this.errorCode;
    }

    public static void throwIf(boolean condition, ErrorMessage errorMessage, Object...params) {
        if (errorMessage==null){
            errorMessage = ErrorMessage.ERROR_500;
        }
        if (condition) {
            throw new TransferException(errorMessage.getCode(), errorMessage.getMessage(), params);
        }
    }
}
