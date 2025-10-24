package org.example.userservice.services.helper.designPatterns.userSaving;

import org.example.userservice.requestBodies.SignUpRequest;

import java.io.IOException;

public interface UserSave {
    void signUp(SignUpRequest request) throws IOException;
}
