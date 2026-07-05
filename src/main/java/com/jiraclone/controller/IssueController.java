package com.jiraclone.controller;

import com.jiraclone.dto.IssueRequest;
import com.jiraclone.dto.IssueResponse;
import com.jiraclone.dto.IssueStatusUpdateRequest;
import com.jiraclone.model.enums.Priority;
import com.jiraclone.model.enums.Status;
import com.jiraclone.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    // POST /api/issues
    @PostMapping
    public ResponseEntity<IssueResponse> createIssue(@Valid @RequestBody IssueRequest request,
                                                      Authentication authentication) {
        String reporter = authentication.getName();
        return ResponseEntity.ok(issueService.createIssue(request, reporter));
    }

    // GET /api/issues?projectId=1&status=TODO&priority=HIGH&assigneeId=2
    // projectId is required; status/priority/assigneeId are optional filters (used one at a time)
    @GetMapping
    public ResponseEntity<List<IssueResponse>> getIssues(
            @RequestParam Long projectId,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) Long assigneeId) {
        return ResponseEntity.ok(issueService.getIssues(projectId, status, priority, assigneeId));
    }

    // GET /api/issues/{id}
    @GetMapping("/{id}")
    public ResponseEntity<IssueResponse> getIssueById(@PathVariable Long id) {
        return ResponseEntity.ok(issueService.getIssueById(id));
    }

    // PUT /api/issues/{id}  (full edit: title, description, priority, assignee, etc.)
    @PutMapping("/{id}")
    public ResponseEntity<IssueResponse> updateIssue(@PathVariable Long id,
                                                      @Valid @RequestBody IssueRequest request) {
        return ResponseEntity.ok(issueService.updateIssue(id, request));
    }

    // PATCH /api/issues/{id}/status  (lightweight: just moving a card between Kanban columns)
    @PatchMapping("/{id}/status")
    public ResponseEntity<IssueResponse> updateStatus(@PathVariable Long id,
                                                       @Valid @RequestBody IssueStatusUpdateRequest request) {
        return ResponseEntity.ok(issueService.updateStatus(id, request.getStatus()));
    }

    // DELETE /api/issues/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        issueService.deleteIssue(id);
        return ResponseEntity.noContent().build();
    }
}
