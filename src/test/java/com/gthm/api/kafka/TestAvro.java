package com.gthm.api.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericArray;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestAvro {

    public static byte[] jsonToAvroBytes(String json, Schema schema) throws IOException {
        // Parse JSON string to a map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = mapper.readValue(json, Map.class);

        // Convert JSON to GenericRecord based on schema
        GenericRecord record = (GenericRecord) convertToAvro(jsonMap, schema);

        // Serialize the GenericRecord to Avro bytes
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
        GenericDatumWriter<GenericRecord> writer = new GenericDatumWriter<>(schema);
        writer.write(record, encoder);
        encoder.flush();
        return out.toByteArray();
    }

    private static Object convertToAvro(Object jsonValue, Schema schema) {
        switch (schema.getType()) {
            case RECORD:
                // Handle RECORD type by mapping each field
                GenericRecord record = new GenericData.Record(schema);
                Map<String, Object> jsonMap = (Map<String, Object>) jsonValue;
                for (Schema.Field field : schema.getFields()) {
                    Object fieldValue = jsonMap.get(field.name());
                    record.put(field.name(), convertToAvro(fieldValue, field.schema()));
                }
                return record;

            case ARRAY:
                // Handle ARRAY type by converting each element
                List<?> jsonList = (List<?>) jsonValue;
                GenericArray<Object> array = new GenericData.Array<>(jsonList.size(), schema);
                for (Object element : jsonList) {
                    array.add(convertToAvro(element, schema.getElementType()));
                }
                return array;

            case UNION:
                // Handle UNION by finding the matching schema type
                for (Schema unionSchema : schema.getTypes()) {
                    if (isMatchingType(jsonValue, unionSchema)) {
                        return convertToAvro(jsonValue, unionSchema);
                    }
                }
                throw new IllegalArgumentException("No matching schema for UNION type");

            case STRING:
                return jsonValue.toString();

            case INT:
                return ((Number) jsonValue).intValue();

            case LONG:
                return ((Number) jsonValue).longValue();

            case FLOAT:
                return ((Number) jsonValue).floatValue();

            case DOUBLE:
                return ((Number) jsonValue).doubleValue();

            case BOOLEAN:
                return (Boolean) jsonValue;

            case NULL:
                return null;

            default:
                throw new IllegalArgumentException("Unsupported schema type: " + schema.getType());
        }
    }

    private static boolean isMatchingType(Object jsonValue, Schema schema) {
        switch (schema.getType()) {
            case RECORD:
                return jsonValue instanceof Map;
            case ARRAY:
                return jsonValue instanceof List;
            case MAP:
                return jsonValue instanceof Map;
            case UNION:
                return true; // UNION can match multiple types
            case STRING:
                return jsonValue instanceof String;
            case INT:
                return jsonValue instanceof Integer;
            case LONG:
                return jsonValue instanceof Long || jsonValue instanceof Integer;
            case FLOAT:
                return jsonValue instanceof Float || jsonValue instanceof Double;
            case DOUBLE:
                return jsonValue instanceof Double || jsonValue instanceof Float;
            case BOOLEAN:
                return jsonValue instanceof Boolean;
            case NULL:
                return jsonValue == null;
            default:
                return false;
        }
    }
}