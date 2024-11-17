package by.bsu.dependency.example;

import by.bsu.dependency.context.ApplicationContext;
import by.bsu.dependency.context.AutoScanApplicationContext;
import by.bsu.dependency.context.SimpleApplicationContext;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Demo {
    public static void main(String[] args) throws IOException {
        ApplicationContext applicationContext = new SimpleApplicationContext(BeeBean.class, QueenBean.class);
        applicationContext.start();
        QueenBean freddy = applicationContext.getBean(QueenBean.class);
        freddy.BohemianRhapsody();

        GiveTimeToCheck();

        BeeBean pcholka;
        for (int i = 0; i < 10; ++i) {
            pcholka = applicationContext.getBean(BeeBean.class);
            pcholka.Pozhuzhi();
            pcholka.PresentYourself();
        }

        GiveTimeToCheck();

        applicationContext = new AutoScanApplicationContext(BeeBean.class.getPackageName());
        applicationContext.start();

        KobeanBryant Kobe = applicationContext.getBean(KobeanBryant.class);
        Kobe.ZakinutTreshku();

        GiveTimeToCheck();

        System.out.println("Lets find that shit: ");
        if (applicationContext.containsBean("notBean")) {
            System.out.println("We found that racer");
        } else {
            System.out.println("He managed to escape");
        }

    }

    private static void GiveTimeToCheck() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            System.out.println("Hello, Biden!");
        }
    }
}
