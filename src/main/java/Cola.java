public class Cola {
    class Node {
        int elemento;
        Node siguiente;

        public Node(int o) {
            elemento = o;
            siguiente = null;
        }
    }

    private Node primer;
    private Node ultimo;
    private int longitud;

    public Cola() {
        primer = null;
        longitud = 0;
    }

    public void encolar(int o) {
        Node node = new Node(o);
        if (primer == null) {
            primer = node;
            ultimo = node;
        } else {
            ultimo.siguiente = node;
            ultimo = node;
        }
        longitud++;
    };

    public int desencolar() {
        if (primer == null)
            return 0;
        int o = primer.elemento;
        primer = primer.siguiente;
        longitud--;
        return o;
    }

    public boolean vacio() {
        return (longitud == 0);
    }

    public int longitud() {
        return longitud;
    }

    public int primer() {
        if (primer == null)
            return 0;
        else
            return primer.elemento;
    }
}
