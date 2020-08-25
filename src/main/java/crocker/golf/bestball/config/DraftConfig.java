package crocker.golf.bestball.config;

import crocker.golf.bestball.core.draft.DraftExecutor;
import crocker.golf.bestball.core.draft.DraftManager;
import crocker.golf.bestball.core.draft.DraftScheduler;
import crocker.golf.bestball.core.draft.DraftTasklet;
import crocker.golf.bestball.core.repository.DraftRepository;
import crocker.golf.bestball.core.repository.GameRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;

@Configuration
public class DraftConfig {

    @Bean
    public DraftManager draftManager(DraftScheduler draftScheduler, DraftRepository draftRepository) {
        return new DraftManager(draftScheduler, draftRepository);
    }

    @Bean
    public DraftTasklet draftTasklet(DraftExecutor draftExecutor) {
        return new DraftTasklet(draftExecutor);
    }

    @Bean
    public DraftExecutor draftExecutor(DraftScheduler draftScheduler) {
        return new DraftExecutor(draftScheduler);
    }

    @Bean(initMethod = "warmUpDraftSchedules")
    public DraftScheduler draftScheduler(TaskScheduler draftTaskScheduler, DraftRepository draftRepository) {
        return new DraftScheduler(draftTaskScheduler, draftRepository);
    }
}
