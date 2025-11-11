package org.example.notificationservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserClient {
    @PostMapping("/users/emails")
    List<String> getEmailsByIds(@RequestBody List<Long> userIds);
}