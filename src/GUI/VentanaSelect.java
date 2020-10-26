package GUI;

import GUI.Ventanas.VentanaAdmin;
import GUI.Ventanas.VentanaInspector;
import quick.dbtable.DBTable;

/*
 *   Selecciona la venta a inicializar dependiendo del usuario logueado
 */

public class VentanaSelect {

    VentanaAdmin ventanaAdmin;
    VentanaInspector ventanaInspector;

    public VentanaSelect(String user,DBTable conexion){
        if (user.equals("admin")) // Si al loguear se introdujo como usuario "admin" y se valido la contraseña, ejecuta la ventana de admin
            ventanaAdmin = new VentanaAdmin(conexion);

        else if (user.equals("inspector")) // Si al loguear se introdujo como usuario "inspector" y se valido la contraseña, ejecuta la ventana de inspector
                ventanaInspector = new VentanaInspector(conexion);
    }
}
