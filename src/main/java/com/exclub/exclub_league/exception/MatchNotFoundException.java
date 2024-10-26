package com.exclub.exclub_league.exception;

public class MatchNotFoundException extends RuntimeException {

    // 기본 생성자
    public MatchNotFoundException() {
        super();
    }

    // 메시지를 포함하는 생성자
    public MatchNotFoundException(String message) {
        super(message);
    }

    // 메시지와 원인을 포함하는 생성자
    public MatchNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // 원인을 포함하는 생성자
    public MatchNotFoundException(Throwable cause) {
        super(cause);
    }
}