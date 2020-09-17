package crocker.golf.bestball.core.service.user;

import crocker.golf.bestball.core.repository.DraftRepository;
import crocker.golf.bestball.core.repository.GameRepository;
import crocker.golf.bestball.core.repository.UserRepository;
import crocker.golf.bestball.domain.enums.game.TeamRole;
import crocker.golf.bestball.domain.game.Team;
import crocker.golf.bestball.domain.user.RequestDto;
import crocker.golf.bestball.domain.user.UserCredentials;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.UUID;

public class EmailService {

    private JavaMailSender javaMailSender;
    private UserRepository userRepository;
    private DraftRepository draftRepository;
    private GameRepository gameRepository;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void invitePlayerToDraft(RequestDto requestDto) {
        String email = requestDto.getEmail();

        UUID draftId = UUID.fromString(requestDto.getDraftId());
        UserCredentials userCredentials = userRepository.findByEmail(email);

        Team team = gameRepository.getTeamByUserAndDraftId(userCredentials.getUserId(), draftId);

        if (team.getTeamRole() == TeamRole.CREATOR) {
            // send email
            // open email with link to join game game_id on link
            // must login/register before joining game
        } else {
            // do something else
        }
    }

    public void invitePlayer(UserCredentials userCredentials, String inviteEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(inviteEmail);
        message.setFrom("invite@bb.golf");
        message.setSubject("Game Invite for Best Ball from " + userCredentials.getFirstName() + " " + userCredentials.getLastName());

        String body = userCredentials.getFirstName() + " " + userCredentials.getLastName() + " has invited you to their best ball game.";
    }
}
