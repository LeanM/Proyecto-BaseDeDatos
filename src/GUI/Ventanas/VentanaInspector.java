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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import quick.dbtable.DBTable;

public class VentanaInspector extends JFrame{
	
	protected static final long serialVersionUID = 1L;
	
	protected JPanel fondoInicio;
	protected JPanel fondoIngresado;
	
	protected JTextField textoLegajo;
	protected JTextField textoContrase�a;
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
	protected ActionListener oyenteAlturas;
	protected JComboBox <String> parquimetros;
	 
	protected DefaultListModel <String> estacionados;
	protected JList<String> listaE;
	
	protected DBTable tablaBD;
	
	protected String legajo;
	protected String calle;
	protected String altura;
	//protected String parquimetro;  Me parece que es mejor no tener esta variable global asi no hay que hacer un oyente para parquimetros, y ahi actualizar el parquimetro elegido
 	
	Connection conexion;
    Statement stmt;
    String sql;
    ResultSet rs;
	
	 
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
       
       textoContrase�a = new JPasswordField();
       textoContrase�a.setBounds(30, 497, 400, 30);
       textoContrase�a.setBackground(Color.LIGHT_GRAY);
       fondoInicio.add(textoContrase�a);

       ingresar = new JButton("Ingresar");
       ingresar.setBounds(850,309,300,150);
       ingresar.setBackground(Color.DARK_GRAY);
       ingresar.setForeground(Color.WHITE);
       oyenteIngresar= new OyenteIngresar();
       ingresar.addActionListener(oyenteIngresar);
       fondoInicio.add(ingresar);
	
	}
	
	protected void crearFondoIngresado() {  // Crea el panel que se usa una vez que se ingres� a la unidad personal
		
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
	     
	     calles = new JComboBox<String>();  //hay que cargar las ubicaciones como strings desde la bd
	     calles.setSelectedIndex(-1);
	     calles.setBounds(900,250,200,50);
	     oyenteCalles = new OyenteCalles();
	     calles.addActionListener(oyenteCalles);
	     fondoIngresado.add(calles);
	     
	     alturas = new JComboBox<String>(); //hay que cargar los parquimetros de la ubicacion que se elige como strings desde la bd
	     alturas.setSelectedIndex(-1);
	     alturas.setBounds(900,350,200,50);
	     oyenteAlturas = new OyenteAlturas();
	     alturas.addActionListener(oyenteAlturas);
	     fondoIngresado.add(alturas);
	     
	     parquimetros = new JComboBox<String>(); //hay que cargar los parquimetros de la ubicacion que se elige como strings desde la bd
	     parquimetros.setSelectedIndex(-1);
	     parquimetros.setBounds(900,450,200,50);
	     fondoIngresado.add(parquimetros);
	     
	     finalizarCargaPatentes = new JButton("Finalizar Carga");
	     finalizarCargaPatentes.setBounds(900,650,200,50);
	     oyenteFCP = new OyenteFCP();
	     finalizarCargaPatentes.addActionListener(oyenteFCP);
	     finalizarCargaPatentes.setEnabled(false);
	     fondoIngresado.add(finalizarCargaPatentes);
	     
	}
	
	
	
	
