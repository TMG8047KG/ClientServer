import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectedClient {
    Server server;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    int id;

    public ConnectedClient(Server server, Socket clientSocket, int id){
        this.server = server;
        this.socket = clientSocket;
        this.id = id;
        try {
            server.println("Client " + id + ": Client Connected");
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
        } catch (IOException e) {
            server.println(e.getMessage());
        }
    }

    public DataOutputStream getOutput(){
        return out;
    }

    public void read() {
        String message = "";
        try {
            while (!message.equals("dc")){
                message = in.readUTF();
                server.println("Client " + id + ": " + message);
            }
            close();
        } catch (IOException e) {
            server.println(e.getMessage());
        }
    }

    public void close() {
        try {
            socket.close();
            in.close();
            server.println("Client " + id + " disconnected!");
        } catch (IOException e) {
            server.println(e.getMessage());
        }
    }
}
