import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String[] vet = {"172.16.12.171", "172.16.12.166"};
        int[] valores = new int[1001];
        Random random = new Random();
        for (int i = 0; i < 1001; i++) {
            valores[i] = random.nextInt(1001) + 1; // Números aleatórios de 1 a 1000
        }

        int valorProcurado = obterValorProcurado();
        boolean valorEncontrado = false;

        if (valorProcurado != -1) {
            System.out.println("Abrindo conexões e enviando vetor...");

            int numMaquinas = vet.length;
            int tamanhoPedaco = valores.length / numMaquinas;

            for (int i = 0; i < numMaquinas; i++) {
                int inicio = i * tamanhoPedaco;
                int fim = (i == numMaquinas - 1) ? valores.length : (i + 1) * tamanhoPedaco;
                int[] pedacoValores = Arrays.copyOfRange(valores, inicio, fim);

                try {
                    Socket socket = new Socket(vet[i], 12345);
                    ObjectOutputStream transmissor = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream receptor = new ObjectInputStream(socket.getInputStream());

                    System.out.println("Enviando vetor para " + vet[i]);

                    Pedido pedido = new Pedido(pedacoValores, valorProcurado);
                    transmissor.writeObject(pedido);

                    Resposta resposta = (Resposta) receptor.readObject();

                    if (resposta.getValorEncontrado() != null) {
                        int posicaoEncontrada = resposta.getValorEncontrado();
                        if (posicaoEncontrada != -1) {
                            System.out.println("Número encontrado na posição " + posicaoEncontrada + " em " + vet[i]);
                            valorEncontrado = true;
                            break; // Encerra o loop caso encontre o número em alguma máquina
                        } else {
                            System.out.println("Número não encontrado em " + vet[i]);
                        }
                    } else {
                        System.out.println("Resposta inválida recebida de " + vet[i]);
                    }

                    transmissor.close();
                    receptor.close();
                    socket.close();
                } catch (IOException | ClassNotFoundException e) {
                    int numTentativas = 3; // Defina o número de tentativas desejado

                    for (int tentativa = 1; tentativa <= numTentativas; tentativa++) {
                        try {
                            Socket socket = new Socket(vet[i], 12345);
                            // Restante do seu código de envio de dados...

                            break; // Se chegou até aqui, a conexão foi estabelecida com sucesso, então saia do loop
                        } catch (ConnectException error) {
                            System.out.println("Erro na conexão com " + vet[i] + ": " + error.getMessage());
                            if (tentativa < numTentativas) {
                                System.out.println("Tentando novamente...");
                            } else {
                                System.out.println("Número máximo de tentativas alcançado. Avançando para a próxima máquina.");
                                break; // Se atingiu o número máximo de tentativas, saia do loop
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            break; // Outros tipos de exceção podem ser tratados aqui, e o loop pode ser interrompido
                        }
                    }
                }
            }

            if (!valorEncontrado) {
                System.out.println("Número não encontrado em nenhum dos computadores.");
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