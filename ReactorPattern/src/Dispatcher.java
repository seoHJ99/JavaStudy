import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Dispatcher {


    public void callHandler(ServerSocket serverSocket, EventHandlerMap handlerMap) throws IOException {
        Socket socket = serverSocket.accept();
        String handlerName = findHandler(socket);
        handlerMap.get(handlerName).handleEvent(socket);
    }

    public String findHandler(Socket socket) throws IOException {
        System.out.println("Dispatcher.findHandler");
        InputStream inputStream = socket.getInputStream();

        byte[] buffer = new byte[1024];
        inputStream.read(buffer);
        String header = new String(buffer);
        header = header.substring(header.indexOf("Event"), header.indexOf("|"));
        header = header.substring(7);
        System.out.println(header);
        return header;
    }
}
