package org.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game {
    public static void start() {
        SwingUtilities.invokeLater(Game::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Multiplayer Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());


        JLabel tryMyLabel = new JLabel("Click the button");
        JButton tryMeButton = new JButton("Click Me");
        JButton hostServerButton = new JButton("Host a Server");
        JLabel statusLabel = new JLabel("Status: game started.");

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
                try {
                    PlayerServer.start();
                } finally {
                    statusLabel.setText("Status: Server started. Waiting for players...");
                }
            }
        });

        frame.add(tryMyLabel);
        frame.add(tryMeButton);
        frame.add(hostServerButton);
        frame.add(statusLabel);

        frame.setVisible(true);
    }
}
