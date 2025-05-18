package bibliotecaudb.gui;

import bibliotecaudb.modelo.usuario.Usuario;
import bibliotecaudb.servicios.UsuarioService;
import bibliotecaudb.servicios.impl.UsuarioServiceImpl;
import bibliotecaudb.excepciones.UsuarioException;
import bibliotecaudb.conexion.LogsError;
// import bibliotecaudb.gui.Configuraciones; 

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.sql.SQLException;
import javax.swing.JFrame; // Para setDefaultCloseOperation

public class InicioAdministrador extends javax.swing.JFrame {

    private Usuario usuarioLogueado; // Informacion del admin logueado
    private UsuarioService usuarioService; // Servicios de usuario
    private DefaultTableModel modeloTablaUsuarios; // Modelo para tabla de usuarios

    /**
     * Constructor por defecto, ventana sin usuario logueado.
     */
    public InicioAdministrador() {
        initComponents(); // Inicializa componentes graficos
        this.usuarioService = new UsuarioServiceImpl(); // Crea servicio de usuario
        this.setLocationRelativeTo(null); // Centra ventana
        this.setTitle("Panel de Administrador - Biblioteca UDB"); // Titulo ventana
        inicializarTablaUsuarios(); // Prepara tabla de usuarios
        // Si se entra sin login, deshabilitar funcionalidad
        if (usuarioLogueado == null) {
            lblNombreAdmin.setText("Usuario: NO AUTENTICADO");
            // Deshabilitar botones si no hay usuario
            btnIrAgregarUsuario.setEnabled(false);
            btnEditarUsuario.setEnabled(false);
            btnEliminarUsuario.setEnabled(false);
            btnIrConfiguraciones.setEnabled(false);
            // btnRefrescarTabla.setEnabled(false); // Podria quedar habilitado para reintentar
        }
    }

