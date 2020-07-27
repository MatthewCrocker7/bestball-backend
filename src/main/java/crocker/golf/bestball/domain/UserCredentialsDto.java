package crocker.golf.bestball.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialsDto {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
