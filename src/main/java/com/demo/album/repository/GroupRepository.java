package com.demo.album.repository;

import com.demo.album.entity.Groups;
import com.demo.album.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Groups, Long> {


}

