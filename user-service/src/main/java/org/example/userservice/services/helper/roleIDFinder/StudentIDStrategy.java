package org.example.userservice.services.helper.roleIDFinder;

import lombok.RequiredArgsConstructor;
import org.example.userservice.models.Student;
import org.example.userservice.repositories.StudentRepository;
import org.springframework.stereotype.Service;

@Service("STUDENT ROLE")
@RequiredArgsConstructor
public class StudentIDStrategy implements RoleFinderStrategy{
    private final StudentRepository studentRepository;
    @Override
    public long findRoleID(long userID) {
        Student student = studentRepository.findByUsers_Id(userID);
        return student.getId();
    }
}
