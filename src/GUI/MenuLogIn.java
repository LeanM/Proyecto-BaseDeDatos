package GUI;

import LogIn.LogIn;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuLogIn extends JFrame{

    private static MenuLogIn instance=null;
    private JButton Iniciar_Sesion;
    private JTextField user,password;
    private JLabel userTexto,passwordTexto;
    private JPanel contentPane;

    /**
     * Metodo que devuelve el menu ya que es unico en el juego.
     * Si este no esta creado lo crea sino devuelve el creado
     * @return Menu creado o existente
     */
    public static MenuLogIn getMenu(){
        if(instance==null){
            instance = new MenuLogIn();
        }
        return instance;
    }
    private MenuLogIn(){
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(600, 300, 800, 500);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.DARK_GRAY);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        Iniciar_Sesion =new JButton("Iniciar Sesion");
        Iniciar_Sesion.setBounds(330,250,120,30);

        user = new JTextField();
        user.setBounds(350,100,100,20);

        password = new JPasswordField();
        password.setBounds(350,150,100,20);

        userTexto = new JLabel("Usuario :");
        userTexto.setBounds(290,100,50,20);
        userTexto.setForeground(Color.WHITE);

        passwordTexto = new JLabel("Password :");
        passwordTexto.setBounds(280,150,75,20);
        passwordTexto.setForeground(Color.WHITE);

        contentPane.add(passwordTexto);
        contentPane.add(userTexto);
        contentPane.add(password);
        contentPane.add(user);
        contentPane.add(Iniciar_Sesion);

        Iniciar_Sesion.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            	
            	String nombreUser=user.getText();
            	
            	if(!(nombreUser.equals("admin") || nombreUser.equals("inspector"))) {
            		JOptionPane.showMessageDialog(null,"Por favor, ingrese como usuario admin o inspector");
            		user.setText("");
    				password.setText("");
            	}
            		
            	else {
            		LogIn.getLogIn().conectarBD(user.getText(),password.getText());
            		Iniciar_Sesion.setEnabled(false);

            		MenuLogIn.getMenu().setVisible(false);
            		instance=null;
            	}
            }
        });

        this.repaint();
    }
}