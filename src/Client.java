import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class Client implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String nickname = "Anonymous";

    public Client(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (IOException e) {
            System.out.println(e);
        }
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void send(String str) throws IOException {
        out.write(str);
        out.flush();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return socket.equals(client.socket) && nickname.equals(client.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, nickname);
    }

    @Override
    public String toString() {
        return "ServerUtils.Client{" +
                "socket=" + socket +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    //Старт потока
    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                    ServerApp.broadcast(nickname + ": " + in.readLine());
            }
            ServerApp.broadcast(nickname + " leave chat");
        } catch (IOException e) {
            System.out.println(e);
        }

    }
}
