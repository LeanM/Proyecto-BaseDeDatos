package GUI.Ventanas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import quick.dbtable.DBTable;

public class VentanaInspector extends JFrame{
	
	protected static final long serialVersionUID = 1L;
	
	protected JPanel fondoInicio;
	protected JPanel fondoIngresado;
	
	protected JTextField textoLegajo;
	protected JTextField textoContraseña;
	protected JTextField textoPatente;
	 
	protected JButton ingresar;
	protected ActionListener oyenteIngresar;
	 
	protected JButton agregar;
	protected ActionListener oyenteAgregar;
	 
	protected JButton eliminar;
	protected ActionListener oyenteEliminar;
	 
	protected JButton finalizarCargaPatentes;
	protected ActionListener oyenteFCP;
	 
	protected JComboBox <String> calles;
	protected ActionListener oyenteCalles;
	protected JComboBox <String> alturas;
	 
	protected DefaultListModel <String> estacionados;
	protected JList<String> listaE;
	
	protected DBTable tablaBD;
	
	 
	public VentanaInspector(DBTable tabla) {
		
		setVisible(true);
        getContentPane().setLayout(null);
      	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(200, 50, 1360, 768);
        setResizable(false);
		
		tablaBD=tabla;
		 
		crearFondoInicio();
		crearFondoIngresado();
		 
		setContentPane(fondoInicio);
	    validate();
		 
	 }

	
	protected void crearFondoInicio () {  // Crea el panel inicial, desde el cual se accede a la unidad personal
       
       fondoInicio = new JPanel();
       fondoInicio.setBorder(new EmptyBorder(5, 5, 5, 5));
       fondoInicio.setLayout(null);

       textoLegajo = new JTextField();
       textoLegajo.setBounds(30, 241, 400, 30);
       textoLegajo.setBackground(Color.LIGHT_GRAY);
       fondoInicio.add(textoLegajo);
       
       textoContraseña = new JPasswordField();
       textoContraseña.setBounds(30, 497, 400, 30);
       textoContraseña.setBackground(Color.LIGHT_GRAY);
       fondoInicio.add(textoContraseña);

       ingresar = new JButton("Ingresar");
       ingresar.setBounds(850,309,300,150);
       ingresar.setBackground(Color.DARK_GRAY);
       ingresar.setForeground(Color.WHITE);
       oyenteIngresar= new OyenteIngresar();
       ingresar.addActionListener(oyenteIngresar);
       fondoInicio.add(ingresar);
	
	}
	
	protected void crearFondoIngresado() {  // Crea el panel que se usa una vez que se ingresó a la unidad personal
		
	     fondoIngresado = new JPanel();
	     fondoIngresado.setBorder(new EmptyBorder(5, 5, 5, 5));
	     fondoIngresado.setLayout(null);
	
	     estacionados= new DefaultListModel<String>();
	     
	     listaE = new JList<String>(estacionados);
	     
	     listaE.setBounds(100,200,500,500);
	     fondoIngresado.add(listaE);
	     
	     textoPatente = new JTextField();
	     textoPatente.setBounds(800,50,400,30);
	     fondoIngresado.add(textoPatente);
	     
	     agregar = new JButton("Agregar patente");
	     agregar.setBounds(50,50,200,50);
	     oyenteAgregar = new OyenteAgregar();
	     agregar.addActionListener(oyenteAgregar);
	     fondoIngresado.add(agregar);
	     
	     eliminar = new JButton("Eliminar patente");
	     eliminar.setBounds(400,50,200,50);
	     oyenteEliminar = new OyenteEliminar();
	     eliminar.addActionListener(oyenteEliminar);
	     fondoIngresado.add(eliminar);
	     
	     calles = new JComboBox<String>();  //hay que cargar las ubicaciones como strings desde la bd, si es que se puede
	     calles.setSelectedIndex(-1);
	     calles.setBounds(900,300,200,50);
	     oyenteCalles = new OyenteCalles();
	     calles.addActionListener(oyenteCalles);
	     fondoIngresado.add(calles);
	     
	     alturas = new JComboBox<String>(); //hay que cargar los parquimetros de la ubicacion que se elige como strings desde la bd, si es que se puede
	     alturas.setSelectedIndex(-1);
	     alturas.setBounds(900,500,200,50);
	     fondoIngresado.add(alturas);
	     
	     finalizarCargaPatentes = new JButton("Finalizar Carga");
	     finalizarCargaPatentes.setBounds(900,650,200,50);
	     oyenteFCP = new OyenteFCP();
	     finalizarCargaPatentes.addActionListener(oyenteFCP);
	     finalizarCargaPatentes.setEnabled(false);
	     fondoIngresado.add(finalizarCargaPatentes);
	     
	}
	
	
	
	
//  --------               MÉTODOS AUXILIARES                    ---------     // 
	
	
	protected void checkFinalizable() { // Verifica si es posible finalizar la carga de patentes
		
		if(estacionados.isEmpty() || calles.getSelectedIndex()==-1 || alturas.getSelectedIndex()==-1)
			finalizarCargaPatentes.setEnabled(false);
		else finalizarCargaPatentes.setEnabled(true);
	}
	
	
	protected void actualizarAlturas() { // Selecciona las alturas disopibles para la calle elegida
		
		try {
			String calle = (String)calles.getSelectedItem(); 
			
		    Connection conexion = tablaBD.getConnection();
		    Statement stmt = conexion.createStatement();
            String sql = "SELECT altura FROM parquimetros WHERE calle='"+calle+"'";
            ResultSet rs = stmt.executeQuery(sql);
            String [] alturasBD = new String [99999];
           
            int index = 0;
            
            while(rs.next()) 
            	alturasBD[index++]=rs.getString("altura");
            
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(eliminarEspacios(index,alturasBD));
            alturas.setModel(model);
           
            stmt.close();
            rs.close();
		}
		
		catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		checkFinalizable();
	}
	
