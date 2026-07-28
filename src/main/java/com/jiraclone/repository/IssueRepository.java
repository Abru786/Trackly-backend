package com.jiraclone.repository;

import com.jiraclone.model.Issue;
import com.jiraclone.model.enums.Priority;
import com.jiraclone.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByProjectId(Long projectId);
    List<Issue> findByProjectIdAndStatus(Long projectId, Status status);
    List<Issue> findByProjectIdAndAssigneeId(Long projectId, Long assigneeId);
    List<Issue> findByProjectIdAndPriority(Long projectId, Priority priority);
}
