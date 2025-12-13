package com.clapped.scoreboard.messaging.deserializers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class JacksonDeserializer<T> implements Deserializer<T> {

    private static final String TYPE_CONFIG_KEY = "value.deserializer.type";

    private final ObjectMapper mapper;
    private Class<T> targetType;

    /**
     * Constructor for subclasses that know their target type.
     *
     * @param targetType the class to deserialize to
     */
    protected JacksonDeserializer(Class<T> targetType) {
        this.mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.targetType = targetType;
    }

    /**
     * Default constructor for when type is configured via properties.
     */
    public JacksonDeserializer() {
        this.mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure(Map<String, ?> configs, boolean isKey) {
        if (targetType == null) {
            String className = (String) configs.get(TYPE_CONFIG_KEY);
            if (className == null) {
                throw new SerializationException(
                        "No target type configured. Either subclass JacksonDeserializer or set '" + TYPE_CONFIG_KEY + "' config property.");
            }
            try {
                targetType = (Class<T>) Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new SerializationException("Target class not found: " + className, e);
            }
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return mapper.readValue(data, targetType);
        } catch (IOException e) {
            throw new SerializationException("Error deserializing JSON from topic '" + topic + "' to " + targetType.getName(), e);
        }
    }
}

