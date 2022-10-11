package org.vsu.rudakov;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.vsu.rudakov.annotation.Controller;
import org.vsu.rudakov.context.AppContext;
import org.vsu.rudakov.factory.BeanFactory;
import org.vsu.rudakov.request.Request;
import org.vsu.rudakov.responce.Response;
import org.vsu.rudakov.responce.file.ResponseTest;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MainConsoleAppTestRunner extends Runner {
    private static App app = null;
    private final Class<?> testClass;

    public MainConsoleAppTestRunner(Class<?> testClass) {
        super();
        if (app == null) {
            var appContext = new AppContext();
            var beanFactory = new BeanFactory(appContext);
            appContext.setBeanFactory(beanFactory);
            var controllers = beanFactory.getBeanConfigurator().getScanner()
                    .getTypesAnnotatedWith(Controller.class).stream().map(appContext::getBean).collect(Collectors.toList());
            app = new ConsoleApp();
            app.setAppContext(appContext);
            app.setBeanFactory(beanFactory);
            app.setControllers(controllers);
            var shutdownHook = new Thread(() -> {
                app.clean();
                log.info("All databases cleared");
            });
            Runtime.getRuntime().addShutdownHook(shutdownHook);
            app.prepare();
        }
        this.testClass = testClass;
    }

    @Override
    public Description getDescription() {
        return Description
                .createTestDescription(testClass, "My runner description");
    }

    @SneakyThrows
    @Override
    public void run(RunNotifier runNotifier) {
        var test = testClass.getConstructor().newInstance();
        if (testClass.getSuperclass() == ResponseTest.class) {
            var field = test.getClass().getSuperclass().getDeclaredField("controllers");
            field.setAccessible(true);
            field.set(test, app.getControllers());
        }
        for (var method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                runNotifier.fireTestStarted(Description.createTestDescription(testClass, method.getName()));
                method.invoke(test);
                runNotifier.fireTestFinished(Description.createTestDescription(testClass, method.getName()));
            }
        }
    }

    public static List<?> getResponse(String query, Request request, Response resp, List<?> controllers) {
        request.setInput(query);
        var res = resp.response(controllers, request);
        if (res == null) {
            return null;
        }
        return res instanceof List ? (List<?>) res : List.of(res);
    }
}
