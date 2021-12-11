package com.oracle.apps.task;

import com.oracle.apps.task.config.TaskManagementConfig;
import com.oracle.apps.task.dao.TaskDAO;
import com.oracle.apps.task.dto.Task;
import com.oracle.apps.task.resource.TasksResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;


public class TaskManagementApp extends Application<TaskManagementConfig> {

    public static void main(String[] args) throws Exception {
        new TaskManagementApp().run(args);
    }

    private HibernateBundle<TaskManagementConfig> hibernate = new HibernateBundle<TaskManagementConfig>(Task.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(TaskManagementConfig taskManagementConfig) {
            return taskManagementConfig.getDatabase();
        }
    };

    public void run(TaskManagementConfig myConfiguration, Environment environment) {
        // Enable CORS headers
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        // Add DAO
        TaskDAO taskDAO = new TaskDAO(hibernate.getSessionFactory());
        final TasksResource resource = new TasksResource(taskDAO);
        environment.jersey().register(resource);
    }

    @Override
    public void initialize(Bootstrap<TaskManagementConfig> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

}
