package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Home panel with welcome message
 */
public class HomePanel extends JPanel implements Refreshable {
    
    public HomePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Pomona Transit Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(41, 128, 185));
        
        JLabel subtitleLabel = new JLabel("CS4350 Database Systems - Lab #4");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.GRAY);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(subtitleLabel);
        
        centerPanel.add(textPanel);
        add(centerPanel, BorderLayout.CENTER);
        
        // Footer
        JLabel footerLabel = new JLabel("Use the sidebar to navigate between features");
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(Color.GRAY);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(footerLabel, BorderLayout.SOUTH);
    }
    
    @Override
    public void refresh() {
        // Nothing to refresh on home panel
    }
}
