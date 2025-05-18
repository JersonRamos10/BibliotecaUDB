package bibliotecaudb.gui;

import bibliotecaudb.modelo.usuario.Usuario;
import bibliotecaudb.servicios.UsuarioService;
import bibliotecaudb.servicios.impl.UsuarioServiceImpl;
import bibliotecaudb.excepciones.UsuarioException;
import bibliotecaudb.conexion.LogsError;

import javax.swing.JOptionPane;
import java.sql.SQLException;

public class LoginForm extends javax.swing.JFrame {

    private UsuarioService usuarioService;

    public LoginForm() {
        initComponents(); // (A) Inicializacion GUI

        this.usuarioService = new UsuarioServiceImpl();
        this.setLocationRelativeTo(null);
        this.setTitle("Inicio de Sesion - Biblioteca UDB");
        // txtContraseña.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtCorreo = new javax.swing.JTextField();
        btnIniciarSesion = new javax.swing.JButton();
        lblCorreoEtiqueta = new javax.swing.JLabel();
        lblContrasenaEtiqueta = new javax.swing.JLabel();
        lblIconoLogin = new javax.swing.JLabel();
        txtContrasena = new javax.swing.JPasswordField(); // Campo contrasena

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        // setTitle("Biblioteca UDB - Inicio de Sesion"); // Titulo ya puesto en constructor

        btnIniciarSesion.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnIniciarSesion.setText("Iniciar Sesion");

        btnIniciarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarSesionActionPerformed(evt);
            }
        });

        lblCorreoEtiqueta.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblCorreoEtiqueta.setText("Correo electronico");

        lblContrasenaEtiqueta.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblContrasenaEtiqueta.setText("Contraseña");


        // Si no, getResource() devuelve null, new ImageIcon(null) puede dar problemas.
        lblIconoLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bibliotecaudb/imagenes/loginIcono.png"))); // NOI18N

        // txtContrasena.setText("jPasswordField1"); // QUITAR. Establece texto por defecto.


        // Codigo de layout complejo y sensible. Errores aqui dificiles de detectar.
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(90, 90, 90)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblContrasenaEtiqueta, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblCorreoEtiqueta, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(14, 14, 14)
                                                                .addComponent(lblIconoLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(98, 98, 98)
                                                .addComponent(btnIniciarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(109, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(lblIconoLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // Si icono es null, afecta tamano
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCorreoEtiqueta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblContrasenaEtiqueta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnIniciarSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIniciarSesionActionPerformed(java.awt.event.ActionEvent evt) {
        // ... (logica de login) ...
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        if (correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese su correo electronico.", "Campo Vacio", JOptionPane.WARNING_MESSAGE);
            txtCorreo.requestFocus();
            return;
        }
        if (contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese su contraseña.", "Campo Vacio", JOptionPane.WARNING_MESSAGE);
            txtContrasena.requestFocus();
            return;
        }

        try {
            LogsError.info(LoginForm.class, "Intento de login para usuario: " + correo);
            Usuario usuarioAutenticado = usuarioService.autenticarUsuario(correo, contrasena);

            JOptionPane.showMessageDialog(this, "Bienvenido(a) " + usuarioAutenticado.getNombre() + "!", "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
            LogsError.info(LoginForm.class, "Login exitoso para: " + usuarioAutenticado.getCorreo() + ", Tipo: " + usuarioAutenticado.getTipoUsuario().getTipo());

            String tipoUsuario = usuarioAutenticado.getTipoUsuario().getTipo();

            if ("Administrador".equals(tipoUsuario)) {
                InicioAdministrador adminFrame = new InicioAdministrador(usuarioAutenticado);
                adminFrame.setVisible(true);
            } else if ("Profesor".equals(tipoUsuario) || "Docente".equals(tipoUsuario)) {
                InicioDocente docenteFrame = new InicioDocente(usuarioAutenticado);
                docenteFrame.setVisible(true);
            } else if ("Alumno".equals(tipoUsuario)) {
                InicioAlumno alumnoFrame = new InicioAlumno(usuarioAutenticado);
                alumnoFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Tipo de usuario desconocido. Contacte al administrador.", "Error de Configuracion", JOptionPane.ERROR_MESSAGE);
                LogsError.error(LoginForm.class, "Tipo de usuario desconocido para: " + usuarioAutenticado.getCorreo() + " - Tipo: " + tipoUsuario);
                return;
            }
            this.dispose();

        } catch (UsuarioException e) {
            LogsError.warn(LoginForm.class, "Intento de login fallido (UsuarioException): " + correo + " - " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Autenticacion", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            LogsError.error(LoginForm.class, "Error de SQL durante el login para: " + correo, e);
            JOptionPane.showMessageDialog(this, "Error de conexion con la base de datos. Por favor, intente mas tarde.", "Error de Conexion", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException e) {
            LogsError.fatal(LoginForm.class, "Error de NullPointer durante el login para: " + correo + ". Objeto usuario o tipo podria ser null.", e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrio un error interno (puntero nulo). Verifique los datos y logs.", "Error Critico", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception e) {
            LogsError.fatal(LoginForm.class, "Error inesperado durante el login para: " + correo, e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ocurrio un error inesperado. Contacte al administrador.", "Error Critico", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ... (main method) ...
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) { // Captura general para L&F
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIniciarSesion;
    private javax.swing.JLabel lblContrasenaEtiqueta;
    private javax.swing.JLabel lblCorreoEtiqueta;
    private javax.swing.JLabel lblIconoLogin;
    private javax.swing.JPasswordField txtContrasena;
    private javax.swing.JTextField txtCorreo;
    // End of variables declaration//GEN-END:variables
}