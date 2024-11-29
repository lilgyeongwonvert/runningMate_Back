package com.demo.album.repository;

import com.demo.album.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project,Long> {

    Optional<Project> findById(Long projectId);
    List<Project> findAllByGroup_Id(Long groupId);
}
