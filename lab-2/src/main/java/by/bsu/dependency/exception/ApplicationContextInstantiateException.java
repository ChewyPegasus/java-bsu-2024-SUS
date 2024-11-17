package by.bsu.dependency.exception;

import java.text.MessageFormat;

public class ApplicationContextInstantiateException extends RuntimeException {
    public ApplicationContextInstantiateException(String typeName) {
        super(MessageFormat.format("During {0} instantiation occurred failure.", typeName));
    }
}
