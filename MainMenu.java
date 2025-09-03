import java.awt.*;
import javax.swing.*;

public class MainMenu extends JFrame {

    private EventService eventService;
    private ParticipantService participantService;
    private RegistrationService regService;

    private EventPanel eventPanel;
    private ParticipantPanel participantPanel;
    private RegistrationPanel regPanel;

    private EventForm eventForm;
    private ParticipantForm participantForm;
    private RegistrationForm registrationForm;

    public MainMenu() {
        super("Event Registration System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Services
        eventService = new EventService();
        participantService = new ParticipantService();
        regService = new RegistrationService();

        eventService.loadEvents();
        participantService.loadParticipants();
        regService.loadRegistrations();

        // Header
        add(createHeader(), BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(Utils.TAB_FONT);

        // Forms & Panels
        eventPanel = new EventPanel(eventService);
        eventForm = new EventForm(eventService, eventPanel);
        eventForm.setEventPanel(eventPanel);

        participantPanel = new ParticipantPanel(participantService);
        regPanel = new RegistrationPanel(regService, participantService, eventService);

        participantForm = new ParticipantForm(participantService, participantPanel);
        registrationForm = new RegistrationForm(regService, regPanel, eventService, participantService);

        // Compose tabs
        tabs.addTab("Events", composeTab(eventPanel, eventForm));
        tabs.addTab("Participants", composeTab(participantPanel, participantForm));
        tabs.addTab("Registrations", composeTab(regPanel, registrationForm));

        // Refresh tables on tab change
        tabs.addChangeListener(e -> refreshAllTables());

        add(tabs, BorderLayout.CENTER);
        setVisible(true);
    }

    // Header Panel
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Utils.DARK_NAVY);
        header.setPreferredSize(new Dimension(0, 100));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        left.setOpaque(false);
        try {
            ImageIcon icon = new ImageIcon("event.jpg");
            Image img = icon.getImage().getScaledInstance(72, 72, Image.SCALE_SMOOTH);
            JLabel logo = new JLabel(new ImageIcon(img));
            left.add(logo);
        } catch (Exception ignored) {
        }

        JLabel center = new JLabel("Event Registration System", SwingConstants.CENTER);
        center.setForeground(Utils.WHITE);
        center.setFont(new Font("Roboto", Font.BOLD, 26));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 36));
        right.setOpaque(false);
        JButton about = Utils.createButton("About", Utils.ACCENT_LIGHT_BLUE);
        Utils.addHoverEffect(about, Utils.ACCENT_LIGHT_BLUE);
        about.addActionListener(e -> showAbout());
        right.add(about);

        header.add(left, BorderLayout.WEST);
        header.add(center, BorderLayout.CENTER);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel composeTab(JComponent tablePanel, JComponent formPanel) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Utils.WHITE);
        p.add(tablePanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        bottom.setBackground(Utils.WHITE);
        bottom.add(formPanel, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }

    private void refreshAllTables() {
        eventPanel.refreshTable();
        participantPanel.refreshTable();
        regPanel.refreshTable();
    }

    private void showAbout() {
        String msg = """
                     <html><h2 style='color:#0A1946;'>Event Registration System</h2></html>
                     Manage events & participants easily.
                     Features:
                     - Add, update, delete events & participants
                     - Register participants to events
                     - Search & sort tables
                     - Persistent data using .ser files
                     """;

        ImageIcon icon = new ImageIcon("event.jpg");
        Image scaled = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaled);

        JOptionPane.showMessageDialog(
                this,
                msg,
                "About",
                JOptionPane.PLAIN_MESSAGE,
                scaledIcon);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }
}