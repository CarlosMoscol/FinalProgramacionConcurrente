public class ColaS {
    class Node {
        String elemento;
        Node siguiente;

        public Node(String o) {
            elemento = o;
            siguiente = null;
        }
    }

    private Node primer;
    private Node ultimo;
    private int longitud;

    public ColaS() {
        primer = null;
        longitud = 0;
    }

    public void encolar(String o) {
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

    public String desencolar() {
        if (primer == null)
            return null;
        String o = primer.elemento;
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

    public String primer() {
        if (primer == null)
            return null;
        else
            return primer.elemento;
    }
}
