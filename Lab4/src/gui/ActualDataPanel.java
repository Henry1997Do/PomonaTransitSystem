package gui;

import dao.ActualTripStopInfoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Panel for recording actual trip data
 */
public class ActualDataPanel extends JPanel implements Refreshable {
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JTextField tripNumField, dateField, stopNumField;
    private JTextField schedStartField, schedArrivalField;
    private JTextField actualStartField, actualArrivalField;
    private JTextField passInField, passOutField;
    
    public ActualDataPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Actual Trip Data Recording");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Center: Table
        String[] columns = {"Trip#", "Date", "Sched Start", "Stop#", "Sched Arrival",
                           "Actual Start", "Actual Arrival", "Pass In", "Pass Out"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dataTable = new JTable(tableModel);
        dataTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        
        // Right panel: Add form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        
        JPanel addPanel = createAddPanel();
        rightPanel.add(addPanel);
        
        // Layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, rightPanel);
        splitPane.setDividerLocation(600);
        add(splitPane, BorderLayout.CENTER);
        
        // Load initial data
        refresh();
    }
    
    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Add Actual Trip Data"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        tripNumField = new JTextField(15);
        dateField = new JTextField(15);
        stopNumField = new JTextField(15);
        schedStartField = new JTextField(15);
        schedArrivalField = new JTextField(15);
        actualStartField = new JTextField(15);
        actualArrivalField = new JTextField(15);
        passInField = new JTextField(15);
        passOutField = new JTextField(15);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Trip#:"), gbc);
        gbc.gridx = 1;
        panel.add(tripNumField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        panel.add(dateField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Stop#:"), gbc);
        gbc.gridx = 1;
        panel.add(stopNumField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Scheduled Start:"), gbc);
        gbc.gridx = 1;
        panel.add(schedStartField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Scheduled Arrival:"), gbc);
        gbc.gridx = 1;
        panel.add(schedArrivalField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Actual Start:"), gbc);
        gbc.gridx = 1;
        panel.add(actualStartField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Actual Arrival:"), gbc);
        gbc.gridx = 1;
        panel.add(actualArrivalField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Passengers In:"), gbc);
        gbc.gridx = 1;
        panel.add(passInField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8;
        panel.add(new JLabel("Passengers Out:"), gbc);
        gbc.gridx = 1;
        panel.add(passOutField, gbc);
        
        JButton addBtn = new JButton("Add Record");
        addBtn.addActionListener(e -> addRecord());
        
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        panel.add(addBtn, gbc);
        
        return panel;
    }
    
    private void addRecord() {
        try {
            int tripNum = Integer.parseInt(tripNumField.getText().trim());
            String date = dateField.getText().trim();
            int stopNum = Integer.parseInt(stopNumField.getText().trim());
            String schedStart = schedStartField.getText().trim();
            String schedArrival = schedArrivalField.getText().trim();
            String actualStart = actualStartField.getText().trim();
            String actualArrival = actualArrivalField.getText().trim();
            int passIn = Integer.parseInt(passInField.getText().trim());
            int passOut = Integer.parseInt(passOutField.getText().trim());
            
            if (ActualTripStopInfoDAO.addActualTripStopInfo(tripNum, date, schedStart, stopNum,
                    schedArrival, actualStart, actualArrival, passIn, passOut)) {
                JOptionPane.showMessageDialog(this, "Record added successfully!");
                clearFields();
                refresh();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add record",
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
    
    private void clearFields() {
        tripNumField.setText("");
        dateField.setText("");
        stopNumField.setText("");
        schedStartField.setText("");
        schedArrivalField.setText("");
        actualStartField.setText("");
        actualArrivalField.setText("");
        passInField.setText("");
        passOutField.setText("");
    }
    
    @Override
    public void refresh() {
        try {
            List<Object[]> records = ActualTripStopInfoDAO.getAllActualTripStopInfo();
            tableModel.setRowCount(0);
            for (Object[] row : records) {
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
