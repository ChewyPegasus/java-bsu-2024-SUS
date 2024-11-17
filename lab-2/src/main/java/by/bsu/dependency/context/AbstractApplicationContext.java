package by.bsu.dependency.context;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.Inject;
import by.bsu.dependency.annotation.PostConstruct;
import by.bsu.dependency.exception.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractApplicationContext implements ApplicationContext {

    ContextStatus status = ContextStatus.NOT_STARTED;

    private final Map<String, Class<?>> name2class = new HashMap<>();
    private final Map<Class<?>, Object> class2bean = new HashMap<>();
    private final Map<Class<?>, BeanScope> class2scope = new HashMap<>();

    @Override
    public boolean isRunning() {
        return (status == ContextStatus.STARTED);
    }

    @Override
    public void start() {
        this.class2scope.forEach((type, scope) -> {
            if (scope == BeanScope.SINGLETON)
                this.class2bean.put(type, Utility.Instantiate(type));
        });
        this.class2bean.values().forEach(object->PostConstruct(Injection(object)));
        status = ContextStatus.STARTED;
    }

    public void debug() {
        for (Map.Entry<String, Class<?>> bean : name2class.entrySet()) {
            System.out.println(bean.getKey() + " " + bean.getValue());
        }
    }

    @Override
    public boolean containsBean(String name) throws RuntimeException {
        assertRunning();
        return name2class.containsKey(name);
    }

    @Override
    public Object getBean(String name) throws RuntimeException {
        assertRunning();
        validateBean(name);
        return GetBean(name2class.get(name));
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        assertRunning();
        validateBean(clazz);
        try {
            return (T) GetBean(clazz);
        } catch (ClassCastException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;
    }

    @Override
    public boolean isPrototype(String name) {
        validateBean(name);
        return class2scope.get(name2class.get(name)) == BeanScope.PROTOTYPE;
    }

    @Override
    public boolean isSingleton(String name) {
        validateBean(name);
        return class2scope.get(name2class.get(name)) == BeanScope.SINGLETON;
    }

    private void assertRunning() {
        if (status == ContextStatus.NOT_STARTED) {
            throw new ApplicationContextNotStartedException();
        }
    }

    private void validateBean(String name) {
        if (!name2class.containsKey(name)) {
            throw new NoSuchBeanDefinitionException(name);
        }
    }

    private void validateBean(Class<?> clazz) {
        if (!class2scope.containsKey(clazz))
            throw new NoSuchBeanDefinitionException(Utility.MakeNameOfClassName(clazz));
    }

    private Object GetBean(Class<?> clazz) {
        if (class2scope.get(clazz) == BeanScope.SINGLETON)
            return class2bean.get(clazz);
        return PostConstruct(Injection(Utility.Instantiate(clazz)));
    }

    protected void addBean(Class<?> clazz) {
        Utility.bean bean = Utility.GetBeaniamin(clazz);
        name2class.put(bean.name, bean.clazz);
        class2scope.put(bean.clazz, bean.scope);
    }

    private Object Injection(Object object) {
        Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> {
                    field.setAccessible(true);
                    validateBean(field.getType());
                    try {
                        field.set(object, GetBean(field.getType()));
                    } catch (IllegalAccessException e) {
                        throw new ApplicationContextInjectException(field.getClass());
                    }
                });
        return object;
    }

    private Object PostConstruct(Object object) {
        Arrays.stream(object.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(PostConstruct.class))
                .forEach(method -> {
                    method.setAccessible(true);
                    try {
                        method.invoke(object);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new ApplicationContextPostConstructException(method);
                    }
                });
        return object;
    }

    public static class Utility {
        public record bean (String name, Class<?> clazz, BeanScope scope) {}

        public static String MakeNameOfClassName(Class<?> clazz) {
            return Character.toLowerCase(clazz.getSimpleName().charAt(0)) + clazz.getSimpleName().substring(1);
        }

        public static bean GetBeaniamin(Class<?> clazz) {
            var annot = clazz.getAnnotation(Bean.class);
            return new bean(
                    annot == null || annot.name().isEmpty() ? MakeNameOfClassName(clazz) : annot.name(),
                    clazz,
                    annot == null ? BeanScope.SINGLETON : annot.scope()
            );
        }

        public static Object Instantiate(Class<?> clazz) {
            try {
                return clazz.getDeclaredConstructor(new Class[]{}).newInstance();
            } catch (NoSuchMethodException |
                     InvocationTargetException |
                     InstantiationException |
                     IllegalAccessException e) {
                throw new ApplicationContextInstantiateException(clazz.getName());
            }
        }
    }

    protected enum ContextStatus {
        NOT_STARTED,
        STARTED
    }
}
