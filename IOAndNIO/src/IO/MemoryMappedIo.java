package IO;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MemoryMappedIo {
    private static final int BUFFER_SIZE = 128;

    // channel.map 방식을 이용한 mmio 버퍼
    // allocateDirect() 도 mmio 이기는 하나, map 방식이 데이터 전체 또는 일부를 메모리에 저장해두는 것과 달리 하드에서 가져온 데이터를
    // 자바 heap 이 아닌 네이티브 메모리에 저장하는게 전부이기에 버퍼를 갱신할때 다시 i/o 가 일어나게 된다.
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("C:\\Users\\Hojun\\Desktop\\git\\JavaStudy\\IOAndNIO\\Gather-Scatter.txt");

        File file = path.toFile();
        long size = Files.size(path);

        int bufferCount = (int) (size / BUFFER_SIZE +1);
        size = bufferCount * BUFFER_SIZE;

        try (RandomAccessFile fileData = new RandomAccessFile(file, "rw")) {
            fileData.setLength(size);
            ByteBuffer buffer = fileData.getChannel()
                    .map(FileChannel.MapMode.READ_WRITE, 0L, size);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
