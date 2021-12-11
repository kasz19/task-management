package com.oracle.apps.task.resource;

import com.oracle.apps.task.dao.TaskDAO;
import com.oracle.apps.task.dto.Task;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class TestTasksResource {

    private static final TaskDAO DAO = mock(TaskDAO.class);
    private static final ResourceExtension service = ResourceExtension.builder()
            .addResource(new TasksResource(DAO))
            .build();
    private long idTask = System.currentTimeMillis();

    @BeforeEach
    void setup() {
        List<Task> tasksResponse = new ArrayList<>();
        Task task1 = new Task("Description1", LocalDate.now(), idTask);
        Task task2 = new Task("Description2", LocalDate.now(), System.currentTimeMillis());
        Task task3 = new Task("Description3", LocalDate.now(), System.currentTimeMillis());
        tasksResponse.add(task1);
        tasksResponse.add(task2);
        tasksResponse.add(task3);
        when(DAO.findAll()).thenReturn(tasksResponse);
        when(DAO.findById(idTask)).thenReturn(task1);
        when(DAO.save(any())).thenReturn(idTask);
    }

    @AfterEach
    void tearDown() {
        reset(DAO);
    }

    @Test
    void addTask() {
        Task savedTask = new Task();
        savedTask.setDescription("Testing Add");
        savedTask.setTaskDate(LocalDate.now());
        savedTask.setId(null);
        Response addedTask = service.target("/ora/task-manager/task").request(MediaType.APPLICATION_JSON).post(Entity.json(savedTask));
        assertTrue(addedTask.getStatus() == Response.Status.CREATED.getStatusCode());
        Task response = addedTask.readEntity(Task.class);
        assertTrue(response.getId() != null);
    }

    @Test
    void fetchAll() {
        Response tasks = service.target("/ora/task-manager/task/fetchAll").request(MediaType.APPLICATION_JSON).get();
        assertTrue(tasks.getStatus() == Response.Status.OK.getStatusCode());
        List<Task> tasksList = tasks.readEntity(List.class);
        assertTrue(tasksList.size() == 3);
    }

    @Test
    void fetchById() {
        Response tasks = service.target("/ora/task-manager/task/" + idTask).request(MediaType.APPLICATION_JSON).get();
        assertTrue(tasks.getStatus() == Response.Status.OK.getStatusCode());
        Task task = tasks.readEntity(Task.class);
        assertTrue(task.getId() == idTask);
    }

    @Test
    void deleteTask() {
        Response removedTask = service.target("/ora/task-manager/task/12345").request(MediaType.APPLICATION_JSON).delete();
        assertTrue(removedTask.getStatus() == Response.Status.ACCEPTED.getStatusCode());
    }

    @Test
    void modifyTask() {
        Task savedTask = new Task();
        savedTask.setDescription("Testing Modify");
        savedTask.setTaskDate(LocalDate.now());
        savedTask.setId(idTask);
        Response task = service.target("/ora/task-manager/task").request(MediaType.APPLICATION_JSON).put(Entity.json(savedTask));
        assertTrue(task.getStatus() == Response.Status.ACCEPTED.getStatusCode());
    }

    /*********************************************************/
    /**************** Negative cases *************************/
    /*********************************************************/

    @Test
    void deleteTaskNullId() {
        Response removedTask = service.target("/ora/task-manager/task/").request(MediaType.APPLICATION_JSON).delete();
        assertTrue(removedTask.getStatus() == Response.Status.METHOD_NOT_ALLOWED.getStatusCode());
    }

    @Test
    void addTaskNullDescription() {
        Task savedTask = new Task();
        savedTask.setDescription(null);
        savedTask.setTaskDate(LocalDate.now());
        savedTask.setId(null);
        Response addedTask = service.target("/ora/task-manager/task").request(MediaType.APPLICATION_JSON).post(Entity.json(savedTask));
        assertTrue(addedTask.getStatus() == 422);
    }

    @Test
    void modifyTaskNullId() {
        Task savedTask = new Task();
        savedTask.setDescription("Testing Modify");
        savedTask.setTaskDate(LocalDate.now());
        savedTask.setId(null);
        Response task = service.target("/ora/task-manager/task").request(MediaType.APPLICATION_JSON).put(Entity.json(savedTask));
        assertTrue(task.getStatus() == Response.Status.BAD_REQUEST.getStatusCode());
    }

}