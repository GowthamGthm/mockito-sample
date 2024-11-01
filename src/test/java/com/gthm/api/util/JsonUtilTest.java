package com.gthm.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.gthm.api.models.Pokemon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonUtilTest {

    String jsonSTring = "{\n" + "  \"id\": 8301,\n" + "  \"name\": \"IOSEU\",\n" + "  \"type\": \"PHSG\"," +
            "\n" + "  \"reviews\": [\n" + "    {\n" + "      \"id\": 2971,\n" + "      \"title\": " +
            "\"GXTECKLQLS\",\n" + "      \"content\": \"XCY\",\n" + "      \"stars\": 2713,\n" + "      " +
            "\"pokemon\": null\n" + "    },\n" + "    {\n" + "      \"id\": 7509,\n" + "      \"title\": " +
            "\"SQXMPZUBZM\",\n" + "      \"content\": \"PHQXOE\",\n" + "      \"stars\": 6741,\n" + "      " +
            "\"pokemon\": null\n" + "    },\n" + "    {\n" + "      \"id\": 4439,\n" + "      \"title\": " +
            "\"HKKFPNGMTI\",\n" + "      \"content\": \"VWTAQA\",\n" + "      \"stars\": 1882,\n" + "      " +
            "\"pokemon\": null\n" + "    },\n" + "    {\n" + "      \"id\": 1538,\n" + "      \"title\": " +
            "\"SCQKWOTKOA\",\n" + "      \"content\": \"GUOV\",\n" + "      \"stars\": 4882,\n" + "      " +
            "\"pokemon\": null\n" + "    },\n" + "    {\n" + "      \"id\": 5339,\n" + "      \"title\": " +
            "\"DCC\",\n" + "      \"content\": \"RVYLACI\",\n" + "      \"stars\": 430,\n" + "      " +
            "\"pokemon\": null\n" + "    },\n" + "    {\n" + "      \"id\": 497,\n" + "      \"title\": " +
            "\"ZAUMBSN\",\n" + "      \"content\": \"RRCNS\",\n" + "      \"stars\": 9596,\n" + "      " +
            "\"pokemon\": null\n" + "    }\n" + "  ]\n" + "}";


    @Test
    void testProcessJsonString() throws JsonProcessingException {
        JsonUtil jsonUtil = new JsonUtil(jsonSTring);
        Pokemon result = jsonUtil.processJsonString();

        Assertions.assertThat(result)
                  .isNotNull()
                  .isInstanceOf(Pokemon.class);
        Assertions.assertThat(result.getId())
                  .isEqualTo(8301);
    }

    @Test
    void testProcessJsonString_throws_exception() {
        JsonUtil jsonUtil = new JsonUtil("");

        MismatchedInputException mismatchedInputException =
                org.junit.jupiter.api.Assertions.assertThrows(MismatchedInputException.class,
                        jsonUtil::processJsonString);

        mismatchedInputException.printStackTrace();


    }


}
