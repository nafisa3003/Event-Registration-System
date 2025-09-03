import java.awt.*;
import javax.swing.*;

public class RegistrationForm extends JPanel {
    private RegistrationService regService;
    private RegistrationPanel regPanel;
    private EventService eventService;
    private ParticipantService participantService;

    private JTextField regIdField;
    private JComboBox<String> participantCb;
    private JComboBox<String> eventCb;

    private JButton addBtn, updateBtn, deleteBtn, clearBtn;

    public RegistrationForm(RegistrationService regService, RegistrationPanel regPanel,
                            EventService eventService, ParticipantService participantService) {
        this.regService = regService;
        this.regPanel = regPanel;
        this.eventService = eventService;
        this.participantService = participantService;

        setLayout(new GridBagLayout());
        setBackground(Utils.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Reg ID Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Reg ID:"), gbc);

        regIdField = new JTextField(15);
        regIdField.setEditable(false); // prevent manual typing
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(regIdField, gbc);

        // Show next ID without incrementing (peek)
        regIdField.setText(regService.peekNextId()); 

        // Participant Dropdown 
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Participant:"), gbc);
        participantCb = new JComboBox<>(participantService.getParticipantNames());
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(participantCb, gbc);

        // Event Dropdown 
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Event:"), gbc);
        eventCb = new JComboBox<>(eventService.getEventNames());
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(eventCb, gbc);

        //  Buttons 
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        btnPanel.setBackground(Utils.WHITE);

        addBtn = Utils.createButton("Add", Utils.ACCENT_LIGHT_BLUE);
        updateBtn = Utils.createButton("Update", Utils.DARK_NAVY);
        deleteBtn = Utils.createButton("Delete", Utils.RED);
        clearBtn = Utils.createButton("Clear", Utils.GRAY);

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(clearBtn);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(btnPanel, gbc);

        //Table Row Selection 
        regPanel.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = regPanel.getTable().getSelectedRow();
                if (row != -1) {
                    row = regPanel.getTable().convertRowIndexToModel(row);
                    Registration r = regService.getAllRegistrations().get(row);

                    // Fill fields from selected row
                    regIdField.setText(r.getId());
                    participantCb.setSelectedItem(participantService.getParticipantById(r.getParticipantId()));
                    eventCb.setSelectedItem(eventService.getEventById(r.getEventId()));
                }
            }
        });

        // Action Listeners
        addBtn.addActionListener(e -> addRegistration());
        updateBtn.addActionListener(e -> updateRegistration());
        deleteBtn.addActionListener(e -> deleteRegistration());
        clearBtn.addActionListener(e -> clearFields());

        // Refresh Dropdowns when data changes 
        eventService.addListener(this::refreshEventDropdown);
        participantService.addListener(this::refreshParticipantDropdown);
    }

    // Add Registration
    private void addRegistration() {
        String regId = regService.generateId(); // increment counter only when adding
        String participantName = (String) participantCb.getSelectedItem();
        String eventName = (String) eventCb.getSelectedItem();

        if (participantName == null || eventName == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        String participantId = participantService.getParticipantIdByName(participantName);
        String eventId = eventService.getEventIdByName(eventName);

        Registration r = new Registration(regId, participantId, eventId, eventName);
        regService.addRegistration(r);

        JOptionPane.showMessageDialog(this, "Registration added successfully!");
        regPanel.refreshTable();

        clearFields(); // after adding, show next available ID (peek)
    }

    // Update Registration
    private void updateRegistration() {
        int selectedRow = regPanel.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a registration to update");
            return;
        }

        Registration r = regService.getAllRegistrations().get(regPanel.getTable().convertRowIndexToModel(selectedRow));
        r.setParticipantId(participantService.getParticipantIdByName((String) participantCb.getSelectedItem()));
        r.setEventId(eventService.getEventIdByName((String) eventCb.getSelectedItem()));
        r.setEventName((String) eventCb.getSelectedItem());

        regService.saveRegistrations();
        regPanel.refreshTable();
        clearFields();
    }

    // Delete Registration
    private void deleteRegistration() {
        int selectedRow = regPanel.getTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a registration to delete");
            return;
        }

        Registration r = regService.getAllRegistrations().get(regPanel.getTable().convertRowIndexToModel(selectedRow));
        regService.deleteRegistration(r.getId());
        regPanel.refreshTable();
        clearFields();
    }

    // Clear Fields 
    private void clearFields() {
        regIdField.setText(regService.peekNextId()); // show next ID without incrementing
        participantCb.setSelectedIndex(0);
        eventCb.setSelectedIndex(0);
    }

    // Refresh Dropdowns
    public void refreshEventDropdown() {
        eventCb.setModel(new DefaultComboBoxModel<>(eventService.getEventNames()));
    }

    public void refreshParticipantDropdown() {
        participantCb.setModel(new DefaultComboBoxModel<>(participantService.getParticipantNames()));
    }
}