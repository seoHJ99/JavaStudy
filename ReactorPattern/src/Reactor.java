import events.EventHandler;
import events.EventOne;

import java.io.IOException;
import java.net.ServerSocket;

public class Reactor {

    private ServerSocket serverSocket;
    private EventHandlerMap handlerMap;

    public static void main(String[] args) throws IOException {
        Reactor reactor  = new Reactor(8080);
        reactor.registerHandler(new EventOne());
        reactor.runServer();
    }

    public Reactor(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.handlerMap = new EventHandlerMap();
    }

    public void runServer() throws IOException {
        Dispatcher dispatcher = new Dispatcher();
        while (true){
            dispatcher.callHandler(serverSocket, handlerMap);
        }
    }

    public void registerHandler(EventHandler eventHandler){
        handlerMap.put(eventHandler.getName(), eventHandler);
    }

    public void removeHandler(EventHandler eventHandler){
        handlerMap.remove(eventHandler.getName(), eventHandler);
    }
}
