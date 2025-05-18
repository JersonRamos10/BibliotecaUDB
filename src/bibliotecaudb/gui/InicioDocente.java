package bibliotecaudb.gui;

import bibliotecaudb.modelo.usuario.Usuario;
import bibliotecaudb.modelo.biblioteca.Documento;
import bibliotecaudb.modelo.biblioteca.Ejemplar;
import bibliotecaudb.servicios.BibliotecaService;
import bibliotecaudb.servicios.impl.BibliotecaServiceImpl;
import bibliotecaudb.excepciones.BibliotecaException;
import bibliotecaudb.conexion.LogsError;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent; // Para evento seleccion tabla
import javax.swing.event.ListSelectionListener; // Para evento seleccion tabla
import java.util.ArrayList; // Para inicializar listas vacias

public class InicioDocente extends javax.swing.JFrame {

    private Usuario usuarioLogueado;
    private BibliotecaService bibliotecaService;
    private DefaultTableModel modeloTablaDocumentos;
    private DefaultTableModel modeloTablaEjemplares; // Modelo para tabla ejemplares

    private Integer idDocumentoSeleccionadoEnTabla = null; // ID doc seleccionado en 1ra tabla
    private Integer idEjemplarSeleccionadoEnTabla = null;  // ID ejemplar seleccionado en 2da tabla

    public InicioDocente() {
        initComponents();
        this.bibliotecaService = new BibliotecaServiceImpl();
        this.setLocationRelativeTo(null);
        this.setTitle("Panel de Docente - Biblioteca UDB");
        lblNombreUsuarioLogueado.setText("Usuario: DESCONOCIDO");

        inicializarTablaDocumentos();
        inicializarTablaEjemplares();


        btnGestionarDocumentos.setEnabled(false);
        btnPrestarDocumento.setEnabled(false);
        btnDevolverDocumento.setEnabled(false);
        btnBuscarDocumento.setEnabled(false);

        txtIdEjemplarSeleccionado.setEditable(false);
        // Inicializar etiqueta tabla ejemplares
        lblTituloEjemplares.setText("Ejemplares Disponibles del Documento:");
    }

    public InicioDocente(Usuario usuario) {
        initComponents();
        this.usuarioLogueado = usuario;
        this.bibliotecaService = new BibliotecaServiceImpl();
        this.setLocationRelativeTo(null);
        this.setTitle("Panel de Docente - Biblioteca UDB (" + (usuario != null ? usuario.getNombre() : "N/D") + ")");


        txtIdEjemplarSeleccionado.setEditable(false);
        lblTituloEjemplares.setText("Seleccione un documento para ver sus ejemplares");


        if (this.usuarioLogueado != null &&
                (this.usuarioLogueado.getTipoUsuario() != null &&
                        ("Docente".equals(this.usuarioLogueado.getTipoUsuario().getTipo()) || "Profesor".equals(this.usuarioLogueado.getTipoUsuario().getTipo()))
                )
        ) {
            lblNombreUsuarioLogueado.setText("Usuario: " + this.usuarioLogueado.getNombre() + " (" + this.usuarioLogueado.getTipoUsuario().getTipo() + ")");
            inicializarTablaDocumentos();
            inicializarTablaEjemplares();
            cargarDocumentosEnTabla(""); // Cargar documentos al inicio

            // Listener para seleccion en tabla DOCUMENTOS
            tblResultadosDocumentos.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
                public void valueChanged(ListSelectionEvent event) {
                    if (!event.getValueIsAdjusting() && tblResultadosDocumentos.getSelectedRow() != -1) {
                        int filaDoc = tblResultadosDocumentos.getSelectedRow();
                        if (filaDoc >= 0) {
                            idDocumentoSeleccionadoEnTabla = (Integer) modeloTablaDocumentos.getValueAt(filaDoc, 0); // Col ID Doc
                            String tituloDoc = (String) modeloTablaDocumentos.getValueAt(filaDoc, 1); // Col Titulo
                            lblTituloEjemplares.setText("Ejemplares para: " + tituloDoc + " (ID: " + idDocumentoSeleccionadoEnTabla + ")");
                            cargarEjemplaresEnTabla(idDocumentoSeleccionadoEnTabla);
                            txtIdEjemplarSeleccionado.setText(""); // Limpiar seleccion ejemplar anterior
                            idEjemplarSeleccionadoEnTabla = null;
                        }
                    }
                }
            });

