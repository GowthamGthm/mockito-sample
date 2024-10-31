package com.gthm.api.util;


import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ToggleService {

    static Map<String , Boolean> map;

    static {
        map = new HashMap<>();
        map.put("a" , true);
        map.put("b" , false);
    }

    public boolean isEnabled(String key) {
        System.out.println("feature toggle called");
        return map.get(key);
    }

}