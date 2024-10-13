package com.nhnacademy.miniDooray.server.exception.account;

public class UserRegisterFailedException extends RuntimeException {
    public UserRegisterFailedException(String message) {
        super(message);
    }
}