//  --------               M�TODOS AUXILIARES                    ---------     // 
	
	
	protected void checkFinalizable() { // Verifica si es posible finalizar la carga de patentes
		
		if(estacionados.isEmpty() || calles.getSelectedIndex()==-1 || alturas.getSelectedIndex()==-1)
			finalizarCargaPatentes.setEnabled(false);
		else finalizarCargaPatentes.setEnabled(true);
	}
	
	
	protected void actualizarAlturas() { // Selecciona las alturas disopibles para la calle elegida
		
		try {
		    conexion = tablaBD.getConnection();
		    stmt = conexion.createStatement();
            sql = "SELECT altura FROM parquimetros WHERE calle='"+calle+"'";
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
		checkFinalizable();
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
		
		//parquimetro=(String)parquimetros.getSelectedItem();
		checkFinalizable();
	}
	
	protected String[] habilitado() {
	
		String ret[]=null;
		String dia=null;
		String turno=null;
		String fecha=null;
		String hora=null;
		String parquimetro=(String) parquimetros.getSelectedItem();
		
		try {
		    conexion = tablaBD.getConnection();
		    stmt = conexion.createStatement();
            sql = "SELECT curtime(),curdate(),hour(curtime()),dayofweek(curdate())";
            rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
            	hora="09:05:04";//rs.getString(1);
            	fecha="2020-10-20";//rs.getString(2);
            	turno="m";//turno(rs.getInt(3));
            	dia="ma";//dia(rs.getInt(4));
            }
            	
            stmt.close();
            rs.close();      
		}
		catch (SQLException ex) {
			System.out.println("Error al buscar la fecha y hora");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
        //-----------------------------------------------------------------------------------------------//
		 try {
			conexion = tablaBD.getConnection();System.out.println(legajo+calle+altura+parquimetro);
		    stmt = conexion.createStatement();
            sql = "SELECT * FROM asociado_con WHERE legajo='"+legajo+"'";
            rs = stmt.executeQuery(sql);
            
            while(rs.next()) 
            	if(rs.getString("calle").equals(calle) && rs.getString("altura").equals(altura) && 
            	   rs.getString("dia").equals(dia) && rs.getString("turno").equals(turno)) {
            		
            		ret= new String[3];
            		ret[0]=fecha;
            		ret[1]=hora;
            		ret[2]=rs.getString("id_asociado_con");
            	}
           
            stmt.close();
            rs.close();
		}
		
		catch (SQLException ex) {
			System.out.println("Error al buscar las ubicaciones del permitadas para el legajo");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		return ret;
	}
	
	protected String turno (int hora) {
		
		String ret;
		
		if(8<=hora && hora<=13)
			ret="m";
		else 
			if(14<=hora && hora<=19) 
				ret="t";
			 else ret=null;
		
		return ret;
		
	}
	
	protected String dia (int numero) {
		
		String dia=null;
		
		switch (numero) {
			case 1: dia="lu";
			break;
			case 2: dia="ma";
			break;
			case 3: dia="mi";
			break;
			case 4: dia="ju";
			break;
			case 5: dia="vi";
			break;
			case 6: dia="sa";
			break;
			case 7: dia="do";
			break;
		}
		
		return dia;
	}
	
	protected void registrarAcceso(String fecha,String hora) {
		
		String parquimetro=(String) parquimetros.getSelectedItem();
		
		try {
			conexion = tablaBD.getConnection();
		    stmt = conexion.createStatement();
            sql = "INSERT INTO accede(legajo, id_parq, fecha, hora) VALUES ("+legajo+","+parquimetro+",'"+fecha+"','"+hora+"');";
            stmt.execute(sql);
  
            stmt.close();
		}
		
		catch (SQLException ex) {
			System.out.println("Error al insertar acceso a parquimetro");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	protected DefaultTableModel generarMultas(String fecha,String hora,String id_asociado_con) {
		
		int cantEstacionados=0;
		int cantAnotados=listaE.getModel().getSize();
		String patentesEstacionadas[] = new String[99999];
		String patenteActual;
		boolean estaEnInfraccion=true;
		
		String[] nombresColumnas = {"Fecha","Hora","Calle","Altura","Patente","Legajo inspector"};
		DefaultTableModel model = new DefaultTableModel(nombresColumnas, 0);

		
		//String [][] multasCreadas;
		
		try {
			conexion = tablaBD.getConnection();
		    stmt = conexion.createStatement();
            sql = "SELECT patente FROM estacionados WHERE calle='"+calle+"' and altura='"+altura+"'";
            rs = stmt.executeQuery(sql);
           
            while(rs.next()) {
            	patentesEstacionadas[cantEstacionados++]=rs.getString("patente");
            }
            
            stmt.close();
            rs.close();
		}
		
		catch (SQLException ex) {
			System.out.println("Error al recuperar patentes estacionadas");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		
		//multasCreadas = new String[cantAnotados][7];
		
		//int pointerMultas = 0;
		
		for(int i=0;i<cantAnotados;i++) {
			int j=0;
			patenteActual=listaE.getModel().getElementAt(i);
			
			while(estaEnInfraccion && j<cantEstacionados) 
				estaEnInfraccion = !patenteActual.equals(patentesEstacionadas[j++]);
			
			if(estaEnInfraccion)
				try {					
					conexion = tablaBD.getConnection();
				    stmt = conexion.createStatement();
		            sql = "INSERT INTO multa(fecha, hora, patente, id_asociado_con) VALUES ('"+fecha+"','"+hora+"','"+patenteActual+"',"+id_asociado_con+")";
		            stmt.execute(sql);
		            
		            model.addRow(new Object[] {fecha,hora,calle,altura,patenteActual,legajo}); //faltar�a el numero de multa
		            	           	            
		            stmt.close();
		         }
				
				catch (SQLException ex) {
					System.out.println("Error al insertar multa");
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
			}	
		
		return model;
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
			
			String legajoIngresado = textoLegajo.getText();
			String pwd = textoContrase�a.getText();;
			boolean legajoValido=false;
			boolean pwdValida=false;
			String [] callesBD;
			DefaultComboBoxModel<String> model;
			
			if( legajoIngresado.isEmpty()|| pwd.isEmpty() ) {
				JOptionPane.showMessageDialog(null,"Por favor, complete ambos campos");
				return;
			}
			
			try {
			    conexion = tablaBD.getConnection();
	            stmt = conexion.createStatement();
	            sql = "SELECT legajo,password FROM inspectores";
	            rs = stmt.executeQuery(sql);
	            
	            while(rs.next() && !legajoValido) 
	            	if(rs.getString("legajo").equals(legajoIngresado)) {
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
				JOptionPane.showMessageDialog(null,"No se encontr� ningun inspector con ese n�mero de legajo, por favor compruebelo");
				textoLegajo.setText("");
				textoContrase�a.setText("");
				return;
			}
			
			if (!pwdValida) { //esta mal la contrase�a
				JOptionPane.showMessageDialog(null,"La contrase�a provista para ese n�mero de legajo es incorrecta, por favor compruebela");
				textoContrase�a.setText("");
				return;
			}
			
			try {
			    conexion = tablaBD.getConnection();
			    stmt = conexion.createStatement();
	            sql = "SELECT calle FROM parquimetros";
	            rs = stmt.executeQuery(sql);
	            
	            callesBD = new String [99999];
	            int index = 0;
	            
	            while(rs.next()) 
	            	callesBD[index++]=rs.getString("calle");
	            
	            model = new DefaultComboBoxModel<String>(eliminarEspacios(index,callesBD));
	            calles.setModel(model);
	           
	            stmt.close();
	            rs.close();
			}
			
			catch (SQLException ex) {
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			}
			
			legajo=legajoIngresado;
			calle=(String)calles.getSelectedItem();
			actualizarAlturas();
			actualizarParquimetros();
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
			calle=(String)calles.getSelectedItem();
			actualizarAlturas();
			actualizarParquimetros();
		}
		
	}
	
protected class OyenteAlturas implements ActionListener {

		
		public void actionPerformed(ActionEvent e) {
			actualizarParquimetros();
		}
		
	}
	
	protected class OyenteFCP implements ActionListener {

	
		public void actionPerformed(ActionEvent arg0) { // Hay que checkear que el inspector pueda hacer multas en la hora y lugar que esta intentando, y si puede, simular la conexion
			
			String datos[] = habilitado();  //datos[0] es la fecha, datos[1] es la hora. datos[2] es el id_asociado_con
			TableModel modelo;  // aca se guardan las multas generadas, si es que hay
			
			if(datos!=null) {
				registrarAcceso(datos[0],datos[1]);
				modelo = generarMultas(datos[0],datos[1],datos[2]);
				
				if(modelo.getRowCount()>0)
					JOptionPane.showMessageDialog(null, new JScrollPane(new JTable(modelo)));
				else JOptionPane.showMessageDialog(null, "No hay autos en infraccion entre los cargados");
			}
			else {
				JOptionPane.showMessageDialog(null,"No esta habilitado para labar multas en esta ubicacion");
			}
		}
		
	}
		
}
