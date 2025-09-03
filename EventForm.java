import java.awt.*;
import javax.swing.*;

public class EventForm extends JPanel {

    private EventService eventService;
    private EventPanel eventPanel;
    private RegistrationForm registrationForm;

    private JTextField nameField, locationField, dateField;

    public EventForm(EventService eventService, EventPanel eventPanel) {
        this.eventService = eventService;
        this.eventPanel = eventPanel;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Utils.WHITE);

        // Input Fields
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        row1.setOpaque(false);

        row1.add(new JLabel("Name:"));
        nameField = new JTextField(15);
        row1.add(nameField);

        row1.add(new JLabel("Date:"));
        dateField = new JTextField(10);
        row1.add(dateField);

        row1.add(new JLabel("Location:"));
        locationField = new JTextField(15);
        row1.add(locationField);

        add(row1);

        // Buttons
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        row2.setOpaque(false);

        JButton addBtn = Utils.createButton("Add", Utils.PRIMARY_BLUE);
        JButton updateBtn = Utils.createButton("Update", Utils.DARK_NAVY);
        JButton deleteBtn = Utils.createButton("Delete", Utils.RED);
        JButton resetBtn = Utils.createButton("Reset", Utils.ACCENT_LIGHT_BLUE);

        Utils.addHoverEffect(addBtn, Utils.PRIMARY_BLUE);
        Utils.addHoverEffect(updateBtn, Utils.DARK_NAVY);
        Utils.addHoverEffect(deleteBtn, Utils.RED);
        Utils.addHoverEffect(resetBtn, Utils.ACCENT_LIGHT_BLUE);

        row2.add(addBtn);
        row2.add(updateBtn);
        row2.add(deleteBtn);
        row2.add(resetBtn);

        add(row2);

        // AUTO-FILL 
        eventPanel.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = eventPanel.getTable().getSelectedRow();
                if (row != -1) {
                    row = eventPanel.getTable().convertRowIndexToModel(row); // handle sorting
                    fillFieldsFromSelectedRow(row);
                }
            }
        });

        // ===== Button Actions =====
        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            String date = dateField.getText().trim();

            if (name.isEmpty() || location.isEmpty() || date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String id = eventService.generateId();
            Event ev = new Event(id, name, location, date);
            eventService.addEvent(ev);
            eventService.saveEvents();
            eventPanel.refreshTable();

            eventService.notifyListeners();
            resetFields();
        });

        updateBtn.addActionListener(e -> {
            int row = eventPanel.getTable().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an event to update.");
                return;
            }
            String id = (String) eventPanel.getTable().getValueAt(row, 0);
            Event ev = eventService.getEventById(id);
            if (ev == null)
                return;

            ev.setName(nameField.getText().trim());
            ev.setLocation(locationField.getText().trim());
            ev.setDate(dateField.getText().trim());

            eventService.saveEvents();
            eventPanel.refreshTable();

            eventService.notifyListeners();
        });

        deleteBtn.addActionListener(e -> {
            int row = eventPanel.getTable().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an event to delete.");
                return;
            }
            String id = (String) eventPanel.getTable().getValueAt(row, 0);
            eventService.deleteEvent(id);
            eventService.saveEvents();
            eventPanel.refreshTable();

            eventService.notifyListeners();
        });

        resetBtn.addActionListener(e -> resetFields());

        eventService.addListener(() -> {
            if (registrationForm != null)
                registrationForm.refreshEventDropdown();
        });
    }

    // Auto fill form when a row is selected
    public void loadFromEvent(Event ev) {
        if (ev == null)
            return;
        nameField.setText(ev.getName());
        locationField.setText(ev.getLocation());
        dateField.setText(ev.getDate());
    }

    private void addEvent() {
        String name = nameField.getText().trim();
        String location = locationField.getText().trim();
        String date = dateField.getText().trim();

        if (name.isEmpty() || location.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = eventService.generateId();
        Event ev = new Event(id, name, location, date);
        eventService.addEvent(ev);
        eventService.saveEvents();
        if (eventPanel != null)
            eventPanel.refreshTable();

        eventService.notifyListeners();
        resetFields();
    }

    private void updateEvent() {
        if (eventPanel == null)
            return;
        int row = eventPanel.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an event to update.");
            return;
        }
        String id = (String) eventPanel.getTable().getValueAt(row, 0);
        Event ev = eventService.getEventById(id);
        if (ev == null)
            return;

        ev.setName(nameField.getText().trim());
        ev.setLocation(locationField.getText().trim());
        ev.setDate(dateField.getText().trim());

        eventService.saveEvents();
        eventPanel.refreshTable();

        eventService.notifyListeners();
    }

    private void deleteEvent() {
        if (eventPanel == null)
            return;
        int row = eventPanel.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an event to delete.");
            return;
        }
        String id = (String) eventPanel.getTable().getValueAt(row, 0);
        eventService.deleteEvent(id);
        eventService.saveEvents();
        eventPanel.refreshTable();

        eventService.notifyListeners();
    }

    private void resetFields() {
        nameField.setText("");
        locationField.setText("");
        dateField.setText("");
        eventPanel.getTable().clearSelection(); // remove row highlight
    }

    private void fillFieldsFromSelectedRow(int row) {
        Event ev = eventService.getAllEvents().get(row);
        nameField.setText(ev.getName());
        locationField.setText(ev.getLocation());
        dateField.setText(ev.getDate());
    }

    public void setEventPanel(EventPanel ep) {
        this.eventPanel = ep; // link panel for updates
    }

    public void setRegistrationForm(RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }
}