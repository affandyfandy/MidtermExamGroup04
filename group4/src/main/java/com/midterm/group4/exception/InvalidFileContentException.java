package com.midterm.group4.exception;

public class InvalidFileContentException extends RuntimeException {

    public InvalidFileContentException() {
        super();
    }

    public InvalidFileContentException(String message) {
        super(message);
    }

    public InvalidFileContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFileContentException(Throwable cause) {
        super(cause);
    }
}
