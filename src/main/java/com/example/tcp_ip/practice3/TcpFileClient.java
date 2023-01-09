package com.example.tcp_ip.practice3;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class TcpFileClient {
    enum DialogChoice { OPEN, SAVE, DELETE }

    private void clientStart(){
        //파일을 읽어서 Socket으로 쓰기: 전송

        //File file = new File("d:/d_other/멍멍.jpg");
        File file =showDialog(DialogChoice.OPEN);

        if(file == null){
            System.out.println("전송한 파일을 선택하지 않았습니다.");
            System.out.println("작업을 중단합니다...");
            return;
        }

        String fileName = file.getName();
        if(!file.exists()){
            System.out.println(fileName + "파일이 없습니다.");
            System.out.println("작업을 중단 합니다...");
            return;
        }

        try {
            Socket socket = new Socket("localhost" , 7777);
            System.out.println("파일 전송 시작...");


            //Socket용 OutputStream 객체 생성
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            BufferedOutputStream bos = new BufferedOutputStream(dos);

            // 서버에 접속하면 첫번째로 전송할 파일의 파일명을 전송한다.
            dos.writeUTF(fileName);


            //파일 입력용 InputStream 객체 생성
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

//           여기까지 for문 돌고
            System.out.println("파일 전송 완료..");

            //스트림과 소켓 닫기
            bis.close();
            bos.close();
            socket.close();


        } catch (Exception e) {
            System.out.println("파일 전송 실패 : " + e.getMessage());
        }

    }


    public File showDialog(DialogChoice choice){

       // 사용자가 파일열게
        // SWING의 파일 열기, 저장 창 연습
        JFileChooser chooser = new JFileChooser();

        // 선택할 파일의 확장자 설정
        FileNameExtensionFilter doc =
                new FileNameExtensionFilter("Word File", "docx", "doc");
        FileNameExtensionFilter img =
                new FileNameExtensionFilter("Image File", new String[]{"png", "jpg", "gif"});
        FileNameExtensionFilter txt =
                new FileNameExtensionFilter("text파일(.txt)", "txt");

        chooser.addChoosableFileFilter(doc);
        chooser.addChoosableFileFilter(img);
        chooser.addChoosableFileFilter(txt);

        // 확장자 목록 중 기본적으로 선택될 확장자 지정
        chooser.setFileFilter(txt);

        // 모든 파일 목록 표시 여부 설정(true: 설정, false: 해제)
        //chooser.setAcceptAllFileFilterUsed(false);
        chooser.setAcceptAllFileFilterUsed(true);

        // Dialog창에 나타날 기본 경로 설정하기
        chooser.setCurrentDirectory(new File("d:/d_other"));
        int result = -1;
        if(choice==DialogChoice.OPEN){

            // 열기 창
            result = chooser.showOpenDialog(new Panel());//부모창?
        }else if(choice==DialogChoice.SAVE){
            // 저장 창
            result = chooser.showSaveDialog(new Panel());
        }
        // '열기 창' 또는 '저장 창'에서 선택한 파일 정보 가져오기


        File selectedFile = null;
        // '열기' 또는 '저장'버튼을 눌렀을 경우에는...
        if(result == JFileChooser.APPROVE_OPTION){
            // 선택한 파일 정보 구하기
            selectedFile = chooser.getSelectedFile();
            //System.out.println("선택 파일 : " + selectedFile.getAbsolutePath());
        }
        return selectedFile;
    }

    public static void main(String[] args) {
        new TcpFileClient().clientStart();

    }

}