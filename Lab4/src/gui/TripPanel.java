package gui;

import dao.TripOfferingDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Panel for managing trips and trip offerings
 */
public class TripPanel extends JPanel implements Refreshable {
    private JTable tripTable;
    private DefaultTableModel tableModel;
    private JTextField startLocationField, destinationField, dateField;
    private JTextField searchDriverField, searchBusIDField, searchTripNumberField;
    private JTextField searchStartTimeField, searchDriverIDField;
    private JTextField tripNumberField, tripDateField, startTimeField, arrivalTimeField;
    private JTextField tripStartLocationField, tripDestinationField;
    private JTextField driverIDField, driverNameField, busIDField;
    
    public TripPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Trip Schedule Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Center: Table
        String[] columns = {"Trip#", "Start", "Destination", "Date", "Start Time", 
                           "Arrival Time", "Driver ID", "Driver", "Phone", "Bus ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tripTable = new JTable(tableModel);
        tripTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tripTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tripTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        
        // Right panel: Search and Add forms
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        // Search Panel
        JPanel searchPanel = createSearchPanel();
        rightPanel.add(searchPanel);
        rightPanel.add(Box.createVerticalStrut(20));
        
        // Add Trip Panel
        JPanel addPanel = createAddTripPanel();
        rightPanel.add(addPanel);
        rightPanel.add(Box.createVerticalStrut(20));
        
        // Action Buttons Panel
        JPanel actionPanel = createActionPanel();
        rightPanel.add(actionPanel);
        
        // Wrap right panel in scroll pane
        JScrollPane rightScrollPane = new JScrollPane(rightPanel);
        rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, rightScrollPane);
        splitPane.setDividerLocation(550);
        add(splitPane, BorderLayout.CENTER);
        
