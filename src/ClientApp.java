import java.io.*;
import java.net.Socket;

public class ClientApp  {
    private static String ipServer = "192.168.0.188";
    private static int port = 55555;
    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedReader in;
    private BufferedWriter out;
    private String nickname;

    // Конструктор клиентского приложения
    public ClientApp(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.reader = new BufferedReader(new InputStreamReader(System.in));;
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()));
    }

    public static void main(String[] args) {
        try {
            Socket clientSocket= new Socket(ipServer, port);
            ClientApp clientApp = new ClientApp(clientSocket);
            System.out.println("Socket open. Server connection.");
            clientApp.new InThread().start();
//            clientApp.new OutTread().start();
            while (true) {
                try {
                    clientApp.out.write(clientApp.reader.readLine() + "\n");
                    clientApp.out.flush();
                } catch (IOException e) {
                }
            }
        } catch(IOException e){
            System.out.println(e);
        }
    }

    private void setNickname() throws IOException {
        System.out.println("Please entry your nickname: ");
        this.nickname = reader.readLine(); // Получение ника
        out.write(nickname + "\n"); // Отправка ника на сервер
        out.flush();
    }

    //Поток для чтение с сервера
    private class InThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    String msg = in.readLine().strip();
                    if (msg.equals("NICK")) {
                        setNickname();
                        continue;
                    }
                    System.out.println(msg); // Получение строки и печать
                }
            } catch (IOException e) {
            }
        }
    }
}
