package com.gthm.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gthm.api.models.Pokemon;

public class JsonUtil {

    private String jsonString;

    public JsonUtil(String jsonString) {
        this.jsonString = jsonString;
    }


    public Pokemon processJsonString() throws JsonProcessingException {
        System.out.println("executing process json");
        ObjectMapper mapper = new ObjectMapper();
        Pokemon pokemon = mapper.readValue(this.jsonString, Pokemon.class);
        return pokemon;
    }

}
