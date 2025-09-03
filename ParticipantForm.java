import java.awt.*;
import java.util.regex.Pattern;
import javax.swing.*;

public class ParticipantForm extends JPanel {
    private ParticipantService participantService;
    private ParticipantPanel participantPanel;

    private JTextField idField, nameField, emailField, phoneField;
    private JComboBox<String> genderCb;
    private JTextField dobField;

    private JButton addBtn, updateBtn, deleteBtn, clearBtn;

    public ParticipantForm(ParticipantService participantService, ParticipantPanel participantPanel) {
        this.participantService = participantService;
        this.participantPanel = participantPanel;

        setLayout(new GridLayout(2, 1, 10, 10));
        setBackground(Utils.WHITE);

        JPanel fieldsPanel = new JPanel(new GridLayout(2, 6, 10, 10));
        fieldsPanel.setBackground(Utils.WHITE);

        idField = new JTextField();
        idField.setEditable(false); // auto-generated
        nameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        dobField = new JTextField();
        genderCb = new JComboBox<>(new String[] { "Male", "Female", "Other" });

        fieldsPanel.add(new JLabel("ID:"));
        fieldsPanel.add(idField);
        fieldsPanel.add(new JLabel("Name:"));
        fieldsPanel.add(nameField);
        fieldsPanel.add(new JLabel("Email:"));
        fieldsPanel.add(emailField);
        fieldsPanel.add(new JLabel("Phone:"));
        fieldsPanel.add(phoneField);
        fieldsPanel.add(new JLabel("Gender:"));
        fieldsPanel.add(genderCb);
        fieldsPanel.add(new JLabel("DoB (YYYY-MM-DD):"));
        fieldsPanel.add(dobField);

        add(fieldsPanel);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttonsPanel.setBackground(Utils.WHITE);

        addBtn = Utils.createButton("Add", Utils.PRIMARY_BLUE);
        updateBtn = Utils.createButton("Update", Utils.DARK_NAVY);
        deleteBtn = Utils.createButton("Delete", Utils.RED);
        clearBtn = Utils.createButton("Clear", Utils.GRAY);

        Utils.addHoverEffect(addBtn, Utils.PRIMARY_BLUE);
        Utils.addHoverEffect(updateBtn, Utils.DARK_NAVY);
        Utils.addHoverEffect(deleteBtn, Utils.RED);
        Utils.addHoverEffect(clearBtn, Utils.GRAY);

        buttonsPanel.add(addBtn);
        buttonsPanel.add(updateBtn);
        buttonsPanel.add(deleteBtn);
        buttonsPanel.add(clearBtn);

        add(buttonsPanel);

        // ACTIONS

        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String gender = (String) genderCb.getSelectedItem();
            String dob = dobField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || dob.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Pattern.matches("\\d{11}", phone)) {
                JOptionPane.showMessageDialog(this, "Phone must be 11 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String id = participantService.generateId();
            Participant p = new Participant(id, name, email, phone, gender, dob);

            participantService.addParticipant(p);
            participantPanel.refreshTable();
            JOptionPane.showMessageDialog(this, "Participant added successfully!");
            clearFields();
        });

        updateBtn.addActionListener(e -> {
            int selected = participantPanel.getTable().getSelectedRow();
            if (selected == -1) {
                JOptionPane.showMessageDialog(this, "Select a participant to update.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            selected = participantPanel.getTable().convertRowIndexToModel(selected);
            String id = (String) participantPanel.getTable().getModel().getValueAt(selected, 0);

            // get updated values from text fields
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String gender = (String) genderCb.getSelectedItem();
            String dob = dobField.getText();

            participantService.updateParticipant(id, name, email, phone, gender, dob);
            participantPanel.refreshTable();
            JOptionPane.showMessageDialog(this, "Updated successfully!");
        });

        deleteBtn.addActionListener(e ->

        {
            int selected = participantPanel.getTable().getSelectedRow();
            if (selected == -1) {
                JOptionPane.showMessageDialog(this, "Select a participant to delete.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            selected = participantPanel.getTable().convertRowIndexToModel(selected); // important if table sorting is
                                                                                     // enabled
            String id = (String) participantPanel.getTable().getModel().getValueAt(selected, 0); // column 0 = ID
            participantService.deleteParticipant(id);
            participantPanel.refreshTable();
            JOptionPane.showMessageDialog(this, "Deleted successfully!");
            clearFields();
        });

        clearBtn.addActionListener(e -> {
            clearFields();
        });

        participantPanel.getTable().getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int row = participantPanel.getTable().getSelectedRow();
                if (row != -1) {
                    row = participantPanel.getTable().convertRowIndexToModel(row); // handle sorting
                    fillFieldsFromSelectedRow(row);
                }
            }
        });
    }

    // ===== Methods =====
    private void addParticipant() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String gender = (String) genderCb.getSelectedItem();
        String dob = dobField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || dob.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Pattern.matches("\\d{11}", phone)) {
            JOptionPane.showMessageDialog(this, "Phone must be 11 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = participantService.generateId();
        Participant p = new Participant(id, name, email, phone, gender, dob);

        participantService.addParticipant(p);
        participantPanel.refreshTable();
        JOptionPane.showMessageDialog(this, "Participant added successfully!");
        clearFields();
    }

    private void updateParticipant() {
        int selected = participantPanel.getTable().getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Select a participant to update.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        selected = participantPanel.getTable().convertRowIndexToModel(selected);
        String id = (String) participantPanel.getTable().getModel().getValueAt(selected, 0);

        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String gender = (String) genderCb.getSelectedItem();
        String dob = dobField.getText();

        participantService.updateParticipant(id, name, email, phone, gender, dob);
        participantPanel.refreshTable();
        JOptionPane.showMessageDialog(this, "Updated successfully!");
    }

    private void deleteParticipant() {
        int selected = participantPanel.getTable().getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Select a participant to delete.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        selected = participantPanel.getTable().convertRowIndexToModel(selected);
        String id = (String) participantPanel.getTable().getModel().getValueAt(selected, 0);
        participantService.deleteParticipant(id);
        participantPanel.refreshTable();
        JOptionPane.showMessageDialog(this, "Deleted successfully!");

        clearFields();
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        dobField.setText("");
        genderCb.setSelectedIndex(0);
        participantPanel.getTable().clearSelection(); // clear selection
    }

    private void fillFieldsFromSelectedRow(int row) {
        Participant p = participantService.getAllParticipants().get(row);
        idField.setText(p.getId());
        nameField.setText(p.getName());
        emailField.setText(p.getEmail());
        phoneField.setText(p.getPhone());
        dobField.setText(p.getDob());
        genderCb.setSelectedItem(p.getGender());
    }
}