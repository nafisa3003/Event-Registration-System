import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ParticipantTableModel extends AbstractTableModel {
    private List<Participant> data;
    private final String[] columns = { "ID", "Name", "Email", "Phone", "Gender", "Date of Birth" };

    public ParticipantTableModel(List<Participant> data) {
        this.data = data;
    }

    public void setData(List<Participant> data) {
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
        Participant p = data.get(row);
        return switch (col) {
            case 0 -> p.getId();
            case 1 -> p.getName();
            case 2 -> p.getEmail();
            case 3 -> p.getPhone();
            case 4 -> p.getGender();
            case 5 -> p.getDob();
            default -> null;
        };
    }
}