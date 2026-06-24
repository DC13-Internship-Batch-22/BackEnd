package com.batch22bd.BackEnd.Repository;

import com.batch22bd.BackEnd.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
