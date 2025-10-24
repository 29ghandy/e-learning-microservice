package org.example.userservice.services.helper.designPatterns.userSaving;

import lombok.RequiredArgsConstructor;
import org.example.userservice.models.Role;
import org.example.userservice.models.Teacher;
import org.example.userservice.models.Users;
import org.example.userservice.repositories.RoleRepository;
import org.example.userservice.repositories.TeacherRepository;
import org.example.userservice.repositories.UserRepository;
import org.example.userservice.requestBodies.SignUpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("TEACHER")
@RequiredArgsConstructor
public  class TeacherStrategy implements UserSave{
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void signUp(SignUpRequest request) throws IOException {
        Users user = new Users();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role role = roleRepository.findByName(request.getRole());
        user.setRole(role);
        String imageUrl = ImageSaver.saveImage(request.getImage());
        user.setImageUrl(imageUrl);
        userRepository.save(user);

        Teacher teacher = new Teacher();
        teacher.setUsers(user);
        teacher.setYearsOfExperience(request.getYearsOfExperience());
        teacherRepository.save(teacher);
    }

}
