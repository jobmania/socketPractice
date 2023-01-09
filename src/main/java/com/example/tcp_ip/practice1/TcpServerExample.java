package com.example.tcp_ip.practice1;

import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerExample {

    public static int tcpServerPort = 9999;

    public static void main(String[] args) {

    }

    public TcpServerExample(int portNum) {
        tcpServerPort = portNum;

        try {
            // 서버 소켓을 생성... socket() -> bind() -> listen() -> accept()순..

            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(tcpServerPort));
            System.out.println("tcpServerPort = " + tcpServerPort + "\n [실행시작!] ");

            //지속 연결
            while (true) {
                // socket -> bind -> listen socket 클래스 내부에 구현되어 있음
                Socket socket = serverSocket.accept();
                System.out.println("연결된 포트는  " + socket.getLocalPort() + " 이며 From " + socket.getRemoteSocketAddress().toString()  );
                //Thread
                Server tcpServer = new Server(socket);
                tcpServer.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public class Server extends Thread {
        private Socket socket;

        public Server(Socket socket) {
            this.socket = socket;

        }



        //Thread 클래스는 start() 메서드 실행 시 run() 메서드가 수행되도록 내부적으로 동작합니다.
        public void run() {

            try {
                while (true) {

                     /*데이터 스트림 :
                    DataInputStream, DataOutputStream 클래스
                    기초 자료형 단위로 데이터를 읽고 쓸 수 있다.바이트 단위가 아니라 double 타입으로 읽고 쓸 수 있고 각 자료형의 크기가 다르기 때문에 동일한 순서로 읽어야 한다.
                    */

                    // Socket에서 가져온 출력스트림
                    OutputStream os = this.socket.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(os);



                    // Socket에서의 입력스트립
                    InputStream is = this.socket.getInputStream();
                    DataInputStream dis = new DataInputStream(is);

                    // read int
                    int recieveLength = dis.readInt();

                    // receive bytes
                    byte recieveByte[] = new byte[recieveLength];
                    dis.readFully(recieveByte, 0, recieveLength);
                    String recieveMessage = new String(recieveByte);

                    System.out.println("recieveMessage = " + recieveMessage);
                    System.out.println("성공적으로 데이터 수신 완료 ");

                    // send bytes
                    String sendMessage = "서버에서 보내는 데이터";
                    byte[] sendBytes = sendMessage.getBytes("UTF-8");
                    int sendLength = sendBytes.length;

                    dos.writeInt(sendLength);
                    dos.write(sendBytes, 0, sendLength);
                    dos.flush();


                    System.out.println("sendMessage : " + sendMessage);
                    System.out.println("성공적으로 데이터 송신 완료!");


                }
            } catch (IOException e) {
                // read int 에러 발생 () -> readInt()를 호출했을 때 더 이상 읽을 내용이 없으면 발생
                throw new RuntimeException(e);
            } finally {
                try {
                    if (this.socket != null) {
                        System.out.println(" 소켓 Close");
                        System.out.println("Disconnected " + this.socket.getInetAddress().getHostAddress() + " : " +
                                this.socket.getPort());

                        this.socket.close();

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
