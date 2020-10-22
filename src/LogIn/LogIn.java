package LogIn;

import GUI.Ventanas.VentanaAdmin;
import GUI.Ventanas.VentanaInspector;
import quick.dbtable.*;
import sun.rmi.runtime.Log;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;



public class LogIn extends Component {

    protected DBTable conexionBD = null;
    private JFrame nuevaVentana;

    public static LogIn instance = null;

    private LogIn(){
    }

    public static LogIn getLogIn(){
        if(instance==null)
            return instance = new LogIn();
        else return instance;
    }

    public void conectarBD(String user, String password){
    		
        if (this.conexionBD == null)
        {
            try
            {
                String driver ="com.mysql.cj.jdbc.Driver";
                String servidor = "localhost:3306";
                String baseDatos = "parquimetros";
                String uriConexion = "jdbc:mysql://" + servidor + "/" +
                        baseDatos + "?serverTimezone=America/Argentina/Buenos_Aires";

                conexionBD = new DBTable();
                this.conexionBD.connectDatabase(driver,uriConexion,user,password);
                nuevaVentana = nuevaVentana(user);
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(this,
                        "Se produjo un error al intentar conectarse a la base de datos.\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
            catch (ClassNotFoundException ex) {ex.printStackTrace();}
        }



    }


    private void desconectarBD() {
    	
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
    
    private JFrame nuevaVentana(String user) {
    	
    	if (user.equals("admin"))
            return new VentanaAdmin(conexionBD);

        else return new VentanaInspector(conexionBD);
    }


}
