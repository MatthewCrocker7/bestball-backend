package crocker.golf.bestball.config;

import crocker.golf.bestball.core.draft.DraftExecutor;
import crocker.golf.bestball.core.draft.DraftManager;
import crocker.golf.bestball.core.draft.DraftScheduler;
import crocker.golf.bestball.core.draft.DraftTasklet;
import crocker.golf.bestball.core.repository.DraftRepository;
import crocker.golf.bestball.core.repository.GameRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class DraftConfig {

    @Bean
    public DraftManager draftManager(DraftScheduler draftScheduler, DraftRepository draftRepository) {
        return new DraftManager(draftScheduler, draftRepository);
    }

    @Bean
    @Scope("prototype")
    public DraftTasklet draftTasklet(DraftExecutor draftExecutor) {
        return new DraftTasklet(draftExecutor);
    }

    @Bean
    public DraftExecutor draftExecutor(DraftScheduler draftScheduler, DraftRepository draftRepository) {
        return new DraftExecutor(draftScheduler, draftRepository);
    }

    @Bean(initMethod = "warmUpDraftSchedules")
    public DraftScheduler draftScheduler(TaskScheduler draftTaskScheduler, DraftRepository draftRepository) {
        return new DraftScheduler(draftTaskScheduler, draftRepository);
    }

    @Bean
    public TaskScheduler draftTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }
}
