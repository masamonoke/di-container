package org.vsu.rudakov.configuration;

import org.vsu.rudakov.App;
import org.vsu.rudakov.ConsoleApp;
import org.vsu.rudakov.dao.ChildDao;
import org.vsu.rudakov.dao.EducatorDao;
import org.vsu.rudakov.dao.file.ChildFileDao;
import org.vsu.rudakov.dao.file.EducatorFileDao;
import org.vsu.rudakov.request.ConsoleRequest;
import org.vsu.rudakov.request.Request;
import org.vsu.rudakov.responce.Delete;
import org.vsu.rudakov.responce.Get;
import org.vsu.rudakov.responce.Post;
import org.vsu.rudakov.responce.Update;
import org.vsu.rudakov.responce.file.DeleteResponse;
import org.vsu.rudakov.responce.file.GetResponse;
import org.vsu.rudakov.responce.file.PostResponse;
import org.vsu.rudakov.responce.file.UpdateResponse;
import org.vsu.rudakov.utils.FileResource;

import java.util.HashMap;
import java.util.Map;

public class JavaConfiguration implements Configuration {
    private Map<String, String> config = null;
    private Map<Class, Class> interfaceToImplementation = null;

    @Override
    public String getPackageToScan() {
        return getConfig().get("groupId");
    }

    @Override
    public Map<Class, Class> getInterfaceToImpl() {
        if (interfaceToImplementation == null) {
            interfaceToImplementation = new HashMap<>();
            if (config.get("db.type").equals("file")) {
                interfaceToImplementation.put(ChildDao.class, ChildFileDao.class);
                interfaceToImplementation.put(EducatorDao.class, EducatorFileDao.class);
                interfaceToImplementation.put(Get.class, GetResponse.class);
                interfaceToImplementation.put(Update.class, UpdateResponse.class);
                interfaceToImplementation.put(Post.class, PostResponse.class);
                interfaceToImplementation.put(Delete.class, DeleteResponse.class);
                interfaceToImplementation.put(Request.class, ConsoleRequest.class);
            }
            if (config.get("app.type").equals("console")) {
                interfaceToImplementation.put(App.class, ConsoleApp.class);
            }
        }
        return interfaceToImplementation;
    }

    @Override
    public Map<String, String> getConfig() {
        if (config == null) {
            config = new HashMap<>();
            var lines = FileResource.getLines("src/main/resources/properties.conf");
            for (var line : lines) {
                var entry = line.split("=");
                config.put(entry[0].replaceAll("\\s+", ""), entry[1].replaceAll("\\s+", ""));
            }
        }
        return config;
    }
}
