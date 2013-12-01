package de.goeuro.devtest.exception;

/**
 * @author Rolf Schuster
 */
public class ApplicationException extends Exception {
    private ErrorCode code;

    private String detailMessage;

    public ApplicationException(ErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ApplicationException(ErrorCode code, String message, String detailMessage, Throwable cause) {
        this(code, message, cause);
        this.detailMessage = detailMessage;
    }



    public enum ErrorCode {
        WRONG_CMD_ARGUMENTS_ERROR,
        INTERNAL_ERROR,
        JSON_PARSING_ERROR
    }

    public ErrorCode getCode() {
        return code;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
