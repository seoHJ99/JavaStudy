package IO;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileLockTest {

    public static void main(String[] args) {
        File file = new File("C:\\Users\\Hojun\\Desktop\\git\\JavaStudy\\IOAndNIO\\Gather-Scatter.txt");

        try (FileChannel channel = new RandomAccessFile(file, "rw").getChannel()){
            try (FileLock lock = channel.lock(0, Long.MAX_VALUE, false)) {
                boolean isShared = lock.isShared();
                System.out.println("Is Shared Lock? : " + isShared);
                Thread newThread = new Thread(new MyRunnable(), "Thread 1");
                newThread.start();
                ByteBuffer buffer = ByteBuffer.allocate(1024); // 버퍼 생성
                int bytesRead = channel.read(buffer); // 파일 읽기
                System.out.println("Bytes read: " + bytesRead);
                buffer.flip(); // 버퍼를 읽기 모드로 전환
                byte[] bytes = new byte[buffer.remaining()];
                System.out.println("File content: " + new String(bytes)); // 파일 내용 출력
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    static class MyRunnable implements Runnable{

        @Override
        public void run() {
            File file = new File("C:\\Users\\Hojun\\Desktop\\git\\JavaStudy\\IOAndNIO\\Gather-Scatter.txt");
            try (FileChannel channel = new RandomAccessFile(file, "r").getChannel()){

                ByteBuffer buffer = ByteBuffer.allocate(1024); // 버퍼 생성

                int bytesRead = channel.read(buffer); // 파일 읽기
                System.out.println("Bytes read2: " + bytesRead);

                buffer.flip(); // 버퍼를 읽기 모드로 전환

                byte[] bytes = new byte[buffer.remaining()];
                System.out.println("File content2: " + new String(bytes)); // 파일 내용 출력

                buffer.get(bytes); // 버퍼에서 데이터 읽기
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
