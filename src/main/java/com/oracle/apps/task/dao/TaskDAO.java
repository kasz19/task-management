package com.oracle.apps.task.dao;

import com.oracle.apps.task.dto.Task;
import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.SessionFactory;

import javax.ws.rs.NotFoundException;
import java.util.List;

public class TaskDAO extends AbstractDAO<Task> {

    public TaskDAO(SessionFactory factory) {
        super(factory);
    }

    public Task findById(Long id) throws NotFoundException {
        Task t = get(id);
        if (t == null) {
            throw new NotFoundException();
        }
        return get(id);
    }

    @UnitOfWork
    public long save(Task task) {
        return persist(task).getId();
    }

    public void deleteById(Long id) {
        currentSession().delete(findById(id));
    }

    public List<Task> findAll() {
        return list(namedTypedQuery("com.oracle.apps.task.dto.findAll"));
    }
}