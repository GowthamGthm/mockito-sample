package com.gthm.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gthm.api.dto.PokemonResponse;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ScheduledUtilTest {

//     InjectMock - when the real execution as to happen
    @InjectMocks
    ScheduledUtil scheduledUtil;

//     MOck when the method should not be executed,
//     if not stubbed with when and then, metjod will not execute but default will be returned
    @Mock
    ToggleService toggleService;


//    Spy is used
    @Spy
    DtoProcessor dtoProcessor;


    @BeforeEach
    public void init() throws IOException {

        Resource resource = new ClassPathResource("json/pokemon.json");
        String jsonString = FileUtility.loadFile(resource);
        ReflectionTestUtils.setField(scheduledUtil, "jsonString", jsonString);
        ReflectionTestUtils.setField(scheduledUtil, "featureKey", "c");

    }


    @Test
    public void test1() throws JsonProcessingException {
        PokemonResponse pokemonResponse = Instancio.of(PokemonResponse.class)
                                                   .create();

        Mockito.when(toggleService.isEnabled(ArgumentMatchers.anyString())).thenReturn(true);
        PokemonResponse pokemonResponse1 = scheduledUtil.scheduledTasks(pokemonResponse);
        Assertions.assertThat(pokemonResponse1).isNotNull();

    }


}