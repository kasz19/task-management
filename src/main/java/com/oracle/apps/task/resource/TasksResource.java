package com.oracle.apps.task.resource;

import com.oracle.apps.task.dao.TaskDAO;
import com.oracle.apps.task.dto.Task;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/ora/task-manager/task")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class TasksResource {

    private final TaskDAO store;

    public TasksResource(TaskDAO store) {
        this.store = store;
    }

    @GET
    @Path("/fetchAll")
    @UnitOfWork
    public Response fetchAll() {
        final List<Task> tasks = store.findAll();
        if (tasks == null) {
            return Response.noContent().build();
        }
        return Response.ok().entity(tasks).build();
    }

    @POST
    @UnitOfWork
    public Response add(@Valid Task task) {
        log.info("Adding task " + task.toString());
        if(task.getId() != null) {
            throw new BadRequestException();
        }
        long idTask = store.save(task);
        task.setId(idTask);
        return Response.status(Response.Status.CREATED).entity(task).build();
    }

    @PUT
    @UnitOfWork
    public Response update(Task task) {
        log.info("Updating task " + task.toString());
        if(task.getId() == null) {
            throw new BadRequestException();
        }
        store.save(task);
        return Response.accepted().build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {
        log.info("Deleting task " + id.toString());
        store.deleteById(id);
        return Response.accepted().build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response findById(@PathParam("id") Long id) {
        log.info("Finding task by ID " + id.toString());
        Task result = store.findById(id);
        return Response.ok(result).build();
    }

}
