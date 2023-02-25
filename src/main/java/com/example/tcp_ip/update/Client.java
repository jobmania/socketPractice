package com.example.tcp_ip.update;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.*;
import java.util.List;

public class Client {

    // WatchService에 디렉토리 등록을 의미하는 객체
    static WatchKey watchKey;

    public static void main(String[] args) throws IOException, InterruptedException {
        // 1. 파일 변경 감지 기능 구현
        // https://scshim.tistory.com/460

        WatchService watchService = FileSystems.getDefault().newWatchService();  // 특정 디렉토리에 변경사항을 감지한다.
        // 조사할 디렉터리 경로 입력
        Path path = FileSystems.getDefault().getPath("C:\\socket\\clientTest");


        path.register(watchService,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.OVERFLOW  // 이벤트 운영 체제에서 이벤트가 소실되었거나 버려진 경우에 알아서 발생
        );

        Thread thread = getThread(watchService);
        thread.start();



    }

    private static Thread getThread(WatchService watchService) {
        return new Thread(() -> {
            while (true) { // 무한 반복문으로 이벤트가 발생할 때마다 계속 실행
                try {
                    watchKey = watchService.take(); // 이벤트가 발생할 때까지 block된다.

                    List<WatchEvent<?>> events = watchKey.pollEvents();//이벤트들을 가져옴

                    for (WatchEvent<?> event : events) {
                        // 가져온 이벤트 종류
                        WatchEvent.Kind<?> kind = event.kind();
                        // 이벤트가 발생한 경로
                        Path paths = (Path) event.context();
                        if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)) {

                            /// 1. 파일 생성.
                            System.out.println("디렉토리에 " + paths.getFileName() + "파일이 생성되었습니다.");
                            Socket socket = new Socket("127.0.0.1", 9999);  // 접속하려는 ip,port
                            sendFileToServer(paths, socket);
                            socket.close(); // 파일 정보를 전송한 후에 소켓을 닫는다.

                        } else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                            /// 2. 파일 삭제
                            System.out.println("디렉토리에 " + paths.getFileName() + "파일이 삭제되었습니다.");

                            Socket socket = new Socket("127.0.0.1", 9999);  // 접속하려는 ip,port
                            socket.close();

                        } else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                            /// 3. 파일 수정..
                            System.out.println("디렉토리에 " + paths.getFileName() + "파일이 수정되었습니다.");

                            Socket socket = new Socket("127.0.0.1", 9999);  // 접속하려는 ip,port
                            socket.close();
                        } else if (kind.equals(StandardWatchEventKinds.OVERFLOW)) {
                            System.out.println("이벤트가 손실되거나 삭제되었습니다.");
                        }
                       ;
                    }



                    watchKey.reset(); // WatchKey를 리셋하여 다음 이벤트를 처리할 수 있도록 한다.
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);

                }
            }
        });
    }

    private static void sendFileToServer(Path paths, Socket socket ) throws IOException {
        //Socket용 OutputStream 객체 생성
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        String fileName = String.valueOf(paths.getFileName());
        dos.writeUTF(fileName);  // 파일 이름 전송

        BufferedOutputStream bos = new BufferedOutputStream(dos);
        bos.flush();//현재 버퍼에 저장되어 있는 내용을 클라이언트로 전송하고 버퍼를 비운다.

    }
}

