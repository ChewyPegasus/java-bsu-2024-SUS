package by.bsu.dependency.exception;

public class ApplicationContextInjectException extends RuntimeException {
    public ApplicationContextInjectException(Class<?> clazz) {
        super("field class: " + clazz.getSimpleName());
    }
}
