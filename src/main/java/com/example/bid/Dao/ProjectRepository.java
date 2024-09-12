package com.example.bid.Dao;

import com.example.bid.Utilities.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {


    List<Project> findDistinctByOrderByIdDesc(Pageable pageable);

}
