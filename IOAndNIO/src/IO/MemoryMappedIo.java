package IO;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MemoryMappedIo {
    private static final int BUFFER_SIZE = 128;

    // channel.map 방식을 이용한 mmio 버퍼
    // allocateDirect() 도 mmio 이기는 하나, map 방식이 데이터 전체 또는 일부를 메모리에 저장해두는 것과 달리 하드에서 가져온 데이터를
    // 자바 heap 이 아닌 네이티브 메모리에 저장하는게 전부이기에 버퍼를 갱신할때 다시 i/o 가 일어나게 된다.
    public static void main(String[] args) {
        long time = measureRunTime(MemoryMappedIo::mmioTest);
        System.out.println("mmio 를 통한 파일 읽기 속도: " + time + " millisecond");

        long time2 = measureRunTime(MemoryMappedIo::mmioTansferTest);
        System.out.println("mmio 를 통한 파일 전송 속도: " + time2 + " millisecond");

    }

    public static void mmioTest() {
        Path path = Paths.get("C:\\Users\\Hojun\\Desktop\\git\\JavaStudy\\IOAndNIO\\CopyDummy.txt");
        File file1 = path.toFile();

        long size;
        try {
            size = Files.size(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        int bufferCount = (int) (size / BUFFER_SIZE + 1);
        size = bufferCount * BUFFER_SIZE;

        try (RandomAccessFile fileData = new RandomAccessFile(file1, "rw")) {
            fileData.setLength(size);
            ByteBuffer buffer = fileData.getChannel()
                    .map(FileChannel.MapMode.READ_WRITE, 0L, size);
            buffer.flip();
            while (buffer.hasRemaining()) {
                byte b = buffer.get();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mmioTansferTest() {
        Path path = Paths.get("C:\\Users\\Hojun\\Desktop\\git\\JavaStudy\\IOAndNIO\\CopyDummy.txt");
        Path path2 = Paths.get("C:\\Users\\Hojun\\Desktop\\git\\JavaStudy\\IOAndNIO\\testDummy.txt");

        File file1 = path.toFile();
        File file2 = path2.toFile();

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file2);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        FileChannel fileOutputChannel = fileOutputStream.getChannel();

        long size;
        try {
            size = Files.size(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        int bufferCount = (int) (size / BUFFER_SIZE + 1);
        size = bufferCount * BUFFER_SIZE;

        try (RandomAccessFile fileData = new RandomAccessFile(file1, "rw")) {
            fileData.setLength(size);
            FileChannel channel = fileData.getChannel();
            ByteBuffer originalData = fileData.getChannel()
                    .map(FileChannel.MapMode.READ_WRITE, 0L, size);

            Charset charset = Charset.defaultCharset();
            ByteBuffer newData = charset.encode("new Data");

            ByteBuffer combine = ByteBuffer.allocate((int) size+100);
            combine.put(newData);
            combine.put(originalData);
            combine.position(0);
            channel.write(combine);

            channel.transferTo(0, size, fileOutputChannel);
            channel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static long measureRunTime(Runnable task) {
        long startTime = System.currentTimeMillis();
        task.run();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
