package org.example.userservice.requestBodies;

import lombok.Data;

@Data
public class UpdateSocialLinkRequest {
      private Long socialId;
      private String url;
}
