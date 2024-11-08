//Database

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection conn;

    public Database() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:tarefas.db");
            criarTabelaUsuarios();
            criarTabelaTarefas();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
  
     // Método que retorna a conexão ativa
     public Connection getConnection() {
        return conn;
    }
    

    private void criarTabelaUsuarios() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "usuario TEXT NOT NULL, " +
                     "senha TEXT NOT NULL);";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela de usuários: " + e.getMessage());
        }
    }

    private void criarTabelaTarefas() {
        String sql = "CREATE TABLE IF NOT EXISTS tarefas (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "usuario TEXT NOT NULL, " +
                     "descricao TEXT, " +
                     "titulo TEXT NOT NULL);";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela de tarefas: " + e.getMessage());
        }
    }

    public boolean registrarUsuario(String usuario, String senha) {
        String sql = "INSERT INTO usuarios (usuario, senha) VALUES (?, ?);";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, senha);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao registrar usuário: " + e.getMessage());
            return false;
        }
    }

    public boolean autenticarUsuario(String usuario, String senha) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND senha = ?;";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, senha);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao autenticar usuário: " + e.getMessage());
            return false;
        }
    }

    public List<String> obterTarefas(String usuario) {
        List<String> tarefas = new ArrayList<>();
        String sql = "SELECT titulo || ' - ' || descricao AS tarefa FROM tarefas WHERE usuario = ?;";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tarefas.add(rs.getString("tarefa"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao obter tarefas: " + e.getMessage());
        }
        return tarefas;
    }

    public boolean adicionarTarefa(String usuario, String titulo, String descricao) {
        String sql = "INSERT INTO tarefas (usuario, titulo, descricao) VALUES (?, ?, ?);";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, titulo);
            pstmt.setString(3, descricao);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar tarefa: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizarTarefa(int id, String novoTitulo, String novaDescricao) {
        String sql = "UPDATE tarefas SET titulo = ?, descricao = ? WHERE id = ?;";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, novoTitulo);
            pstmt.setString(2, novaDescricao);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar tarefa: " + e.getMessage());
            return false;
        }
    }

    public boolean excluirTarefa(int id) {
        String sql = "DELETE FROM tarefas WHERE id = ?;";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir tarefa: " + e.getMessage());
            return false;
        }
    }
}
