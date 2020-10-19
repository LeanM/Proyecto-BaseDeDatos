package GUI.Ventanas;

import GUI.MenuLogIn;
import LogIn.LogIn;
import quick.dbtable.DBTable;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class VentanaAdmin extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextArea textoConsultas;
    private JButton realizarConsulta;
    private JLabel mensajeError;
    private JList listaTablas,listaAtributosTabla;
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
        setBounds(200, 50, 1360, 768);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        textoConsultas = new JTextArea();
        textoConsultas.setBounds(30, 5, 800, 150);
        textoConsultas.setBackground(Color.LIGHT_GRAY);

        realizarConsulta = new JButton("Consultar");
        realizarConsulta.setBounds(850,30,100,50);
        realizarConsulta.setBackground(Color.DARK_GRAY);
        realizarConsulta.setForeground(Color.WHITE);

        realizarConsulta.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                refrescarTabla();
            }
        });


        mensajeError = new JLabel();
        mensajeError.setBounds(1150, 500, 200, 100);
        mensajeError.setForeground(Color.RED);

        listaAtributosTabla = new JList();
        listaAtributosTabla.setBounds(1000, 300, 100, 200);
        listaAtributosTabla.setBackground(Color.lightGray);
        model_Lista_Atrubutos_Tabla = new DefaultListModel<String>();
        listaAtributosTabla.setModel(model_Lista_Atrubutos_Tabla);

        listaTablas = new JList();
        listaTablas.setBounds(775, 300, 200, 300);
        listaTablas.setBackground(Color.lightGray);
        model_Lista_Tablas = new DefaultListModel<String>();
        listaTablas.setModel(model_Lista_Tablas);

        listaTablas.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                int indice = listaTablas.getSelectedIndex();
                String nombreTabla = model_Lista_Tablas.getElementAt(indice);

                model_Lista_Atrubutos_Tabla.removeAllElements();
                mostrarArributosTabla(nombreTabla);
            }
        });


        tabla.setBounds(30,170,500,500);
        tabla.setBackground(Color.LIGHT_GRAY);
        tabla.setEditable(false);

        contentPane.add(textoConsultas);
        contentPane.add(realizarConsulta);
        contentPane.add(mensajeError);
        contentPane.add(tabla);
        contentPane.add(listaTablas);
        contentPane.add(listaAtributosTabla);

        mostrarTablasBD();
        this.repaint();
    }

    private void refrescarTabla() {

        try {

            // se ejecuta la sentencia
            tabla.setSelectSql(textoConsultas.getText().trim());
            tabla.createColumnModelFromQuery();

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

            tabla.refresh();

        } catch (SQLException ex) {
            // en caso de error, se muestra la causa en la consola
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    private void mostrarTablasBD(){

        try
        {
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