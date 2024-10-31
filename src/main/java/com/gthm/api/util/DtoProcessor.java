package com.gthm.api.util;


import com.gthm.api.dto.PokemonDto;
import com.gthm.api.dto.PokemonResponse;
import com.gthm.api.models.Pokemon;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DtoProcessor {

    public PokemonResponse toProcess(Pokemon pokemon) {
        System.out.println("Processing DTO from the DtoProcessor");
        PokemonResponse pokemonResponse1 = new PokemonResponse();
        PokemonDto pokemonDto = new PokemonDto();
        pokemonDto.setId(pokemon.getId());
        pokemonResponse1.setContent(Arrays.asList(pokemonDto));
        return pokemonResponse1;
    }

}