	protected String [] eliminarEspacios (int cant,String original []) {
		String nuevo [] = new String [cant];
		
		for(int i=0;i<cant;i++)
			nuevo[i]=original[i];
		
		return nuevo;
	}
	
	
	
	
	//   --------               OYENTES                    ---------     //   
	
	
	
	
	protected class OyenteIngresar implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			
			String legajo, pwd;
			boolean legajoValido=false;
			boolean pwdValida=false;
			
			legajo=textoLegajo.getText();
			pwd=textoContraseña.getText();
			
			if( legajo.isEmpty()|| pwd.isEmpty() ) {
				JOptionPane.showMessageDialog(null,"Por favor, complete ambos campos");
				return;
			}
			
			try {
			    Connection conexion = tablaBD.getConnection();
	            Statement stmt = conexion.createStatement();
	            String sql = "SELECT legajo,password FROM inspectores";
	            ResultSet rs = stmt.executeQuery(sql);
	            
	            while(rs.next() && !legajoValido) 
	            	if(rs.getString("legajo").equals(legajo)) {
	            		legajoValido=true;
	            		pwdValida=rs.getString("password").equals(pwd);
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
				textoLegajo.setText("");
				textoContraseña.setText("");
				return;
			}
			
			if (!pwdValida) { //esta mal la contraseña
				JOptionPane.showMessageDialog(null,"La contraseña provista para ese número de legajo es incorrecta, por favor compruebela");
				textoContraseña.setText("");
				return;
			}
			
			try {
			    Connection conexion = tablaBD.getConnection();
			    Statement stmt = conexion.createStatement();
	            String sql = "SELECT calle FROM parquimetros";
	            ResultSet rs = stmt.executeQuery(sql);
	            String [] callesBD = new String [99999];
	           
	            int index = 0;
	            
	            while(rs.next()) 
	            	callesBD[index++]=rs.getString("calle");
	            
	            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(eliminarEspacios(index,callesBD));
	            calles.setModel(model);
	           
	            stmt.close();
	            rs.close();
			}
			
			catch (SQLException ex) {
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
			
			actualizarAlturas();
			setContentPane(fondoIngresado);
		    validate();
			
		}
	}
	
	protected class OyenteAgregar implements ActionListener {

		
		public void actionPerformed(ActionEvent arg0) {
			
			String patente = textoPatente.getText();
			
			if(patente.isEmpty())
				JOptionPane.showMessageDialog(null,"Por favor, ingrese una patente");
			
			else { 
				estacionados.addElement(patente);
				textoPatente.setText("");
			}
			
			checkFinalizable();
			
		}
		
		
	}
	
	protected class OyenteEliminar implements ActionListener {

		
		public void actionPerformed(ActionEvent arg0) {
			
			int posPatenteElegida = listaE.getSelectedIndex();
			
			if(posPatenteElegida==-1)
				JOptionPane.showMessageDialog(null,"Por favor, elija una patente a eliminar");
			
			else estacionados.remove(posPatenteElegida);
			
			checkFinalizable();
			
		}
		
	}
	
	protected class OyenteCalles implements ActionListener {

		
		public void actionPerformed(ActionEvent e) {
			actualizarAlturas();
		}
		
	}
	
	protected class OyenteFCP implements ActionListener {

	
		public void actionPerformed(ActionEvent arg0) { // Hay que checkear que el inspector pueda hacer multas en la hora y lugar que esta intentando, y si puede, simular la conexion
			
			/*if(habilitado()) {
				registrarAcceso();
				generarMultas();
				mostrarMultas();
			}
			else {
				JOptionPane.showMessageDialog(null,"No esta habilitado para labar multas en esta ubicacion");
			}*/
		}
		
	}
		
}
