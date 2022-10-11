package org.vsu.rudakov;

import lombok.extern.slf4j.Slf4j;
import org.vsu.rudakov.annotation.Controller;
import org.vsu.rudakov.context.AppContext;
import org.vsu.rudakov.factory.BeanFactory;

import java.util.stream.Collectors;

@Slf4j
public class Main {
    public static void main( String[] args ) {
        var appContext = new AppContext();
        var beanFactory = new BeanFactory(appContext);
        appContext.setBeanFactory(beanFactory);
        var controllers = beanFactory.getBeanConfigurator().getScanner()
                .getTypesAnnotatedWith(Controller.class).stream().map(appContext::getBean).collect(Collectors.toList());
        var app = appContext.getBean(App.class);
        app.setAppContext(appContext);
        app.setBeanFactory(beanFactory);
        app.setControllers(controllers);
        var shutdownHook = new Thread(() -> {
            app.clean();
            log.info("All clear");
        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        app.prepare();
        app.run();
    }
}
