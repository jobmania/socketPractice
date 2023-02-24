package com.example.tcp_ip.update;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchService;

public class Client {

    public static void main(String[] args) throws IOException {


        WatchService watchService = FileSystems.getDefault().newWatchService();  // 특정 디렉토리에 변경사항을 감지한다.
        Path dir = FileSystems.getDefault().getPath("C:\\Users\\SON\\client");
    }
}

