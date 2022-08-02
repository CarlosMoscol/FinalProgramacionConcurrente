public class ColaB {
    class Node {
        byte[] elemento;
        Node siguiente;

        public Node(byte[] o) {
            elemento = o;
            siguiente = null;
        }
    }

    private Node primer;
    private Node ultimo;
    private int longitud;

    public ColaB() {
        primer = null;
        longitud = 0;
    }

    public void encolar(byte[] o) {
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

    public byte[] desencolar() {
        if (primer == null)
            return null;
        byte[] o = primer.elemento;
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

    public byte[] primer() {
        if (primer == null)
            return null;
        else
            return primer.elemento;
    }
}
