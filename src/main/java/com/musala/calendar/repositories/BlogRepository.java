package com.musala.calendar.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.musala.calendar.entities.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
}