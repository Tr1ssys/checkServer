import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));

        try (ServerSocket serverSocket = new ServerSocket(ServerConfig.PORT)) { // стартуем сервер один(!) раз
            while (true) { // в цикле(!) принимаем подключения
                try(
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        OutputStream outStream = socket.getOutputStream();
                        )  {

                    System.out.println("подключились");
                    //String word = in.readLine();
//                    List<PageEntry> entries = engine.search(word);
//                    String jsonEntries = new Gson().toJson(entries);
//                    outStream.write("HTTP/1.1 200 OK\r\n".getBytes());
//                    outStream.write("Main: OneServer 0.1\r\n".getBytes());
//                    outStream.write("Content-length: 5\r\n".getBytes()); // if text/plain the length is required
//                    outStream.write("Content-Type: text/plain\r\n".getBytes());
//                    outStream.write("hello".getBytes());
//                    outStream.flush();
                    //String jsonEntries = "hello";
                    //out.println(jsonEntries);
                } catch (Exception ex){

                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }


        // здесь создайте сервер, который отвечал бы на нужные запросы
        // слушать он должен порт 8989
        // отвечать на запросы /{word} -> возвращённое значение метода search(word) в JSON-формате
    }
}