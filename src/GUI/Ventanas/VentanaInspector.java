package GUI.Ventanas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import quick.dbtable.DBTable;

@SuppressWarnings("serial")
public class VentanaInspector extends VentanaConUbicaciones{
	
	protected JButton volverInicio;
	protected ActionListener oyenteVolverInicio;
	 
	protected JButton agregar;
	protected ActionListener oyenteAgregar;
	 
	protected JButton eliminar;
	protected ActionListener oyenteEliminar;
	 
	protected JButton finalizarCargaPatentes;
	protected ActionListener oyenteFCP;
	
	protected JTextField textoPatente;
	
	protected JLabel labelPatentes;
	 
	protected DefaultListModel <String> estacionados;
	protected JList<String> listaE;
	protected List<String> patentes;
	
	protected String legajo;
	
	 
	public VentanaInspector(DBTable tabla,String legajo) {
		super(tabla);
		
		this.legajo=legajo; 
		crearFondo();
	    validate();
	 }

	
	
	protected void crearFondo() {  // Crea el panel 
		
	     /*fondo = new JPanel();
	     fondo.setBorder(new EmptyBorder(5, 5, 5, 5));
	     fondo.setBackground(Color.LIGHT_GRAY);
	     fondo.setLayout(null);*/

		 //Creo bordes
		 Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		 Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		 Border compuesto = BorderFactory.createCompoundBorder(raisedbevel,loweredbevel);
	
	     estacionados= new DefaultListModel<String>();
	     
	     listaE = new JList<String>(estacionados);

	     listaE.setBorder(compuesto);
	     listaE.setBounds(100,200,500,500);
	     fondo.add(listaE);
	     
	     textoPatente = new JTextField();
	     textoPatente.setBounds(175,125,250,30);
	     fondo.add(textoPatente);
	     
	     labelPatentes = new JLabel("Patente a ingresar:");
	     labelPatentes.setBounds(50, 130, 125, 20);
	     fondo.add(labelPatentes);
	     
	     /*volverInicio=LogIn.newVolverInicio();
	     volverInicio.setBounds(900,50,200,50);
	     fondo.add(volverInicio);*/
	     
	     agregar = new JButton("Agregar patente");
	     agregar.setBounds(50,50,200,50);
	     agregar.setBackground(Color.DARK_GRAY);
	     agregar.setForeground(Color.WHITE);
	     oyenteAgregar = new OyenteAgregar();
	     agregar.addActionListener(oyenteAgregar);
	     fondo.add(agregar);
	     
	     eliminar = new JButton("Eliminar patente");
	     eliminar.setBounds(400,50,200,50);
	     eliminar.setBackground(Color.DARK_GRAY);
	     eliminar.setForeground(Color.WHITE);
	     oyenteEliminar = new OyenteEliminar();
	     eliminar.addActionListener(oyenteEliminar);
	     fondo.add(eliminar);
	     
	     finalizarCargaPatentes = new JButton("Finalizar Carga");
	     finalizarCargaPatentes.setBounds(900,650,200,50);
		 finalizarCargaPatentes.setBackground(Color.DARK_GRAY);
		 finalizarCargaPatentes.setForeground(Color.WHITE);
	     oyenteFCP = new OyenteFCP();
	     finalizarCargaPatentes.addActionListener(oyenteFCP);
	     finalizarCargaPatentes.setEnabled(false);
	     fondo.add(finalizarCargaPatentes);
	     
	     cargarPatentes();
	}
	
	
	
	
//  --------               MÉTODOS AUXILIARES                    ---------     // 
	
	
	protected void checkFinalizable() { // Verifica si es posible finalizar la carga de patentes
		
		if(estacionados.isEmpty() || calles.getSelectedIndex()==-1 || alturas.getSelectedIndex()==-1)
			finalizarCargaPatentes.setEnabled(false);
		else finalizarCargaPatentes.setEnabled(true);
	}
	
