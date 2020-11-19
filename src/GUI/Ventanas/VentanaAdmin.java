package GUI.Ventanas;

import quick.dbtable.DBTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import LogIn.LogIn;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VentanaAdmin extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextArea textoConsultas;
    private JScrollPane scroll_texto_Consulta;
    private JButton realizarConsulta;
    private JButton volverInicio;
    private JLabel etiquetaTablas;
    private JList<String> listaTablas,listaAtributosTabla;
    private DefaultListModel<String> model_Lista_Tablas,model_Lista_Atrubutos_Tabla;
    private DBTable tabla;

    public VentanaAdmin(DBTable tabla) {
        this.tabla = tabla;
        inicializarVentana();
    }

    private void inicializarVentana(){

        setVisible(true);
        getContentPane().setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1360, 768);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(Color.DARK_GRAY);
        setContentPane(contentPane);
        contentPane.setLayout(null);

        //Creo bordes
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        Border compuesto = BorderFactory.createCompoundBorder(raisedbevel,loweredbevel);

        textoConsultas = new JTextArea();
        textoConsultas.setBackground(Color.LIGHT_GRAY);
        textoConsultas.setBorder(compuesto);

        scroll_texto_Consulta = new JScrollPane(textoConsultas,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll_texto_Consulta.setBackground(Color.DARK_GRAY);
        scroll_texto_Consulta.setBounds(30,5,1100,150);

        realizarConsulta = new JButton("Consultar");
        realizarConsulta.setBounds(1150,10,150,50);
        realizarConsulta.setBackground(Color.DARK_GRAY);
        realizarConsulta.setForeground(Color.WHITE);
        
        volverInicio=LogIn.newVolverInicio();
        volverInicio.setBounds(1150,70,150,50);
        volverInicio.setBackground(Color.DARK_GRAY);
        volverInicio.setForeground(Color.WHITE);

        realizarConsulta.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                refrescarTabla();
            }
        });


        etiquetaTablas = new JLabel("Tablas de la base de datos :");
        etiquetaTablas.setBounds(1100,200,200,20);
        etiquetaTablas.setForeground(Color.WHITE);

        listaAtributosTabla = new JList<String>();
        listaAtributosTabla.setBounds(1120, 465, 150, 200);
        listaAtributosTabla.setBackground(Color.lightGray);
        listaAtributosTabla.setBorder(compuesto);

        model_Lista_Atrubutos_Tabla = new DefaultListModel<String>();
        listaAtributosTabla.setModel(model_Lista_Atrubutos_Tabla);

        listaTablas = new JList<String>();
        listaTablas.setBounds(1100, 225, 200, 225);
        listaTablas.setBackground(Color.lightGray);
        listaTablas.setBorder(compuesto);

        model_Lista_Tablas = new DefaultListModel<String>();
        listaTablas.setModel(model_Lista_Tablas);

        listaTablas.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
            	if(listaTablas.getModel().getSize()!=0) {
            		int indice = listaTablas.getSelectedIndex();
            		String nombreTabla = model_Lista_Tablas.getElementAt(indice);

            		model_Lista_Atrubutos_Tabla.removeAllElements();
            		mostrarArributosTabla(nombreTabla);
            	}
            }
        });


        tabla.setBounds(30,170,1050,550);
        tabla.setBackground(Color.LIGHT_GRAY);
        tabla.setBorder(compuesto);
        tabla.setEditable(false);

        contentPane.add(etiquetaTablas);
        contentPane.add(scroll_texto_Consulta);
        contentPane.add(realizarConsulta);
        contentPane.add(volverInicio);
        contentPane.add(tabla);
        contentPane.add(listaTablas);
        contentPane.add(listaAtributosTabla);

        mostrarTablasBD();
        this.repaint();
    }

    private void refrescarTabla() {

        try {

            // se ejecuta la sentencia
            //tabla.setSelectSql(textoConsultas.getText().trim());
            //tabla.createColumnModelFromQuery();
        	
        	Statement stmt = tabla.getConnection().createStatement();
        	String sql = textoConsultas.getText().trim();
        	textoConsultas.setText("");
        	
        	if(stmt.execute(sql)) // Si era una consulta
        		tabla.refresh(stmt.getResultSet());
        	else {
        		mostrarTablasBD();  // En caso de que se haya añadido o eliminado una tabla de la BD
        		model_Lista_Atrubutos_Tabla.removeAllElements(); // En caso de que se este mostrando los atributos de una tabla modificada
        		tabla.removeAllRows();// En caso de que se este mostrando una consulta sobre de una tabla modificada
        		JOptionPane.showMessageDialog(null,"Se actualizó la base de datos");  // Si era una alta, baja o modificacion
        	}
        	
        	stmt.close();
        	

            for (int i = 0; i < tabla.getColumnCount(); i++)
            { // para que muestre correctamente los valores de tipo TIME (hora)
                if     (tabla.getColumn(i).getType()== Types.TIME)
                {
                    tabla.getColumn(i).setType(Types.CHAR);
                }
                // cambiar el formato en que se muestran los valores de tipo DATE
                if     (tabla.getColumn(i).getType()==Types.DATE)
                {
                    tabla.getColumn(i).setDateFormat("dd/MM/YYYY");
                }
            }

            //tabla.refresh();

        } catch (SQLException ex) {
            // en caso de error, se muestra la causa en la consola
            JOptionPane.showMessageDialog(this,
                    "Se produjo un error al intentar realizar la consulta.\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    private void mostrarTablasBD(){

        try
        {
        	model_Lista_Tablas.removeAllElements();
            Connection conexion = tabla.getConnection();
            Statement stmt = conexion.createStatement();
            String sql = "SHOW TABLES";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                model_Lista_Tablas.addElement(rs.getString(1));
            }

            stmt.close();
            rs.close();
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(this,
                    "Se produjo un error al insertar el nuevo registro.\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarArributosTabla(String nombreTabla){

        try
        {
            Connection conexion = tabla.getConnection();
            Statement stmt = conexion.createStatement();
            String sql = "DESCRIBE "+ nombreTabla;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()){
                model_Lista_Atrubutos_Tabla.addElement(rs.getString(1));
            }

            stmt.close();
            rs.close();
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(this,
                    "Se produjo un error al insertar el nuevo registro.\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}