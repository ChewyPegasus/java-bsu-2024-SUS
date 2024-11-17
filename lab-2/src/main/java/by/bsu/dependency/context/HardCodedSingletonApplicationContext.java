package by.bsu.dependency.context;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import by.bsu.dependency.annotation.Bean;

import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.exception.ApplicationContextInstantiateException;
import by.bsu.dependency.exception.ApplicationContextNotStartedException;
import by.bsu.dependency.exception.NoSuchBeanDefinitionException;


public class HardCodedSingletonApplicationContext extends AbstractApplicationContext {
    public HardCodedSingletonApplicationContext(Class<?>... beanClasses) {
        for (Class<?> clazz : beanClasses) {
            addBean(clazz);
        }
    }
}
