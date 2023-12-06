// Classe Pedido
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Pedido extends Comunicado {
    private int[] valores;
    private int valorProcurado;
    private static final long serialVersionUID = 1L; // Adicionando o serialVersionUID

    public Pedido(int[] valores, int valorProcurado) {
        this.valores = valores;
        this.valorProcurado = valorProcurado;
    }

    public Integer getValorEncontrado() {
        for (int i = 0; i < valores.length; i++) {
            if (valores[i] == valorProcurado) {
                return i; // Retorna a posição onde foi encontrado
            }
        }
        return null; // Retorna null se não encontrado
    }

    public int getValorProcurado() {
        return valorProcurado;
    }

    public int[] getValores() {
        return valores;
    }

}