package IO;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class Gather {

    public static void main(String[] args) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(
                "C:\\Users\\Hojun\\Desktop\\git\\JavaStudy\\IOAndNIO\\Gather-Scatter.txt");

        FileChannel channel = fileOutputStream.getChannel();

        ByteBuffer a = ByteBuffer.allocate(128);
        ByteBuffer b = ByteBuffer.allocate(1024);
        ByteBuffer[] array = {a, b};

        a.put("Hello".getBytes(StandardCharsets.UTF_8));
        b.put("World".getBytes(StandardCharsets.UTF_8));

        a.flip();
        b.flip();

        channel.write(array);
        channel.close();


        // 이때 a와 b 버퍼 위치는 서로 다르지만, write 작업이 이뤄질때 한번에 접근함.
        // 즉, a 접근해서 데이터 가져오고, 다시 b 접근해서 데이터를 가져와서 두번의 오버헤드가 일어나지 않음
    }
}
