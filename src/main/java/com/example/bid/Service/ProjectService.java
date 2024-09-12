package com.example.bid.Service;

import com.example.bid.Dao.BidRepository;
import com.example.bid.Dao.ProjectRepository;
import com.example.bid.Utilities.Bid;
import com.example.bid.Utilities.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BidRepository bidRepository;

    public Project CreateProject(Project project) {
        return projectRepository.save(project);
    }

    @Transactional
    public Bid CreateBid(Bid bid, Long projectId) {
        Project target = projectRepository.findById(projectId).orElseThrow(
                ()-> new IllegalArgumentException("cannot find the project" + projectId)
        );

        if (bid.getBidTime().isAfter(target.getBidingDDL())){
            throw new RuntimeException("bid time is after bidding DDL");
        }

//        // Ensure the bid is not already in the list
//        if (!target.getBids().contains(bid)) {
//            target.getBids().add(bid);  // Add bid to the project
//        }

        bid.setProject(target);
        Bid saveBid = bidRepository.save(bid);
        udpateLowestBid(target,bid);
        return saveBid;
    }

    private void udpateLowestBid(Project target, Bid bid) {
        if (target.getCurLowestPrice() == null || bid.getAmount().compareTo(target.getCurLowestPrice())< 0){
            target.setCurLowestPrice(bid.getAmount());
            //target.setWinningBuyer(bid.getName());
            projectRepository.save(target);
        }
    }

    public List<Project> getRecentProjects() {

        List<Project> recentProjects = projectRepository.findAll(PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "id"))).getContent();
        // For each project, check if the deadline has passed and set the winning buyer
        recentProjects.forEach(project -> {
            if (LocalDateTime.now().isAfter(project.getBidingDDL())) {
                // If the deadline has passed, find the winning bid and set the winning buyer
                project.setWinningBuyer(
                        project.getBids().stream()
                                .min(Comparator.comparing(Bid::getAmount))  // Find the minimum bid
                                .map(bid -> bid.getName())  // Get the buyer's name
                                .orElse(null)  // If no bids, set null
                );
            }
        });
        return recentProjects;
//        Pageable pageable = PageRequest.of(0, 100, Sort.by("id").descending());
//        List<Project> recentProjects = projectRepository.findDistinctByOrderByIdDesc(pageable);
//        return recentProjects;
    }


//    public String getWinningBuyer(long projectId) {
//        Project target = projectRepository.findById(projectId).orElseThrow(
//                ()->new IllegalArgumentException("cannot find the project" + projectId)
//        );
//
//        if(LocalDateTime.now().isBefore(target.getBidingDDL())){
//            throw new RuntimeException("bid time is after bidding DDL");
//        }
//
//
//
//        String name = target.getBids().stream().min(Comparator.comparing(Bid::getAmount))
//                .map(bid ->bid.getName()).orElseThrow(
//                        ()->new IllegalStateException("no bids found for the project" + projectId)
//                );
//        if (name != null){
//            target.setWinningBuyer(name);
//            projectRepository.save(target);
//        }
//        return name;
//    }
}