        // Load initial data
        refresh();
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Search Trips (Enter any combination)"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        searchTripNumberField = new JTextField(15);
        startLocationField = new JTextField(15);
        destinationField = new JTextField(15);
        dateField = new JTextField(15);
        searchStartTimeField = new JTextField(15);
        searchDriverIDField = new JTextField(15);
        searchDriverField = new JTextField(15);
        searchBusIDField = new JTextField(15);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Trip#:"), gbc);
        gbc.gridx = 1;
        panel.add(searchTripNumberField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Start Location:"), gbc);
        gbc.gridx = 1;
        panel.add(startLocationField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Destination:"), gbc);
        gbc.gridx = 1;
        panel.add(destinationField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Start Time (HH:MM:SS):"), gbc);
        gbc.gridx = 1;
        panel.add(searchStartTimeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Driver ID:"), gbc);
        gbc.gridx = 1;
        panel.add(searchDriverIDField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Driver Name:"), gbc);
        gbc.gridx = 1;
        panel.add(searchDriverField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Bus ID:"), gbc);
        gbc.gridx = 1;
        panel.add(searchBusIDField, gbc);
        
        JButton searchBtn = new JButton("Search");
        JButton showAllBtn = new JButton("Show All");
        JButton clearBtn = new JButton("Clear");
        
        searchBtn.addActionListener(e -> searchTrips());
        showAllBtn.addActionListener(e -> refresh());
        clearBtn.addActionListener(e -> clearSearchFields());
        
        gbc.gridx = 0; gbc.gridy = 8;
        panel.add(searchBtn, gbc);
        gbc.gridx = 1;
        panel.add(showAllBtn, gbc);
        
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        panel.add(clearBtn, gbc);
        
        return panel;
    }
    
    private JPanel createAddTripPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Add Trip Offering"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        tripNumberField = new JTextField(15);
        tripStartLocationField = new JTextField(15);
        tripDestinationField = new JTextField(15);
        tripDateField = new JTextField(15);
        startTimeField = new JTextField(15);
        arrivalTimeField = new JTextField(15);
        driverIDField = new JTextField(15);
        driverNameField = new JTextField(15);
        busIDField = new JTextField(15);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Trip#:"), gbc);
        gbc.gridx = 1;
        panel.add(tripNumberField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Start Location:"), gbc);
        gbc.gridx = 1;
        panel.add(tripStartLocationField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Destination:"), gbc);
        gbc.gridx = 1;
        panel.add(tripDestinationField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        panel.add(tripDateField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Start Time:"), gbc);
        gbc.gridx = 1;
        panel.add(startTimeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Arrival Time:"), gbc);
        gbc.gridx = 1;
        panel.add(arrivalTimeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Driver ID (optional):"), gbc);
        gbc.gridx = 1;
        panel.add(driverIDField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Driver Name (optional):"), gbc);
        gbc.gridx = 1;
        panel.add(driverNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8;
        panel.add(new JLabel("Bus ID:"), gbc);
        gbc.gridx = 1;
        panel.add(busIDField, gbc);
        
        JButton addBtn = new JButton("Add Trip");
        addBtn.addActionListener(e -> addTrip());
        
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        panel.add(addBtn, gbc);
        
        return panel;
    }
    
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Trip Actions"));
        
        JButton deleteBtn = new JButton("Delete Trip");
        JButton changeDriverBtn = new JButton("Change Driver");
        JButton changeBusBtn = new JButton("Change Bus");
        
        deleteBtn.addActionListener(e -> deleteTrip());
        changeDriverBtn.addActionListener(e -> changeDriver());
        changeBusBtn.addActionListener(e -> changeBus());
        
        panel.add(deleteBtn);
        panel.add(changeDriverBtn);
        panel.add(changeBusBtn);
        
        return panel;
    }
    
    private void searchTrips() {
        try {
            String tripNum = searchTripNumberField.getText().trim();
            String start = startLocationField.getText().trim();
            String dest = destinationField.getText().trim();
            String date = dateField.getText().trim();
            String startTime = searchStartTimeField.getText().trim();
            String driverID = searchDriverIDField.getText().trim();
            String driver = searchDriverField.getText().trim();
            String busID = searchBusIDField.getText().trim();
            
            List<Object[]> trips = TripOfferingDAO.searchTripOfferings(
                tripNum, start, dest, date, startTime, driverID, driver, busID);
            updateTable(trips);
            
            if (trips.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No trips found matching the criteria");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for Trip#, Driver ID, or Bus ID",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error searching trips: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearSearchFields() {
        searchTripNumberField.setText("");
        startLocationField.setText("");
        destinationField.setText("");
        dateField.setText("");
        searchStartTimeField.setText("");
        searchDriverIDField.setText("");
        searchDriverField.setText("");
        searchBusIDField.setText("");
    }
    
    private void addTrip() {
        try {
            int tripNum = Integer.parseInt(tripNumberField.getText().trim());
            String tripStartLoc = tripStartLocationField.getText().trim();
            String tripDest = tripDestinationField.getText().trim();
            String date = tripDateField.getText().trim();
            String startTime = startTimeField.getText().trim();
            String arrivalTime = arrivalTimeField.getText().trim();
            String driverIDStr = driverIDField.getText().trim();
            String driverName = driverNameField.getText().trim();
            int busID = Integer.parseInt(busIDField.getText().trim());
            
            // Auto-create Trip if it doesn't exist
            if (!dao.TripDAO.tripExists(tripNum)) {
                // Validate that Start Location and Destination are provided
                if (tripStartLoc.isEmpty() || tripDest.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Trip #" + tripNum + " does not exist!\n" +
                        "Please provide Start Location and Destination to create it.",
                        "Missing Trip Information", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create the new Trip
                if (dao.TripDAO.addTrip(tripNum, tripStartLoc, tripDest)) {
                    JOptionPane.showMessageDialog(this, 
                        "Trip #" + tripNum + " created: " + tripStartLoc + " → " + tripDest,
                        "Trip Created", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to create Trip #" + tripNum,
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Validate that at least one driver identifier is provided
            if (driverIDStr.isEmpty() && driverName.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please provide either Driver ID or Driver Name",
                    "Missing Driver Information", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // If Driver ID is provided, use it; otherwise use Driver Name
            String finalDriverName = driverName;
            if (!driverIDStr.isEmpty()) {
                // Look up driver name from driver ID
                int driverID = Integer.parseInt(driverIDStr);
                finalDriverName = dao.DriverDAO.getDriverNameByID(driverID);
                if (finalDriverName == null) {
                    JOptionPane.showMessageDialog(this, 
                        "Driver ID " + driverID + " does not exist!",
                        "Invalid Driver ID", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            if (TripOfferingDAO.addTripOffering(tripNum, date, startTime, arrivalTime, finalDriverName, busID)) {
                clearAddFields();
                refresh();
                JOptionPane.showMessageDialog(this, "Trip offering added successfully!\nThe table has been refreshed.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add trip offering",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format for Trip#, Driver ID, or Bus ID",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteTrip() {
        int selectedRow = tripTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a trip to delete");
            return;
        }
        
        try {
            int tripNum = (int) tableModel.getValueAt(selectedRow, 0);
            String date = tableModel.getValueAt(selectedRow, 3).toString();
            String startTime = tableModel.getValueAt(selectedRow, 4).toString();
            
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete trip #" + tripNum + " on " + date + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (TripOfferingDAO.deleteTripOffering(tripNum, date, startTime)) {
                    JOptionPane.showMessageDialog(this, "Trip deleted successfully!");
                    refresh();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete trip",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void changeDriver() {
        int selectedRow = tripTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a trip");
            return;
        }
        
        // Create custom dialog with Driver ID and Driver Name fields
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField driverIDField = new JTextField(10);
        JTextField driverNameField = new JTextField(10);
        
        panel.add(new JLabel("Driver ID (optional):"));
        panel.add(driverIDField);
        panel.add(new JLabel("Driver Name (optional):"));
        panel.add(driverNameField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
                "Change Driver - Enter Driver ID or Name", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String driverIDStr = driverIDField.getText().trim();
                String driverName = driverNameField.getText().trim();
                
                // Validate that at least one is provided
                if (driverIDStr.isEmpty() && driverName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Please provide either Driver ID or Driver Name",
                        "Missing Information", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
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
                
                int tripNum = (int) tableModel.getValueAt(selectedRow, 0);
                String date = tableModel.getValueAt(selectedRow, 3).toString();
                String startTime = tableModel.getValueAt(selectedRow, 4).toString();
                
                if (TripOfferingDAO.changeDriver(tripNum, date, startTime, finalDriverName)) {
                    JOptionPane.showMessageDialog(this, "Driver changed successfully!");
                    refresh();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to change driver",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Driver ID format",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void changeBus() {
        int selectedRow = tripTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a trip");
            return;
        }
        
        String newBusStr = JOptionPane.showInputDialog(this, "Enter new bus ID:");
        if (newBusStr != null && !newBusStr.trim().isEmpty()) {
            try {
                int newBusID = Integer.parseInt(newBusStr.trim());
                int tripNum = (int) tableModel.getValueAt(selectedRow, 0);
                String date = tableModel.getValueAt(selectedRow, 3).toString();
                String startTime = tableModel.getValueAt(selectedRow, 4).toString();
                
                if (TripOfferingDAO.changeBus(tripNum, date, startTime, newBusID)) {
                    JOptionPane.showMessageDialog(this, "Bus changed successfully!");
                    refresh();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to change bus",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid bus ID",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateTable(List<Object[]> data) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }
    
    private void clearAddFields() {
        tripNumberField.setText("");
        tripStartLocationField.setText("");
        tripDestinationField.setText("");
        tripDateField.setText("");
        startTimeField.setText("");
        arrivalTimeField.setText("");
        driverIDField.setText("");
        driverNameField.setText("");
        busIDField.setText("");
    }
    
    @Override
    public void refresh() {
        try {
            List<Object[]> trips = TripOfferingDAO.getAllTripOfferings();
            updateTable(trips);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading trips: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
