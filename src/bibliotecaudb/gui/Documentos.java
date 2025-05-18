package bibliotecaudb.gui;

import bibliotecaudb.modelo.usuario.Usuario;
import bibliotecaudb.servicios.BibliotecaService;
import bibliotecaudb.modelo.biblioteca.Documento; // Para la tabla
import bibliotecaudb.modelo.biblioteca.Ejemplar; // Para contar disponibles
import bibliotecaudb.conexion.LogsError;
import bibliotecaudb.excepciones.BibliotecaException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;
import javax.swing.JFrame;

public class Documentos extends javax.swing.JFrame {

    private Usuario usuarioActual; // Usuario usando esta ventana (docente)
    private BibliotecaService servicioBib; // Servicio para operaciones de biblioteca
    private DefaultTableModel modeloTablaDocumentos; // Modelo para la tabla

    /**
     * Constructor por defecto (necesario para disenador NetBeans).
     */
    public Documentos() {
        initComponents();

        this.setLocationRelativeTo(null);
        this.setTitle("Gestion de Documentos");
        btnAbrirRegistroDoc.setEnabled(false);
        btnAgregarEjemplarADoc.setEnabled(false);
    }

    /**
     * Constructor principal llamado desde InicioDocente.
     * @param usuario Usuario (docente) que opera.
     * @param service Instancia del servicio de biblioteca.
     */
    public Documentos(Usuario usuario, BibliotecaService service) {
        initComponents();
        this.usuarioActual = usuario;
        this.servicioBib = service;
        this.setLocationRelativeTo(null);
        this.setTitle("Gestion de Documentos - Usuario: " + (usuario != null ? usuario.getNombre() : "N/D"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana



        inicializarTabla();
        cargarDocumentosEnTabla();
    }

    /**
     * Prepara tabla de documentos definiendo columnas.
     */
    private void inicializarTabla() {
        // Configura columnas de la tabla.
        String[] columnas = {"ID", "Titulo", "Autor", "Editorial", "Año Pub.", "Tipo Doc.", "Ej. Totales", "Ej. Disp."};
        modeloTablaDocumentos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edicion directa en tabla
            }
        };
        tblDocumentos.setModel(modeloTablaDocumentos); // Asumiendo JTable se llama tblDocumentos
    }

