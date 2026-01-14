package gui;

import dao.DriverDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Panel for managing drivers
 */
public class DriverPanel extends JPanel implements Refreshable {
    private JTable driverTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, phoneField, searchDriverIDField, searchDriverField;
    
    public DriverPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Driver Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Center: Table
        String[] columns = {"Driver ID", "Driver Name", "Phone Number"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        driverTable = new JTable(tableModel);
        driverTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(driverTable);
        
        // Right panel: Forms
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        // Add Driver Panel
        JPanel addPanel = createAddDriverPanel();
        rightPanel.add(addPanel);
        rightPanel.add(Box.createVerticalStrut(20));
        
        // Weekly Schedule Panel
        JPanel schedulePanel = createWeeklySchedulePanel();
        rightPanel.add(schedulePanel);
        
        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, rightPanel);
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);
        
        // Load initial data
        refresh();
    }
    
    private JPanel createAddDriverPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Add Driver"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);
        
        JButton addBtn = new JButton("Add Driver");
        addBtn.addActionListener(e -> addDriver());
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(addBtn, gbc);
        
        return panel;
    }
    
    private JPanel createWeeklySchedulePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Weekly Schedule"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        searchDriverIDField = new JTextField(20);
        searchDriverField = new JTextField(20);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Driver ID (optional):"), gbc);
        gbc.gridx = 1;
        panel.add(searchDriverIDField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Driver Name (optional):"), gbc);
        gbc.gridx = 1;
        panel.add(searchDriverField, gbc);
        
        JButton scheduleBtn = new JButton("Weekly Schedule");
        scheduleBtn.addActionListener(e -> showWeeklySchedule());
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(scheduleBtn, gbc);
        
        return panel;
    }
    
    private void addDriver() {
        try {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            
            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
                return;
            }
            
            if (DriverDAO.addDriver(name, phone)) {
                JOptionPane.showMessageDialog(this, "Driver added successfully!");
                nameField.setText("");
                phoneField.setText("");
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add driver",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showWeeklySchedule() {
        String driverIDStr = searchDriverIDField.getText().trim();
        String driverName = searchDriverField.getText().trim();
        
        // Validate that at least one is provided
        if (driverIDStr.isEmpty() && driverName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter either Driver ID or Driver Name",
                "Missing Information", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // If Driver ID is provided, look up the driver name
            String finalDriverName = driverName;
            if (!driverIDStr.isEmpty()) {
                int driverID = Integer.parseInt(driverIDStr);
                finalDriverName = dao.DriverDAO.getDriverNameByID(driverID);
                if (finalDriverName == null) {
                    JOptionPane.showMessageDialog(this, 
                        "Driver ID " + driverID + " does not exist!",
                        "Invalid Driver ID", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            String startDate = JOptionPane.showInputDialog(this, 
                    "Enter start date (YYYY-MM-DD):");
            
            if (startDate != null && !startDate.trim().isEmpty()) {
                List<Object[]> schedule = DriverDAO.getWeeklySchedule(finalDriverName, startDate.trim());
                
                if (schedule.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No trips found for this driver");
                    return;
                }
                
                // Create dialog to show schedule
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                        "Weekly Schedule for " + finalDriverName, true);
                dialog.setLayout(new BorderLayout());
                
                String[] columns = {"Trip#", "Start", "Destination", "Date", 
                                   "Start Time", "Arrival Time", "Bus ID"};
                DefaultTableModel model = new DefaultTableModel(columns, 0);
                
                for (Object[] row : schedule) {
                    model.addRow(row);
                }
                
                JTable table = new JTable(model);
                table.setRowHeight(25);
                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setPreferredSize(new Dimension(700, 300));
                
                dialog.add(scrollPane, BorderLayout.CENTER);
                
                JButton closeBtn = new JButton("Close");
                closeBtn.addActionListener(e -> dialog.dispose());
                JPanel btnPanel = new JPanel();
                btnPanel.add(closeBtn);
                dialog.add(btnPanel, BorderLayout.SOUTH);
                
                dialog.pack();
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Driver ID format",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @Override
    public void refresh() {
        try {
            List<Object[]> drivers = DriverDAO.getAllDrivers();
            tableModel.setRowCount(0);
            for (Object[] row : drivers) {
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading drivers: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
