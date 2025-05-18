package bibliotecaudb.gui;

import bibliotecaudb.modelo.usuario.Usuario;
import bibliotecaudb.modelo.usuario.TipoUsuario;
import bibliotecaudb.servicios.UsuarioService;
import bibliotecaudb.servicios.impl.UsuarioServiceImpl;
import bibliotecaudb.dao.usuario.TipoUsuarioDAO;
import bibliotecaudb.dao.usuario.impl.TipoUsuarioDAOImpl;
import bibliotecaudb.excepciones.UsuarioException;
import bibliotecaudb.conexion.LogsError;

import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;

public class AgregarUsuario extends javax.swing.JFrame {

    private UsuarioService usuarioService;
    private TipoUsuarioDAO tipoUsuarioDAO;
    private Usuario usuarioParaEditar; // Usuario para mostrar/editar
    private Usuario adminQueOpera;     // Admin que realiza operacion

    /**
     * Constructor principal para Agregar o Editar Usuario.
     * @param admin Admin que realiza operacion.
     * @param usuarioAEditar Usuario a editar, null si es nuevo.
     */
    public AgregarUsuario(Usuario admin, Usuario usuarioAEditar) {
        initComponents();
        this.usuarioService = new UsuarioServiceImpl();
        this.tipoUsuarioDAO = new TipoUsuarioDAOImpl();
        this.adminQueOpera = admin; // Guarda quien opera
        this.usuarioParaEditar = usuarioAEditar; // Guarda a quien se edita (si aplica)

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cargarTiposUsuarioComboBox();
        cargarEstadosComboBox();

        if (usuarioAEditar != null) { // Modo EDICION
            this.setTitle("Editar Usuario - Biblioteca UDB");
            btnGuardarUsuario.setText("Actualizar Datos"); // Texto general para boton
            popularFormularioParaEdicion();

            // LOGICA PARA HABILITAR CAMBIO CONTRASENA POR ADMIN
            if (adminQueOpera != null && "Administrador".equals(adminQueOpera.getTipoUsuario().getTipo())) {
                lblContrasena.setText("Nueva Contraseña (opcional):");
                txtContrasena.setEnabled(true); // Habilita campo
                txtContrasena.setText("");      // Vacio para nueva contrasena
                txtContrasena.setToolTipText("Dejar en blanco si no desea cambiar la contraseña actual.");
            } else {
                // Si no es admin (o no se permite aqui), se mantiene deshabilitado
                lblContrasena.setText("Contraseña (No se modifica aqui)");
                txtContrasena.setText("********"); // No mostrar nada
                txtContrasena.setEnabled(false);
            }
            // FIN LOGICA CAMBIO CONTRASENA

        } else { // Modo AGREGAR NUEVO USUARIO
            this.setTitle("Agregar Nuevo Usuario - Biblioteca UDB");
            btnGuardarUsuario.setText("Guardar Nuevo Usuario");
            lblContrasena.setText("Contrasena (*):"); // Obligatoria para nuevos
            txtContrasena.setEnabled(true);
            txtContrasena.setText("");
            txtContrasena.setToolTipText("Ingrese la contraseña para el nuevo usuario.");
        }
    }

    /**
     * Carga tipos de usuario en ComboBox.
     */
    private void cargarTiposUsuarioComboBox() {
        // Llena menu desplegable de tipos de usuario.
        try {
            List<TipoUsuario> tipos = tipoUsuarioDAO.obtenerTodos();
            DefaultComboBoxModel<String> modeloCombo = new DefaultComboBoxModel<>();
            if (tipos == null || tipos.isEmpty()) {
                modeloCombo.addElement("Error: Sin tipos");
                LogsError.warn(AgregarUsuario.class, "No se encontraron tipos de usuario para el ComboBox.");
            } else {
                for (TipoUsuario tipo : tipos) {
                    modeloCombo.addElement(tipo.getTipo());
                }
            }
            cmbTipoUsuario.setModel(modeloCombo);
        } catch (SQLException e) {
            LogsError.error(AgregarUsuario.class, "Error al cargar tipos de usuario en ComboBox", e);
            JOptionPane.showMessageDialog(this, "Error al cargar tipos de usuario.", "Error de Carga", JOptionPane.ERROR_MESSAGE);
            cmbTipoUsuario.setModel(new DefaultComboBoxModel<>(new String[]{"Error al cargar"}));
        }
    }

    /**
     * Configura ComboBox de estados.
     */
    private void cargarEstadosComboBox() {
        // Configura menu desplegable de estados.
        // Items "Activo", "Inactivo" ya en disenador GUI.
        if (this.usuarioParaEditar == null) { // Si es usuario nuevo
            cmbEstado.setSelectedItem("Activo"); // "Activo" por defecto
        }
    }

