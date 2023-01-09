package com.example.tcp_ip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.net.Socket;

@EnableScheduling
@SpringBootApplication
public class TcpIpApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TcpIpApplication.class, args);
    }

}
