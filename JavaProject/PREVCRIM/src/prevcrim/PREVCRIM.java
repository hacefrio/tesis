/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prevcrim;

import view.Login;
import view.SplashScreen;

/**
 *
 * @author Sebastian
 */
public class PREVCRIM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        SplashScreen sc=new SplashScreen();
        sc.setVisible(true);

        Thread.sleep(5000);
        sc.dispose();
        Login login=new Login();
        login.setVisible(true);
        
    }
    
}
