import java.awt.Color;
import java.awt.Font;

import javax.swing.*;

public class IntroCliente extends JFrame {
    private JLabel in = new JLabel("Ingrese su nombre:");
    private JTextField name = new JTextField();

    private JLabel in1 = new JLabel("Ingrese la ip:");
    private JTextField ip = new JTextField("127.0.0.1");

    private JButton ok = new JButton("Listo");

    public IntroCliente() {
        this.setSize(400, 330);
        this.setTitle("Cliente FC");
        this.setLayout(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        in.setBounds(10,20,400-35,40);
        in.setFont(new Font("Consolas", Font.ITALIC, 20));
        in.setOpaque(true);
        in.setForeground(Color.GRAY);
        in.setHorizontalAlignment(SwingConstants.CENTER);
        add(in);

        name.setBounds(10,70,400-35,40);
        name.setFont(new Font("Consolas", Font.ITALIC, 20));
        name.setForeground(Color.BLUE);
        name.setHorizontalAlignment(SwingConstants.CENTER);
        add(name);

        in1.setBounds(10,120,400-35,40);
        in1.setFont(new Font("Consolas", Font.ITALIC, 20));
        in1.setOpaque(true);
        in1.setForeground(Color.GRAY);
        in1.setHorizontalAlignment(SwingConstants.CENTER);
        add(in1);

        ip.setBounds(10,170,400-35,40);
        ip.setFont(new Font("Consolas", Font.ITALIC, 20));
        ip.setForeground(Color.BLUE);
        ip.setHorizontalAlignment(SwingConstants.CENTER);
        add(ip);


        ok.setBounds(10+100,230,400-35-200,40);
        ok.setFont(new Font("Consolas", Font.ITALIC, 20));
        ok.setForeground(Color.BLACK);
        ok.setHorizontalAlignment(SwingConstants.CENTER);
        add(ok);

        ok.addActionListener(e->{
            Cliente ventana = new Cliente(name.getText(),ip.getText());
            ventana.setVisible(true);
            ventana.setLocationRelativeTo(null);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        IntroCliente ventana = new IntroCliente();
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
    }
}
