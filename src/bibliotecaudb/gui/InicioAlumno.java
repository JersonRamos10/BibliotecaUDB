package bibliotecaudb.gui; // Paquete correcto

import bibliotecaudb.modelo.usuario.Usuario;
import bibliotecaudb.modelo.biblioteca.Documento;
import bibliotecaudb.modelo.biblioteca.Ejemplar;
import bibliotecaudb.modelo.biblioteca.Prestamo;
import bibliotecaudb.modelo.biblioteca.Devolucion; // Para historial
import bibliotecaudb.servicios.BibliotecaService;
import bibliotecaudb.servicios.impl.BibliotecaServiceImpl;
import bibliotecaudb.excepciones.BibliotecaException;
import bibliotecaudb.conexion.LogsError;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;
import javax.swing.JFrame;

public class InicioAlumno extends javax.swing.JFrame {

    private Usuario usuarioLogueado; // Info alumno logueado
    private BibliotecaService bibliotecaService; // Operaciones biblioteca
    private DefaultTableModel modeloTablaDocumentos;
    private DefaultTableModel modeloTablaPrestamosActivos;
    private DefaultTableModel modeloTablaHistorialDevoluciones;

    /**
     * Constructor por defecto.
     */
    public InicioAlumno() {
        initComponents();
        this.bibliotecaService = new BibliotecaServiceImpl();
        this.setLocationRelativeTo(null);
        this.setTitle("Panel de Alumno - Biblioteca UDB");
        lblUsuarioLogueado.setText("Usuario: DESCONOCIDO");
        // Deshabilitar si no hay usuario
        btnBuscarDocumento.setEnabled(false);
        // Tablas vacias si no hay datos
        inicializarTablas();
    }

