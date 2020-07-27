package crocker.golf.bestball.domain;

import lombok.Builder;

import java.util.UUID;

@Builder
public class User {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
}
