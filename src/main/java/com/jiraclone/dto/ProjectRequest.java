package com.jiraclone.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String projectKey;

    private String description;
}
