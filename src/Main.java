import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("Aguardando conexão...");

            ServerSocket pedido = new ServerSocket(12345);

            while (true) {
                Socket conexao = pedido.accept();
                System.out.println("Conexão estabelecida com: " + conexao.getInetAddress().getHostAddress());
                ObjectOutputStream transmissor = new ObjectOutputStream(conexao.getOutputStream());
                ObjectInputStream receptor = new ObjectInputStream(conexao.getInputStream());

                Comunicado comunicado = (Comunicado) receptor.readObject();
                if (comunicado instanceof Pedido) {
                    Pedido pedidoRecebido = (Pedido) comunicado;

                    int valorProcurado = pedidoRecebido.getValorProcurado();
                    int[] valores = pedidoRecebido.getValores();

                    int encontrado = procurarValor(valorProcurado, valores);

                    // Enviar resposta para o Programa D
                    transmissor.writeObject(new Resposta(encontrado));
                }

                // Não fechar a conexão antes de enviar a resposta
                // transmissor.close();
                // receptor.close();
                // conexao.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Método para buscar o valor no array
    private static int procurarValor(int valorProcurado, int[] valores) {
        for (int i = 0; i < valores.length; i++) {
            if (valores[i] == valorProcurado) {
                return i; // Retorna a posição se encontrar o valor
            }
        }
        return -1; // Retorna -1 se o valor não for encontrado
    }


}
