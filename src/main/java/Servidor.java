import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    private ServerSocket serversocket1 = null, serversocket2 = null;
    private Thread hilo1, hilo2;

    private static int id_cliente1 = 0, id_cliente2 = 0;
    private static int max_clientes = 10;

    private Socket socket1[] = new Socket[max_clientes];
    private Socket socket2[] = new Socket[max_clientes];
    private static PrintStream output1[] = new PrintStream[max_clientes];
    private static PrintStream output2[] = new PrintStream[max_clientes];

    private BufferedReader input1[] = new BufferedReader[max_clientes];
    private DataInputStream input2[] = new DataInputStream[max_clientes];

    private ExecutorService pool1 = null, pool2 = null;

    private static Cola COLA = new Cola();
    private static Cola cola_l[] = new Cola[max_clientes];
    private static ColaB cola_a[] = new ColaB[max_clientes];
    private static String nombres[] = new String[max_clientes];
    //private static FileOutputStream file;
    //private static BufferedOutputStream buffered;

    public Servidor() {
        pool1 = Executors.newFixedThreadPool(max_clientes);
        pool2 = Executors.newFixedThreadPool(max_clientes+1);

        for (int i=0; i<max_clientes; i++) {
            cola_l[i] = new Cola();
            cola_a[i] = new ColaB();
        }

        System.out.println("Iniciando servidor...");
        hilo1 = new Thread(new Runnable() {
            public void run() {
                try {
                    serversocket1 = new ServerSocket(5000);
                    while (true) {
                        socket1[id_cliente1] = serversocket1.accept();
                        input1[id_cliente1] = new BufferedReader(new InputStreamReader(socket1[id_cliente1].getInputStream()));
                        output1[id_cliente1] = new PrintStream(socket1[id_cliente1].getOutputStream());
                        ServidorThread1 runnable = new ServidorThread1(id_cliente1,input1);
                        pool1.execute(runnable);
                        id_cliente1++;
                        System.out.println("Cliente "+id_cliente1+" conectado");
                    }
                } catch (Exception ex) {}
            }
        });
        hilo1.start();

        hilo2 = new Thread(new Runnable() {
            public void run() {
                try {
                    serversocket2 = new ServerSocket(5001);
                    while (true) {
                        socket2[id_cliente2] = serversocket2.accept();
                        input2[id_cliente2] = new DataInputStream(socket2[id_cliente2].getInputStream());
                        output2[id_cliente2] = new PrintStream(socket2[id_cliente2].getOutputStream());
                        ServidorThread2 runnable = new ServidorThread2(id_cliente2,input2);
                        pool2.execute(runnable);
                        id_cliente2++;
                        System.out.println("Cliente "+id_cliente2+" conectado");
                    }
                } catch (Exception ex) {}
            }
        });
        hilo2.start();

        try {
            ServidorThread3 runnable = new ServidorThread3();
            pool2.execute(runnable);
        } catch (Exception e) {}
    }


    private static class ServidorThread1 implements Runnable {
        private int id;
        private BufferedReader input[];

        ServidorThread1(int id, BufferedReader input[]){
            this.id = id;
            this.input = input;
        }

        public void run() {
            while (true) {
                try {
                    String m_entrada = input[id].readLine();
                    System.out.println("Cliente "+(id+1)+": "+m_entrada);

                    String t1 = m_entrada.split("&")[0];
                    String t2 = m_entrada.split("&")[1];
                    if(t2.equals("Ini-Imagen")) {
                        COLA.encolar(id);
                    }
                    else if(t2.equals("Ini-Nombre")) {
                        nombres[id] = t1;
                    }
                    else {
                        for(int x=0; x<id_cliente1; x++) {
                            if(id!=x)
                                output1[x].println(m_entrada);
                        }
                    }
                } catch (Exception ex) {}
            }
        }
    }

    private static class ServidorThread2 implements Runnable {
        private int id;
        private DataInputStream input[];

        ServidorThread2(int id, DataInputStream input[]){
            this.id = id;
            this.input = input;
        }

        public void run() {
            int total = 0;
            while (true) {
                try {
                    byte[] array = new byte[1024];
                    int length = input[id].read(array);
                    cola_l[id].encolar(length);
                    cola_a[id].encolar(array);

                    total = total + length;
                    if(length != 1024)
                        if(length != 1024) {
                            System.out.println("Tama�osEntrada: "+cola_l[id].longitud()+"---"+cola_a[id].longitud());
                            System.out.println("ID["+id+"]R -> "+length+"  Total -> "+total);
                        }

                } catch (Exception ex) {}
            }
        }
    }

    private static class ServidorThread3 implements Runnable {
        ServidorThread3(){}

        public void run() {
            while (true) {
                try {
                    Thread.sleep(1);
                    if(COLA.longitud() > 0) {
                        System.out.println("->"+COLA.primer()+"<-");
                        int i = COLA.primer();
                        //int total = 0;

                        boolean stop = true;
                        do {
                            if(cola_l[i].longitud() > 0) {

                                if(cola_l[i].primer() == 1024) {
                                    for(int x=0; x<id_cliente1; x++) {
                                        if(i!=x)
                                            output2[x].write(cola_a[i].primer(),0,cola_l[i].primer());
                                    }

                                    cola_l[i].desencolar();
                                    cola_a[i].desencolar();
                                }

                                else {
                                    if(cola_a[i].primer()[0]=='I' &&
                                            cola_a[i].primer()[1]=='N' &&
                                            cola_a[i].primer()[2]=='I' &&
                                            cola_a[i].primer()[3]=='C' &&
                                            cola_a[i].primer()[4]=='I') {

                                        System.out.println("SaleI -> "+cola_l[i].primer());
                                        for(int x=0; x<id_cliente1; x++) {
                                            if(i!=x) {
                                                output1[x].println(nombres[i]+"&Ini-Imagen");
                                            }
                                        }
                                    }

                                    else if(cola_a[i].primer()[cola_l[i].primer()-5]=='F' &&
                                            cola_a[i].primer()[cola_l[i].primer()-4]=='I' &&
                                            cola_a[i].primer()[cola_l[i].primer()-3]=='N' &&
                                            cola_a[i].primer()[cola_l[i].primer()-2]=='A' &&
                                            cola_a[i].primer()[cola_l[i].primer()-1]=='L') {

                                        System.out.println("SaleF -> "+cola_l[i].primer());

                                        stop = false;
                                        output1[i].println("FIN&Fin-Imagen");
                                    }

                                    for(int x=0; x<id_cliente1; x++) {
                                        if(i!=x)
                                            output2[x].write(cola_a[i].primer(),0,cola_l[i].primer());
                                    }

                                    cola_l[i].desencolar();
                                    cola_a[i].desencolar();
                                }
                            }
                        }while(stop);

                        COLA.desencolar();
                    }
                } catch (Exception ex) {}
            }
        }
    }


    public static void main(String[] args) {
        new Servidor();
    }
}
