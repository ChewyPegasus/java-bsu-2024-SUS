package by.bsu.dependency.exception;

import java.lang.reflect.Method;

public class ApplicationContextPostConstructException extends RuntimeException {
    public ApplicationContextPostConstructException(Method method) {
        super("While invocation of " + method.getName() + " occured problem");
    }
}
