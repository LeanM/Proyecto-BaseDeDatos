package GUI.Ventanas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import LogIn.LogIn;
import quick.dbtable.DBTable;

@SuppressWarnings("serial")
public abstract class VentanaConUbicaciones extends JFrame {
	
	protected JPanel fondo;
	
	protected JButton volverInicio;
	protected ActionListener oyenteVolverInicio;
	
	protected JLabel labelCalles;
	protected JLabel labelAlturas;
	protected JLabel labelParquimetros;
	 
	protected JComboBox <String> calles;
	protected ActionListener oyenteCalles;
	protected JComboBox <String> alturas;
	protected ActionListener oyenteAlturas;
	protected JComboBox <String> parquimetros;
	
	protected String calle;
	protected String altura;
	
	Connection conexion;
    Statement stmt;
    String sql;
    ResultSet rs;
    
    DBTable tablaBD;
	
	public VentanaConUbicaciones(DBTable tabla) {
		
		tablaBD=tabla; 
		
		setVisible(true);
        getContentPane().setLayout(null);
      	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1360, 768);
        setResizable(false);
        
        fondo = new JPanel();
	    fondo.setBorder(new EmptyBorder(5, 5, 5, 5));
	    fondo.setBackground(Color.LIGHT_GRAY);
	    fondo.setLayout(null);
	    
	    volverInicio=LogIn.newVolverInicio();
	    volverInicio.setBounds(900,50,200,50);
	    fondo.add(volverInicio);
		
		calles = new JComboBox<String>();  //hay que cargar las ubicaciones como strings desde la bd
	    calles.setSelectedIndex(-1);
	    calles.setBounds(900,250,200,50);
	    oyenteCalles = new OyenteCalles();
	    calles.addActionListener(oyenteCalles);
	    fondo.add(calles);
	     
	    alturas = new JComboBox<String>(); //hay que cargar las alturas de la calle elegida como strings desde la bd
	    alturas.setSelectedIndex(-1);
	    alturas.setBounds(900,350,200,50);
	    oyenteAlturas = new OyenteAlturas();
	    alturas.addActionListener(oyenteAlturas);
	    fondo.add(alturas);
	     
	    parquimetros = new JComboBox<String>(); //hay que cargar los parquimetros de la calle y altura elegidas como strings desde la bd
	    parquimetros.setSelectedIndex(-1);
	    parquimetros.setBounds(900,450,200,50);
	    fondo.add(parquimetros);
	    
	    labelCalles = new JLabel("Calle:");
	    labelCalles.setBounds(800, 260, 75, 20);
	    fondo.add(labelCalles);
	     
	    labelAlturas = new JLabel("Altura:");
	    labelAlturas.setBounds(800, 360, 75, 20);
	    fondo.add(labelAlturas);
	     
	    labelParquimetros = new JLabel("Parquimetro:");
	    labelParquimetros.setBounds(800, 460, 100, 20);
	    fondo.add(labelParquimetros);
	    
	    actualizarCalles();
	    actualizarAlturas();
	    actualizarParquimetros();
	    
	    setContentPane(fondo);
	}
	
	protected void actualizarCalles() {

		try {
		    conexion = tablaBD.getConnection();
		    stmt = conexion.createStatement();
            sql = "SELECT DISTINCT calle FROM parquimetros";
            rs = stmt.executeQuery(sql);
            
            String [] callesBD = new String [99999]; // Lo hago de esta forma porque no encontre como obtener el tamaño del resultset
            int index = 0;
            DefaultComboBoxModel<String> model;
            
            while(rs.next()) 
            	callesBD[index++]=rs.getString("calle");
            
            model = new DefaultComboBoxModel<String>(eliminarEspacios(index,callesBD));
            calles.setModel(model);
           
            stmt.close();
            rs.close();
		}
		
		catch (SQLException ex) {
			System.out.println("Error al obtener las calles");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		calle=(String)calles.getSelectedItem();
	}
	
	
	protected void actualizarAlturas() { // Selecciona las alturas disopibles para la calle elegida
		
		try {
		    conexion = tablaBD.getConnection();
		    stmt = conexion.createStatement();
            sql = "SELECT DISTINCT altura FROM parquimetros WHERE calle='"+calle+"'";
            rs = stmt.executeQuery(sql);
            
            String [] alturasBD = new String [99999];
            int index = 0;
            DefaultComboBoxModel<String> model;
            
            while(rs.next()) 
            	alturasBD[index++]=rs.getString("altura");
            
            model = new DefaultComboBoxModel<String>(eliminarEspacios(index,alturasBD));
            alturas.setModel(model);
           
            stmt.close();
            rs.close();
		}
		
		catch (SQLException ex) {
			System.out.println("Error al actualizar las alturas");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		altura=(String)alturas.getSelectedItem();

	}
	
	protected void actualizarParquimetros() { // Selecciona los parquimetros disponibles para la calle y alturas elegidas
		
		try {
		    conexion = tablaBD.getConnection();
		    stmt = conexion.createStatement();
            sql = "SELECT id_parq FROM parquimetros WHERE calle='"+calle+"' and altura='"+altura+"'";
            rs = stmt.executeQuery(sql);
            
            String [] parquimetrosBD = new String [99999];
            int index = 0;
            DefaultComboBoxModel<String> model;
            
            while(rs.next()) 
            	parquimetrosBD[index++]=rs.getString("id_parq");
            
            model = new DefaultComboBoxModel<String>(eliminarEspacios(index,parquimetrosBD));
            parquimetros.setModel(model);
           
            stmt.close();
            rs.close();
		}
		
		catch (SQLException ex) {
			System.out.println("Error al actualizar los parquimetros");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
	}
	
	protected String [] eliminarEspacios (int cant,String original []) {
		
		String nuevo [] = new String [cant];
		
		for(int i=0;i<cant;i++)
			nuevo[i]=original[i];
		
		return nuevo;
	}
	
	protected class OyenteCalles implements ActionListener {

		
		public void actionPerformed(ActionEvent e) {
			calle=(String)calles.getSelectedItem();
			actualizarAlturas();
			actualizarParquimetros();
		}
		
	}
	
	protected class OyenteAlturas implements ActionListener {

		
		public void actionPerformed(ActionEvent e) {
			altura=(String)alturas.getSelectedItem();
			actualizarParquimetros();
		}
		
	}
	
}
