package com.jiraclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private String name;
    private String projectKey;
    private String description;
    private String createdBy;
}
