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


        JLabel label = new JLabel("Click the button");
        JButton button = new JButton("Click Me");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText("Button clicked!");
            }
        });

        frame.add(label);
        frame.add(button);

        frame.setVisible(true);
    }
}
