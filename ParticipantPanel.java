import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class ParticipantPanel extends JPanel {
    private ParticipantService participantService;
    private JTable table;
    private ParticipantTableModel tableModel;
    private JTextField searchField;

    public ParticipantPanel(ParticipantService participantService) {
        this.participantService = participantService;
        setLayout(new BorderLayout());
        setBackground(Utils.WHITE);

        tableModel = new ParticipantTableModel(new ArrayList<>());
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        Font tableFont = new Font("Roboto", Font.PLAIN, 12);
        Font headerFont = new Font("Roboto", Font.BOLD, 14);
        int rowHeight = 20;

        table.setFont(tableFont);
        table.setRowHeight(rowHeight);
        table.getTableHeader().setFont(headerFont);

        // Center align row data 
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Apply to each column
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        topPanel.setOpaque(false);
        topPanel.add(new JLabel("Search:"));
        searchField = new JTextField(25);
        topPanel.add(searchField);

        searchField.getDocument().addDocumentListener(Utils.createSearchListener(searchField, table));

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttonPanel.setOpaque(false);

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                row = table.convertRowIndexToModel(row); // adjust if sorting
                String id = (String) tableModel.getValueAt(row, 0); // col 0 = ID
                participantService.deleteParticipant(id);
                refreshTable();
                JOptionPane.showMessageDialog(this, "Participant deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a participant to delete.");
            }
        });
        buttonPanel.add(deleteBtn);

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setData(participantService.getAllParticipants());
    }

    public JTable getTable() {
        return table;
    }

    public JTextField getSearchField() {
        return searchField;
    }
}