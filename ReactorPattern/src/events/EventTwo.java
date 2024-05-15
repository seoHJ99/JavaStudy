package events;


import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class EventTwo implements EventHandler {

    private final String NAME= "Event2";
    private final int DATA_SIZE = 1024;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void handleEvent(Socket socket) throws IOException {
        System.out.println("This is " + NAME +"handler");

        InputStream inputStream = socket.getInputStream();

        byte[] buffer = new byte[DATA_SIZE];

        inputStream.read(buffer);
        String data = new String(buffer, StandardCharsets.UTF_8);

        StringTokenizer token = new StringTokenizer(data, "|");

        while (token.hasMoreTokens()){
            System.out.println("data2 : " + token.nextToken());
        }
    }
}
