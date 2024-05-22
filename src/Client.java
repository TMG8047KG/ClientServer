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
    public DataInputStream in;
    public DataOutputStream output;


    public Client(){
        Console.setBorder(BorderFactory.createEmptyBorder());
        input.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBorder(BorderFactory.createEmptyBorder());
        ConsolePanel.add(scroll);

        println("Starting client!");

        //Figure this out(make it able to reconnect)
        connect();

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

    private void connect(){
        new Thread(() -> {
            try {
                socket = new Socket(address, port);
                output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF("Connected!");
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                println("Connected to the server!");
                read();
            }catch (Exception e){
                println(e.getMessage());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    println(ex.getMessage());
                }
                connect();
            }
        }).start();
    }

    public void read() throws IOException {
        String message = "";
        while (!message.equals("dc")){
            message = in.readUTF();
            println("Server: " + message);
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
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}