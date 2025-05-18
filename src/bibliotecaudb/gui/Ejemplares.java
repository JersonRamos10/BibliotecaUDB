package bibliotecaudb.gui;

import bibliotecaudb.modelo.biblioteca.Ejemplar;
import bibliotecaudb.modelo.usuario.Usuario;
import bibliotecaudb.servicios.BibliotecaService;
import bibliotecaudb.excepciones.BibliotecaException;
import bibliotecaudb.conexion.LogsError;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.sql.SQLException;

public class Ejemplares extends javax.swing.JFrame {

    private Usuario usuarioActual; // Docente que opera
    private BibliotecaService servicioBib; // Servicio de biblioteca
    private int idDocumentoPadre; // ID documento al que se agregara ejemplar
    private String tituloDocumentoPadre; // Titulo documento para mostrarlo

    /**
     * Constructor por defecto.
     */
    public Ejemplares() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Agregar Ejemplar - Error de Contexto");

        btnAgregarEjemplarForm.setEnabled(false);
        lblTituloDocumentoEjemplar.setText("Documento: N/A");
    }

    /**
     * Constructor principal para agregar ejemplar a documento.
     * @param usuario Usuario (docente) que realiza operacion.
     * @param service Instancia del servicio de biblioteca.
     * @param idDocPadre ID del documento al que pertenece nuevo ejemplar.
     * @param tituloDocPadre Titulo del documento padre, para mostrar en UI.
     */
    public Ejemplares(Usuario usuario, BibliotecaService service, int idDocPadre, String tituloDocPadre) {
        initComponents();
        this.usuarioActual = usuario;
        this.servicioBib = service;
        this.idDocumentoPadre = idDocPadre;
        this.tituloDocumentoPadre = tituloDocPadre;

        this.setLocationRelativeTo(null);
        this.setTitle("Agregar Ejemplar a: " + tituloDocPadre);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



        lblTituloDocumentoEjemplar.setText("Documento: " + this.tituloDocumentoPadre + " (ID: " + this.idDocumentoPadre + ")");

        // Para nuevo ejemplar, "Disponible" suele ser estado por defecto.
        cmbEstadoEjemplar.setSelectedItem("Disponible");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTituloVentanaEjemplar = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtUbicacionEjemplar = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbEstadoEjemplar = new javax.swing.JComboBox<>();
        btnAgregarEjemplarForm = new javax.swing.JButton();
        lblTituloDocumentoEjemplar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblTituloVentanaEjemplar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTituloVentanaEjemplar.setText("Informacion del Nuevo Ejemplar");

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        jLabel8.setText("Ubicacion (*):");

        jLabel9.setText("Estado (*):");
        jLabel9.setToolTipText("");

        cmbEstadoEjemplar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Disponible", "Prestado", "En Reparacion", "Extraviado" })); // Mas opciones estado

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtUbicacionEjemplar)
                                        .addComponent(cmbEstadoEjemplar, 0, 217, Short.MAX_VALUE))
                                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtUbicacionEjemplar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8))
                                .addGap(23, 23, 23)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(cmbEstadoEjemplar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(38, Short.MAX_VALUE))
        );

        btnAgregarEjemplarForm.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAgregarEjemplarForm.setText("Agregar Ejemplar");
        btnAgregarEjemplarForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarEjemplarFormActionPerformed(evt);
            }
        });

        lblTituloDocumentoEjemplar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTituloDocumentoEjemplar.setText("Documento: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(lblTituloVentanaEjemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addComponent(lblTituloDocumentoEjemplar, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(116, 116, 116)
                                                .addComponent(btnAgregarEjemplarForm, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(lblTituloVentanaEjemplar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblTituloDocumentoEjemplar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnAgregarEjemplarForm)
                                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Accion boton "Agregar Ejemplar". Valida datos y llama servicio para guardar.
     */
    private void btnAgregarEjemplarFormActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnAgregarEjemplarFormActionPerformed
        // Guarda nuevo ejemplar.
        String ubicacion = txtUbicacionEjemplar.getText().trim();
        String estado = cmbEstadoEjemplar.getSelectedItem() != null ? cmbEstadoEjemplar.getSelectedItem().toString() : "";

        if (ubicacion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La ubicacion es obligatoria.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtUbicacionEjemplar.requestFocus();
            return;
        }
        if (estado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un estado para el ejemplar.", "Validacion", JOptionPane.WARNING_MESSAGE);
            cmbEstadoEjemplar.requestFocus();
            return;
        }

        if (servicioBib == null || usuarioActual == null) {
            JOptionPane.showMessageDialog(this, "Error de inicializacion. No se puede guardar.", "Error Interno", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Ejemplar nuevoEjemplar = new Ejemplar();
        nuevoEjemplar.setIdDocumento(this.idDocumentoPadre);
        nuevoEjemplar.setUbicacion(ubicacion);
        nuevoEjemplar.setEstado(estado); // Modelo Ejemplar deberia validar estados permitidos

        try {
            boolean agregado = servicioBib.agregarEjemplarADocumentoExistente(this.idDocumentoPadre, nuevoEjemplar);
            if (agregado) {
                LogsError.info(this.getClass(), "Ejemplar agregado al documento ID " + this.idDocumentoPadre + " por " + usuarioActual.getCorreo());
                JOptionPane.showMessageDialog(this, "Ejemplar agregado exitosamente al documento '" + this.tituloDocumentoPadre + "'.", "Ejemplar Agregado", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Cierra esta ventana
                // Ventana 'Documentos' deberia refrescar tabla si es visible.
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar el ejemplar.", "Error en Operacion", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            LogsError.error(this.getClass(), "Error SQL al agregar ejemplar.", e);
            JOptionPane.showMessageDialog(this, "Error de base de datos: " + e.getMessage(), "Error de Conexion", JOptionPane.ERROR_MESSAGE);
        } catch (BibliotecaException e) {
            LogsError.warn(this.getClass(), "Error de negocio al agregar ejemplar: " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Validacion", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnAgregarEjemplarFormActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarEjemplarForm;
    private javax.swing.JComboBox<String> cmbEstadoEjemplar;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblTituloDocumentoEjemplar;
    private javax.swing.JLabel lblTituloVentanaEjemplar;
    private javax.swing.JTextField txtUbicacionEjemplar;
    // End of variables declaration//GEN-END:variables
}