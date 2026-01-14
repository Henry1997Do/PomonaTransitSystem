package gui;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;

/**
 * Main application window for Pomona Transit Management System
 */
public class TransitApp extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private Refreshable currentPanel;
    
    public TransitApp() {
        setTitle("Pomona Transit Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        // Test database connection
        if (!DBConnection.testConnection()) {
            JOptionPane.showMessageDialog(this,
                    "Failed to connect to database. Please check your connection settings.",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        
        // Create main layout
        setLayout(new BorderLayout());
        
        // Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);
        
        // Content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Add panels
        HomePanel homePanel = new HomePanel();
        TripPanel tripPanel = new TripPanel();
        DriverPanel driverPanel = new DriverPanel();
        BusPanel busPanel = new BusPanel();
        StopPanel stopPanel = new StopPanel();
        ActualDataPanel actualDataPanel = new ActualDataPanel();
        
        contentPanel.add(homePanel, "Home");
        contentPanel.add(tripPanel, "Trips");
        contentPanel.add(driverPanel, "Drivers");
        contentPanel.add(busPanel, "Buses");
        contentPanel.add(stopPanel, "Stops");
        contentPanel.add(actualDataPanel, "ActualData");
        
        add(contentPanel, BorderLayout.CENTER);
        
        currentPanel = homePanel;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(52, 73, 94));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title
        JLabel titleLabel = new JLabel("Navigation");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(titleLabel);
        sidebar.add(Box.createVerticalStrut(20));
        
        // Menu buttons
        addMenuButton(sidebar, "Home", "Home");
        addMenuButton(sidebar, "Trips", "Trips");
        addMenuButton(sidebar, "Drivers", "Drivers");
        addMenuButton(sidebar, "Buses", "Buses");
        addMenuButton(sidebar, "Stops", "Stops");
        addMenuButton(sidebar, "Actual Data", "ActualData");
        
        sidebar.add(Box.createVerticalGlue());
        
        // Exit button
        JButton exitBtn = createStyledButton("Exit");
        exitBtn.setBackground(new Color(231, 76, 60));
        exitBtn.addActionListener(e -> {
            DBConnection.closeConnection();
            System.exit(0);
        });
        sidebar.add(exitBtn);
        
        return sidebar;
    }
    
    private void addMenuButton(JPanel sidebar, String label, String panelName) {
        JButton button = createStyledButton(label);
        button.addActionListener(e -> {
            cardLayout.show(contentPanel, panelName);
            // Refresh the panel when shown
            Component comp = getComponentByName(contentPanel, panelName);
            if (comp instanceof Refreshable) {
                currentPanel = (Refreshable) comp;
                currentPanel.refresh();
            }
        });
        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(5));
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!text.equals("Exit")) {
                    button.setBackground(new Color(41, 128, 185));
                } else {
                    button.setBackground(new Color(231, 76, 60));
                }
            }
        });
        
        return button;
    }
    
    private Component getComponentByName(Container container, String name) {
        for (Component comp : container.getComponents()) {
            if (name.equals(comp.getName())) {
                return comp;
            }
        }
        // Search by panel type
        for (Component comp : container.getComponents()) {
            if (name.equals("Home") && comp instanceof HomePanel) return comp;
            if (name.equals("Trips") && comp instanceof TripPanel) return comp;
            if (name.equals("Drivers") && comp instanceof DriverPanel) return comp;
            if (name.equals("Buses") && comp instanceof BusPanel) return comp;
            if (name.equals("Stops") && comp instanceof StopPanel) return comp;
            if (name.equals("ActualData") && comp instanceof ActualDataPanel) return comp;
        }
        return null;
    }
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show GUI
        SwingUtilities.invokeLater(() -> {
            TransitApp app = new TransitApp();
            app.setVisible(true);
        });
    }
}
