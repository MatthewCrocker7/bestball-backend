package crocker.golf.bestball.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
