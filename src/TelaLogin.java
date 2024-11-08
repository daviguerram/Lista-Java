//TelaLogin

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaLogin extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private Database database; // Instância da classe Database

    public TelaLogin() {
        database = new Database(); // Inicializa a instância do Database

        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Usuário:"));
        campoUsuario = new JTextField();
        add(campoUsuario);

        add(new JLabel("Senha:"));
        campoSenha = new JPasswordField();
        add(campoSenha);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = campoUsuario.getText().trim();
                String senha = new String(campoSenha.getPassword()).trim();

                // Validação
                if (usuario.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.");
                    return;
                }

                // Chamando o método autenticarUsuario da instância database corretamente
                if (database.autenticarUsuario(usuario, senha)) { 
                    new TelaTarefas(usuario).setVisible(true);
                    dispose(); // Fecha a tela de login
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos.");
                }
            }
        });
        add(btnLogin);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(e -> {
            new TelaRegistro().setVisible(true);
            dispose(); // Fecha a tela de login
        });
        add(btnRegistrar);
    }
}
