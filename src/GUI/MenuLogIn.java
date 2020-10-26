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

    /**
     * Constructor de menu login que inicializa la ventana de login para
     * permitirle al usuario conectarse a la base de datos con su
     * usuario y contraseña.
     *
     */
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

                Iniciar_Sesion.setEnabled(false);

                //  Listener de boton de iniciar sesion el cual llama a la clase LogIn la cual realiza la conexion
                LogIn.getLogIn().conectarBD(user.getText(),password.getText());

                if (LogIn.getLogIn().conexionEstablecida()) {
                    //  Elimina la ventana MenuLogin
                    MenuLogIn.getMenu().setVisible(false);
                    instance = null;
                }
                else {
                    user.setText("");
                    password.setText("");
                    Iniciar_Sesion.setEnabled(true);
                }
            }
        });

        this.repaint();
    }
}