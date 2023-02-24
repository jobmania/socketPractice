package com.example.tcp_ip.old.practice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
public class TcpClient {
    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public static void main(String[] args) throws IOException {

        //////// 로컬 파일 정보 ///////////

        log.info("클라이언트 실행" + LocalDateTime.now());
//        File dir = new File("C:/Users/SON/son/local");
        File dir = new File("C:/Users/admin/local");

        //해당 경로에 파일 리스트 출력
        File files[] = dir.listFiles();

        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < files.length; i++) {

            // 파일 경로 및 파일 마지막 수정 날짜 저장..
            Path filePath = Paths.get(String.valueOf(files[i]));
            FileTime lastModifiedTime = Files.getLastModifiedTime(filePath);

            // key(이름) : value(수정된 날짜)로 저장..
            String fileName = String.valueOf(files[i]).substring(21);
            //디렉토리 명 빼고 순수 파일명만 담기
            map.put(fileName, String.valueOf(lastModifiedTime));


            //////////////////////////////////////////////////////

            /////////      파일 전송        //////////


            Socket socket = null;




            try {

                System.out.println("ready to store");
                socket = new Socket("localhost", 9988);


                //Socket용 OutputStream 객체 생성
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                BufferedOutputStream bos = new BufferedOutputStream(dos);

                // 서버에 접속하면 첫번째로 전송할 파일의 파일명을 전송한다.
                dos.writeUTF(fileName);


                //파일 입력용 InputStream 객체 생성
                File file = new File(String.valueOf(filePath));
                BufferedInputStream bis = new BufferedInputStream(
                        new FileInputStream(file)
                );

                byte[] temp = new byte[1024];
                int length = 0;

                // 파일 내용을 읽어와 소켓으로 전송하기
                while((length = bis.read(temp)) > 0){
                    //읽어온 데이터 갯수가 0개보다 많으면 출력
                    bos.write(temp, 0, length);
                }
                bos.flush();//현재 버퍼에 저장되어 있는 내용을 클라이언트로 전송하고 버퍼를 비운다.


                System.out.println("file transfer complete");



                //스트림과 소켓 닫기
                bis.close();
                bos.close();
                socket.close();


            } catch (Exception e) {
                System.out.println("fail to transfer : " + e.getMessage());
            }

        }

    }
}
