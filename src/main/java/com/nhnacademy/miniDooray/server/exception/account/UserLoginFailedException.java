package com.nhnacademy.miniDooray.server.exception.account;

public class UserLoginFailedException extends RuntimeException {
    public UserLoginFailedException(String message) {
        super(message);
    }
}