    /**
     * Llena campos del form con datos del usuario a editar.
     */
    private void popularFormularioParaEdicion() {
        // Pone datos del usuario a editar en campos.
        if (usuarioParaEditar != null) {
            txtNombre.setText(usuarioParaEditar.getNombre());
            txtCorreo.setText(usuarioParaEditar.getCorreo());


            if (usuarioParaEditar.getTipoUsuario() != null) {
                cmbTipoUsuario.setSelectedItem(usuarioParaEditar.getTipoUsuario().getTipo());
            } else { // Fallback si tipo es null pero tiene ID
                try {
                    TipoUsuario tu = tipoUsuarioDAO.obtenerPorId(usuarioParaEditar.getIdTipoUsuario());
                    if (tu != null) {
                        cmbTipoUsuario.setSelectedItem(tu.getTipo());
                    } else {
                        LogsError.warn(AgregarUsuario.class, "No se pudo encontrar el tipo de usuario ID: " + usuarioParaEditar.getIdTipoUsuario() + " para edicion.");
                        if (cmbTipoUsuario.getItemCount() > 0) cmbTipoUsuario.setSelectedIndex(0);
                    }
                } catch (SQLException e) {
                    LogsError.error(AgregarUsuario.class, "Error SQL obteniendo tipo de usuario para edicion", e);
                    if (cmbTipoUsuario.getItemCount() > 0) cmbTipoUsuario.setSelectedIndex(0);
                }
            }
            cmbEstado.setSelectedItem(usuarioParaEditar.isEstado() ? "Activo" : "Inactivo");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtNombre = new javax.swing.JTextField();
        lblNombreEtiqueta = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        lblCorreoEtiqueta = new javax.swing.JLabel();
        lblContrasena = new javax.swing.JLabel();
        lblTipoUsuarioEtiqueta = new javax.swing.JLabel();
        cmbTipoUsuario = new javax.swing.JComboBox<>();
        lblEstadoEtiqueta = new javax.swing.JLabel();
        btnGuardarUsuario = new javax.swing.JButton();
        cmbEstado = new javax.swing.JComboBox<>();
        txtContrasena = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblNombreEtiqueta.setText("Nombre:");

        txtCorreo.setToolTipText("");

        lblCorreoEtiqueta.setText("Correo:");

        lblContrasena.setText("Contraseña:");

        lblTipoUsuarioEtiqueta.setText("Tipo Usuario:");

        cmbTipoUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cargando..." }));

        lblEstadoEtiqueta.setText("Estado:");

        btnGuardarUsuario.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGuardarUsuario.setText("Guardar");
        btnGuardarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarUsuarioActionPerformed(evt);
            }
        });

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(30, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblNombreEtiqueta)
                                        .addComponent(lblCorreoEtiqueta)
                                        .addComponent(lblContrasena)
                                        .addComponent(lblTipoUsuarioEtiqueta)
                                        .addComponent(lblEstadoEtiqueta))
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cmbEstado, 0, 200, Short.MAX_VALUE)
                                        .addComponent(cmbTipoUsuario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtContrasena)
                                        .addComponent(txtCorreo)
                                        .addComponent(txtNombre))
                                .addGap(30, 30, 30))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(100, 100, 100)
                                .addComponent(btnGuardarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblNombreEtiqueta)
                                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblCorreoEtiqueta)
                                        .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblContrasena)
                                        .addComponent(txtContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblTipoUsuarioEtiqueta)
                                        .addComponent(cmbTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblEstadoEtiqueta)
                                        .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addComponent(btnGuardarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Accion boton "Guardar" o "Actualizar".
     * Recolecta datos, valida y llama servicio correspondiente.
     */
    private void btnGuardarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarUsuarioActionPerformed
        // Se ejecuta al clic en boton guardar o actualizar.
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String nuevaContrasenaIngresada = new String(txtContrasena.getPassword()).trim(); // Contraseña de JPasswordField
        Object tipoUsuarioSeleccionadoObj = cmbTipoUsuario.getSelectedItem();
        Object estadoSeleccionadoObj = cmbEstado.getSelectedItem();

        // Validaciones basicas
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Nombre es obligatorio.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus(); return;
        }
        if (correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Correo es obligatorio.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtCorreo.requestFocus(); return;
        }
        if (usuarioParaEditar == null && nuevaContrasenaIngresada.isEmpty()) { // NUEVO usuario, contrasena obligatoria
            JOptionPane.showMessageDialog(this, "La Contraseña es obligatoria para nuevos usuarios.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtContrasena.requestFocus(); return;
        }
        if (tipoUsuarioSeleccionadoObj == null || !(tipoUsuarioSeleccionadoObj instanceof String) ||
                "Cargando...".equals(tipoUsuarioSeleccionadoObj.toString()) ||
                "Error: Sin tipos".equals(tipoUsuarioSeleccionadoObj.toString()) ||
                "Error al cargar".equals(tipoUsuarioSeleccionadoObj.toString())) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Tipo de Usuario valido.", "Validacion", JOptionPane.WARNING_MESSAGE);
            cmbTipoUsuario.requestFocus(); return;
        }
        if (estadoSeleccionadoObj == null || !(estadoSeleccionadoObj instanceof String)) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Estado valido.", "Validacion", JOptionPane.WARNING_MESSAGE);
            cmbEstado.requestFocus(); return;
        }

        String tipoUsuarioSeleccionadoNombre = tipoUsuarioSeleccionadoObj.toString();
        String estadoSeleccionadoNombre = estadoSeleccionadoObj.toString();

        try {
            TipoUsuario tipoObj = tipoUsuarioDAO.obtenerPorNombre(tipoUsuarioSeleccionadoNombre);
            if (tipoObj == null) {
                JOptionPane.showMessageDialog(this, "El Tipo de Usuario seleccionado no es valido.", "Error de Datos", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean estadoBool = "Activo".equalsIgnoreCase(estadoSeleccionadoNombre);

            if (adminQueOpera == null) {
                JOptionPane.showMessageDialog(this, "Error: No se ha identificado al operador (administrador).", "Error de Sesion", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (usuarioParaEditar == null) { // --- MODO: AGREGAR NUEVO USUARIO ---
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setNombre(nombre);
                nuevoUsuario.setCorreo(correo);
                nuevoUsuario.setContrasena(nuevaContrasenaIngresada); // Contraseña para nuevo usuario
                nuevoUsuario.setIdTipoUsuario(tipoObj.getId());
                nuevoUsuario.setEstado(estadoBool);

                usuarioService.registrarNuevoUsuario(nuevoUsuario, adminQueOpera);
                LogsError.info(this.getClass(), "Usuario '" + nombre + "' registrado por " + adminQueOpera.getCorreo());
                JOptionPane.showMessageDialog(this, "Usuario '" + nombre + "' registrado exitosamente.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Cierra ventana actual

            } else { // --- MODO: EDITAR USUARIO EXISTENTE ---
                usuarioParaEditar.setNombre(nombre);
                usuarioParaEditar.setCorreo(correo);

                usuarioParaEditar.setIdTipoUsuario(tipoObj.getId());
                usuarioParaEditar.setEstado(estadoBool);

                boolean datosBasicosActualizados = usuarioService.actualizarDatosUsuario(usuarioParaEditar, adminQueOpera);

                if (datosBasicosActualizados) {
                    LogsError.info(this.getClass(), "Datos basicos del Usuario ID: " + usuarioParaEditar.getId() + " actualizados por " + adminQueOpera.getCorreo());

                    // Verifica si admin ingreso nueva contraseña
                    if (txtContrasena.isEnabled() && !nuevaContrasenaIngresada.isEmpty()) {
                        LogsError.info(this.getClass(), "Intentando restablecer contraseña para usuario: " + usuarioParaEditar.getCorreo());
                        boolean contrasenaCambiada = usuarioService.cambiarContrasena(usuarioParaEditar.getCorreo(), nuevaContrasenaIngresada, adminQueOpera);
                        if (contrasenaCambiada) {
                            LogsError.info(this.getClass(), "Contraseña para " + usuarioParaEditar.getCorreo() + " restablecida exitosamente.");
                            JOptionPane.showMessageDialog(this, "Usuario '" + nombre + "' actualizado y contraseña restablecida exitosamente.", "Actualizacion Completa", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            // Actualizacion datos basicos ok, cambio contraseña fallo
                            JOptionPane.showMessageDialog(this, "Datos del usuario actualizados, pero no se pudo restablecer la contraseña. Verifique los logs.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        // No se ingreso nueva contraseña, solo datos basicos
                        JOptionPane.showMessageDialog(this, "Usuario '" + nombre + "' actualizado exitosamente (contraseña no modificada).", "Actualizacion Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    }
                    this.dispose(); // Cierra ventana actual
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudieron actualizar los datos del usuario.", "Error de Actualizacion", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (UsuarioException e) {
            LogsError.warn(AgregarUsuario.class, "Error de negocio al guardar/actualizar usuario: " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Validacion", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            LogsError.error(AgregarUsuario.class, "Error SQL al guardar/actualizar usuario.", e);
            JOptionPane.showMessageDialog(this, "Error de base de datos al guardar/actualizar el usuario.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) { // Captura general errores inesperados
            LogsError.fatal(AgregarUsuario.class, "Error inesperado al guardar/actualizar usuario.", e);
            e.printStackTrace(); // Imprime traza error en consola
            JOptionPane.showMessageDialog(this, "Ocurrio un error inesperado: " + e.getMessage(), "Error Critico", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarUsuarioActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardarUsuario;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbTipoUsuario;
    private javax.swing.JLabel lblContrasena;
    private javax.swing.JLabel lblCorreoEtiqueta;
    private javax.swing.JLabel lblEstadoEtiqueta;
    private javax.swing.JLabel lblNombreEtiqueta;
    private javax.swing.JLabel lblTipoUsuarioEtiqueta;
    private javax.swing.JPasswordField txtContrasena;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}