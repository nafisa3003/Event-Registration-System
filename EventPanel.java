import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class EventPanel extends JPanel {
    private EventService eventService;
    private EventForm eventForm;
    private JTable table;
    private EventTableModel tableModel;
    private JTextField searchField;

    public EventPanel(EventService eventService) {
        this.eventService = eventService;
        this.eventForm = eventForm;

        setLayout(new BorderLayout());
        setBackground(Utils.WHITE);

        // Table
        tableModel = new EventTableModel(new ArrayList<>());
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true); // enables sorting

        Font tableFont = new Font("Roboto", Font.PLAIN, 14);
        Font headerFont = new Font("Roboto", Font.BOLD, 15);
        int rowHeight = 20;

        table.setFont(tableFont);
        table.setRowHeight(rowHeight);
        table.getTableHeader().setFont(headerFont);

        //        Center align row data 
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

        // Auto-fill form on row select
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                String id = (String) table.getValueAt(table.getSelectedRow(), 0); // ID in column 0
                Event ev = eventService.getEventById(id);
                if (eventForm != null) {
                    eventForm.loadFromEvent(ev);
                }
            }
        });

        refreshTable();
    }

    public void refreshTable() {
        tableModel.setData(eventService.getAllEvents());
    }

    public JTable getTable() {
        return table;
    }

    public JTextField getSearchField() {
        return searchField;
    }
}