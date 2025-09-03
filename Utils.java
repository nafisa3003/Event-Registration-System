import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

public class Utils {

    // COLORS
    public static final Color WHITE = Color.WHITE;
    public static final Color DARK_NAVY = new Color(10, 25, 70);
    public static final Color PRIMARY_BLUE = new Color(0, 123, 255);
    public static final Color ACCENT_LIGHT_BLUE = new Color(100, 149, 237);
    public static final Color RED = new Color(220, 53, 69);
    public static final Color GRAY = new Color(128, 128, 128);

    // FONTS
    public static final Font TAB_FONT = new Font("Roboto", Font.PLAIN, 18);

    // BUTTON HELPERS
    public static JButton createButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Roboto", Font.BOLD, 16));
        return b;
    }

    public static void addHoverEffect(JButton button, Color hoverColor) {
        Color original = button.getBackground();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(original);
            }
        });
    }

    // SEARCH LISTENER
    public static DocumentListener createSearchListener(JTextField field, JTable table) {
        return new DocumentListener() {

            private void filter() {
                String text = field.getText().trim();
                TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) table.getRowSorter();
                if (sorter == null) {
                    sorter = new TableRowSorter<>(table.getModel());
                    table.setRowSorter(sorter);
                }
                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filter();
            }
        };
    }

    // UTILITY
    public static void showErrorMessage(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfoMessage(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}