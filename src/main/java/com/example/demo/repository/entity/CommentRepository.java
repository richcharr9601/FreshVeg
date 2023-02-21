package com.example.demo.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
