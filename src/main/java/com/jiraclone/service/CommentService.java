package com.jiraclone.service;

import com.jiraclone.dto.CommentRequest;
import com.jiraclone.dto.CommentResponse;
import com.jiraclone.model.Comment;
import com.jiraclone.model.Issue;
import com.jiraclone.model.User;
import com.jiraclone.repository.CommentRepository;
import com.jiraclone.repository.IssueRepository;
import com.jiraclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    public CommentResponse addComment(Long issueId, CommentRequest request, String authorEmail) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Issue not found"));

        User author = userRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setIssue(issue);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);
        return toResponse(comment);
    }

    public List<CommentResponse> getCommentsForIssue(Long issueId) {
        return commentRepository.findByIssueIdOrderByCreatedAtAsc(issueId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getUsername(),
                comment.getCreatedAt()
        );
    }
}
