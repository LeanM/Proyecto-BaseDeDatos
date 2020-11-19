package LogIn;

import GUI.Ventanas.MenuLogIn;
import GUI.Ventanas.VentanaAdmin;
import GUI.Ventanas.VentanaConexionParquimetro;
import GUI.Ventanas.VentanaInspector;
import quick.dbtable.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;



@SuppressWarnings("serial")
public class LogIn extends Component {

    private static DBTable conexionBD = null;
    private static JFrame ventanaActual;

    public static LogIn instance = null;

    private LogIn(){
    	ventanaActual = new MenuLogIn();
    }

    public static LogIn getLogIn(){
        if(instance==null)
            return instance = new LogIn();
        else return instance;
    }

    /**
     * Metodo encargado de realizar la conexion con la base de datos
     * usando el usuario "user" y la contraseña "password" pasados
     * como parametros
     *
     * @param user
     * @param password
     */

    public DBTable conectarBD(String user, String password){
        if (conexionBD == null)
        {   //Intenta conectar con la base de datos utilizando DBTable
            try
            {
                String driver ="com.mysql.cj.jdbc.Driver";
                String servidor = "localhost:3306";
                String baseDatos = "parquimetros";
                String uriConexion = "jdbc:mysql://" + servidor + "/" +
                        baseDatos + "?serverTimezone=America/Argentina/Buenos_Aires";

                conexionBD = new DBTable();
                conexionBD.connectDatabase(driver,uriConexion,user,password); // Conecta con la base de datos
            }
            catch (SQLException ex)
            {   // No se pudo conectar de forma correcta a la base de datos
                JOptionPane.showMessageDialog(this,
                        "Se produjo un error al intentar conectarse a la base de datos.\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());

                conexionBD = null;
            }
            catch (ClassNotFoundException ex) {ex.printStackTrace();}
            
        }
        
        return conexionBD;
    }

    /**
     *  Metodo encargado de desconectarse de la base de datos.
     */
    public static void desconectarBD()
    {
        if (conexionBD != null)
        {
            try
            {
                conexionBD.close();
                conexionBD = null;
            }
            catch (SQLException ex)
            {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }
    }
    
    public static JButton newVolverInicio() {
    	
    	JButton volverInicio = new JButton("Regresar al inicio");
	    volverInicio.setBackground(Color.DARK_GRAY);
	    volverInicio.setForeground(Color.WHITE);
	    volverInicio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ventanaActual.dispose();
				desconectarBD();
				ventanaActual=new MenuLogIn();
			}
	    });
	   
    	return volverInicio;
    }
    
    public void ingresoAdmin() {
    	ventanaActual.dispose();
    	ventanaActual=new VentanaAdmin(conexionBD); // ya se verifico que la conexion no sea nula
    }
    
   public void ingresoInpector(String legajo) {
	   ventanaActual.dispose();
	   ventanaActual=new VentanaInspector(conexionBD,legajo);  // ya se verifico que la conexion no sea nula
   }
   
   public void ingresoCDP() {
	   ventanaActual.dispose();
	   ventanaActual=new VentanaConexionParquimetro(conexionBD);  // ya se verifico que la conexion no sea nula
   }

}
