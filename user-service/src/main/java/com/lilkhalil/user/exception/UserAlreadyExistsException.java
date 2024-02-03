package com.lilkhalil.user.exception;

public class UserAlreadyExistsException extends IllegalStateException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
