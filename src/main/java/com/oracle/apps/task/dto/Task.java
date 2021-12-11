package com.oracle.apps.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TASK")
@NamedQueries({
        @NamedQuery(name = "com.oracle.apps.task.dto.findAll",
                query = "select t from Task t")
})
public class Task {

    @JsonProperty("description")
    @NotNull
    @Size(max = 20, min=1)
    @Getter
    @Setter
    private String description;

    @JsonProperty("taskDate")
    @NotNull
    @Getter
    @Setter
    private LocalDate taskDate;

    @JsonProperty("id")
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getTaskDate() {
        return taskDate.toString();
    }

}
