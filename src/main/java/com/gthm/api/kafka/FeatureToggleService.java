package com.gthm.api.kafka;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class FeatureToggleService {

    static Map<String, Boolean> map;

    static {
        map = new HashMap<>();
        map.put("a", true);
        map.put("b", false);
    }

    public boolean isFeatureToggleEnabled(String key) {
        System.out.println("feature toggle called");
        key = Optional.ofNullable(key).orElse("");
        Boolean bool = map.get(key.toLowerCase());
        return Boolean.TRUE.equals(bool);
    }


}