import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cliente extends JFrame {
    private static JTextField texto = new JTextField();
    private static JButton enviar = new JButton(">>");
    private static JButton cargar_imagen = new JButton("[]");
    private static int enviar_imagen = 0;
    private String ruta = "";
    private static int nro_imagen = 1;

    private static int max_msjes = 60;
    private static JLabel area[] = new JLabel[max_msjes];
    private JScrollPane scrollpane;
    private JPanel panel = new JPanel();
    private static JScrollBar scrollbar;

    private static int nro = 0;
    private static int pos_y[] = new int[max_msjes];
    private static int pos_g = 10;
    private static int maximo = 10;
    private static int lim = 0;

    private static String nombre = "";

    private Socket socket1, socket2;
    private static BufferedReader input1;
    private static DataInputStream input2;
    private PrintStream output1, output2;

    private ExecutorService pool = null;
    private static Cola cola_l = new Cola();
    private static ColaB cola_a = new ColaB();
    private static ColaS cola_s = new ColaS();
    private static FileOutputStream file;
    private static BufferedOutputStream buffered;
    private static String link;


    public Cliente(String nombre, String ip) {
        this.setSize(400, 547);
        this.setTitle(nombre);
        this.setLayout(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Cliente.nombre = nombre;
        pool = Executors.newFixedThreadPool(3);

        for(int i=0; i<max_msjes; i++) {
            area[i] = new JLabel();
            area[i].setOpaque(true);
            if(i%2==0) {
                area[i].setBounds(10,10+55*i,250,50);
                area[i].setFont(new Font("Consolas", Font.ITALIC, 14));
                area[i].setBackground(Color.DARK_GRAY);
            }
            else {
                area[i].setBounds(380-17-270,10+55*i,250,50);
                area[i].setFont(new Font("Consolas", Font.ITALIC, 20));
                area[i].setBackground(Color.WHITE);
            }
        }

        texto.setBounds(10,460,250,40);
        texto.setFont(new Font("Consolas", Font.ITALIC, 20));
        add(texto);
        enviar.setBounds(260,460,59,40);
        add(enviar);
        cargar_imagen.setBounds(319,460,59,40);
        add(cargar_imagen);


        panel.setLayout(null);
        panel.setBackground(Color.GRAY);
        for(int i=0; i<max_msjes; i++) {
            panel.add(area[i]);
            area[i].setVisible(false);
        }


        scrollbar = new JScrollBar();
        scrollbar.setMinimum(0);
        scrollbar.setMaximum(maximo);
        scrollbar.setBounds(380-17,10,17,445);
        scrollbar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                for(int i=0; i<max_msjes; i++) {
                    area[i].setBounds(area[i].getX(),pos_y[i]-scrollbar.getValue(),
                            area[i].getWidth(),area[i].getHeight());
                }
            }
        });
        add(scrollbar);


        scrollpane = new JScrollPane(panel);
        scrollpane.setBounds(10,10,380-17,445);
        add(scrollpane);

        enviar.addActionListener(e->{
            area[nro].setText("Yo:");
            area[nro].setBounds(380-17-270,pos_g,250,20);
            pos_y[nro] = pos_g;
            pos_g = pos_g + 21;
            area[nro].setHorizontalAlignment(SwingConstants.CENTER);
            area[nro].setForeground(Color.CYAN);
            area[nro].setVisible(true);
            nro++;

            area[nro].setText(texto.getText());
            area[nro].setBounds(380-17-270,pos_g,250,35);
            pos_y[nro] = pos_g;
            pos_g = pos_g + 40;
            area[nro].setHorizontalAlignment(SwingConstants.CENTER);
            area[nro].setForeground(Color.BLUE);
            area[nro].setVisible(true);
            nro++;

            if (pos_g > 445) {
                if (lim == 0) {
                    maximo = maximo + pos_g - 440;
                    lim = 1;
                }
                else {
                    maximo = maximo + 61;
                }
                scrollbar.setMaximum(maximo);
                scrollbar.setValue(maximo);
            }

            output1.println(nombre+"&"+texto.getText());
            texto.setText("");
        });

        cargar_imagen.addActionListener(e->{
            if (enviar_imagen == 0) {
                //"C:\\Users\\CPU\\Desktop"
                JFileChooser fileChooser = new JFileChooser();
                FileFilter filtro = new FileNameExtensionFilter("Imagenes", "jpg", "png");
                fileChooser.setFileFilter(filtro);
                int result = fileChooser.showOpenDialog(fileChooser);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        ruta = fileChooser.getSelectedFile().getAbsolutePath();
                        Image image = new ImageIcon(ruta).getImage();
                        ImageIcon imageicon = new ImageIcon(image.getScaledInstance(250,200,Image.SCALE_SMOOTH));

                        area[nro].setText("Yo:");
                        area[nro].setBounds(380-17-270,pos_g,250,20);
                        pos_y[nro] = pos_g;
                        pos_g = pos_g + 21;
                        area[nro].setHorizontalAlignment(SwingConstants.CENTER);
                        area[nro].setForeground(Color.CYAN);
                        area[nro].setVisible(true);
                        nro++;

                        area[nro].setIcon(imageicon);
                        area[nro].setBounds(380-17-270,pos_g,250,200);
                        pos_y[nro] = pos_g;
                        pos_g = pos_g + 205;
                        area[nro].setVisible(true);
                        nro++;

                        if (pos_g > 445) {
                            if (lim == 0) {
                                maximo = maximo + pos_g - 440;
                                lim = 1;
                            }
                            else {
                                maximo = maximo + 226;
                            }
                            scrollbar.setMaximum(maximo);
                            scrollbar.setValue(maximo);
                        }
                    } catch (Exception ex) {System.out.println("Erro al cargar archivo");}

                    enviar_imagen = 1;
                    cargar_imagen.setText(">>");
                    texto.setEnabled(false);
                    enviar.setEnabled(false);
                    cargar_imagen.setBackground(Color.GREEN);
                }
            }

            else if(enviar_imagen == 1) {
                enviar_imagen = 2;
                cargar_imagen.setEnabled(false);
                cargar_imagen.setBackground(null);
                try
                {
                    output1.println(nombre+"&Ini-Imagen");

                    byte [] array_ini = new byte[5];
                    array_ini[0] = 'I';
                    array_ini[1] = 'N';
                    array_ini[2] = 'I';
                    array_ini[3] = 'C';
                    array_ini[4] = 'I';
                    output2.write(array_ini,0,5);

                    FileInputStream file = new FileInputStream(ruta);
                    BufferedInputStream buffered = new BufferedInputStream(file);

                    byte [] array = new byte[1024];
                    int length = buffered.read(array);
                    while (length > 0){
                        output2.write(array,0,length);
                        length = buffered.read(array);
                    }
                    buffered.close();
                    file.close();

                    byte [] array_fin = new byte[5];
                    array_fin[0] = 'F';
                    array_fin[1] = 'I';
                    array_fin[2] = 'N';
                    array_fin[3] = 'A';
                    array_fin[4] = 'L';
                    output2.write(array_fin,0,5);
                }
                catch (Exception ex) {System.out.println("Error");}
            }
        });


        //===================== CONEXION =====================//
        try {
            socket1 = new Socket(ip, 5000);
            socket2 = new Socket(ip, 5001);
            input1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            input2 = new DataInputStream(socket2.getInputStream());
            output1 = new PrintStream(socket1.getOutputStream());
            output2 = new PrintStream(socket2.getOutputStream());

            output1.println(nombre+"&Ini-Nombre");
        } catch (Exception ex) {}


        ClienteThread1 runnable1 = new ClienteThread1();
        pool.execute(runnable1);
        ClienteThread2 runnable2 = new ClienteThread2();
        pool.execute(runnable2);
        ClienteThread3 runnable3 = new ClienteThread3();
        pool.execute(runnable3);
    }

    private static class ClienteThread1 implements Runnable {
        ClienteThread1(){}
        public void run() {
            while (true) {
                try {
                    String m_entrada = input1.readLine();
                    String t1 = m_entrada.split("&")[0];
                    String t2 = m_entrada.split("&")[1];

                    if(t2.equals("Ini-Imagen")) {
                        cola_s.encolar(t1);
                    }
                    else if(t2.equals("Fin-Imagen")) {
                        cargar_imagen.setText("[]");
                        texto.setEnabled(true);
                        enviar.setEnabled(true);
                        cargar_imagen.setEnabled(true);
                        enviar_imagen = 0;
                    }
                    else {
                        area[nro].setText(t1+":");
                        area[nro].setBounds(10,pos_g,250,20);
                        pos_y[nro] = pos_g;
                        pos_g = pos_g + 21;
                        area[nro].setHorizontalAlignment(SwingConstants.CENTER);
                        area[nro].setForeground(Color.ORANGE);
                        area[nro].setVisible(true);
                        nro++;

                        area[nro].setText(t2);
                        area[nro].setBounds(10,pos_g,250,35);
                        pos_y[nro] = pos_g;
                        pos_g = pos_g + 40;
                        area[nro].setHorizontalAlignment(SwingConstants.CENTER);
                        area[nro].setForeground(Color.MAGENTA);
                        area[nro].setVisible(true);
                        nro++;

                        if (pos_g > 445) {
                            if (lim == 0) {
                                maximo = maximo + pos_g - 440;
                                lim = 1;
                            }
                            else {
                                maximo = maximo + 61;
                            }
                            scrollbar.setMaximum(maximo);
                            scrollbar.setValue(maximo);
                        }
                    }
                } catch (Exception ex) {}
            }
        }
    }

    private static class ClienteThread2 implements Runnable {
        ClienteThread2(){}
        public void run() {
            int total = 0;
            while (true) {
                try {
                    byte[] array = new byte[1024];
                    int length = input2.read(array);
                    cola_l.encolar(length);
                    cola_a.encolar(array);

                    total = total + length;
                    if(length != 1024) {
                        //System.out.println("Tama�osEntrada: "+cola_l.longitud()+"---"+cola_a.longitud());
                        System.out.println("R -> "+length+"  Total -> "+total);
                    }

                    if(cola_l.longitud()!=cola_a.longitud())
                        System.out.println("Tama�osEntrada: "+cola_l.longitud()+"---"+cola_a.longitud());

                } catch (Exception ex) {}
            }
        }
    }

    private static class ClienteThread3 implements Runnable {
        ClienteThread3(){}
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1);
                    if(cola_l.longitud() > 0) {
                        //System.out.println("Tama�osSalida: "+cola_l.longitud()+"---"+cola_a.longitud());
                        if(cola_l.primer() == 1024) {
                            buffered.write(cola_a.primer(),0,cola_l.primer());

                            cola_l.desencolar();
                            cola_a.desencolar();
                        }

                        else {
                            if(cola_a.primer()[0]=='I' &&
                                    cola_a.primer()[1]=='N' &&
                                    cola_a.primer()[2]=='I' &&
                                    cola_a.primer()[3]=='C' &&
                                    cola_a.primer()[4]=='I') {
                                link = "imagen_"+nombre+nro_imagen+".jpg";
                                nro_imagen++;
                                file = new FileOutputStream (link);
                                buffered = new BufferedOutputStream(file);

                                byte temp[] = new byte[cola_l.primer()-5];
                                for(int k=0; k<cola_l.primer()-5; k++) {
                                    temp[k] = cola_a.primer()[k+5];
                                }
                                if(cola_l.primer()>5)
                                    buffered.write(temp,0,cola_l.primer()-5);

                                System.out.println("INI-GUARDADO: "+cola_l.primer());
                                String ttt = new String(cola_a.primer());
                                System.out.println("ini: "+ttt);

                                cola_l.desencolar();
                                cola_a.desencolar();
                            }

                            else if(cola_a.primer()[cola_l.primer()-5]=='F' &&
                                    cola_a.primer()[cola_l.primer()-4]=='I' &&
                                    cola_a.primer()[cola_l.primer()-3]=='N' &&
                                    cola_a.primer()[cola_l.primer()-2]=='A' &&
                                    cola_a.primer()[cola_l.primer()-1]=='L') {

                                if(cola_l.primer()>5)
                                    buffered.write(cola_a.primer(),0,cola_l.primer()-5);

                                System.out.println("FIN-GUARDADO: "+cola_l.primer());
                                String ttt = new String(cola_a.primer());
                                System.out.println("fin: "+ttt);

                                cola_a.desencolar();
                                cola_l.desencolar();
                                buffered.close();
                                file.close();

                                try {
                                    Image image = new ImageIcon(link).getImage();
                                    ImageIcon imageicon = new ImageIcon(image.getScaledInstance(250,200,Image.SCALE_SMOOTH));

                                    try {
                                        area[nro].setText(cola_s.primer()+":");
                                        cola_s.desencolar();
                                    }catch(Exception ex) {area[nro].setText("Unknow:");}

                                    area[nro].setBounds(10,pos_g,250,20);
                                    pos_y[nro] = pos_g;
                                    pos_g = pos_g + 21;
                                    area[nro].setHorizontalAlignment(SwingConstants.CENTER);
                                    area[nro].setForeground(Color.ORANGE);
                                    area[nro].setVisible(true);
                                    nro++;

                                    area[nro].setIcon(imageicon);
                                    area[nro].setBounds(10,pos_g,250,200);
                                    pos_y[nro] = pos_g;
                                    pos_g = pos_g + 205;
                                    area[nro].setVisible(true);
                                    nro++;

                                    if (pos_g > 445) {
                                        if (lim == 0) {
                                            maximo = maximo + pos_g - 440;
                                            lim = 1;
                                        }
                                        else {
                                            maximo = maximo + 226;
                                        }
                                        scrollbar.setMaximum(maximo);
                                        scrollbar.setValue(maximo);
                                    }
                                } catch (Exception ex) {System.out.println("Error al cargar archivo");}
                            }
                            else {
                                buffered.write(cola_a.primer(),0,cola_l.primer());
                                cola_l.desencolar();
                                cola_a.desencolar();
                            }
                        }
                    }
                } catch (Exception ex) {}
            }
        }
    }
	/*
	public static void main(String[] args) {
		Cliente ventana = new Cliente("Cristhian");
		ventana.setVisible(true);
		ventana.setLocationRelativeTo(null);
	}
	*/
}
