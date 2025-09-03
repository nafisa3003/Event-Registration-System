import java.util.List;
import javax.swing.table.AbstractTableModel;

public class RegistrationTableModel extends AbstractTableModel {
    private List<Registration> data;
    private ParticipantService participantService;
    private EventService eventService;

    private final String[] columns = {
            "Reg ID", "Participant ID", "Name", "Email", "Phone", "Gender", "DoB", "Event ID", "Event Name"
    };

    // Updated constructor
    public RegistrationTableModel(List<Registration> data,
            ParticipantService participantService,
            EventService eventService) {
        this.data = data;
        this.participantService = participantService;
        this.eventService = eventService;
    }

    public void setData(List<Registration> data) {
        this.data = data;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Registration r = data.get(row);
        Participant p = participantService.getParticipantById(r.getParticipantId());

        return switch (col) {
            case 0 -> r.getId();
            case 1 -> r.getParticipantId();
            case 2 -> p != null ? p.getName() : "";
            case 3 -> p != null ? p.getEmail() : "";
            case 4 -> p != null ? p.getPhone() : "";
            case 5 -> p != null ? p.getGender() : "";
            case 6 -> p != null ? p.getDob() : "";
            case 7 -> r.getEventId();
            case 8 -> r.getEventName();
            default -> null;
        };
    }
}