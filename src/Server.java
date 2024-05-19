import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    //UI
    private JTextArea Console;
    private JPanel mainPanel;
    private JTextField input;
    private JPanel ConsolePanel;
    private JScrollPane scroll = new JScrollPane(Console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    //Server
    ServerSocket server;
    static int port;
    int index = 0;
    List<ConnectedClient> clientList = new ArrayList<>();

    public Server(){
        Console.setBorder(BorderFactory.createEmptyBorder());
        input.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBorder(BorderFactory.createEmptyBorder());
        ConsolePanel.add(scroll);


        println("Starting server!");
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            println(e.getMessage());
        }
        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        try {
                            write();
                        } catch (IOException ex) {
                            println(ex.getMessage());
                        }
                    }
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
                clientList.add(client);
                client.read();
            }).start();
    }

    private void write() throws IOException {
        String message = input.getText();
        for(ConnectedClient client : clientList){
            client.getOutput().writeUTF(message);
            println(message);
        }
        input.setText("");
    }

    public void println(String text){
        Console.append(text + "\n");
    }

    public void print(String text){
        Console.append(text);
    }

    public static void main(String[] args) {
        PreConfig config = new PreConfig(0);
        port = config.getPort();
        Server server = new Server();
        JFrame frame = new JFrame("Server");
        frame.setContentPane(server.mainPanel);
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        try {
            server.startListening();
        } catch (IOException er) {
            server.println(er.getMessage());
        }
    }
}
