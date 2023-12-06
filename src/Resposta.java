// Classe Resposta
public class Resposta extends Comunicado {
    private Integer valorEncontrado;
    private static final long serialVersionUID = 1L; // Adicionando o serialVersionUID

    public Resposta(Integer valorEncontrado) {
        this.valorEncontrado = valorEncontrado;
    }

    public Integer getValorEncontrado() {
        return valorEncontrado;
    }
}