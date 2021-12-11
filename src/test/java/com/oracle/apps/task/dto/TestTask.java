package com.oracle.apps.task.dto;

import static io.dropwizard.testing.FixtureHelpers.*;
import static org.junit.Assert.assertTrue;

import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

public class TestTask {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void deserializesFromJSON() throws Exception {
        final Task task = new Task();
        task.setDescription("Test");
        task.setTaskDate(LocalDate.parse("2021-10-10"));
        task.setId((long)123);
        final Task taskFromFile = MAPPER.readValue(fixture("fixtures/task-example.json"), Task.class);
        assertTrue(taskFromFile.getDescription().equals(task.getDescription()));
        assertTrue(taskFromFile.getTaskDate().equals(task.getTaskDate()));
        assertTrue(taskFromFile.getId().equals(task.getId()));
    }

    @Test
    public void serializesToJSON() throws Exception {
        final Task task = new Task();
        task.setDescription("Test");
        task.setTaskDate(LocalDate.parse("2021-10-10"));
        task.setId((long)123);
        final String received = MAPPER.writeValueAsString(task);

        final Task taskFromFile = MAPPER.readValue(fixture("fixtures/task-example.json"), Task.class);
        final String expected = MAPPER.writeValueAsString(taskFromFile);

        assertTrue(expected.equals(received));
    }
}