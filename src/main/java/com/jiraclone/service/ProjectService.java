package com.jiraclone.service;

import com.jiraclone.dto.ProjectRequest;
import com.jiraclone.dto.ProjectResponse;
import com.jiraclone.model.Project;
import com.jiraclone.model.User;
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
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectResponse createProject(ProjectRequest request, String creatorEmail) {
        if (projectRepository.existsByProjectKey(request.getProjectKey())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project key already exists");
        }

        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Project project = new Project();
        project.setName(request.getName());
        project.setProjectKey(request.getProjectKey().toUpperCase());
        project.setDescription(request.getDescription());
        project.setCreatedBy(creator);

        projectRepository.save(project);
        return toResponse(project);
    }

    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProjectResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return toResponse(project);
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getProjectKey(),
                project.getDescription(),
                project.getCreatedBy() != null ? project.getCreatedBy().getUsername() : null
        );
    }
}
