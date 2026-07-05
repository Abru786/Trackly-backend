package com.jiraclone.dto;

import com.jiraclone.model.enums.IssueType;
import com.jiraclone.model.enums.Priority;
import com.jiraclone.model.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IssueRequest {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private IssueType type;

    @NotNull
    private Priority priority;

    // Optional on create (defaults to TODO); used on update to change column
    private Status status;

    @NotNull
    private Long projectId;

    // Optional - can be unassigned initially
    private Long assigneeId;
}
