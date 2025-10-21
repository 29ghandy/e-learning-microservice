package org.example.userservice.repositories;

import org.example.userservice.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
     Teacher findByUsers_Id(long userId);;
}
