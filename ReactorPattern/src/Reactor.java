import events.EventHandler;
import events.EventOne;

import java.io.IOException;
import java.net.ServerSocket;

public class Reactor {

    // 리액터 패턴
    // 이벤트를 처리하는 함수를 여러개로 나눠, 동시에 여러 이벤트 요청이 들어와도 비동기적으로 작동함
    // 단, 이를 위해선 non-blocking i/o 를 사용해야 함.

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
