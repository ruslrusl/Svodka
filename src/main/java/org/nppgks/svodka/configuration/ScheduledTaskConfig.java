package org.nppgks.svodka.configuration;


import org.nppgks.svodka.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduledTaskConfig {

    @Autowired
    private FileService fileService;


    @Scheduled(fixedRate = 60_000)
    public void reportCurrentTime() {
        fileService.setDataTags();
    }

}
