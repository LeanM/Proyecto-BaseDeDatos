package GUI;

import GUI.Ventanas.VentanaAdmin;
import quick.dbtable.DBTable;

public class VentanaSelect {

    VentanaAdmin ventanaAdmin;

    public VentanaSelect(String user,DBTable conexion){
        if (user.equals("admin"))
            ventanaAdmin = new VentanaAdmin(conexion);

        else if (user.equals("inspector"));
    }
}
