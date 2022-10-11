package org.vsu.rudakov;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.vsu.rudakov.request.Request;
import org.vsu.rudakov.responce.Delete;
import org.vsu.rudakov.responce.Get;
import org.vsu.rudakov.responce.Post;
import org.vsu.rudakov.responce.Update;
import org.vsu.rudakov.utils.FileResource;

import java.io.File;
import java.util.Objects;
import java.util.Scanner;

@Slf4j
public class ConsoleApp extends App {

    public void prepare() {
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

    public void clean() {
        var pathToDb = appContext.getConfiguration().getConfig().get("db.url");
        var directory = new File(pathToDb);
        for (var file : Objects.requireNonNull(directory.list())) {
            var pathToDbFile = pathToDb.concat("/").concat(file);
            FileResource.writeFile(pathToDbFile, null);
        }
    }

    @SneakyThrows
    public void run() {
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
                    //todo: priority test
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
    }
}
