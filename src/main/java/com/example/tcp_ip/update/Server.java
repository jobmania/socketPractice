package com.example.tcp_ip.update;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.WatchService;

public class Server {

    private final static String serverPath = "C:\\socket\\serverTest";

    public static void main(String[] args) {

        File saveDir = new File(serverPath);

        try {
            ServerSocket serverSocket = new ServerSocket(9999);

            while (true) {
                Socket socket = serverSocket.accept();

                //소켓용 입력 스트림 객체 생성
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                BufferedInputStream bis = new BufferedInputStream(dis);

                try {
                    //클라이언트가 접속되었을 때 첫번째로 보내온 파일이름을 받는다.
                    String fileName = dis.readUTF();

                    //File saveFile = new File(saveDir, "파일이름..");
                    File saveFile = new File(saveDir, fileName);

                    //파일 출력용 스트림 객체 생성
                    BufferedOutputStream bos = new BufferedOutputStream(
                            new FileOutputStream(saveFile)
                    );

                    byte[] temp = new byte[1024];
                    int length = 0;

                    while ((length = bis.read(temp)) > 0) {
                        bos.write(temp, 0, length);
                    }

                    bos.flush();
                    socket.close();

                    System.out.println("save complete");

                } catch (IOException e) {
                    // 클라이언트와의 연결이 끊어졌을 때, 예외를 발생시켜서 while 루프를 빠져나옴
                    break;
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}



