//TelaTarefa

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

public class TelaTarefas extends JFrame {
    private Database database;
    private String usuario;
    private DefaultListModel<String> listaModelo;
    private JList<String> listaTarefas;

    public TelaTarefas(String usuario) {
        this.usuario = usuario;
        this.database = new Database(); // Inicializa a instância do Database

        setTitle("Lista de Tarefas");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        listaModelo = new DefaultListModel<>();
        listaTarefas = new JList<>(listaModelo);
        atualizarListaTarefas();

        JScrollPane scrollPane = new JScrollPane(listaTarefas);
        add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar Tarefa");
        JButton btnEditar = new JButton("Editar Tarefa");
        JButton btnExcluir = new JButton("Excluir Tarefa");
        JButton btnLogout = new JButton("Logout");

        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titulo = JOptionPane.showInputDialog("Título da tarefa:");
                String descricao = JOptionPane.showInputDialog("Descrição da tarefa:");
                if (titulo != null && descricao != null && !titulo.trim().isEmpty()) {
                    database.adicionarTarefa(usuario, titulo.trim(), descricao.trim());
                    atualizarListaTarefas();
                } else {
                    JOptionPane.showMessageDialog(null, "Título da tarefa não pode ser vazio.");
                }
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indice = listaTarefas.getSelectedIndex();
                if (indice != -1) {
                    String tarefaSelecionada = listaModelo.getElementAt(indice);
                    String novoTitulo = JOptionPane.showInputDialog("Novo título da tarefa:", tarefaSelecionada);
                    String novaDescricao = JOptionPane.showInputDialog("Nova descrição da tarefa:");
                    if (novoTitulo != null && novaDescricao != null) {
                        int id = obterIdTarefa(tarefaSelecionada); // Obtém o ID da tarefa
                        database.atualizarTarefa(id, novoTitulo, novaDescricao); // Atualiza a tarefa no banco
                        atualizarListaTarefas();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione uma tarefa para editar.");
                }
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indice = listaTarefas.getSelectedIndex();
                if (indice != -1) {
                    String tarefaSelecionada = listaModelo.getElementAt(indice);
                    int id = obterIdTarefa(tarefaSelecionada); // Obtém o ID da tarefa
                    database.excluirTarefa(id); // Exclui a tarefa no banco
                    atualizarListaTarefas();
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione uma tarefa para excluir.");
                }
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TelaLogin().setVisible(true);
                dispose(); // Fecha a tela de tarefas
            }
        });

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLogout);

        add(painelBotoes, BorderLayout.SOUTH);
    }

    private void atualizarListaTarefas() {
        listaModelo.clear();
        List<String> tarefas = database.obterTarefas(usuario);
        for (String tarefa : tarefas) {
            listaModelo.addElement(tarefa);
        }
    }

    private int obterIdTarefa(String tarefaCompleta) {
        String titulo = tarefaCompleta.split(" - ")[0];
        String sql = "SELECT id FROM tarefas WHERE usuario = ? AND titulo = ?;";
        try (PreparedStatement pstmt = database.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, titulo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter ID da tarefa: " + e.getMessage());
        }
        return -1;
    }
}
