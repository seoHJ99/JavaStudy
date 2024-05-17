package IO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NonBlockingTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        serverStart();

    }

    public static void serverStart() throws IOException {

        ServerSocketChannel server = ServerSocketChannel.open(); // 소켓 서버 생성
        server.configureBlocking(false); // 논블로킹 설정
        server.bind(new InetSocketAddress(9090)); // 9090 포트

        Selector selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT); // 서버는 항상 accept 상태로 연결을 대기하고 있어야 함

        sendMessage("This is client1 test", 9090);// 클라이언트 메시지 발송

        while (true) { //연결이 들어올 때까지 대기

            selector.select();// 준비된 채널이 있을 때까지 블로킹(스레드 정지)

            Set<SelectionKey> selectedKeys = selector.selectedKeys(); // selectedKeys 연결된 요청의 상태를 반환함
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove(); // 해당 요청이 처리되면 지우기

                if (key.isAcceptable()) { // 연결 수락이 가능한 경우.
                    // 서버와 연결되기 클라이언트 채널의 상태는 직접 SelectionKey값을 변경해줘도 무조건 연결 요청으로 인식됨
                    System.out.println("연결중!");

                    // ServerSocketChannel : 소켓 채널 연결을 위해 사용되는 클래스
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                    SocketChannel clientChannel = serverChannel.accept(); // 소켓 채널 연결

                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ); // 클라이언트 채널을 읽을수 있는 상태로 변경

                    System.out.println("연결 완료: " + clientChannel.getRemoteAddress());

                } else if (key.isConnectable()) { // 연결을 하고자 하는 경우
                    System.out.println("새로운 연결 가능!");

                } else if (key.isReadable()) { // 연결된 상대를 읽을수 있을때
                    System.out.println("상대 데이터를 읽을수 있음");

                    // 클라이언트로부터 데이터 읽기
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int bytesRead = clientChannel.read(buffer);

                    if (bytesRead == -1) {
                        System.out.println("데이터 없음");
                        clientChannel.close();
                    } else {
                        buffer.flip();
                        System.out.println("받은 메시지: " + new String(buffer.array(), 0, buffer.limit()));
                        buffer.clear();
                    }

                } else if (key.isWritable()) { // 연결된 상대에게 쓸수 있을때
                    System.out.println("상대에게 데이터를 보낼수 있음");
                }
            }
        }
    }

    public static SocketChannel sendMessage(String message, int port) throws IOException {
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
        return socketChannel;
    }

}
