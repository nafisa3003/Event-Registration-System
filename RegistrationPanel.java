import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class RegistrationPanel extends JPanel {
    private RegistrationService regService;
    private ParticipantService participantService;
    private EventService eventService;

    private JTable table;
    private RegistrationTableModel tableModel;
    private JTextField searchField;

    public RegistrationPanel(RegistrationService regService,
            ParticipantService participantService,
            EventService eventService) {
        this.regService = regService;
        this.participantService = participantService;
        this.eventService = eventService;

        setLayout(new BorderLayout());
        setBackground(Utils.WHITE);

        // Table
        tableModel = new RegistrationTableModel(new ArrayList<>(), participantService, eventService);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        Font tableFont = new Font("Roboto", Font.PLAIN, 14);
        Font headerFont = new Font("Roboto", Font.BOLD, 15);
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

        // Search bar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        topPanel.setOpaque(false);
        topPanel.add(new JLabel("Search:"));
        searchField = new JTextField(25);
        topPanel.add(searchField);

        searchField.getDocument().addDocumentListener(Utils.createSearchListener(searchField, table));

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setData(regService.getAllRegistrations());
    }

    public JTable getTable() {
        return table;
    }

    public JTextField getSearchField() {
        return searchField;
    }
}