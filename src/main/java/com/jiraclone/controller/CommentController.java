package com.jiraclone.controller;

import com.jiraclone.dto.CommentRequest;
import com.jiraclone.dto.CommentResponse;
import com.jiraclone.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues/{issueId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // POST /api/issues/{issueId}/comments
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long issueId,
                                                       @Valid @RequestBody CommentRequest request,
                                                       Authentication authentication) {
        String author = authentication.getName();
        return ResponseEntity.ok(commentService.addComment(issueId, request, author));
    }

    // GET /api/issues/{issueId}/comments
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long issueId) {
        return ResponseEntity.ok(commentService.getCommentsForIssue(issueId));
    }
}
