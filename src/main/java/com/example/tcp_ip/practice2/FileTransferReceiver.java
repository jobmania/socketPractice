package com.example.tcp_ip.practice2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 서버와 클라이언트가 접속이 완료되면
 * 클라이언트가 'd:/d_other/멍멍.jpg'파일을 서버로 전송한다.
 * 서버는 클라이언트가 전송한 파일을 받아서 'd:/d_other/upload/' 폴더에
 * 같은 이름으로 저장되도록 한다.
 *
 */

public class FileTransferReceiver {
    public static final int DEFAULT_BUFFER_SIZE = 10000;
    public static void main(String[] args) {
        int port =  9999;  //int port =  9999;
        String filename = "test.txt";              //String filename = "test.mp4"; //저장할 파일 이름



        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("This server is listening... (Port: " + port  + ")");
            Socket socket = server.accept();  //새로운 연결 소켓 생성 및 accept대기
            InetSocketAddress isaClient = (InetSocketAddress) socket.getRemoteSocketAddress();

            System.out.println("A client("+isaClient.getAddress().getHostAddress()+
                    " is connected. (Port: " +isaClient.getPort() + ")");

            FileOutputStream fos = new FileOutputStream(filename);
            InputStream is = socket.getInputStream();

            double startTime = System.currentTimeMillis();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int readBytes;
            while ((readBytes = is.read(buffer)) != -1) {
                fos.write(buffer, 0, readBytes);

            }
            double endTime = System.currentTimeMillis();
            double diffTime = (endTime - startTime)/ 1000;;

            System.out.println("time: " + diffTime+ " second(s)");

            is.close();
            fos.close();
            socket.close();
            server.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
