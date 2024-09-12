package com.example.bid.Controller;

import com.example.bid.Service.ProjectService;
import com.example.bid.Utilities.Bid;
import com.example.bid.Utilities.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/project")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.CreateProject(project));
    }

    @PostMapping("/{projectId}/bid")
    public ResponseEntity<Bid> createBid(@PathVariable Long projectId, @RequestBody Bid bid) {
        return ResponseEntity.ok(projectService.CreateBid(bid,projectId));
    }

    @GetMapping("/recentProjects")
    public ResponseEntity<List<Project>> getRecentProjects() {
        
        return ResponseEntity.ok(projectService.getRecentProjects());
    }

//    @GetMapping("/winningBuyer/{projectId}")
//    public ResponseEntity<String> getWinningBuyer(@PathVariable long projectId) {
//        return ResponseEntity.ok(projectService.getWinningBuyer(projectId));
//    }
}
