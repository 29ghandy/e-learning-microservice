package org.example.userservice.services.helper.userSaving;

import lombok.RequiredArgsConstructor;
import org.example.userservice.models.Role;
import org.example.userservice.models.Student;
import org.example.userservice.models.Users;
import org.example.userservice.repositories.RoleRepository;
import org.example.userservice.repositories.StudentRepository;
import org.example.userservice.repositories.UserRepository;
import org.example.userservice.requestBodies.SignUpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("STUDENT")
@RequiredArgsConstructor
public class StudentStrategy implements UserSave{

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
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


        Student student = new Student();
        student.setUsers(user);
        studentRepository.save(student);
    }

}
