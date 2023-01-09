package com.example.tcp_ip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.Socket;

@SpringBootApplication
public class TcpIpApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(TcpIpApplication.class, args);



    }

}
