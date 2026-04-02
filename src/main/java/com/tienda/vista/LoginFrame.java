package com.tienda.vista;

import com.tienda.AppContext;
import com.tienda.modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Login");
        setSize(300,200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3,2));

        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JButton btnLogin = new JButton("Ingresar");

        panel.add(new JLabel("Usuario"));
        panel.add(txtUser);
        panel.add(new JLabel("Contraseña"));
        panel.add(txtPass);
        panel.add(btnLogin);

        add(panel);

        btnLogin.addActionListener(e->{
            Usuario u = AppContext.auth.login(
                    txtUser.getText(),
                    new String(txtPass.getPassword())
            );

            if(u==null){
                JOptionPane.showMessageDialog(this,"Error");
            }else{
                new MenuFrame(u).setVisible(true);
                dispose();
            }
        });
    }
}
