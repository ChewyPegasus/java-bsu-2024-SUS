package by.bsu.dependency.example;

import by.bsu.dependency.annotation.Bean;
import by.bsu.dependency.annotation.BeanScope;
import by.bsu.dependency.annotation.PostConstruct;

@Bean(name = "GOAT", scope = BeanScope.SINGLETON)
public class KobeanBryant {
    private boolean isAlive = true;

    @PostConstruct
    public void RealLife() {
        isAlive = false;
    }

    public boolean ZakinutTreshku() {
        if (isAlive) {
            System.out.println("I am the king");
        } else {
            System.out.println("Rest in Peace, brother");
        }
        return isAlive;
    }
}
