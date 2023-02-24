package com.example.tcp_ip.update;

import java.io.IOException;
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
        Path path = FileSystems.getDefault().getPath("C:\\clientTest");


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
                            System.out.println("디렉토리에 " + paths.getFileName() + "파일이 생성되었습니다.");
                        } else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                            System.out.println("디렉토리에 " + paths.getFileName() + "파일이 삭제되었습니다.");
                        } else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                            System.out.println("디렉토리에 " + paths.getFileName() + "파일이 수정되었습니다.");
                        } else if (kind.equals(StandardWatchEventKinds.OVERFLOW)) {
                            System.out.println("이벤트가 손실되거나 삭제되었습니다.");
                        }
                    }

                    watchKey.reset(); // WatchKey를 리셋하여 다음 이벤트를 처리할 수 있도록 한다.
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

