import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3800);
        while (true){
            new Handle(serverSocket.accept());
        }
    }
}
