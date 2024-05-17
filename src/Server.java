import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    //UI
    private JTextArea Console;
    private JPanel panel1;
    private JTextField input;
    private JScrollPane scroll = new JScrollPane(Console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    //Server
    ServerSocket server;
    boolean launched = false;
    int index = 0;

    public Server(){
        println("Starting server!");
        try {
            server = new ServerSocket(12181);
        } catch (IOException e) {
            println(e.getMessage());
        }
        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
//                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
//                        try {
//                            write();
//                        } catch (IOException ex) {
//                            println(ex.getMessage());
//                        }
//                    }
            }
        });
    }

    public void startListening() throws IOException {
        println("Started listening...");
        while (true) initConnection();
    }

    private void initConnection() throws IOException {
        Socket socket = server.accept();

        if(socket.isConnected())
            new Thread(()->{
                index++;
                ConnectedClient client = new ConnectedClient(this, socket, index);
                client.readMessages();
            }).start();
    }
    private void setState(boolean b) {
        launched = b;
    }

    public void println(String text){
        Console.append(text + "\n");
    }

    public void print(String text){
        Console.append(text);
    }

    public static void main(String[] args) {
        Server server = new Server();
        JFrame frame = new JFrame("Server");
        server.panel1.add(server.scroll);
        frame.setContentPane(server.panel1);
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        try {
            server.startListening();
        } catch (IOException e) {
            server.println(e.getMessage());
        }
    }
}
