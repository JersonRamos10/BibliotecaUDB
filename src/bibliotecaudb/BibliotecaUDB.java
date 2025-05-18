package bibliotecaudb;

import bibliotecaudb.gui.LoginForm;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BibliotecaUDB {

    public static void main(String[] args) {
        // 1. Intentar establecer el Look & Feel de Windows (si está disponible)
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // Si falla, sigue con el L&F por defecto
            Logger.getLogger(BibliotecaUDB.class.getName())
                  .log(Level.WARNING, "No se pudo establecer el Look and Feel.", ex);
        }

        // 2. Lanzar la GUI en el Event Dispatch Thread de Swing
        java.awt.EventQueue.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
