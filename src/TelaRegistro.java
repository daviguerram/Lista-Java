//TelaRegistro

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaRegistro extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private Database database; // Instância da classe Database

    public TelaRegistro() {
        database = new Database(); // Inicializa a instância do Database

        setTitle("Registro");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2)); // Aumenta o número de linhas

        add(new JLabel("Usuário:"));
        campoUsuario = new JTextField();
        add(campoUsuario);

        add(new JLabel("Senha:"));
        campoSenha = new JPasswordField();
        add(campoSenha);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = campoUsuario.getText().trim();
                String senha = new String(campoSenha.getPassword()).trim();

                // Validação
                if (usuario.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.");
                    return;
                }

                if (database.registrarUsuario(usuario, senha)) { // Chama o método na instância
                    JOptionPane.showMessageDialog(null, "Registro realizado com sucesso!");
                    new TelaLogin().setVisible(true);
                    dispose(); // Fecha a tela de registro
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao registrar usuário. Tente novamente.");
                }
            }
        });
        add(btnRegistrar);

        JButton btnVoltar = new JButton("Voltar ao Login"); // Botão para voltar ao login
        btnVoltar.addActionListener(e -> {
            new TelaLogin().setVisible(true);
            dispose(); // Fecha a tela de registro
        });
        add(btnVoltar); // Adiciona o botão de voltar
    }
}
