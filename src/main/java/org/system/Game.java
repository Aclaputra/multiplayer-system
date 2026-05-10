package org.system;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game {
    private static JPanel mainPanel;
    private static CardLayout cardLayout;
    public static void start() {
        SwingUtilities.invokeLater(Game::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Multiplayer Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
//        frame.setLayout(new FlowLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // main menu
        JPanel menuPanel = new JPanel(new FlowLayout());
        JLabel tryMyLabel = new JLabel("Click the button");
        JButton tryMeButton = new JButton("Click Me");
        JButton hostServerButton = new JButton("Host a Server");
        JLabel statusLabel = new JLabel("Status: game started.");
        JButton joinServerButton = new JButton("Join Server");

        menuPanel.add(tryMyLabel);
        menuPanel.add(tryMeButton);
        menuPanel.add(hostServerButton);
        menuPanel.add(joinServerButton);
        menuPanel.add(statusLabel);

        // lobby
        JPanel lobbyPanel = new JPanel(new BorderLayout());
        JPanel buttonLobbyPanel = new JPanel();
        lobbyPanel.add(new JLabel("Welcome to the lobby!", SwingConstants.CENTER), BorderLayout.CENTER);
        JButton leaveButton = new JButton("Disconnect");
        JButton startGameButton = new JButton("Start Game");

        buttonLobbyPanel.add(startGameButton);
        buttonLobbyPanel.add(leaveButton);
        lobbyPanel.add(buttonLobbyPanel, BorderLayout.SOUTH);

        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(lobbyPanel, "LOBBY");

        tryMeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tryMyLabel.setText("Button clicked!");

                PlayerClient.sendMessage("PLAYER_CLICKED");
            }
        });
        hostServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("Status: Starting server...");
                hostServerButton.setEnabled(false); // Prevent double-clicking

                new Thread(() -> {
                    try {
                        PlayerServer.start();
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() -> {
                            statusLabel.setText("Status: Server failed to start.");
                            hostServerButton.setEnabled(true);
                        });
                    }
                }).start();
            }
        });
        joinServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("Connecting to server...");
                joinServerButton.setEnabled(false); // Prevent multiple connection attempts

                new Thread(() -> {
                    try {
                        PlayerClient.start();

                        SwingUtilities.invokeLater(() -> {
//                            statusLabel.setText("Status: Connected to Lobby!");
                            cardLayout.show(mainPanel, "LOBBY");
                            PlayerClient.start();
                        });

                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() -> {
                            statusLabel.setText("Connection failed: " + ex.getMessage());
                            joinServerButton.setEnabled(true);
                        });
                    }
                }).start();
            }
        });

        startGameButton.addActionListener(e -> {
            PlayerClient.sendMessage("Game Started!");
        });
        leaveButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "MENU");
            joinServerButton.setEnabled(true);
            hostServerButton.setEnabled(true);
            PlayerClient.stop();
        });

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
