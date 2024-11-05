package com.gthm.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gthm.api.dto.PokemonResponse;
import com.gthm.api.models.Pokemon;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;


@Component
public class ScheduledUtil {

    @Value("classpath:json/pokemon.json")
    Resource resource;

    @Value("${featureKey:a}")
    String featureKey;

    private String jsonString;

    @Autowired
    private ToggleService toggleService;

    @Autowired
    DtoProcessor dtoProcessor;


    @PostConstruct
    public void loadFile() throws IOException {
        jsonString = FileUtility.loadFile(resource);
    }


    @Scheduled(fixedRate = 3000)
    public PokemonResponse scheduledTasks(PokemonResponse pokemonResponse) throws JsonProcessingException {
        System.out.println("scheduled task executed");
        if(toggleService.isEnabled(featureKey)) {
            System.out.println("feature key enabled condition passed");
            JsonUtil jsonUtil = new JsonUtil(jsonString);
            Pokemon pokemon = jsonUtil.processJsonString();

            PokemonResponse process = dtoProcessor.toProcess(pokemon);
            return pokemonResponse;
        }
        return null;
    }


}