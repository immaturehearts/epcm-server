package com.epcm.exception;

import com.epcm.enunn.StatusEnum;

public class StatusException extends RuntimeException{
    private int code;

    private String message;

    public StatusException (String errorName) {
        super(StatusEnum.valueOf(errorName).getDescription());
        StatusEnum statusEnum = StatusEnum.valueOf(errorName);
        this.code = statusEnum.getCode();
        this.message = super.getMessage();
    }

    public StatusException (StatusEnum statusEnum) {
        super(statusEnum.getDescription());
        this.code = statusEnum.getCode();
        this.message = super.getMessage();
    }

    public StatusException (int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
