package crocker.golf.bestball.domain.user;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentials {

    private UUID userId;
    private String userName;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private Set<String> roles;
}
