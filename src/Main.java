import GUI.MenuLogIn;

public class Main {


    private static MenuLogIn login;

    public static void main (String [] ags){
        login = MenuLogIn.getMenu(); // Ejecuta el constructor de la clase MenuLogIn la cual inicializa la GUI para iniciar sesion.
    }
}