    /**
     * Constructor principal, administrador inicia sesion.
     * @param usuario Usuario admin logueado.
     */
    public InicioAdministrador(Usuario usuario) {
        initComponents(); // Inicializa componentes graficos
        this.usuarioLogueado = usuario; // Guarda usuario admin
        this.usuarioService = new UsuarioServiceImpl(); // Crea servicio de usuario
        this.setLocationRelativeTo(null); // Centra ventana
        this.setTitle("Panel de Administrador - Biblioteca UDB"); // Pone titulo
        inicializarTablaUsuarios(); // Prepara tabla

        if (this.usuarioLogueado != null && "Administrador".equals(this.usuarioLogueado.getTipoUsuario().getTipo())) {
            lblNombreAdmin.setText("Usuario Admin: " + this.usuarioLogueado.getNombre()); // Muestra nombre admin
            cargarUsuariosEnTabla(); // Carga usuarios en tabla
        } else {
            lblNombreAdmin.setText("Usuario: NO AUTORIZADO");
            JOptionPane.showMessageDialog(this, "Acceso no autorizado o tipo de usuario incorrecto.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            this.dispose(); // Cierra ventana si no es admin autorizado
        }
    }

    /**
     * Prepara tabla de usuarios, define columnas.
     */
    private void inicializarTablaUsuarios() {
        String[] columnas = {"ID", "Nombre", "Correo", "Tipo Usuario", "Estado"}; // Nombres columnas
        // Modelo de tabla no editable
        modeloTablaUsuarios = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Celdas no editables
            }
        };
        tblUsuarios.setModel(modeloTablaUsuarios); // Asigna modelo a tabla visual
    }

    /**
     * Carga lista de todos los usuarios en la tabla.
     */
    private void cargarUsuariosEnTabla() {
        modeloTablaUsuarios.setRowCount(0); // Limpia tabla antes de cargar
        if (usuarioLogueado == null) return; // Si no hay admin logueado, no hacer nada

        try {
            // Obtiene lista de usuarios (servicio verifica permisos)
            List<Usuario> listaUsuarios = usuarioService.obtenerTodosLosUsuarios(usuarioLogueado);
            for (Usuario u : listaUsuarios) { // Recorre cada usuario
                Object[] fila = { // Crea fila con datos del usuario
                        u.getId(),
                        u.getNombre(),
                        u.getCorreo(),
                        (u.getTipoUsuario() != null) ? u.getTipoUsuario().getTipo() : "N/D", // Muestra tipo o N/D
                        u.isEstado() ? "Activo" : "Inactivo" // Muestra Activo o Inactivo
                };
                modeloTablaUsuarios.addRow(fila); // Agrega fila al modelo
            }
        } catch (UsuarioException e) { // Error de logica de negocio (ej. sin permisos)
            LogsError.warn(InicioAdministrador.class, "Error de negocio al cargar usuarios: " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error al Cargar Usuarios", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) { // Error con base de datos
            LogsError.error(InicioAdministrador.class, "Error de SQL al cargar usuarios.", e);
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios de la BD.", "Error de Conexion", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnIrAgregarUsuario = new javax.swing.JButton();
        btnIrConfiguraciones = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        lblNombreAdmin = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnEliminarUsuario = new javax.swing.JButton();
        btnEditarUsuario = new javax.swing.JButton();
        txtIdUsuarioSeleccionado = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        btnRefrescarTabla = new javax.swing.JButton(); // Anadido para que coincida con el codigo

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        btnIrAgregarUsuario.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnIrAgregarUsuario.setText("Agregar Usuario");
        btnIrAgregarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIrAgregarUsuarioActionPerformed(evt);
            }
        });

        btnIrConfiguraciones.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnIrConfiguraciones.setText("Configuraciones");
        btnIrConfiguraciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIrConfiguracionesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnIrAgregarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(57, 57, 57)
                                .addComponent(btnIrConfiguraciones, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(102, 102, 102))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnIrAgregarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnIrConfiguraciones, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(30, Short.MAX_VALUE))
        );

        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String [] {
                        "Id_usuario", "Nombre", "correo", "Tipo Usuario"
                }
        ) {
            Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblUsuarios.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUsuarios);

        lblNombreAdmin.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblNombreAdmin.setText("Usuario: ");

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        btnEliminarUsuario.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEliminarUsuario.setText("Eliminar");
        btnEliminarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarUsuarioActionPerformed(evt);
            }
        });

        btnEditarUsuario.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEditarUsuario.setText("Editar");
        btnEditarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarUsuarioActionPerformed(evt);
            }
        });

        txtIdUsuarioSeleccionado.setEditable(false); // Campo ID no editable

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Id Usuario");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(btnEditarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(49, 49, 49)
                                                .addComponent(btnEliminarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(26, 26, 26)
                                                .addComponent(txtIdUsuarioSeleccionado, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(116, 116, 116))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtIdUsuarioSeleccionado, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                                        .addComponent(jLabel2))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnEditarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnEliminarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20))
        );

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnRefrescarTabla.setText("Refrescar Tabla"); // Boton para refrescar
        btnRefrescarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescarTablaActionPerformed(evt);
            }
        });


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblNombreAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE) // Ajustar tamano
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnRefrescarTabla) // Poner boton refrescar
                                                .addGap(18, 18, 18)
                                                .addComponent(btnSalir))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 593, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblNombreAdmin)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnSalir)
                                                .addComponent(btnRefrescarTabla))) // Anadir boton al layout
                                .addGap(28, 28, 28)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Accion boton "Agregar Usuario". Abre form para agregar nuevo usuario.
     */
    private void btnIrAgregarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIrAgregarUsuarioActionPerformed
        // Se ejecuta al clic en "Agregar Usuario".
        if (this.usuarioLogueado == null) { // Verifica admin logueado
            JOptionPane.showMessageDialog(this, "Se requiere un administrador logueado para esta accion.", "Error de Permiso", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Crea form para agregar, pasa admin actual y null (usuario nuevo)
        AgregarUsuario formAgregar = new AgregarUsuario(this.usuarioLogueado, null);
        formAgregar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana, no toda la app
        formAgregar.setVisible(true); // Hace visible el form
    }//GEN-LAST:event_btnIrAgregarUsuarioActionPerformed

    /**
     * Accion boton "Editar". Abre form para editar usuario seleccionado.
     */
    private void btnEditarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarUsuarioActionPerformed
        // Se ejecuta al clic en "Editar".
        int filaSeleccionada = tblUsuarios.getSelectedRow(); // Obtiene fila seleccionada
        if (filaSeleccionada == -1) { // Si no selecciono fila
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario de la tabla para editar.", "Ningun Usuario Seleccionado", JOptionPane.WARNING_MESSAGE);
            txtIdUsuarioSeleccionado.setText(""); // Limpia campo ID
            return;
        }
        if (this.usuarioLogueado == null) { // Verifica admin logueado
            JOptionPane.showMessageDialog(this, "Se requiere un administrador logueado para esta accion.", "Error de Permiso", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int idUsuarioAEditar = (Integer) tblUsuarios.getValueAt(filaSeleccionada, 0); // Obtiene ID de usuario de fila (columna 0)
            Usuario usuarioAEditar = usuarioService.obtenerUsuarioPorId(idUsuarioAEditar); // Busca usuario completo en BD

            if (usuarioAEditar != null) { // Si encuentra usuario
                // Abre form AgregarUsuario, pasa usuario a editar
                AgregarUsuario formEditar = new AgregarUsuario(this.usuarioLogueado, usuarioAEditar);
                formEditar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
                formEditar.setVisible(true); // Hace visible
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo encontrar el usuario seleccionado (ID: " + idUsuarioAEditar + ")", "Error", JOptionPane.ERROR_MESSAGE);
                cargarUsuariosEnTabla(); // Refresca tabla por si fue eliminado
            }
        } catch (SQLException e) { // Error de base de datos
            LogsError.error(InicioAdministrador.class, "Error SQL al obtener usuario para editar.", e);
            JOptionPane.showMessageDialog(this, "Error al obtener datos del usuario.", "Error de BD", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) { // Otro error inesperado
            LogsError.error(InicioAdministrador.class, "Error inesperado al intentar editar usuario.", e);
            JOptionPane.showMessageDialog(this, "Error inesperado. Verifique la seleccion.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditarUsuarioActionPerformed

    /**
     * Evento al clic en fila de tabla usuarios. Muestra ID en campo texto.
     */
    private void tblUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsuariosMouseClicked
        // Se ejecuta al clic en tabla.
        int filaSeleccionada = tblUsuarios.getSelectedRow(); // Obtiene fila clickeada
        if (filaSeleccionada != -1) { // Si selecciono fila valida
            Object idObj = tblUsuarios.getValueAt(filaSeleccionada, 0); // Obtiene valor de primera columna (ID)
            txtIdUsuarioSeleccionado.setText(idObj != null ? idObj.toString() : ""); // Pone ID en campo texto
        } else {
            txtIdUsuarioSeleccionado.setText(""); // Sino, limpia campo
        }
    }//GEN-LAST:event_tblUsuariosMouseClicked

    /**
     * Accion boton "Eliminar Usuario". Elimina usuario del ID en campo texto.
     */
    private void btnEliminarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarUsuarioActionPerformed
        // Se ejecuta al clic en "Eliminar".
        String idSeleccionadoStr = txtIdUsuarioSeleccionado.getText(); // Obtiene ID del campo texto
        if (idSeleccionadoStr.isEmpty()) { // Si no hay ID
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario de la tabla para eliminar.", "Ningun Usuario Seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (this.usuarioLogueado == null) { // Verifica admin logueado
            JOptionPane.showMessageDialog(this, "Se requiere un administrador logueado para esta accion.", "Error de Permiso", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int idUsuarioAEliminar = Integer.parseInt(idSeleccionadoStr); // Convierte ID a numero

            String nombreUsuario = ""; // Para mensaje confirmacion
            int filaSeleccionada = tblUsuarios.getSelectedRow(); // Ve si hay fila seleccionada
            // Intenta obtener nombre de tabla si seleccion coincide con ID
            if (filaSeleccionada != -1 && tblUsuarios.getValueAt(filaSeleccionada,0) instanceof Integer && ((Integer)tblUsuarios.getValueAt(filaSeleccionada,0)) == idUsuarioAEliminar ) {
                nombreUsuario = (String) tblUsuarios.getValueAt(filaSeleccionada, 1); // Columna Nombre
            } else { // Sino, busca nombre en BD
                Usuario tempUser = usuarioService.obtenerUsuarioPorId(idUsuarioAEliminar);
                if(tempUser != null) nombreUsuario = tempUser.getNombre();
            }


            if (usuarioLogueado.getId() == idUsuarioAEliminar) { // Admin no se puede eliminar a si mismo
                JOptionPane.showMessageDialog(this, "No puede eliminar su propia cuenta de administrador.", "Accion No Permitida", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Pide confirmacion antes de eliminar
            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "Esta seguro de que desea eliminar al usuario '" + nombreUsuario + "' (ID: " + idUsuarioAEliminar + ")?",
                    "Confirmar Eliminacion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) { // Si usuario confirma
                // Llama servicio para eliminar, pasa quien elimina
                boolean eliminado = usuarioService.eliminarUsuario(idUsuarioAEliminar, usuarioLogueado);

                if (eliminado) {
                    LogsError.info(InicioAdministrador.class, "Usuario ID: " + idUsuarioAEliminar + " eliminado por " + usuarioLogueado.getCorreo());
                    JOptionPane.showMessageDialog(this, "Usuario eliminado exitosamente.", "Eliminacion Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    txtIdUsuarioSeleccionado.setText(""); // Limpia campo ID
                    cargarUsuariosEnTabla(); // Recarga tabla
                } else {
                    // No deberia pasar si logica de eliminarUsuario maneja errores
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el usuario. Verifique los logs del sistema.", "Error de Eliminacion", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) { // Si ID no es numero
            LogsError.warn(InicioAdministrador.class, "ID de usuario seleccionado no es un numero valido: " + idSeleccionadoStr, e);
            JOptionPane.showMessageDialog(this, "El ID de usuario seleccionado no es valido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (UsuarioException e) { // Errores de logica (ej. sin permisos, no encontrado)
            LogsError.warn(InicioAdministrador.class, "Error de negocio al eliminar usuario: " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Eliminacion", JOptionPane.WARNING_MESSAGE);
        }
        catch (SQLException e) { // Errores de base de datos
            LogsError.error(InicioAdministrador.class, "Error SQL al eliminar usuario", e);
            String mensajeError = "Error de base de datos al eliminar el usuario.";
            // Mensaje especifico si es por llave foranea (FK)
            if (e.getMessage().toLowerCase().contains("foreign key constraint fails")) {
                mensajeError += "\nEl usuario podria tener prestamos u otros registros asociados.";
            }
            JOptionPane.showMessageDialog(this, mensajeError, "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarUsuarioActionPerformed

    /**
     * Accion boton "Configuraciones". Abre ventana de configuraciones.
     */
    private void btnIrConfiguracionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIrConfiguracionesActionPerformed
        // Se ejecuta al clic en "Configuraciones".
        if (this.usuarioLogueado == null || !"Administrador".equals(this.usuarioLogueado.getTipoUsuario().getTipo())) {
            JOptionPane.showMessageDialog(this, "Acceso denegado. Se requiere ser administrador.", "Error de Permiso", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crea y muestra ventana de configuraciones, pasa admin logueado
        Configuraciones formConfig = new Configuraciones(this.usuarioLogueado);
        formConfig.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta
        formConfig.setVisible(true);
    }//GEN-LAST:event_btnIrConfiguracionesActionPerformed

    /**
     * Accion boton "Refrescar Tabla". Recarga datos de usuarios en tabla.
     */
    private void btnRefrescarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarTablaActionPerformed
        // Se ejecuta al clic en "Refrescar Tabla".
        cargarUsuariosEnTabla(); // Llama metodo que carga usuarios
        txtIdUsuarioSeleccionado.setText(""); // Limpia campo ID seleccionado
        LogsError.info(InicioAdministrador.class, "Tabla de usuarios refrescada manualmente.");
    }//GEN-LAST:event_btnRefrescarTablaActionPerformed

    /**
     * Evento cuando ventana gana foco. Refresca tabla usuarios automaticamente.
     */
    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        // Se ejecuta cuando ventana se vuelve activa.
        // Ej. despues de cerrar "Agregar Usuario".
        if (this.usuarioLogueado != null) { // Solo si hay admin logueado
            LogsError.info(InicioAdministrador.class, "Ventana InicioAdministrador gano foco, refrescando tabla.");
            cargarUsuariosEnTabla(); // Recarga tabla
        }
    }//GEN-LAST:event_formWindowGainedFocus

    /**
     * Accion boton "Salir". Cierra ventana actual y muestra Login.
     */
    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // Se ejecuta al clic en "Salir".
        LogsError.info(InicioAdministrador.class, "Usuario " + (usuarioLogueado != null ? usuarioLogueado.getCorreo() : "desconocido") + " saliendo del panel de administrador.");
        this.dispose(); // Cierra esta ventana (InicioAdministrador)
        LoginForm loginForm = new LoginForm(); // Crea nueva instancia de Login
        loginForm.setVisible(true); // Hace visible Login
    }//GEN-LAST:event_btnSalirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditarUsuario;
    private javax.swing.JButton btnEliminarUsuario;
    private javax.swing.JButton btnIrAgregarUsuario;
    private javax.swing.JButton btnIrConfiguraciones;
    private javax.swing.JButton btnRefrescarTabla;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblNombreAdmin;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JTextField txtIdUsuarioSeleccionado;
    // End of variables declaration//GEN-END:variables
}