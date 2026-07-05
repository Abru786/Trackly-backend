package com.jiraclone.service;

import com.jiraclone.dto.IssueRequest;
import com.jiraclone.dto.IssueResponse;
import com.jiraclone.model.Issue;
import com.jiraclone.model.Project;
import com.jiraclone.model.User;
import com.jiraclone.model.enums.Priority;
import com.jiraclone.model.enums.Status;
import com.jiraclone.repository.IssueRepository;
import com.jiraclone.repository.ProjectRepository;
import com.jiraclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public IssueResponse createIssue(IssueRequest request, String reporterEmail) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        User reporter = userRepository.findByEmail(reporterEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reporter not found"));

        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignee not found"));
        }

        Issue issue = new Issue();
        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());
        issue.setType(request.getType());
        issue.setPriority(request.getPriority());
        issue.setStatus(request.getStatus() != null ? request.getStatus() : Status.TODO);
        issue.setProject(project);
        issue.setAssignee(assignee);
        issue.setReporter(reporter);

        issueRepository.save(issue);
        return toResponse(issue);
    }

    // Supports GET /api/issues?projectId=1&status=TODO&priority=HIGH&assigneeId=2
    public List<IssueResponse> getIssues(Long projectId, Status status, Priority priority, Long assigneeId) {
        List<Issue> issues;

        if (status != null) {
            issues = issueRepository.findByProjectIdAndStatus(projectId, status);
        } else if (priority != null) {
            issues = issueRepository.findByProjectIdAndPriority(projectId, priority);
        } else if (assigneeId != null) {
            issues = issueRepository.findByProjectIdAndAssigneeId(projectId, assigneeId);
        } else {
            issues = issueRepository.findByProjectId(projectId);
        }

        return issues.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public IssueResponse getIssueById(Long id) {
        Issue issue = findIssueOrThrow(id);
        return toResponse(issue);
    }

    public IssueResponse updateIssue(Long id, IssueRequest request) {
        Issue issue = findIssueOrThrow(id);

        issue.setTitle(request.getTitle());
        issue.setDescription(request.getDescription());
        issue.setType(request.getType());
        issue.setPriority(request.getPriority());
        if (request.getStatus() != null) {
            issue.setStatus(request.getStatus());
        }
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignee not found"));
            issue.setAssignee(assignee);
        }

        issueRepository.save(issue);
        return toResponse(issue);
    }

    // Lightweight endpoint just for dragging a card between Kanban columns
    public IssueResponse updateStatus(Long id, Status newStatus) {
        Issue issue = findIssueOrThrow(id);
        issue.setStatus(newStatus);
        issueRepository.save(issue);
        return toResponse(issue);
    }

    public void deleteIssue(Long id) {
        if (!issueRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Issue not found");
        }
        issueRepository.deleteById(id);
    }

    private Issue findIssueOrThrow(Long id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Issue not found"));
    }

    private IssueResponse toResponse(Issue issue) {
        return new IssueResponse(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getType(),
                issue.getPriority(),
                issue.getStatus(),
                issue.getProject().getId(),
                issue.getAssignee() != null ? issue.getAssignee().getUsername() : null,
                issue.getReporter() != null ? issue.getReporter().getUsername() : null
        );
    }
}
