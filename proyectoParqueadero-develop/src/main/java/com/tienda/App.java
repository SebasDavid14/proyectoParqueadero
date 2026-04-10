package com.tienda;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import com.tienda.vista.LoginFrame;

public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(AppContext::guardarDatos));

        new LoginFrame().setVisible(true);
    }
}
