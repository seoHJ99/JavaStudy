import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Dispatcher {

    public void callHandler(ServerSocket serverSocket, EventHandlerMap handlerMap) throws IOException {
        Socket socket = serverSocket.accept();
        String handlerName = findHandler(socket);
        handlerMap.get(handlerName).handleEvent(socket);
    }

    public String findHandler(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();

        byte[] buffer = new byte[30];
        inputStream.read(buffer);
        String header = new String(buffer);
        return header;
    }
}
