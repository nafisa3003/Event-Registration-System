import java.util.List;
import javax.swing.table.AbstractTableModel;

public class EventTableModel extends AbstractTableModel {
    private List<Event> data;
    private final String[] columns = { "ID", "Name", "Date", "Location" };

    public EventTableModel(List<Event> data) {
        this.data = data;
    }

    public void setData(List<Event> data) {
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
        Event e = data.get(row);
        return switch (col) {
            case 0 -> e.getId();
            case 1 -> e.getName();
            case 2 -> e.getLocation();
            case 3 -> e.getDate();
            default -> null;
        };
    }
}