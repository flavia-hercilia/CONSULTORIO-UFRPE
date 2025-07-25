package consulta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Agenda extends JFrame {
    private JTable tabelaConsultas;
    private DefaultTableModel modeloTabela;

    public Agenda(ControladorConsulta controladorConsulta) {
        setTitle("Agenda de Consultas");
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Criação da tabela
        modeloTabela = new DefaultTableModel(new Object[]{"Paciente", "Médico", "Data", "Hora"}, 0);
        tabelaConsultas = new JTable(modeloTabela);
        add(new JScrollPane(tabelaConsultas), BorderLayout.CENTER);

        // Atualiza a tabela com as consultas do controlador
        atualizarTabela(controladorConsulta.getConsultas());
    }

    private void atualizarTabela(List<Consultadados> consultas) {
        modeloTabela.setRowCount(0); // Limpa a tabela
        for (Consultadados consulta : consultas) {
            modeloTabela.addRow(new Object[]{
                    consulta.getPaciente().getNome(),
                    consulta.getMedico().getNome(),
                    consulta.getData(),
                    consulta.getHora()
            });
        }
    }
}