            // Listener para seleccion en tabla EJEMPLARES
            tblEjemplaresDisponibles.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
                public void valueChanged(ListSelectionEvent event) {
                    if (!event.getValueIsAdjusting() && tblEjemplaresDisponibles.getSelectedRow() != -1) {
                        int filaEj = tblEjemplaresDisponibles.getSelectedRow();
                        if (filaEj >= 0) {
                            idEjemplarSeleccionadoEnTabla = (Integer) modeloTablaEjemplares.getValueAt(filaEj, 0); // Col ID Ejemplar
                            txtIdEjemplarSeleccionado.setText(String.valueOf(idEjemplarSeleccionadoEnTabla));
                        }
                    }
                }
            });

        } else {
            lblNombreUsuarioLogueado.setText("Usuario: NO AUTORIZADO");
            JOptionPane.showMessageDialog(this, "Acceso no autorizado. Se requiere ser Docente o Profesor.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            btnGestionarDocumentos.setEnabled(false);
            btnPrestarDocumento.setEnabled(false);
            btnDevolverDocumento.setEnabled(false);
            btnBuscarDocumento.setEnabled(false);
        }
    }

    /**
     * Prepara tabla donde se mostraran documentos.
     */
    private void inicializarTablaDocumentos() {
        // Define columnas para tabla documentos.
        String[] columnas = {"ID Doc", "Titulo", "Autor", "Editorial", "Anio", "Tipo", "Total Ej.", "Disp."};
        modeloTablaDocumentos = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tblResultadosDocumentos.setModel(modeloTablaDocumentos);
    }

    /**
     * Prepara tabla donde se mostraran ejemplares del doc seleccionado.
     */
    private void inicializarTablaEjemplares() {
        // Define columnas para nueva tabla ejemplares.
        String[] columnas = {"ID Ejemplar", "Ubicacion", "Estado"};
        modeloTablaEjemplares = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tblEjemplaresDisponibles.setModel(modeloTablaEjemplares);
    }

    /**
     * Carga documentos en la tabla.
     * @param termino Texto a buscar. Si es vacio, muestra todos.
     */
    private void cargarDocumentosEnTabla(String termino) {
        // Llena tabla de documentos.
        modeloTablaDocumentos.setRowCount(0); // Limpiar tabla
        modeloTablaEjemplares.setRowCount(0); // Limpiar tabla ejemplares
        txtIdEjemplarSeleccionado.setText("");
        idDocumentoSeleccionadoEnTabla = null;
        idEjemplarSeleccionadoEnTabla = null;
        lblTituloEjemplares.setText("Seleccione un documento para ver sus ejemplares");


        if (usuarioLogueado == null || bibliotecaService == null) return;

        try {
            List<Documento> listaDocumentos = bibliotecaService.buscarDocumentos(termino);
            if (listaDocumentos.isEmpty() && !termino.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron documentos para: " + termino, "Busqueda Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            }

            for (Documento doc : listaDocumentos) {
                long disponibles = 0;
                int totalEjemplares = 0;
                try {
                    Map<String, Object> detalleDoc = bibliotecaService.consultarDetalleDocumento(doc.getId());
                    List<Ejemplar> listaEjs = (List<Ejemplar>) detalleDoc.getOrDefault("ejemplares", new ArrayList<Ejemplar>());
                    totalEjemplares = listaEjs.size();
                    disponibles = (long) detalleDoc.getOrDefault("ejemplaresDisponibles", 0L);

                } catch (Exception e) {
                    LogsError.warn(this.getClass(), "No se pudo obtener detalle de ejemplares para doc ID " + doc.getId() + ": " + e.getMessage());
                }

                modeloTablaDocumentos.addRow(new Object[]{
                        doc.getId(),
                        doc.getTitulo(),
                        doc.getAutor() != null ? doc.getAutor() : "-",
                        doc.getEditorial() != null ? doc.getEditorial() : "-",
                        doc.getAnioPublicacion() != null ? doc.getAnioPublicacion() : "-",
                        doc.getTipoDocumento() != null ? doc.getTipoDocumento().getTipo() : "N/D",
                        totalEjemplares,
                        disponibles
                });
            }
        } catch (SQLException e) {
            LogsError.error(this.getClass(), "Error SQL al buscar documentos: " + termino, e);
            JOptionPane.showMessageDialog(this, "Error al buscar documentos en la BD.", "Error de Conexion", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga ejemplares disponibles de un documento especifico.
     * @param idDoc ID del documento del cual cargar ejemplares.
     */
    private void cargarEjemplaresEnTabla(int idDoc) {
        // Llena tabla de ejemplares.
        modeloTablaEjemplares.setRowCount(0); // Limpiar tabla ejemplares
        txtIdEjemplarSeleccionado.setText("");
        idEjemplarSeleccionadoEnTabla = null;

        if (usuarioLogueado == null || bibliotecaService == null) return;

        try {
            Map<String, Object> detalleDoc = bibliotecaService.consultarDetalleDocumento(idDoc);
            if (detalleDoc.get("ejemplares") instanceof List) {
                List<Ejemplar> listaEjemplares = (List<Ejemplar>) detalleDoc.get("ejemplares");
                boolean ejemplaresParaMostrar = false;
                for (Ejemplar ej : listaEjemplares) {

                    // if (Ejemplar.ESTADO_DISPONIBLE.equals(ej.getEstado())) {
                    modeloTablaEjemplares.addRow(new Object[]{
                            ej.getId(),
                            ej.getUbicacion() != null ? ej.getUbicacion() : "-",
                            ej.getEstado()
                    });
                    ejemplaresParaMostrar = true;
                    // }
                }
                if (!ejemplaresParaMostrar) {
                    lblTituloEjemplares.setText("No hay ejemplares para el documento ID: " + idDoc);
                }
            }
        } catch (SQLException | BibliotecaException e) {
            LogsError.error(this.getClass(), "Error al cargar ejemplares para documento ID " + idDoc, e);
            JOptionPane.showMessageDialog(this, "Error al cargar ejemplares del documento.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNombreUsuarioLogueado = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblResultadosDocumentos = new javax.swing.JTable(); // Tabla principal documentos
        txtBusquedaDocumento = new javax.swing.JTextField();
        btnBuscarDocumento = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        btnGestionarDocumentos = new javax.swing.JButton();
        btnPrestarDocumento = new javax.swing.JButton();
        btnDevolverDocumento = new javax.swing.JButton();
        txtIdEjemplarSeleccionado = new javax.swing.JTextField(); // CAMPO MOSTRAR ID EJEMPLAR
        lblIdEjemplarInfo = new javax.swing.JLabel(); // ETIQUETA CAMPO ANTERIOR
        btnSalir = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEjemplaresDisponibles = new javax.swing.JTable(); // NUEVA TABLA EJEMPLARES
        lblTituloDocumentos = new javax.swing.JLabel(); // ETIQUETA TABLA DOCUMENTOS
        lblTituloEjemplares = new javax.swing.JLabel(); // ETIQUETA TABLA EJEMPLARES

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblNombreUsuarioLogueado.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblNombreUsuarioLogueado.setText("Usuario: ");

        tblResultadosDocumentos.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {}
        ));
        jScrollPane1.setViewportView(tblResultadosDocumentos);

        btnBuscarDocumento.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBuscarDocumento.setText("Buscar Documento");
        btnBuscarDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarDocumentoActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(51, 51, 51)); // Color oscuro panel

        btnGestionarDocumentos.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGestionarDocumentos.setText("Gestionar Catalogo");
        btnGestionarDocumentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGestionarDocumentosActionPerformed(evt);
            }
        });

        btnPrestarDocumento.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPrestarDocumento.setText("Realizar Prestamo");
        btnPrestarDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrestarDocumentoActionPerformed(evt);
            }
        });

        btnDevolverDocumento.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDevolverDocumento.setText("Registrar Devolucion");
        btnDevolverDocumento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevolverDocumentoActionPerformed(evt);
            }
        });

        txtIdEjemplarSeleccionado.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        lblIdEjemplarInfo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblIdEjemplarInfo.setForeground(new java.awt.Color(255, 255, 255));
        lblIdEjemplarInfo.setText("ID Ejemplar Seleccionado para Prestar:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnGestionarDocumentos, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(lblIdEjemplarInfo)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtIdEjemplarSeleccionado, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(btnPrestarDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnDevolverDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(15, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtIdEjemplarSeleccionado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblIdEjemplarInfo))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnGestionarDocumentos, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnPrestarDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDevolverDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15, 15, 15))
        );

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        tblEjemplaresDisponibles.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {}
        ));
        jScrollPane2.setViewportView(tblEjemplaresDisponibles);

        lblTituloDocumentos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTituloDocumentos.setText("Documentos en Catalogo:");

        lblTituloEjemplares.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTituloEjemplares.setText("Ejemplares del Documento Seleccionado:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblNombreUsuarioLogueado, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnSalir))
                                        .addComponent(jScrollPane2)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(txtBusquedaDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnBuscarDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(lblTituloDocumentos)
                                                        .addComponent(lblTituloEjemplares))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblNombreUsuarioLogueado)
                                        .addComponent(btnSalir))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtBusquedaDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnBuscarDocumento))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblTituloDocumentos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblTituloEjemplares)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarDocumentoActionPerformed(java.awt.event.ActionEvent evt) {
        if (usuarioLogueado == null) {
            JOptionPane.showMessageDialog(this, "Se requiere un usuario logueado para buscar.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String termino = txtBusquedaDocumento.getText().trim();
        LogsError.info(this.getClass(), "Docente " + usuarioLogueado.getCorreo() + " buscando documentos con termino: " + termino);
        cargarDocumentosEnTabla(termino);
    }

    private void btnGestionarDocumentosActionPerformed(java.awt.event.ActionEvent evt) {
        if (usuarioLogueado == null) {
            JOptionPane.showMessageDialog(this, "Se requiere un usuario logueado.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LogsError.info(this.getClass(), "Docente " + usuarioLogueado.getCorreo() + " abriendo gestion de documentos.");
        Documentos formDocumentos = new Documentos(this.usuarioLogueado, this.bibliotecaService);
        formDocumentos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formDocumentos.setVisible(true);
    }

    private void btnPrestarDocumentoActionPerformed(java.awt.event.ActionEvent evt) {
        if (usuarioLogueado == null) {
            JOptionPane.showMessageDialog(this, "Se requiere un usuario logueado.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if(idEjemplarSeleccionadoEnTabla == null || idEjemplarSeleccionadoEnTabla <= 0) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un documento y luego un ejemplar de la lista para prestar.", "Seleccion Requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //  }


        LogsError.info(this.getClass(), "Docente " + usuarioLogueado.getCorreo() + " abriendo ventana de prestamos para ejemplar ID: " + idEjemplarSeleccionadoEnTabla);
        Prestamos formPrestamos = new Prestamos(this.usuarioLogueado, this.bibliotecaService, idEjemplarSeleccionadoEnTabla);
        formPrestamos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formPrestamos.setVisible(true);
    }

    private void btnDevolverDocumentoActionPerformed(java.awt.event.ActionEvent evt) {
        if (usuarioLogueado == null) {
            JOptionPane.showMessageDialog(this, "Se requiere un usuario logueado.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LogsError.info(this.getClass(), "Docente " + usuarioLogueado.getCorreo() + " abriendo ventana de devoluciones.");
        Devoluciones formDevoluciones = new Devoluciones(this.usuarioLogueado, this.bibliotecaService);
        formDevoluciones.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formDevoluciones.setVisible(true);
    }

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {
        LogsError.info(InicioDocente.class, "Docente " + (usuarioLogueado != null ? usuarioLogueado.getCorreo() : "desconocido") + " saliendo del panel de docente.");
        this.dispose();
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarDocumento;
    private javax.swing.JButton btnDevolverDocumento;
    private javax.swing.JButton btnGestionarDocumentos;
    private javax.swing.JButton btnPrestarDocumento;
    private javax.swing.JButton btnSalir;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblIdEjemplarInfo;
    private javax.swing.JLabel lblNombreUsuarioLogueado;
    private javax.swing.JLabel lblTituloDocumentos;
    private javax.swing.JLabel lblTituloEjemplares;
    private javax.swing.JTable tblEjemplaresDisponibles;
    private javax.swing.JTable tblResultadosDocumentos;
    private javax.swing.JTextField txtBusquedaDocumento;
    private javax.swing.JTextField txtIdEjemplarSeleccionado;
    // End of variables declaration//GEN-END:variables
}