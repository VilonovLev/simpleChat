import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class ServerApp {
    private static int countThreads;
    private static int port;
    private static ServerSocket serverSocket;
    private static ExecutorService executeIt;
    private static LinkedList<Client> clients;
    private static Logger logger;

    static {
        clients = new LinkedList<>();
        countThreads = 5;
        port = 55555;
        executeIt = Executors.newFixedThreadPool(countThreads);
        logger = Logger.getAnonymousLogger();
    }

    private ServerApp(){}

    public static void main(String[] args) throws IOException {
        try {
            serverSocket = new ServerSocket(port); //Ip хоста
            logger.info("Server socket create.");
        } catch (IOException e) {
            logger.info(e.getMessage());
        }

        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept(); //Открытие сессии
            Client client = new Client(socket); //Создание клиента
            logger.info("new client connect ");
            hello(client);
            executeIt.execute(client); //Открытие потока и его выполнение (Выделение потока из ThreadPool)
            clients.add(client);
        }
        serverSocket.close();
        executeIt.shutdown();
    }

    public static void broadcast(String msg) throws IOException {
        for (Client c:clients) {
            c.send(msg + "\n");
        }
    }


    private static void hello(Client client){
        try {
            client.send("NICK\n");
            String nickname = client.getIn().readLine().strip();
            client.setNickname(nickname);
            ServerApp.broadcast(nickname + " join chat");
        }catch (IOException e){
            System.out.println(e);
        }
     }

}
