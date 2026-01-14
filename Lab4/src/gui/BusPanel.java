package gui;

import dao.BusDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for managing buses
 */
public class BusPanel extends JPanel implements Refreshable {
    private JTable busTable;
    private DefaultTableModel tableModel;
    private JTextField busIDField, modelField, yearField;
    
    public BusPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Bus Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Center: Table
        String[] columns = {"Bus ID", "Model", "Year"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        busTable = new JTable(tableModel);
        busTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(busTable);
        
        // Right panel: Forms
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        // Add Bus Panel
        JPanel addPanel = createAddBusPanel();
        rightPanel.add(addPanel);
        rightPanel.add(Box.createVerticalStrut(20));
        
        // Delete Bus Panel
        JPanel deletePanel = createDeleteBusPanel();
        rightPanel.add(deletePanel);
        
        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, rightPanel);
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);
        
        // Load initial data
        refresh();
    }
    
    private JPanel createAddBusPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Add Bus"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        busIDField = new JTextField(20);
        modelField = new JTextField(20);
        yearField = new JTextField(20);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Bus ID:"), gbc);
        gbc.gridx = 1;
        panel.add(busIDField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Model:"), gbc);
        gbc.gridx = 1;
        panel.add(modelField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 1;
        panel.add(yearField, gbc);
        
        JButton addBtn = new JButton("Add Bus");
        addBtn.addActionListener(e -> addBus());
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(addBtn, gbc);
        
        return panel;
    }
    
    private JPanel createDeleteBusPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Delete Bus"));
        
        JButton deleteBtn = new JButton("Delete Selected Bus");
        deleteBtn.addActionListener(e -> deleteBus());
        panel.add(deleteBtn);
        
        return panel;
    }
    
    private void addBus() {
        try {
            int busID = Integer.parseInt(busIDField.getText().trim());
            String model = modelField.getText().trim();
            int year = Integer.parseInt(yearField.getText().trim());
            
            if (model.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
                return;
            }
            
            if (BusDAO.addBus(busID, model, year)) {
                JOptionPane.showMessageDialog(this, "Bus added successfully!");
                busIDField.setText("");
                modelField.setText("");
                yearField.setText("");
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add bus",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteBus() {
        int selectedRow = busTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a bus to delete");
            return;
        }
        
        try {
            int busID = (int) tableModel.getValueAt(selectedRow, 0);
            
            // Check if bus is being used by any trips
            List<Object[]> tripsUsingBus = BusDAO.getTripsUsingBus(busID);
            
            if (!tripsUsingBus.isEmpty()) {
                // Build message showing which trips use this bus
                StringBuilder message = new StringBuilder();
                message.append("Bus #").append(busID).append(" is currently assigned to ")
                       .append(tripsUsingBus.size()).append(" trip(s):\n\n");
                
                int count = 0;
                for (Object[] trip : tripsUsingBus) {
                    if (count < 5) { // Show first 5 trips
                        message.append("• Trip #").append(trip[0])
                               .append(" (").append(trip[1]).append(" → ").append(trip[2]).append(")")
                               .append(" on ").append(trip[3])
                               .append(" at ").append(trip[4]).append("\n");
                        count++;
                    }
                }
                
                if (tripsUsingBus.size() > 5) {
                    message.append("... and ").append(tripsUsingBus.size() - 5).append(" more trip(s)\n");
                }
                
                message.append("\nTo delete this bus, please select a replacement bus\n");
                message.append("to reassign all these trips.");
                
                // Get list of available buses (excluding the one being deleted)
                List<Object[]> allBuses = BusDAO.getAllBuses();
                List<String> availableBuses = new ArrayList<>();
                for (Object[] bus : allBuses) {
                    int id = (int) bus[0];
                    if (id != busID) {
                        String model = (String) bus[1];
                        int year = (int) bus[2];
                        availableBuses.add(id + " - " + model + " (" + year + ")");
                    }
                }
                
                if (availableBuses.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Cannot delete Bus #" + busID + "!\n\n" +
                        "This is the only bus in the system.\n" +
                        "Please add another bus before deleting this one.",
                        "Cannot Delete", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Show selection dialog
                String selected = (String) JOptionPane.showInputDialog(this,
                        message.toString(),
                        "Select Replacement Bus",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        availableBuses.toArray(),
                        availableBuses.get(0));
                
                if (selected != null) {
                    // Extract bus ID from selection (format: "ID - Model (Year)")
                    int replacementBusID = Integer.parseInt(selected.split(" - ")[0]);
                    
                    // Reassign all trips to the new bus
                    if (BusDAO.reassignTripsToNewBus(busID, replacementBusID)) {
                        // Now delete the bus
                        if (BusDAO.deleteBus(busID)) {
                            JOptionPane.showMessageDialog(this, 
                                "Bus #" + busID + " deleted successfully!\n" +
                                tripsUsingBus.size() + " trip(s) reassigned to Bus #" + replacementBusID);
                            refresh();
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to delete bus",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to reassign trips",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                // No trips using this bus, delete directly
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Delete bus #" + busID + "?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    if (BusDAO.deleteBus(busID)) {
                        JOptionPane.showMessageDialog(this, "Bus #" + busID + " deleted successfully!");
                        refresh();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete bus",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid bus ID format",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void refresh() {
        try {
            List<Object[]> buses = BusDAO.getAllBuses();
            tableModel.setRowCount(0);
            for (Object[] row : buses) {
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading buses: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
