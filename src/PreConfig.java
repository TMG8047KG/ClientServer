import javax.swing.*;
import java.awt.event.*;

public class PreConfig extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField address;
    private JTextField port;
    private int state;

    public PreConfig(int state) {
        this.state = state;

        address.setBorder(BorderFactory.createEmptyBorder());
        port.setBorder(BorderFactory.createEmptyBorder());
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        if(state == 0){
            setSize(300, 120);
            address.setVisible(false);
        }
        if(state == 1){
            setSize(300, 150);
        }
        setVisible(true);
    }

    private void onOK() {
        if(address.getText().isEmpty() && state != 0){
            JOptionPane.showMessageDialog(null, "Fill out the empty fields!");
            return;
        }
        if(port.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Fill out the empty fields!");
        }
        dispose();
    }

    private void onCancel() {
        System.exit(0);
    }

    public String getAddress(){
        return address.getText();
    }

    public int getPort(){
        return Integer.parseInt(port.getText());
    }
}
