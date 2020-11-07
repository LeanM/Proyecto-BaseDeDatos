package GUI.Ventanas;

import LogIn.LogIn;
import quick.dbtable.DBTable;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class MenuLogIn extends JFrame{

    //private static MenuLogIn instance=null;
    private JButton Iniciar_Sesion;
    private JButton botonVolver;
    private JButton Inspector;
    private JButton Admin;
    private JTextField legajo,password;
    private JLabel legajoTexto,passwordTexto;
    private JPanel contentPane;
    private boolean admin=false;
    private DBTable tabla;
    
    Connection conexion;
    Statement stmt;
    String sql;
    ResultSet rs;

    /*
    /**
     * Metodo que devuelve el menu ya que es unico en el juego.
     * Si este no esta creado lo crea sino devuelve el creado
     * @return Menu creado o existente
     
    public static MenuLogIn getMenu(){
        if(instance==null){
            instance = new MenuLogIn();
        }
        return instance;
    }*/

    /**
     * Constructor de menu login que inicializa la ventana de login para
     * permitirle al usuario conectarse a la base de datos con su
     * usuario y contraseÃ±a.
     *
     */
    public MenuLogIn(){
    	
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
        Iniciar_Sesion.setVisible(false);
        Iniciar_Sesion.setEnabled(false);
        
        Inspector = new JButton("Ingresar como Inspector");
        Inspector.setBounds(266-125,250-15,250,30);
        
        Admin =new JButton("Ingresar como Administrador");
        Admin.setBounds(533-125,250-15,230,30);
        
        botonVolver =new JButton("Volver");
        botonVolver.setBounds(330,300,120,30);
        botonVolver.setVisible(false);
        botonVolver.setEnabled(false);

        legajo = new JTextField();
        legajo.setBounds(350,100,100,20);
        legajo.setVisible(false);
        legajo.setEnabled(false);

        password = new JPasswordField();
        password.setBounds(350,150,100,20);
        password.setVisible(false);
        password.setEnabled(false);

        legajoTexto = new JLabel("N° de legajo :");
        legajoTexto.setBounds(270,100,75,20);
        legajoTexto.setForeground(Color.WHITE);
        legajoTexto.setVisible(false);

        passwordTexto = new JLabel("Password :");
        passwordTexto.setBounds(280,150,75,20);
        passwordTexto.setForeground(Color.WHITE);
        passwordTexto.setVisible(false);

        contentPane.add(passwordTexto);
        contentPane.add(Inspector);
        contentPane.add(Admin);
        contentPane.add(legajoTexto);
        contentPane.add(password);
        contentPane.add(legajo);
        contentPane.add(Iniciar_Sesion);
        contentPane.add(botonVolver);

        Iniciar_Sesion.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                //  Listener de boton de iniciar sesion el cual llama a la clase LogIn la cual realiza la conexion
                if(admin)
                	tabla=LogIn.getLogIn().conectarBD("admin",password.getText());
                else tabla=LogIn.getLogIn().conectarBD("inspector","inspector");
                
                if (tabla!=null) {
                	if(!admin) {
                		
                		String legajoIngresado = legajo.getText();
            			String pwd = password.getText();;
            			boolean legajoValido=false;
            			boolean pwdValida=false;
            			
                		try {
            			    String pwd_cifrada = getPWDCifrada(pwd);
            			    
            			    conexion = tabla.getConnection();
            	            stmt = conexion.createStatement();
            	            sql = "SELECT legajo,password FROM inspectores";
            	            rs = stmt.executeQuery(sql);

            	            while(rs.next() && !legajoValido) {
            					if (rs.getString("legajo").equals(legajoIngresado)) {
            						legajoValido = true;
            						pwdValida = rs.getString("password").equals(pwd_cifrada);
            					}
            				}
            	            stmt.close();
            	            rs.close();
            			}
            			
            			catch (SQLException ex) {
            				System.out.println("SQLException: " + ex.getMessage());
            				System.out.println("SQLState: " + ex.getSQLState());
            				System.out.println("VendorError: " + ex.getErrorCode());
            			}
                		
                		if (!legajoValido) { //no hay ningun inspector con ese legajo
            				JOptionPane.showMessageDialog(null,"No se encontró ningun inspector con ese número de legajo, por favor compruebelo");
            				legajo.setText("");
            				legajo.setText("");
            				return;
            			}
            			
            			if (!pwdValida) { //esta mal la contraseña
            				JOptionPane.showMessageDialog(null,"La contraseña provista para ese número de legajo es incorrecta, por favor compruebela");
            				password.setText("");
            				return;
            			}
                	}
                	
                	if(admin)
                		LogIn.getLogIn().ingresoAdmin();
                	else LogIn.getLogIn().ingresoInpector(legajo.getText());
                 
                }
                else {
                    legajo.setText("");
                    password.setText("");
                    Iniciar_Sesion.setEnabled(true);
                }
            }
        });
        
        Inspector.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				admin=false;
				deshabilitar();
			}
        	
        });
        
        Admin.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				admin=true;
				deshabilitar();
			}
        	
        });
        
        botonVolver.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				volver();
			}
        	
        });

        this.repaint();
    }
    
    private void deshabilitar() {
    	
    	if(!admin) {
    		legajo.setVisible(true);
			legajo.setEnabled(true);
			legajoTexto.setVisible(true);
    	}
    	
		password.setVisible(true);
		password.setEnabled(true);
		passwordTexto.setVisible(true);
		
		Iniciar_Sesion.setVisible(true);
		Iniciar_Sesion.setEnabled(true);
		
		Inspector.setVisible(false);
		Inspector.setEnabled(false);
		
		Admin.setVisible(false);
		Admin.setEnabled(false);
		
		botonVolver.setVisible(true);
		botonVolver.setEnabled(true);
    }
    
    private void volver() {
    	legajo.setVisible(false);
		legajo.setEnabled(false);
		legajoTexto.setVisible(false);
		
    	password.setVisible(false);
		password.setEnabled(false);
		passwordTexto.setVisible(false);
		
		Iniciar_Sesion.setVisible(false);
		Iniciar_Sesion.setEnabled(false);
		
		Inspector.setVisible(true);
		Inspector.setEnabled(true);
		
		Admin.setVisible(true);
		Admin.setEnabled(true);
		
		botonVolver.setVisible(false);
		botonVolver.setEnabled(false);
    }
    
    private String getPWDCifrada(String pwd){

		String toReturn=null;

		try {
			//	Statement para obtener la password cifrada con md5 de mysql
			conexion = tabla.getConnection();
			stmt = conexion.createStatement();
			sql = "SELECT md5('" + pwd + "') as password_cifrada";
			rs = stmt.executeQuery(sql);

			rs.next();
			toReturn = rs.getString("password_cifrada");
			
			rs.close();
			stmt.close();
			//	Ya obtube la password cifrada (guardada en variable toReturn)
		}
		catch (SQLException ex) {ex.printStackTrace();}

		return toReturn;
	}
}