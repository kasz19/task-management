package com.oracle.apps.task.dao;

import com.oracle.apps.task.dto.Task;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@ExtendWith(DropwizardExtensionsSupport.class)
public class TestTaskDAO {

    public DAOTestExtension database = DAOTestExtension.newBuilder().addEntityClass(Task.class).build();

    private TaskDAO taskDao;

    @BeforeEach
    public void setUp() {
        taskDao = new TaskDAO(database.getSessionFactory());
    }

    @Test
    void createTaskTest() throws Exception {
        Task task = new Task();
        task.setTaskDate(LocalDate.now());
        task.setDescription("1234");
        long id = database.inTransaction(() -> {
            return taskDao.save(task);
        });
        assertTrue(id > -1);
    }

    @Test
    void fetchAllTest() {
        List<Task> tasks = database.inTransaction(() -> {
            Task task = new Task();
            task.setTaskDate(LocalDate.now());
            task.setDescription("t1");
            taskDao.save(task);
            task = new Task();
            task.setTaskDate(LocalDate.now());
            task.setDescription("t2");
            taskDao.save(task);
            return taskDao.findAll();
        });
        assertTrue(tasks.size() >= 2);
    }

    @Test
    void fetchByIdTest() {
        Task t = database.inTransaction(() -> {
            Task task = new Task();
            task.setTaskDate(LocalDate.now());
            task.setDescription("t3");
            long id = taskDao.save(task);
            assertTrue(task.getId().longValue() == id);
            return taskDao.findById(id);
        });
        assertTrue(t != null && t.getId().longValue() > -1);
    }

    @Test
    void deleteByIdTest() {
        database.inTransaction(() -> {
            Task task = new Task();
            task.setTaskDate(LocalDate.now());
            task.setDescription("t4");
            long id = taskDao.save(task);
            assertTrue(task.getId().longValue() == id);
            taskDao.deleteById(id);
            assertThrows(NotFoundException.class, () -> {
                taskDao.deleteById(id);
            });
        });
    }

    @Test
    void modifyTest() {
        database.inTransaction(() -> {
            Task task = new Task();
            task.setTaskDate(LocalDate.now());
            task.setDescription("t5");
            long id = taskDao.save(task);
            assertTrue(task.getId().longValue() == id);
            String descriptionMod = "t5Modificated";
            task.setDescription(descriptionMod);
            id = taskDao.save(task);
            Task tMod = taskDao.findById(id);
            assertTrue(tMod.getDescription().equals(descriptionMod));
        });
    }
}