    /**
     * Constructor principal cuando alumno inicia sesion.
     * @param usuario Objeto Usuario del alumno.
     */
    public InicioAlumno(Usuario usuario) {
        initComponents();
        this.usuarioLogueado = usuario;
        this.bibliotecaService = new BibliotecaServiceImpl();
        this.setLocationRelativeTo(null);
        this.setTitle("Portal del Alumno - Biblioteca UDB");

        if (this.usuarioLogueado != null &&
                (this.usuarioLogueado.getTipoUsuario() != null &&
                        "Alumno".equals(this.usuarioLogueado.getTipoUsuario().getTipo())
                )
        ) {
            lblUsuarioLogueado.setText("Bienvenido(a): " + this.usuarioLogueado.getNombre());
            inicializarTablas();
            cargarDocumentosEnTabla(""); // Cargar todos los documentos o populares/nuevos
            cargarPrestamosActivos();
            cargarHistorialDevoluciones();
        } else {
            lblUsuarioLogueado.setText("Usuario: NO AUTORIZADO");
            JOptionPane.showMessageDialog(this, "Acceso no autorizado. Se requiere ser Alumno.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            btnBuscarDocumento.setEnabled(false);
            // Considerar this.dispose();
        }
    }

    /**
     * Prepara las tres tablas de la interfaz del alumno.
     */
    private void inicializarTablas() {
        // Configura columnas para cada tabla.

        // Tabla Documentos Disponibles
        String[] columnasDocumentos = {"ID Doc", "Titulo", "Autor", "Tipo", "Anio", "Ej. Disp."};
        modeloTablaDocumentos = new DefaultTableModel(columnasDocumentos, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblDocumentosDisponibles.setModel(modeloTablaDocumentos);

        // Tabla Prestamos Activos
        String[] columnasPrestamos = {"ID Prestamo", "Titulo Documento", "F. Prestamo", "F. Limite", "Mora Estimada"};
        modeloTablaPrestamosActivos = new DefaultTableModel(columnasPrestamos, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblMisPrestamosActivos.setModel(modeloTablaPrestamosActivos);

        // Tabla Historial Devoluciones
        String[] columnasDevoluciones = {"ID Devol.", "Titulo Documento", "F. Prestamo", "F. Devolucion", "Mora Pagada"};
        modeloTablaHistorialDevoluciones = new DefaultTableModel(columnasDevoluciones, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tblMisDevoluciones.setModel(modeloTablaHistorialDevoluciones);
    }

    /**
     * Carga documentos en tabla de busqueda.
     * @param termino Texto a buscar. Vacio para todos/recientes.
     */
    private void cargarDocumentosEnTabla(String termino) {
        // Llena tabla de documentos disponibles.
        modeloTablaDocumentos.setRowCount(0);
        if (usuarioLogueado == null || bibliotecaService == null) return;

        try {
            List<Documento> listaDocumentos = bibliotecaService.buscarDocumentos(termino);
            if (listaDocumentos.isEmpty() && !termino.isEmpty()) {
                // Tabla vacia es suficiente indicativo.
                // LogsError.info(this.getClass(), "Busqueda de documentos sin resultados para: " + termino);
            }

            for (Documento doc : listaDocumentos) {
                long disponibles = 0;
                try {
                    Map<String, Object> detalleDoc = bibliotecaService.consultarDetalleDocumento(doc.getId());
                    if (detalleDoc.get("ejemplaresDisponibles") instanceof Long) {
                        disponibles = (Long) detalleDoc.get("ejemplaresDisponibles");
                    }
                } catch (Exception e) { // SQLException o BibliotecaException
                    LogsError.warn(this.getClass(), "No se pudo obtener ejemplares disponibles para doc ID " + doc.getId() + ": " + e.getMessage());
                }

                // Solo mostrar docs con ejemplares disponibles
                if (disponibles > 0) {
                    modeloTablaDocumentos.addRow(new Object[]{
                            doc.getId(),
                            doc.getTitulo(),
                            doc.getAutor() != null ? doc.getAutor() : "-",
                            doc.getTipoDocumento() != null ? doc.getTipoDocumento().getTipo() : "N/D",
                            doc.getAnioPublicacion() != null ? doc.getAnioPublicacion() : "-",
                            disponibles
                    });
                }
            }
        } catch (SQLException e) {
            LogsError.error(this.getClass(), "Error SQL al buscar documentos para alumno: " + termino, e);
            JOptionPane.showMessageDialog(this, "Error al buscar documentos.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga prestamos activos del alumno logueado.
     */
    private void cargarPrestamosActivos() {
        // Llena tabla de prestamos activos del alumno.
        modeloTablaPrestamosActivos.setRowCount(0);
        if (usuarioLogueado == null || bibliotecaService == null) return;

        try {
            List<Prestamo> prestamos = bibliotecaService.obtenerPrestamosActivosUsuario(usuarioLogueado.getId());
            for (Prestamo p : prestamos) {
                String tituloDoc = (p.getEjemplar() != null && p.getEjemplar().getDocumento() != null) ?
                        p.getEjemplar().getDocumento().getTitulo() : "N/D";
                // Calcular mora estimada si fecha limite paso
                BigDecimal moraEstimada = BigDecimal.ZERO;
                if (LocalDate.now().isAfter(p.getFechaLimite())) {
                    // Para estimacion simple, indicar que hay mora.
                    // O si servicio mora es accesible, calcularla.
                    moraEstimada = p.getMora() != null ? p.getMora() : BigDecimal.ZERO;
                    // Si quieres recalcular:
                    // MoraService moraServ = new MoraServiceImpl(); // o inyectar
                    // moraEstimada = moraServ.calcularMoraParaPrestamo(p, LocalDate.now());
                }

                modeloTablaPrestamosActivos.addRow(new Object[]{
                        p.getId(),
                        tituloDoc,
                        p.getFechaPrestamo(),
                        p.getFechaLimite(),
                        String.format("%.2f", moraEstimada) // Mostrar con dos decimales
                });
            }
        } catch (SQLException e) {
            LogsError.error(this.getClass(), "Error SQL al cargar prestamos activos del alumno.", e);
            JOptionPane.showMessageDialog(this, "Error al cargar sus prestamos activos.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        } /*catch (BibliotecaException e) { // Si calcularMoraParaPrestamo lanzara BibliotecaException
            LogsError.warn(this.getClass(), "Error de negocio al calcular mora estimada para tabla: " + e.getMessage());
        }*/
    }

    /**
     * Carga historial de devoluciones del alumno logueado.
     */
    private void cargarHistorialDevoluciones() {
        // Llena tabla con historial de devoluciones del alumno.
        modeloTablaHistorialDevoluciones.setRowCount(0);
        if (usuarioLogueado == null || bibliotecaService == null) return;

        try {
            // Necesita metodo en BibliotecaService para obtener historial devoluciones
            // Ejemplo: List<Devolucion> historial = bibliotecaService.obtenerHistorialDevolucionesUsuario(usuarioLogueado.getId());
            // Por ahora, simula con prestamos con fecha devolucion.
            // Ideal: tabla 'devoluciones' y servicio para ella.

            List<Prestamo> prestamosHistorial = bibliotecaService.obtenerHistorialPrestamosUsuario(usuarioLogueado.getId());

            for (Prestamo p : prestamosHistorial) {
                if (p.getFechaDevolucion() != null) { // Solo los devueltos
                    String tituloDoc = (p.getEjemplar() != null && p.getEjemplar().getDocumento() != null) ?
                            p.getEjemplar().getDocumento().getTitulo() : "N/D";
                    // ID devolucion vendria de tabla 'devoluciones'.
                    // Por ahora, ID prestamo o placeholder.
                    modeloTablaHistorialDevoluciones.addRow(new Object[]{
                            p.getId(), // Idealmente ID devolucion
                            tituloDoc,
                            p.getFechaPrestamo(),
                            p.getFechaDevolucion(),
                            p.getMora() != null ? String.format("%.2f", p.getMora()) : "0.00"
                    });
                }
            }
        } catch (SQLException e) {
            LogsError.error(this.getClass(), "Error SQL al cargar historial de devoluciones del alumno.", e);
            JOptionPane.showMessageDialog(this, "Error al cargar su historial de devoluciones.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblDocumentosDisponibles = new javax.swing.JTable();
        lblUsuarioLogueado = new javax.swing.JLabel();
        txtBusquedaDocumento = new javax.swing.JTextField();
        btnBuscarDocumento = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMisDevoluciones = new javax.swing.JTable();
        lblMisPrestamosEtiqueta = new javax.swing.JLabel(); // RENOMBRADO de jLabel2
        lblDocumentosEtiqueta = new javax.swing.JLabel(); // RENOMBRADO de jLabel3
        jScrollPane3 = new javax.swing.JScrollPane();
        tblMisPrestamosActivos = new javax.swing.JTable();
        lblMisDevolucionesEtiqueta = new javax.swing.JLabel(); // RENOMBRADO de jLabel4
        btnSalir = new javax.swing.JButton(); // NUEVO boton de salir

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblDocumentosDisponibles.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {},
                        {},
                        {},
                        {}
                },
                new String [] {

                }
        ));
        jScrollPane1.setViewportView(tblDocumentosDisponibles);

        lblUsuarioLogueado.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblUsuarioLogueado.setText("Usuario: ");

        btnBuscarDocumento.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBuscarDocumento.setText("Buscar Documentos");
        btnBuscarDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarDocumentoActionPerformed(evt);
            }
        });

        tblMisDevoluciones.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {},
                        {},
                        {},
                        {}
                },
                new String [] {

                }
        ));
        jScrollPane2.setViewportView(tblMisDevoluciones);

        lblMisPrestamosEtiqueta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMisPrestamosEtiqueta.setText("Mis Prestamos Activos:");

        lblDocumentosEtiqueta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDocumentosEtiqueta.setText("Buscar Documentos Disponibles:");

        tblMisPrestamosActivos.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {},
                        {},
                        {},
                        {}
                },
                new String [] {

                }
        ));
        jScrollPane3.setViewportView(tblMisPrestamosActivos);

        lblMisDevolucionesEtiqueta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMisDevolucionesEtiqueta.setText("Mi Historial de Devoluciones:");

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1)
                                        .addComponent(jScrollPane3)
                                        .addComponent(jScrollPane2)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblMisPrestamosEtiqueta, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblMisDevolucionesEtiqueta, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(lblDocumentosEtiqueta, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(txtBusquedaDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnBuscarDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(lblUsuarioLogueado, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                                                                .addComponent(btnSalir)))
                                                .addGap(0, 6, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblUsuarioLogueado)
                                        .addComponent(btnSalir))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblDocumentosEtiqueta)
                                        .addComponent(txtBusquedaDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnBuscarDocumento))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblMisPrestamosEtiqueta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblMisDevolucionesEtiqueta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Accion boton "Buscar Documentos". Carga tabla con termino de busqueda.
     */
    private void btnBuscarDocumentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarDocumentoActionPerformed
        // Se ejecuta al clic en "Buscar Documentos".
        if (usuarioLogueado == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesion para buscar documentos.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String termino = txtBusquedaDocumento.getText().trim();
        LogsError.info(this.getClass(), "Alumno " + usuarioLogueado.getCorreo() + " buscando documentos con termino: " + termino);
        cargarDocumentosEnTabla(termino);
    }//GEN-LAST:event_btnBuscarDocumentoActionPerformed

    /**
     * Accion boton "Salir". Cierra ventana actual y muestra login.
     */
    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // Se ejecuta al clic en "Salir".
        LogsError.info(this.getClass(), "Alumno " + (usuarioLogueado != null ? usuarioLogueado.getCorreo() : "desconocido") + " saliendo del portal.");
        this.dispose(); // Cierra esta ventana
        LoginForm loginForm = new LoginForm(); // Crea nueva instancia login
        loginForm.setVisible(true); // La hace visible
    }//GEN-LAST:event_btnSalirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarDocumento;
    private javax.swing.JButton btnSalir;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblDocumentosEtiqueta;
    private javax.swing.JLabel lblMisDevolucionesEtiqueta;
    private javax.swing.JLabel lblMisPrestamosEtiqueta;
    private javax.swing.JLabel lblUsuarioLogueado;
    private javax.swing.JTable tblDocumentosDisponibles;
    private javax.swing.JTable tblMisDevoluciones;
    private javax.swing.JTable tblMisPrestamosActivos;
    private javax.swing.JTextField txtBusquedaDocumento;
    // End of variables declaration//GEN-END:variables
}