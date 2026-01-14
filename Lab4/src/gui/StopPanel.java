package gui;

import dao.TripStopInfoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Panel for viewing trip stops
 */
public class StopPanel extends JPanel implements Refreshable {
    private JTable stopTable;
    private DefaultTableModel tableModel;
    private JTextField tripNumberField;
    
    public StopPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Trip Stops Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Center: Table
        String[] columns = {"Stop#", "Address", "Sequence#", "Driving Time (min)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        stopTable = new JTable(tableModel);
        stopTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(stopTable);
        
        // Right panel: Search form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        JPanel searchPanel = createSearchPanel();
        rightPanel.add(searchPanel);
        
        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, rightPanel);
        splitPane.setDividerLocation(500);
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("View Trip Stops"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        tripNumberField = new JTextField(20);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Trip#:"), gbc);
        gbc.gridx = 1;
        panel.add(tripNumberField, gbc);
        
        JButton showBtn = new JButton("Show Trip Stops");
        showBtn.addActionListener(e -> showTripStops());
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(showBtn, gbc);
        
        return panel;
    }
    
    private void showTripStops() {
        try {
            int tripNumber = Integer.parseInt(tripNumberField.getText().trim());
            
            List<Object[]> stops = TripStopInfoDAO.getTripStops(tripNumber);
            
            if (stops.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No stops found for this trip");
            }
            
            tableModel.setRowCount(0);
            for (Object[] row : stops) {
                tableModel.addRow(row);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid trip number",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void refresh() {
        tableModel.setRowCount(0);
        tripNumberField.setText("");
    }
}
