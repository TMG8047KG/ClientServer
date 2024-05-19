import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    //UI
    private JTextArea Console;
    private JPanel mainPanel;
    private JTextField input;
    private JPanel ConsolePanel;
    private JScrollPane scroll = new JScrollPane(Console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    //Client
    Socket socket;
    static String address;
    static int port;
    DataInputStream in;
    DataOutputStream output;


    public Client(){
        Console.setBorder(BorderFactory.createEmptyBorder());
        input.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBorder(BorderFactory.createEmptyBorder());
        ConsolePanel.add(scroll);

        println("Starting client!");
        try {
            socket = new Socket(address, port);
            output = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            println("Connected to the server!");
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

    public void read() {
        String message = "";
        try {
            while (!message.equals("dc")){
                message = in.readUTF();
                println("Server: " + message);
            }
        } catch (IOException e) {
            println(e.getMessage());
        }
    }

    private void write() throws IOException {
        String message = input.getText();
        output.writeUTF(message);
        println(message);
        input.setText("");
    }

    private void close() throws IOException {
        socket.close();
        output.close();
        in.close();
    }

    public void println(String text){
        Console.append(text + "\n");
    }

    public void print(String text){
        Console.append(text);
    }

    public static void main(String[] args) {
        PreConfig config = new PreConfig(1);
        address = config.getAddress();
        port = config.getPort();
        Client client = new Client();
        JFrame frame = new JFrame("Client");
        frame.setContentPane(client.mainPanel);
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        client.read();
    }
}