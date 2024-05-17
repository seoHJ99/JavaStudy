package IO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NonBlockingClient {
    public static void main(String[] args) throws IOException {

        sendMessage("This is client1 test", 9090);// 클라이언트 메시지 발송
        sendMessage("222: This is client2 test", 9090);// 클라이언트 메시지 발송2
    }

    public static void sendMessage(String message, int port) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost", port));

        while (!socketChannel.finishConnect()) { // 연결 완료될 때까지 기다립니다.
            System.out.println("연결중");
        }

        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());

        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
        socketChannel.close();
    }
}