    /**
     * Carga todos los documentos en la tabla.
     */
    private void cargarDocumentosEnTabla() {
        // Llena tabla con documentos de base de datos.
        modeloTablaDocumentos.setRowCount(0); // Limpiar tabla
        if (servicioBib == null) {
            JOptionPane.showMessageDialog(this, "Error: Servicio de biblioteca no inicializado.", "Error Interno", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            List<Documento> documentos = servicioBib.buscarDocumentos(""); // "" para traer todos
            for (Documento doc : documentos) {
                int totalEjemplares = 0;
                long disponibles = 0;
                try {
                    Map<String, Object> detalle = servicioBib.consultarDetalleDocumento(doc.getId());
                    totalEjemplares = ((List<Ejemplar>) detalle.getOrDefault("ejemplares", new ArrayList<>())).size();
                    disponibles = (long) detalle.getOrDefault("ejemplaresDisponibles", 0L);
                } catch (BibliotecaException | SQLException e) {
                    LogsError.warn(this.getClass(), "No se pudo obtener detalle de ejemplares para doc ID " + doc.getId() + ". " + e.getMessage());
                }

                modeloTablaDocumentos.addRow(new Object[]{
                        doc.getId(),
                        doc.getTitulo(),
                        doc.getAutor(),
                        doc.getEditorial(),
                        doc.getAnioPublicacion(),
                        doc.getTipoDocumento() != null ? doc.getTipoDocumento().getTipo() : "N/D",
                        totalEjemplares,
                        disponibles
                });
            }
        } catch (SQLException e) {
            LogsError.error(this.getClass(), "Error SQL al cargar documentos.", e);
            JOptionPane.showMessageDialog(this, "Error al cargar documentos de la base de datos.", "Error de Conexion", JOptionPane.ERROR_MESSAGE);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblDocumentos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnAbrirRegistroDoc = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnAgregarEjemplarADoc = new javax.swing.JButton();
        txtIdDocParaEjemplar = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblDocumentos.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String [] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        tblDocumentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDocumentosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDocumentos);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Gestion de Documentos");

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        btnAbrirRegistroDoc.setText("Agregar Nuevo Documento");
        btnAbrirRegistroDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirRegistroDocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(35, Short.MAX_VALUE)
                                .addComponent(btnAbrirRegistroDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(btnAbrirRegistroDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(48, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        btnAgregarEjemplarADoc.setText("Agregar Ejemplar a Doc.");
        btnAgregarEjemplarADoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarEjemplarADocActionPerformed(evt);
            }
        });

        txtIdDocParaEjemplar.setEditable(false); // ID se toma de seleccion tabla

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID Documento Seleccionado:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtIdDocParaEjemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)) // Ajustado tamano
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                                .addComponent(btnAgregarEjemplarADoc, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE) // Ajustado tamano boton
                                .addGap(21, 21, 21))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnAgregarEjemplarADoc, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtIdDocParaEjemplar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextPane1.setEditable(false);
        jTextPane1.setBackground(new java.awt.Color(255, 255, 204));
        jTextPane1.setText("Seleccione un documento de la tabla para agregarle ejemplares, o agregue un nuevo documento.");
        jScrollPane2.setViewportView(jTextPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(16, 16, 16)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jScrollPane1)
                                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)) // Ajustado tamano
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(150, 150, 150) // Ajustado para centrar mejor
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE) // Ajustado tamano
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Accion boton "Agregar Nuevo Documento". Abre ventana para registrar nuevo documento.
     */
    private void btnAbrirRegistroDocActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnAbrirRegistroDocActionPerformed
        // Abre ventana para registrar nuevo documento.
        if (usuarioActual == null || servicioBib == null) {
            JOptionPane.showMessageDialog(this, "Error de inicializacion. No se puede abrir el formulario.", "Error Interno", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LogsError.info(this.getClass(), "Abriendo formulario de registro de nuevo documento por: " + usuarioActual.getCorreo());
        //  RegistroDocumentos en bibliotecaudb.gui
        RegistroDocumentos formRegistro = new RegistroDocumentos(usuarioActual, servicioBib, null); // null porque es nuevo
        formRegistro.setVisible(true);
    }//GEN-LAST:event_btnAbrirRegistroDocActionPerformed

    /**
     * Accion boton "Agregar Ejemplar a Doc.". Abre ventana para agregar ejemplares.
     */
    private void btnAgregarEjemplarADocActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnAgregarEjemplarADocActionPerformed
        // Abre ventana para agregar ejemplares a documento existente.
        String idDocStr = txtIdDocParaEjemplar.getText();
        if (idDocStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un documento de la tabla primero.", "Seleccion Requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (usuarioActual == null || servicioBib == null) {
            JOptionPane.showMessageDialog(this, "Error de inicializacion.", "Error Interno", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int idDocumento = Integer.parseInt(idDocStr);
            // Verifica que documento exista antes de abrir ventana ejemplares
            Documento doc = servicioBib.consultarDetalleDocumento(idDocumento).get("documento") != null ? (Documento) servicioBib.consultarDetalleDocumento(idDocumento).get("documento") : null;
            if (doc == null) {
                JOptionPane.showMessageDialog(this, "El documento con ID " + idDocumento + " no fue encontrado.", "Documento Invalido", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LogsError.info(this.getClass(), "Abriendo formulario para agregar ejemplares al documento ID: " + idDocumento);
            //  Ejemplares en bibliotecaudb.gui
            Ejemplares formEjemplares = new Ejemplares(usuarioActual, servicioBib, idDocumento, doc.getTitulo());
            formEjemplares.setVisible(true);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID del documento seleccionado no es valido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException | BibliotecaException e) {
            LogsError.error(this.getClass(),"Error al verificar documento para agregar ejemplar: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error al obtener datos del documento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAgregarEjemplarADocActionPerformed

    /**
     * Evento al clic en fila tabla documentos. Coloca ID doc en campo texto.
     */
    private void tblDocumentosMouseClicked(java.awt.event.MouseEvent evt) { //GEN-FIRST:event_tblDocumentosMouseClicked
        // Pone ID del documento en campo texto al seleccionar fila.
        int fila = tblDocumentos.getSelectedRow();
        if (fila != -1) { // Si se selecciono fila valida
            // Columna 0 es ID documento
            txtIdDocParaEjemplar.setText(tblDocumentos.getValueAt(fila, 0).toString());
        }
    } //GEN-LAST:event_tblDocumentosMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbrirRegistroDoc;
    private javax.swing.JButton btnAgregarEjemplarADoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblDocumentos;
    private javax.swing.JTextField txtIdDocParaEjemplar;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}