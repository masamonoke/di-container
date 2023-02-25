package org.vsu.rudakov.app;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.vsu.rudakov.request.Request;
import org.vsu.rudakov.responce.Delete;
import org.vsu.rudakov.responce.Get;
import org.vsu.rudakov.responce.Post;
import org.vsu.rudakov.responce.Update;
import org.vsu.rudakov.utils.FileResource;
import org.vsu.rudakov.annotation.Controller;
import org.vsu.rudakov.context.AppContext;
import org.vsu.rudakov.dao.ChildDao;
import org.vsu.rudakov.dao.file.FileDao;
import org.vsu.rudakov.dao.sql.SqlDao;
import org.vsu.rudakov.factory.BeanFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;

import java.util.stream.Collectors;
import java.util.List;
import java.io.File;
import java.util.Objects;
import java.util.Scanner;

@Slf4j
public class App {
    @Getter
    private static AppContext appContext;
    private static BeanFactory beanFactory;
    @Getter
    private static List<?> controllers;
    @Getter
    private static boolean isAppInit = false;

    public static void prepare() {
        if (isAppInit) {
            return;
        }
        appContext = new AppContext();
        beanFactory = new BeanFactory(appContext);
        appContext.setBeanFactory(beanFactory);
        controllers = beanFactory.getBeanConfigurator().getScanner()
                .getTypesAnnotatedWith(Controller.class).stream().map(appContext::getBean).collect(Collectors.toList());
        
        var childDao = appContext.getBean(ChildDao.class);
        Thread shutdownHook = null;
        if (childDao instanceof FileDao) {
            fillFileDb();
            ((FileDao<?>) childDao).setSequence(childDao.getAll().size());
            shutdownHook = new Thread(() -> {
                clearFileDb();
                log.info("File database cleaned");
            });
        } else if (childDao instanceof SqlDao) {
            executeSqlScript(Action.Create);
            shutdownHook = new Thread(() -> {
                executeSqlScript(Action.Delete);
                log.info("Database cleaned");
            });
        } else {
            log.error("Cannot start application because unable connected to specified database");
            System.exit(1);
        };

        Runtime.getRuntime().addShutdownHook(shutdownHook);
        isAppInit = true;
    } 

    public static void run() {

        prepare();

        var get = appContext.getBean(Get.class);
        var update = appContext.getBean(Update.class);
        var post = appContext.getBean(Post.class);
        var delete = appContext.getBean(Delete.class);
        var request = appContext.getBean(Request.class);

        var scanner = new Scanner(System.in);
        System.out.println("Enter mappings: ");
        while (true) {
            var input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }
            request.setInput(input);
            if (request.getParam().startsWith("-")) {
                Object response = null;
                switch (request.getParam()) {
                    case "-get" -> {
                        response = get.response(controllers, request);
                    }
                    case "-update" -> {
                        response = update.response(controllers, request);
                    }
                    case "-post" -> {
                        response = post.response(controllers, request);
                    }
                    case "-delete" -> {
                        response = delete.response(controllers, request);
                    }
                }
                if (response != null) {
                    log.info(response.toString());
                } else {
                    log.error("Null value");
                }
            }
        }
        scanner.close();
    }

    enum Action {
        Create, 
        Delete
    }

    static final class SqlExecuter extends SQLExec {
        public SqlExecuter() {
            Project project = new Project();
            project.init();
            setProject(project);
            setTaskType("sql");
            setTaskName("sql");
        }
    }

    private static void executeSqlScript(Action action) {
        var strAction = action.toString().toLowerCase();
        SqlExecuter executer = new SqlExecuter();
        var pathToCreate = appContext.getConfiguration().getConfig().get("db.create-url");
        var sqlCreatePath = pathToCreate.concat("/" + strAction + ".sql");
        executer.setSrc(new File(sqlCreatePath));
        executer.setDriver("org.postgresql.Driver");
        var password = appContext.getConfiguration().getConfig().get("db.password");
        executer.setPassword(password);
        var user = appContext.getConfiguration().getConfig().get("db.username");
        executer.setUserid(user);
        var url = appContext.getConfiguration().getConfig().get("db.url");
        executer.setUrl(url);
        try {
            executer.execute();
        } catch (BuildException e) {
            log.error("Cannot connect to database.");
            e.printStackTrace();
            System.exit(-1);
        }    
    }

    private static void fillFileDb() {
        var pathToCreate = appContext.getConfiguration().getConfig().get("db.create-url");
        var pathToDb = appContext.getConfiguration().getConfig().get("db.url");
        var createDirectory = new File(appContext.getConfiguration().getConfig().get("db.create-url"));
        for (var file : Objects.requireNonNull(createDirectory.list())) {
            var pathToCreateFile = pathToCreate.concat("/").concat(file);
            var lines = FileResource.getLines(pathToCreateFile);
            var pathToDbFile = pathToDb.concat("/").concat(file);
            FileResource.writeFile(pathToDbFile, lines);
        }
    }

    private static void clearFileDb() {
        var pathToDb = appContext.getConfiguration().getConfig().get("db.url");
        var directory = new File(pathToDb);
        for (var file : Objects.requireNonNull(directory.list())) {
            var pathToDbFile = pathToDb.concat("/").concat(file);
            FileResource.writeFile(pathToDbFile, null);
        }
    }

}
