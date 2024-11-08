//Tarefa

public class Tarefa {
    private int id;
    private String titulo;
    private String descricao;

    public Tarefa(int id, String titulo, String descricao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return titulo + " - " + descricao; // Customize conforme necessário
    }
}
