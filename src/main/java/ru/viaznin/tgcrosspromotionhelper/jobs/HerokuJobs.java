package ru.viaznin.tgcrosspromotionhelper.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.viaznin.tgcrosspromotionhelper.configuration.HerokuProperties;

/**
 * @author Ilya Viaznin
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class HerokuJobs {

    private final RestTemplate restTemplate = new RestTemplate();

    private final HerokuProperties herokuProperties;

    @Scheduled(fixedRate = 1000 * 60)
    public void selfWakeUp() {
        log.info("Self request started!");

        restTemplate.getForObject(herokuProperties.getSelfUrl(), String.class);

        log.info("Self request completed!");
    }
}
