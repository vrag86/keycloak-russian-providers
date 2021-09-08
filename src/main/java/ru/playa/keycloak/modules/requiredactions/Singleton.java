package ru.playa.keycloak.modules.requiredactions;

import java.util.HashMap;
import java.util.Map;

public enum Singleton {
    SINGLETON;

    private final Map<String, String> map = new HashMap<>();

    public void put(final String email, final String code) {
        map.put(email, code);
    }

    public String get(final String email) {
        return map.get(email);
    }

}
