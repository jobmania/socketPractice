package com.example.tcp_ip.practice1;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpClientExample {

    public static void main(String[] args) {
        Socket socket = null;


        try {

            // Server와 통신하기 위한 Socket
            socket = new Socket();
            System.out.println(" Request is ....");

            // Server 접속
            socket.connect(new InetSocketAddress("localhost", 9999));
            System.out.println(" 통신 성공 ..");

            byte[] bytes = null;
            String message = null;

            // Socket에서 가져온 출력스트림
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            /*
             * 서버와 클라이언트가 접속이 완료되면
             * 클라이언트가 'd:/d_other/멍멍.jpg'파일을 서버로 전송한다.
             * 서버는 클라이언트가 전송한 파일을 받아서 'd:/d_other/upload/' 폴더에
             * 같은 이름으로 저장되도록 한다.
             *
             */

            // send type
            message = "클라이언트에서 보내는 데이터..";
            File file = null;
            bytes = message.getBytes("UTF-8");

            dos.writeInt(bytes.length);
            dos.write(bytes, 0, bytes.length);
            dos.flush();

            System.out.println(" 데이터 전송 성공 " + message);


            //socket에서 가져온 입력 스트림..
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            // read int
            int receiveLength = dis.readInt();


            // receive bytes
            if (receiveLength > 0) {
                byte receiveByte[] = new byte[receiveLength];
                dis.readFully(receiveByte, 0, receiveLength);

                message = new String(receiveByte);
                System.out.println("데이터 수신 완료!" + message);
            }
            // OutputStream, InputStream close
            os.close();
            is.close();

            // Socket 종료
            socket.close();
            System.out.println("  Socket closed ");



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(!socket.isClosed()){
            try{
                //에러 시
                socket.close();
                System.out.println("  Socket closed ");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }




    }
}
