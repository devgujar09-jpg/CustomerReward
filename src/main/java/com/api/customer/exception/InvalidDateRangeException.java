package com.api.customer.exception;


public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException(String msg) { super(msg); }
}