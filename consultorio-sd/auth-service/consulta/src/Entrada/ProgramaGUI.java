package Entrada;

import consulta.*;
import java.time.format.DateTimeFormatter;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.SpinnerDateModel;
import javax.swing.JSpinner;

public class ProgramaGUI extends JFrame {
    private ControladorMedico controladorMedico;
    private ControladorPaciente controladorPaciente;
    private ControladorConsulta controladorConsulta;

    private JTextArea textAreaMedicos;
    private JTextArea textAreaPacientes;
    private JTextArea textAreaConsultas;

    private JTable tabelaConsultas;
    private DefaultTableModel modeloTabela;

    public ProgramaGUI() {
        controladorMedico = ControladorMedico.getInstance();
        controladorPaciente = ControladorPaciente.getInstance();
        controladorConsulta = ControladorConsulta.getInstance();

        setTitle("Sistema de Gerenciamento de Consultas Médicas");
        setSize(2000, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Painéis para médicos, pacientes e consultas
        JPanel panelMedicos = criarPainelComTitulo("Médicos");
        JPanel panelPacientes = criarPainelComTitulo("Pacientes");
        JPanel panelConsultas = criarPainelComTitulo("Consultas");
        JPanel panelAgenda = criarPainelComTitulo("Agenda");

        textAreaMedicos = criarTextArea();
        textAreaPacientes = criarTextArea();
        textAreaConsultas = criarTextArea();

        JButton btnAddMedico = criarBotao("Adicionar Médico");
        JButton btnAddPaciente = criarBotao("Adicionar Paciente");
        JButton btnAgendarConsulta = criarBotao("Agendar Consulta");
        JButton btnVerAgenda = criarBotao("Atualizar Agenda");

        // Configuração da tabela de consultas
        modeloTabela = new DefaultTableModel(new Object[]{"Paciente", "Médico", "Data", "Hora"}, 0);
        tabelaConsultas = new JTable(modeloTabela);
        tabelaConsultas.setFillsViewportHeight(true);
        tabelaConsultas.setRowHeight(30); // Ajuste na altura das linhas
        panelAgenda.add(new JScrollPane(tabelaConsultas), BorderLayout.CENTER);

        // ActionListener para adicionar médicos
        btnAddMedico.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nome = JOptionPane.showInputDialog(null, "Nome do Médico:", "Adicionar Médico", JOptionPane.PLAIN_MESSAGE);
                String especialidade = JOptionPane.showInputDialog(null, "Especialidade Médica:", "Adicionar Médico", JOptionPane.PLAIN_MESSAGE);
                int id = Integer.parseInt(JOptionPane.showInputDialog(null, "Cod:", "Adicionar Médico", JOptionPane.PLAIN_MESSAGE));
                Medico medico = new Medico(id, nome, especialidade);
                controladorMedico.adicionarMedico(medico);
                atualizarMedicos();
            }
        });

        // ActionListener para adicionar pacientes
        btnAddPaciente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nome = JOptionPane.showInputDialog(null, "Nome do Paciente:", "Adicionar Paciente", JOptionPane.PLAIN_MESSAGE);
                int idade = Integer.parseInt(JOptionPane.showInputDialog(null, "Idade do Paciente:", "Adicionar Paciente", JOptionPane.PLAIN_MESSAGE));
                int id = controladorPaciente.getPacientes().size() + 1;
                Paciente paciente = new Paciente(id, nome, idade);
                controladorPaciente.adicionarPaciente(paciente);
                atualizarPacientes();
            }
        });

        // ActionListener para agendar consultas
        btnAgendarConsulta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (controladorMedico.getMedicos().isEmpty() || controladorPaciente.getPacientes().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Adicione médicos e pacientes primeiro.");
                    return;
                }

                String[] medicosArray = controladorMedico.getMedicos().stream().map(Medico::getNome).toArray(String[]::new);
                String[] pacientesArray = controladorPaciente.getPacientes().stream().map(Paciente::getNome).toArray(String[]::new);

                String medicoNome = (String) JOptionPane.showInputDialog(null, "Escolha o Médico:",
                        "Agendar Consulta", JOptionPane.QUESTION_MESSAGE, null, medicosArray, medicosArray[0]);
                String pacienteNome = (String) JOptionPane.showInputDialog(null, "Escolha o Paciente:",
                        "Agendar Consulta", JOptionPane.QUESTION_MESSAGE, null, pacientesArray, pacientesArray[0]);

                Medico medico = controladorMedico.getMedicos().stream().filter(m -> m.getNome().equals(medicoNome)).findFirst().orElse(null);
                Paciente paciente = controladorPaciente.getPacientes().stream().filter(p -> p.getNome().equals(pacienteNome)).findFirst().orElse(null);

                if (medico != null && paciente != null) {
                    JDateChooser dateChooser = new JDateChooser();
                    SpinnerModel timeModel = new SpinnerDateModel();
                    JSpinner timeSpinner = new JSpinner(timeModel);
                    JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
                    timeSpinner.setEditor(timeEditor);
                    timeSpinner.setValue(Calendar.getInstance().getTime());

                    JPanel panel = new JPanel(new GridLayout(0, 1));
                    panel.add(new JLabel("Data da Consulta:"));
                    panel.add(dateChooser);
                    panel.add(new JLabel("Hora da Consulta:"));
                    panel.add(timeSpinner);

                    int result = JOptionPane.showConfirmDialog(null, panel, "Agendar Consulta",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        LocalDate data = LocalDate.of(dateChooser.getDate().getYear() + 1900,
                                dateChooser.getDate().getMonth() + 1,
                                dateChooser.getDate().getDate());

                        if (data.isBefore(LocalDate.now())) {
                            JOptionPane.showMessageDialog(null, "Não é possível agendar consultas em datas passadas.", "Erro", JOptionPane.ERROR_MESSAGE);
                            return; // esse comando irá interromper o agendamento
                        }

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime((java.util.Date) timeSpinner.getValue());
                        LocalTime hora = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

                        for (Consultadados consultaExistente : controladorConsulta.getConsultas()) {
                            if (consultaExistente.getMedico().equals(medico) &&
                                    consultaExistente.getData().equals(data) &&
                                    consultaExistente.getHora().equals(hora)) {
                                JOptionPane.showMessageDialog(null, "Já existe uma consulta marcada para este médico neste horário.", "Conflito de Horário", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }

                        Consultadados consulta = new Consultadados(paciente, medico, data, hora);
                        controladorConsulta.agendarConsulta(consulta);
                        atualizarConsultas();
                    }
                }
            }
        });

        // ActionListener para atualizar a agenda
        btnVerAgenda.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizarConsultas();
            }
        });

        // Adicionando componentes aos painéis
        adicionarComponentes(panelMedicos, textAreaMedicos, btnAddMedico);
        adicionarComponentes(panelPacientes, textAreaPacientes, btnAddPaciente);
        adicionarComponentes(panelConsultas, textAreaConsultas, btnAgendarConsulta);
        panelAgenda.add(btnVerAgenda, BorderLayout.SOUTH);

        // Adicionando painéis à janela principal
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(panelMedicos, gbc);

        gbc.gridx = 1;
        add(panelPacientes, gbc);

        gbc.gridx = 2;
        add(panelConsultas, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        add(panelAgenda, gbc);

        // Atualizando exibições
        atualizarMedicos();
        atualizarPacientes();
        atualizarConsultas();
    }

    private JPanel criarPainelComTitulo(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), titulo, TitledBorder.CENTER, TitledBorder.TOP));
        return panel;
    }

    private JTextArea criarTextArea() {
        JTextArea textArea = new JTextArea(10, 25);
        textArea.setEditable(false);
        textArea.setLineWrap(true); // Ativando quebra automática de linha
        textArea.setWrapStyleWord(true); // Quebra de linha em palavras inteiras
        return textArea;
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setPreferredSize(new Dimension(150, 30));
        botao.setBackground(Color.LIGHT_GRAY); // Cor de fundo
        botao.setFocusPainted(false); // Removendo borda de foco
        return botao;
    }

    private void adicionarComponentes(JPanel panel, JTextArea textArea, JButton botao) {
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        panel.add(botao, BorderLayout.SOUTH);
    }

    private void atualizarMedicos() {
        textAreaMedicos.setText("");
        for (Medico medico : controladorMedico.getMedicos()) {
            textAreaMedicos.append(medico + "\n");
        }
    }

    private void atualizarPacientes() {
        textAreaPacientes.setText("");
        for (Paciente paciente : controladorPaciente.getPacientes()) {
            textAreaPacientes.append(paciente + "\n");
        }
    }

    private void atualizarConsultas() {
        modeloTabela.setRowCount(0); // Limpa a tabela
        DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");
        for (Consultadados consulta : controladorConsulta.getConsultas()) {
            modeloTabela.addRow(new Object[]{
                    consulta.getPaciente().getNome(),
                    consulta.getMedico().getNome(),
                    consulta.getData().format(formatterData),
                    consulta.getHora().format(formatterHora)
            });
        }
    }
}
