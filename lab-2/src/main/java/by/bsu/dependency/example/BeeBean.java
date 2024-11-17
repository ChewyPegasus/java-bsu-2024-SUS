package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.PostConstruct;

import java.text.MessageFormat;
import java.util.concurrent.ThreadLocalRandom;

@Bean(name = "", scope = BeanScope.PROTOTYPE)
public class BeeBean {
    public void Pozhuzhi() {
        System.out.println("bzhzhhzhzhzhhzh");
    }

    public void PresentYourself() {
        System.out.println(MessageFormat.format("I am {0} bee", this.id));
    }

    private long id = 0;

    @PostConstruct
    private void InitId() {
        id = ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);
    }
}
