package com.example.tcp_ip.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class SchedulerService {


    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public void runAfterTenSecondsRepeatTenSeconds() {
        log.info("10초후에실행" + LocalDateTime.now());
    }
}
