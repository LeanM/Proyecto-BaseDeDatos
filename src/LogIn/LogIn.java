package LogIn;

import GUI.VentanaSelect;
import quick.dbtable.*;
import sun.rmi.runtime.Log;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;



public class LogIn extends Component {

    private DBTable conexionBD = null;
    private VentanaSelect ventanaSelect;

    public static LogIn instance = null;

    private LogIn(){
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
        if (this.conexionBD == null)
        {   //Intenta conectar con la base de datos utilizando DBTable
            try
            {
                String driver ="com.mysql.cj.jdbc.Driver";
                String servidor = "localhost:3306";
                String baseDatos = "parquimetros";
                String uriConexion = "jdbc:mysql://" + servidor + "/" +
                        baseDatos + "?serverTimezone=America/Argentina/Buenos_Aires";

                conexionBD = new DBTable();
                this.conexionBD.connectDatabase(driver,uriConexion,user,password); // Conecta con la base de datos
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
    private void desconectarBD()
    {
        if (this.conexionBD != null)
        {
            try
            {
                this.conexionBD.close();
                this.conexionBD = null;
            }
            catch (SQLException ex)
            {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }
    }
    
   /* private void ventanaSelect(String user) {
    	ventanaSelect = new VentanaSelect(user,conexionBD); // Se selecciona la ventana a inicializar si la conexion se realizo correctamente
    }*/

}
