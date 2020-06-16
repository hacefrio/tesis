/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prevcrim;

import conexion.Connect;
import view.Login;
import view.SplashScreen;

/**
 *
 * @author Sebastian
 */
public class PREVCRIM {

    Connect c = new Connect();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        boolean estadoBaseDeDatos = true;
        SplashScreen sc = new SplashScreen();
        sc.setVisible(true);
        Thread.sleep(5000);
        estadoBaseDeDatos = Connect.testConnection();
        if (estadoBaseDeDatos) {
            sc.dispose();
            Login login = new Login();
            login.setVisible(true);
        } else {
            sc.dispose();
        }

    }
}
