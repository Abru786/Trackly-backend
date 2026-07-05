package com.jiraclone.dto;

import com.jiraclone.model.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IssueStatusUpdateRequest {
    @NotNull
    private Status status;
}
