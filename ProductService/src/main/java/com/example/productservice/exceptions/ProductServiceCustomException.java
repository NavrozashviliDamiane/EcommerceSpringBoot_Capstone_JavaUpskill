package com.example.productservice.exceptions;

public class ProductServiceCustomException extends RuntimeException {
    public ProductServiceCustomException(String message) {
        super(message);
    }

    // Optionally, you can include constructors for more specific exception handling
    public ProductServiceCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
