import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.InputMismatchException; // Importando a classe para tratamento de exceção
import java.util.Scanner; // Importando Scanner

public class Main {

    public static void main(String[] args) {
        String[] vet = {"192.168.1.41"};
        int[] valores = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}; // Preencha com valores diferentes

        int valorProcurado = obterValorProcurado();

        if (valorProcurado != -1) {
            System.out.println("Abrindo conexões e enviando vetor...");

            for (int i = 0; i < vet.length; i++) {
                try {
                    Socket socket = new Socket(vet[i], 12345);
                    ObjectOutputStream transmissor = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream receptor = new ObjectInputStream(socket.getInputStream());

                    System.out.println("Enviando vetor para " + vet[i]);

                    Pedido pedido = new Pedido(valores, valorProcurado);
                    transmissor.writeObject(pedido);

                    Resposta resposta = (Resposta) receptor.readObject();

                    if (resposta.getValorEncontrado() != null) {
                        System.out.println("Número encontrado na posição " + resposta.getValorEncontrado() + " em " + vet[i]);
                    } else {
                        System.out.println("Número não encontrado em " + vet[i]);
                    }

                    transmissor.close();
                    receptor.close();
                    socket.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Valor inválido. Tente novamente.");
        }
    }

    // Método para obter o valor procurado do usuário
    private static int obterValorProcurado() {
        Scanner scanner = new Scanner(System.in);
        int valorProcurado = -1;

        try {
            System.out.print("Digite o valor a ser procurado (apenas números inteiros): ");
            valorProcurado = scanner.nextInt();
        } catch (Exception e) {
            // Tratamento caso não seja inserido um número inteiro
            System.out.println("Entrada inválida! Certifique-se de inserir um número inteiro.");
        } finally {
            scanner.close(); // Fechar o scanner
        }

        return valorProcurado;
    }
}
