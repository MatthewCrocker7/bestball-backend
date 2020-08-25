package crocker.golf.bestball.core.draft;

import crocker.golf.bestball.core.repository.DraftRepository;
import crocker.golf.bestball.core.util.TimeHelper;
import crocker.golf.bestball.domain.game.draft.DraftSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.TaskScheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

public class DraftScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DraftScheduler.class);

    private TaskScheduler taskScheduler;
    private Map<UUID, ScheduledFuture<?>> taskMap = new ConcurrentHashMap<>();
    private DraftRepository draftRepository;

    @Autowired
    private ApplicationContext applicationContext;

    public DraftScheduler(TaskScheduler taskScheduler, DraftRepository draftRepository) {
        this.taskScheduler = taskScheduler;
        this.draftRepository = draftRepository;
    }

    public void schedule(DraftSchedule draftSchedule) {
        execute(draftSchedule);
    }

    public void warmUpDraftSchedules() {
        logger.info("Warming up existing draft schedules");
        draftRepository.getDraftSchedules().forEach(this::schedule);
    }

    private void execute(DraftSchedule draftSchedule) {
        UUID draftId = draftSchedule.getDraftId();
        logger.info("Scheduling draft {}", draftId);

        Future<?> scheduledTaskFuture = taskMap.get(draftId);

        if (scheduledTaskFuture == null) {

            DraftTasklet draftTasklet = applicationContext.getBean("draftTasklet", DraftTasklet.class);
            draftTasklet = draftTasklet.withDraftSchedule(draftSchedule);

            LocalDateTime releaseTime = draftSchedule.getReleaseTime();
            ZonedDateTime draftStartTime = TimeHelper.getZonedDateTime(releaseTime);

            logger.info("Draft {} is scheduled at {}", draftId, draftStartTime);

            ScheduledFuture<?> futureDraft = taskScheduler.schedule(draftTasklet, draftStartTime.toInstant());
            taskMap.put(draftId, futureDraft);

            logger.info("Draft scheduled {}", draftId);

        } else {
            logger.info("Draft schedule already exists for draft id {}", draftId);
        }
    }

    public Map<UUID, ScheduledFuture<?>> getTaskMap() {
        return taskMap;
    }
}