	protected void cargarPatentes() {
		
		try {
		    conexion = tablaBD.getConnection();
		    stmt = conexion.createStatement();
            sql = "SELECT patente FROM automoviles";
            rs = stmt.executeQuery(sql);
            
            patentes = new LinkedList<String>();
            
            while(rs.next()) 
            	patentes.add(rs.getString("patente"));
           
            stmt.close();
            rs.close();
		}
		
		catch (SQLException ex) {
			System.out.println("Error al obtener las patentes");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
	}
	
	protected String[] habilitado() {
	
		String ret[]=null;
		String dia=null;
		String turno=null;
		String fecha=null;
		String hora=null;
		
		try {
		    conexion = tablaBD.getConnection();
		    stmt = conexion.createStatement();
            sql = "SELECT curtime(),curdate(),hour(curtime()),dayofweek(curdate())";
            rs = stmt.executeQuery(sql);
            
            if(rs.next()) {
            	/*hora="09:05:04";
            	fecha="2020-10-23";
            	turno="m";
            	dia="vi";*/
            	hora=rs.getString(1);
            	fecha=rs.getString(2);
            	turno=turno(rs.getInt(3)); 
            	dia=dia(rs.getInt(4));
            	//System.out.println(hora+fecha+turno+dia);
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
			conexion = tablaBD.getConnection();//System.out.println(legajo+calle+altura+parquimetro);
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
			System.out.println("Error al buscar las ubicaciones permitadas para el legajo");
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
			case 1: dia="do";
			break;
			case 2: dia="lu";
			break;
			case 3: dia="ma";
			break;
			case 4: dia="mi";
			break;
			case 5: dia="ju";
			break;
			case 6: dia="vi";
			break;
			case 7: dia="sa";
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
		
		String[] nombresColumnas = {"N° de multa","Fecha","Hora","Calle","Altura","Patente","Legajo inspector"};
		DefaultTableModel model = new DefaultTableModel(nombresColumnas, 0);
		
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
		
		
		for(int i=0;i<cantAnotados;i++) {
			estaEnInfraccion=true;
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
				    
				    sql = "select numero from multa where fecha='"+fecha+"' and hora='"+hora+"' and patente='"+patenteActual+"'";
		            rs = stmt.executeQuery(sql);
		            
				    if(rs.next())
				    	model.addRow(new Object[] {rs.getString(1),fecha,hora,calle,altura,patenteActual,legajo}); 
		            	           	            
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
	
	
	
	
	//   --------               OYENTES                    ---------     //   
	
	
	
	protected class OyenteAgregar implements ActionListener {

		
		public void actionPerformed(ActionEvent arg0) {
			
			String patente = textoPatente.getText().toUpperCase();
			boolean patenteValida=false;
			boolean estaAnotada=false;
			int cantAnotadas=estacionados.getSize();
			int i=0;
			Iterator<String> iter=patentes.iterator();
			
			if(patente.isEmpty()) {
				JOptionPane.showMessageDialog(null,"Por favor, ingrese una patente");
				return;
			}
			
			while(i<cantAnotadas && !estaAnotada)
				estaAnotada=estacionados.get(i++).equals(patente);
			
			if(estaAnotada) {
				JOptionPane.showMessageDialog(null,"Esta patente ya fue ingresada");
				textoPatente.setText("");
				return;
			}
			
			while(!patenteValida && iter.hasNext())
				patenteValida=iter.next().equals(patente);
			
			if(patenteValida) 
				estacionados.addElement(patente);
			else 
				JOptionPane.showMessageDialog(null,"La patente ingresada no está en la base de datos");
			
			textoPatente.setText("");
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
	
	
	protected class OyenteFCP implements ActionListener {

	
		public void actionPerformed(ActionEvent arg0) { // Hay que checkear que el inspector pueda hacer multas en la hora y lugar que esta intentando, y si puede, simular la conexion
			
			String datos[] = habilitado();  //datos[0] es la fecha, datos[1] es la hora, datos[2] es el id_asociado_con
			TableModel modelo;  // aca se guardan las multas generadas, si es que hay
			
			if(datos!=null) {
				registrarAcceso(datos[0],datos[1]);
				modelo = generarMultas(datos[0],datos[1],datos[2]);
				
				if(modelo.getRowCount()>0)
					JOptionPane.showMessageDialog(null, new JScrollPane(new JTable(modelo)),"Multas Labradas",JOptionPane.INFORMATION_MESSAGE);
				else JOptionPane.showMessageDialog(null, "No hay autos en infraccion entre los cargados");
				
				estacionados.clear();
				finalizarCargaPatentes.setEnabled(false);
			}
			else {
				JOptionPane.showMessageDialog(null,"No esta habilitado para labrar multas en esta ubicacion");
			}
		}
		
	}
		
}
