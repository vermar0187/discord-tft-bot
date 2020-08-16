package com.rjdiscbots.tftbot.update;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rjdiscbots.tftbot.exceptions.parser.JsonFieldDoesNotExistException;
import com.rjdiscbots.tftbot.exceptions.parser.PatchProcessingException;
import java.io.IOException;
import org.springframework.lang.NonNull;

public interface UpdateEntity {

    ObjectMapper objectMapper = new ObjectMapper();

    void patch() throws PatchProcessingException, IOException;

    JsonNode update(@NonNull JsonNode oldEntity, @NonNull JsonNode newEntity)
        throws PatchProcessingException;

    void save(@NonNull JsonNode entities) throws JsonFieldDoesNotExistException;
}
