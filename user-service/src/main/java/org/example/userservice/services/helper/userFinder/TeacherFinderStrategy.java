package org.example.userservice.services.helper.userFinder;

import lombok.RequiredArgsConstructor;
import org.example.userservice.models.Teacher;
import org.example.userservice.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

@Service(
        "TEACHER ROLE"
)
@RequiredArgsConstructor
public class TeacherFinderStrategy implements UserFinderStrategy {

    private final TeacherRepository teacherRepository;
    @Override
    public long findRoleID(long userID) {
        Teacher teacher = teacherRepository.findByUsers_Id(userID);
        return teacher.getId();
    }
}
