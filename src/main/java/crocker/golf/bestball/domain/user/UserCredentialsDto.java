package crocker.golf.bestball.domain.user;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialsDto {
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;
}
