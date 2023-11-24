/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLIENT.UI.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Shadow
 */
public class header extends JPanel{
    private int height,width;
    private JFrame frame;
    public header(int w,int h)
    {
        width = w;
        height = h;
        init();
    }
    public void init()
    {
        setLayout(null);
        setSize(width,height);
        setBackground(null);
        
        JLabel logo = new JLabel(new ImageIcon("./src/main/java/CLIENT/UI/img/test-passed-16.png"),JLabel.CENTER);
        logo.setBounds(new Rectangle(30,10, 25, 25));
        Font font = new Font("Segoe UI",Font.BOLD,15);
        JLabel name = new JLabel("QUẢN LÝ ĐỀ THI",JLabel.CENTER);
        name.setFont(font);
        name.setForeground(Color.white);
        name.setBounds(new Rectangle(60, 0, 280, 40));
        
        add(logo);
        add(name);
 
    }
}
