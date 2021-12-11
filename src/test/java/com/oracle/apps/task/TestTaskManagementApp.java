package com.oracle.apps.task;

import com.oracle.apps.task.config.TaskManagementConfig;
import com.oracle.apps.task.dto.Task;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertTrue;


@ExtendWith(DropwizardExtensionsSupport.class)
public class TestTaskManagementApp {

    public static final DropwizardTestSupport<TaskManagementConfig> SUPPORT =
            new DropwizardTestSupport<TaskManagementConfig>(TaskManagementApp.class,
                    ResourceHelpers.resourceFilePath("application-config-test.yaml")
            );

    private static String basePath;

    public TestTaskManagementApp() throws Exception {
        SUPPORT.before();
        basePath = String.format("http://localhost:%d/ora/task-manager/task", SUPPORT.getLocalPort());
    }


    @Test
    public void addTasksTest()  throws Exception  {
        Client client = new JerseyClientBuilder(SUPPORT.getEnvironment()).build("AddTaskTestClient");
        Task savedTask = new Task("Testing Add ", LocalDate.now(), null);
        Task savedTask2 = new Task("Testing Add 0", LocalDate.now(), null);
        Response response = client.target(basePath).request().post(Entity.json(savedTask));
        assertTrue(response.getStatus() == Response.Status.CREATED.getStatusCode());
        response = client.target(basePath).request().post(Entity.json(savedTask2));
        assertTrue(response.getStatus() == Response.Status.CREATED.getStatusCode());
        Task t = response.readEntity(Task.class);
        assertTrue(t.getId() != null);
    }

    @Test
    public void findTasksTest()  throws Exception  {
        Client client = new JerseyClientBuilder(SUPPORT.getEnvironment()).build("findTasksTestClient");
        Task savedTask = new Task("Testing Add 1", LocalDate.now(), null);
        Task savedTask2 = new Task("Testing Add 2", LocalDate.now(), null);
        Response response = client.target(basePath).request().post(Entity.json(savedTask));
        assertTrue(response.getStatus() == Response.Status.CREATED.getStatusCode());
        response = client.target(basePath).request().post(Entity.json(savedTask2));
        assertTrue(response.getStatus() == Response.Status.CREATED.getStatusCode());
        response = client.target(basePath + "/fetchAll").request().get();
        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
        List l = response.readEntity(List.class);
        assertTrue(l.size() >= 2);
    }

    @Test
    public void findTaskByIdTest()  throws Exception  {
        Client client = new JerseyClientBuilder(SUPPORT.getEnvironment()).build("findTaskByIdTestClient");
        Task savedTask = new Task("Testing Add 3", LocalDate.now(), null);
        Response response = client.target(basePath).request().post(Entity.json(savedTask));
        assertTrue(response.getStatus() == Response.Status.CREATED.getStatusCode());
        Task t = response.readEntity(Task.class);
        assertTrue(t.getId() != null);
        long returnedId = t.getId().longValue();
        response = client.target(basePath + "/" + returnedId).request().get();
        assertTrue(response.getStatus() == Response.Status.OK.getStatusCode());
        Task tFound = response.readEntity(Task.class);
        assertTrue(tFound.getId().compareTo(t.getId()) == 0);
    }

    @Test
    public void deleteByIdTest()  throws Exception  {
        Client client = new JerseyClientBuilder(SUPPORT.getEnvironment()).build("deleteByIdTestClient");
        Task savedTask = new Task("Testing Add 4", LocalDate.now(), null);
        Response response = client.target(basePath).request().post(Entity.json(savedTask));
        assertTrue(response.getStatus() == Response.Status.CREATED.getStatusCode());
        Task t = response.readEntity(Task.class);
        assertTrue(t.getId() != null);
        long returnedId = t.getId().longValue();
        response = client.target(basePath + "/" + returnedId).request().delete();
        assertTrue(response.getStatus() == Response.Status.ACCEPTED.getStatusCode());
        response = client.target(basePath + "/" + returnedId).request().get();
        assertTrue(response.getStatus() == Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void modifyTaskTest()  throws Exception  {
        Client client = new JerseyClientBuilder(SUPPORT.getEnvironment()).build("modifyTaskTestClient");
        Task savedTask = new Task("Testing Add 7", LocalDate.now(), null);
        Response response = client.target(basePath).request().post(Entity.json(savedTask));
        assertTrue(response.getStatus() == Response.Status.CREATED.getStatusCode());
        Task t = response.readEntity(Task.class);
        assertTrue(t.getId() != null);
        long returnedId = t.getId().longValue();
        savedTask.setDescription("Testing Modification");
        savedTask.setId(returnedId);
        response = client.target(basePath ).request().put(Entity.json(savedTask));
        assertTrue(response.getStatus() == Response.Status.ACCEPTED.getStatusCode());
        response = client.target(basePath + "/" + returnedId).request().get();
        Task tFound = response.readEntity(Task.class);
        assertTrue(tFound.getDescription().equals(savedTask.getDescription()));
    }


}