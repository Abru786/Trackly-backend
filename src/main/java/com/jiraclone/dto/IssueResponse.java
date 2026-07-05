package com.jiraclone.dto;

import com.jiraclone.model.enums.IssueType;
import com.jiraclone.model.enums.Priority;
import com.jiraclone.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IssueResponse {
    private Long id;
    private String title;
    private String description;
    private IssueType type;
    private Priority priority;
    private Status status;
    private Long projectId;
    private String assigneeUsername;
    private String reporterUsername;
}
