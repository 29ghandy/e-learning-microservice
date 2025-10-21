package org.example.userservice.services.helper.userSaving;

import org.example.userservice.requestBodies.SignUpRequest;

import java.io.IOException;

public interface UserSave {
    void signUp(SignUpRequest request) throws IOException;
}
