package com.example.tcp_ip.old.practice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class TcpServer {
    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public static void main(String[] args) throws IOException {

        //경로에 저장...
        File saveDir = new File("C:/Users/admin/servertest");
//        File saveDir = new File("C:/Users/SON/son/testServer");

        ServerSocket serverSocket = null;
        Socket socket = null;
        try{

                serverSocket = new ServerSocket(9988);
                socket = serverSocket.accept(); /// 클라이언트의 요청을 기다린다.
            System.out.println("파일 저장 시작...");

            //소켓용 입력 스트림 객체 생성
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            BufferedInputStream bis = new BufferedInputStream(dis);

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

            while((length = bis.read(temp)) > 0){
                bos.write(temp , 0, length);

            }
            bos.flush();

            System.out.println("save complete");


            //스트림과 소켓 닫기
            bos.close();
            bis.close();
            socket.close();
            serverSocket.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
