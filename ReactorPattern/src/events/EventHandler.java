package events;

import java.io.IOException;
import java.net.Socket;

public interface EventHandler {

    String getName();
    void handleEvent(Socket socket) throws IOException;
}
