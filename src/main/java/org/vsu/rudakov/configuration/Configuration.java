package org.vsu.rudakov.configuration;

import java.util.Map;

public interface Configuration {
    String getPackageToScan();
    Map<Class, Class> getInterfaceToImpl();

    Map<String, String> getConfig();
}
