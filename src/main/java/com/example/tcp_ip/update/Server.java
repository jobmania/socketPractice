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

                try {

                    InputStream is = socket.getInputStream();

                    //소켓용 입력 스트림 객체 생성
                    DataInputStream dis = new DataInputStream(is);
                    BufferedInputStream bis = new BufferedInputStream(dis);

                    //클라이언트가 접속되었을 때 첫번째로 보내온 파일이름을 받는다.
                    String fileName = dis.readUTF();


                    if(fileName.contains("#")){
                        String[] tokens = fileName.split("#");

                        if (tokens[0].equals("FOLDER_CREATED")) {
                            handleFolderCreationNotification(tokens[1]);
                        } else if (tokens[0].equals("FILE_DELETED")) {
                            handleDeleteFile(tokens[1]);
                        }


                    }else {
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
                        System.out.println("save file complete");
                    }





                } catch (IOException e) {
                    // 클라이언트와의 연결이 끊어졌을 때, 예외를 발생시켜서 while 루프를 빠져나옴
                    break;
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static void handleFolderCreationNotification(String folderName) {

        String folderPath = serverPath+"/"+folderName;
        File folder = new File(folderPath);
        if (folder.mkdir()) {
            System.out.println("폴더 생성 완료: " + folderPath);
        } else {
            System.out.println("폴더 생성 실패: " + folderPath);
        }
    }

    public static void handleDeleteFile(String fileName){
        String filePath = serverPath + "/" + fileName;
        File file = new File(filePath);
        if(file.exists()){
            if(file.delete()) System.out.println(fileName + "파일 삭제");;
        }
    }

}



