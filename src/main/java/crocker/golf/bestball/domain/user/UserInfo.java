package crocker.golf.bestball.domain.user;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
public class UserInfo {

    private UUID userId;
    private String userName;
    private Integer pickNumber;
}
