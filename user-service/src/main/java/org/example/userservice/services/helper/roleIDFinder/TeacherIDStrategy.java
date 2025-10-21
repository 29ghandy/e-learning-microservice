package org.example.userservice.services.helper.roleIDFinder;

import lombok.RequiredArgsConstructor;
import org.example.userservice.models.Teacher;
import org.example.userservice.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

@Service(
        "TEACHER ROLE"
)
@RequiredArgsConstructor
public class TeacherIDStrategy implements RoleFinderStrategy{

    private final TeacherRepository teacherRepository;
    @Override
    public long findRoleID(long userID) {
        Teacher teacher = teacherRepository.findByUsers_Id(userID);
        return teacher.getId();
    }
}
