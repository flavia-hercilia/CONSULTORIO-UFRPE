package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import repository.UsuarioRepository;

public class TelaDeEntradaDoPG extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    public UsuarioRepository usuarioRepository;

    public TelaDeEntradaDoPG() {
        usuarioRepository = new UsuarioRepository();
        setTitle("Tela de Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Maximiza a janela para tela cheia
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Carrega a imagem de fundo
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/imagens/Black and Red Minimalist Modern Registration Gym Website Prototype1.png"));
        if (backgroundIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.err.println("Erro ao carregar a imagem de fundo.");
        } else {
            Image image = backgroundIcon.getImage();
            Image scaledImage = image.getScaledInstance(Toolkit.getDefaultToolkit().getScreenSize().width,
                    Toolkit.getDefaultToolkit().getScreenSize().height,
                    Image.SCALE_SMOOTH);
            backgroundIcon = new ImageIcon(scaledImage);

            JLabel backgroundLabel = new JLabel(backgroundIcon);
            backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
            add(backgroundLabel);

            // Configura a posição dos componentes com base na imagem
            JLabel lblFacalogin = new JLabel("Faça Seu Login");
            lblFacalogin.setFont(new Font("SansSerif", Font.BOLD, 24));
            lblFacalogin.setForeground(new Color(255, 69, 0));  // Cor para combinar com o design
            lblFacalogin.setBounds(160, 150, 200, 40);
            backgroundLabel.add(lblFacalogin);

            JLabel lblUsuario = new JLabel("Usuário");
            lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 18));
            lblUsuario.setBounds(160, 200, 80, 25);
            lblUsuario.setForeground(Color.white);
            backgroundLabel.add(lblUsuario);

            txtUsuario = new JTextField();
            txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 18));
            txtUsuario.setBounds(160, 230, 280, 40);
            txtUsuario.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));  // Bordas vazias para um look mais clean
            backgroundLabel.add(txtUsuario);

            JLabel lblSenha = new JLabel("Senha");
            lblSenha.setFont(new Font("SansSerif", Font.PLAIN, 18));
            lblSenha.setBounds(160, 280, 100, 25);
            lblSenha.setForeground(Color.white);
            backgroundLabel.add(lblSenha);

            txtSenha = new JPasswordField();
            txtSenha.setFont(new Font("SansSerif", Font.PLAIN, 18));
            txtSenha.setBounds(160, 310, 280, 40);
            txtSenha.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));  // Bordas vazias para um look mais clean
            backgroundLabel.add(txtSenha);

            // Botão de login customizado com estilo moderno
            JButton btnLogin = new JButton("Entrar") {

                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    // Desenha o fundo do botão
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 69, 0));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                    // Desenha o texto do botão
                    g2.setColor(Color.WHITE);
                    g2.setFont(getFont().deriveFont(Font.BOLD, 18f));
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(getText())) / 2;
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(getText(), x, y);

                    g2.dispose();
                }
                public Dimension getPreferredSize() {
                    return new Dimension(280, 50);
                }
            };

            btnLogin.setFocusPainted(false);
            btnLogin.setContentAreaFilled(false);
            btnLogin.setBorderPainted(false);
            btnLogin.setBounds(160, 370, 280, 50);
            backgroundLabel.add(btnLogin);

            btnLogin.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String usuario = txtUsuario.getText();
                    String senha = new String(txtSenha.getPassword());

                    if (usuario.isEmpty() || senha.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos.");
                    } else if (usuarioRepository.validarLogin(usuario, senha)) {
                        dispose();  // Fecha a tela de login
                        SwingUtilities.invokeLater(() -> {
                            ProgramaGUI programaGUI = new ProgramaGUI();
                            programaGUI.setVisible(true);
                        });
                    } else {
                        JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos.");
                    }
                }
            });

            // Adiciona o backgroundLabel por último para garantir que esteja em baixo
            setContentPane(backgroundLabel);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaDeEntradaDoPG loginScreen = new TelaDeEntradaDoPG();
            loginScreen.setVisible(true);
        });
    }
}
