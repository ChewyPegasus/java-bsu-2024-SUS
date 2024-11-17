package by.bsu.dependency.context;

import by.bsu.dependency.exception.NoSuchBeanDefinitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import by.bsu.dependency.example.*;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class AutoScanApplicationContextTest {
    private ApplicationContext applicationContext;

    @BeforeEach
    void init() {
        applicationContext = new AutoScanApplicationContext(BeeBean.class.getPackageName());
    }

    @Test
    void testIsRunning() {
        assertThat(applicationContext.isRunning()).isFalse();
        applicationContext.start();
        assertThat(applicationContext.isRunning()).isTrue();
    }

    @Test
    void testContextContainsBeans() {
        applicationContext.start();
        assertThat(applicationContext.containsBean("firstBean")).isTrue();
        assertThat(applicationContext.containsBean("otherBean")).isTrue();
        assertThat(applicationContext.containsBean("beeBean")).isTrue();
        assertThat(applicationContext.containsBean("NotBean")).isFalse();
        assertThat(applicationContext.containsBean("GOAT")).isTrue();
        assertThat(applicationContext.containsBean("queenBean")).isTrue();
    }

    @Test
    void postConstructTest() {
        applicationContext.start();
        KobeanBryant x = null;
        try {
            x = (KobeanBryant) applicationContext.getBean("GOAT");
        } catch (RuntimeException e) {
            fail("Get Bean" + e.getMessage());
        }
        try {
            x.getClass()
                    .getDeclaredMethod("ZakinutTreshku")
                    .invoke(x);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            fail("Invokation failure");
        }
        assertTrue(true);
    }

    @Test
    public void excludeMain() {
        applicationContext.start();

        assertThat(applicationContext.containsBean("main")).isFalse();
        assertThat(applicationContext.containsBean("Main")).isFalse();
        assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(Main.class));
    }
}
