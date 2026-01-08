package com.netflix.clone.netflix_clone_backend.dao;

import com.netflix.clone.netflix_clone_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
