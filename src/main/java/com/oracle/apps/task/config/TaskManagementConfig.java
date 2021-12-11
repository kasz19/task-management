package com.oracle.apps.task.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

public class TaskManagementConfig extends Configuration {
    @Valid
    @JsonProperty("database")
    @Getter
    @Setter
    private DataSourceFactory database = new DataSourceFactory();
}
