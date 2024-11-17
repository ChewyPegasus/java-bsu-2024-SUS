package by.bsu.dependency.exception;

import java.text.MessageFormat;

public class NoSuchBeanDefinitionException extends RuntimeException {
    public NoSuchBeanDefinitionException(String name) {
        super(MessageFormat.format("No Bean with \" {0} \" name", name));
    }
}
