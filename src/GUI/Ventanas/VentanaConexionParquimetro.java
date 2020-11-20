package GUI.Ventanas;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import quick.dbtable.DBTable;

@SuppressWarnings("serial")
public class VentanaConexionParquimetro extends VentanaConUbicaciones {
	
	protected JLabel labelTarjetas; 
	protected JComboBox <String> tarjetas;
	protected JButton conexionDesconexion;
	
	public VentanaConexionParquimetro(DBTable tabla) {
		
		super(tabla);
		
		tarjetas = new JComboBox<String>(); //hay que cargar las tarjetas como strings desde la bd
		tarjetas.setSelectedIndex(-1);
		tarjetas.setBounds(900,550,200,50);
		fondo.add(tarjetas);
		    
		labelTarjetas = new JLabel("Tarjeta: ");
		labelTarjetas.setBounds(800, 560, 75, 20);
		fondo.add(labelTarjetas);
		
		conexionDesconexion = new JButton("Conexion/Desconexion parquimetro");
		conexionDesconexion.setBounds(300,384,300,100);
		conexionDesconexion.setBackground(Color.DARK_GRAY);
		conexionDesconexion.setForeground(Color.WHITE);
	    fondo.add(conexionDesconexion);
		
		actualizarTarjetas();
		
		conexionDesconexion.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// llamo al stored procedure
				try {
				    conexion = tablaBD.getConnection();
				    stmt = conexion.createStatement();
		            sql = "CALL conectar("+tarjetas.getSelectedItem()+","+parquimetros.getSelectedItem()+")";
		         
		            rs = stmt.executeQuery(sql);
		            tabla.refresh(rs);
		            
		            JOptionPane.showMessageDialog(null, tabla,"Mensaje",JOptionPane.INFORMATION_MESSAGE);
		            
		            stmt.close();
		            rs.close();
				}
				
				catch (SQLException ex) {
					System.out.println("Error al ejecutar el stored procedure");
					System.out.println("SQLException: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("VendorError: " + ex.getErrorCode());
				}
			}
			
		});
		
		validate();
	}
	
	protected void actualizarTarjetas() {

		try {
		    conexion = tablaBD.getConnection();
		    stmt = conexion.createStatement();
            sql = "SELECT id_tarjeta FROM tarjetas ORDER BY id_tarjeta";
            rs = stmt.executeQuery(sql);
            
            String [] tarjetasBD = new String [99999]; // Lo hago de esta forma porque no encontre como obtener el tama�o del resultset
            int index = 0;
            DefaultComboBoxModel<String> model;
            
            while(rs.next()) 
            	tarjetasBD[index++]=rs.getString("id_tarjeta");
            
            model = new DefaultComboBoxModel<String>(eliminarEspacios(index,tarjetasBD));
            tarjetas.setModel(model);
           
            stmt.close();
            rs.close();
		}
		
		catch (SQLException ex) {
			System.out.println("Error al obtener las tarjetas");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		calle=(String)calles.getSelectedItem();
	}

}
