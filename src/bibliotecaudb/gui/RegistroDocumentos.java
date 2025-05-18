package bibliotecaudb.gui; // Asegurate que el paquete sea el correcto

import bibliotecaudb.modelo.biblioteca.Documento;
import bibliotecaudb.modelo.biblioteca.Ejemplar;
import bibliotecaudb.modelo.biblioteca.TipoDocumento;
import bibliotecaudb.modelo.usuario.Usuario;
import bibliotecaudb.servicios.BibliotecaService;
import bibliotecaudb.dao.biblioteca.TipoDocumentoDAO; // Para cargar combo
import bibliotecaudb.dao.biblioteca.impl.TipoDocumentoDAOImpl; // Para cargar combo
import bibliotecaudb.excepciones.BibliotecaException;
import bibliotecaudb.conexion.LogsError;

import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import java.util.List;
import java.sql.SQLException;
import javax.swing.JFrame;
import java.util.ArrayList; // Para lista de ejemplares

public class RegistroDocumentos extends javax.swing.JFrame {

    private Usuario usuarioActual;
    private BibliotecaService servicioBib;
    private TipoDocumentoDAO tipoDocumentoDAO; // Para llenar ComboBox tipos
    private Documento documentoParaEditar; // Si es para editar

    /**
     * Constructor por defecto.
     */
    public RegistroDocumentos() {
        initComponents();
        // No usar este constructor directamente sin usuario y servicio.
        // Deshabilitar funcionalidad o mostrar error.
        this.setTitle("Registrar Documento - Error de Contexto");
        btnGuardarDocumentoReg.setEnabled(false); // Asumiendo boton renombrado
    }

    /**
     * Constructor para registrar o editar documento.
     * @param usuario Usuario que realiza operacion.
     * @param service Servicio de biblioteca.
     * @param docAEditar Documento a editar (null si es nuevo).
     */
    public RegistroDocumentos(Usuario usuario, BibliotecaService service, Documento docAEditar) {
        initComponents();
        this.usuarioActual = usuario;
        this.servicioBib = service;
        this.tipoDocumentoDAO = new TipoDocumentoDAOImpl(); // Para ComboBox
        this.documentoParaEditar = docAEditar;

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Renombrar componentes en initComponents o aqui:
        // jTextField1 -> txtTituloReg
        // jTextField2 -> txtAutorReg
        // jTextField3 -> txtEditorialReg
        // jComboBox1 -> cmbTipoDocumentoReg
        // jSpinner1 -> spnAnioPublicacionReg
        // jButton1 -> btnGuardarDocumentoReg

        cargarTiposDocumentoComboBox();

        if (docAEditar != null) {
            this.setTitle("Editar Documento - ID: " + docAEditar.getId());
            btnGuardarDocumentoReg.setText("Actualizar Documento");
            popularFormularioParaEdicion();
            // Considerar si edicion de ejemplares se hace aqui o en otra ventana
            // Por simplicidad, este form solo edita datos del Documento.
        } else {
            this.setTitle("Registrar Nuevo Documento");
            btnGuardarDocumentoReg.setText("Guardar Documento");
        }
    }

    /**
     * Carga tipos de documento en ComboBox.
     */
    private void cargarTiposDocumentoComboBox() {
        // Llena menu desplegable de Tipos de Documento.
        try {
            List<TipoDocumento> tipos = tipoDocumentoDAO.obtenerTodos();
            DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
            if (tipos != null && !tipos.isEmpty()) {
                for (TipoDocumento tipo : tipos) {
                    modelo.addElement(tipo.getTipo()); // Agrega nombre del tipo
                }
            } else {
                modelo.addElement("No hay tipos");
                LogsError.warn(this.getClass(), "No se encontraron tipos de documento para el ComboBox.");
            }
            cmbTipoDocumentoReg.setModel(modelo);
        } catch (SQLException e) {
            LogsError.error(this.getClass(), "Error SQL al cargar tipos de documento.", e);
            JOptionPane.showMessageDialog(this, "Error al cargar los tipos de documento.", "Error de Carga", JOptionPane.ERROR_MESSAGE);
            cmbTipoDocumentoReg.setModel(new DefaultComboBoxModel<>(new String[]{"Error"}));
        }
    }

    /**
     * Si se edita, llena formulario con datos del documento.
     */
    private void popularFormularioParaEdicion() {
        // Pone datos del documento a editar en campos.
        if (documentoParaEditar != null) {
            txtTituloReg.setText(documentoParaEditar.getTitulo());
            txtAutorReg.setText(documentoParaEditar.getAutor() != null ? documentoParaEditar.getAutor() : "");
            txtEditorialReg.setText(documentoParaEditar.getEditorial() != null ? documentoParaEditar.getEditorial() : "");
            if (documentoParaEditar.getTipoDocumento() != null) {
                cmbTipoDocumentoReg.setSelectedItem(documentoParaEditar.getTipoDocumento().getTipo());
            }
            if (documentoParaEditar.getAnioPublicacion() != null) {
                spnAnioPublicacionReg.setValue(documentoParaEditar.getAnioPublicacion());
            } else {
                spnAnioPublicacionReg.setValue(java.time.LocalDate.now().getYear()); // Valor por defecto
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtTituloReg = new javax.swing.JTextField(); // RENOMBRADO
        txtAutorReg = new javax.swing.JTextField(); // RENOMBRADO
        txtEditorialReg = new javax.swing.JTextField(); // RENOMBRADO
        cmbTipoDocumentoReg = new javax.swing.JComboBox<>(); // RENOMBRADO
        spnAnioPublicacionReg = new javax.swing.JSpinner(); // RENOMBRADO
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnGuardarDocumentoReg = new javax.swing.JButton(); // RENOMBRADO

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); // Cambiado

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        cmbTipoDocumentoReg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cargando..." }));

        spnAnioPublicacionReg.setModel(new javax.swing.SpinnerNumberModel(java.time.LocalDate.now().getYear(), 1500, java.time.LocalDate.now().getYear() + 1, 1)); // Ajustado
        spnAnioPublicacionReg.setEditor(new javax.swing.JSpinner.NumberEditor(spnAnioPublicacionReg, "#"));

        jLabel3.setText("Titulo (*):"); // Indicador obligatorio

        jLabel4.setText("Autor:");

        jLabel5.setText("Tipo Documento (*):"); // Indicador obligatorio
        jLabel5.setToolTipText("");

        jLabel6.setText("Editorial:");

        jLabel7.setText("Año de Publicacion:"); // Se corrigio "Anio" a "Año"

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel6)
                                                        .addComponent(jLabel5)
                                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtEditorialReg)
                                                        .addComponent(cmbTipoDocumentoReg, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(spnAnioPublicacionReg, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE) // Ajustar tamano spinner
                                                                .addGap(0, 111, Short.MAX_VALUE))))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(txtAutorReg, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE) // Ajustado
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(txtTituloReg, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(34, 34, 34))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtTituloReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtAutorReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtEditorialReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(cmbTipoDocumentoReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel5))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(spnAnioPublicacionReg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7))
                                .addContainerGap(24, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Informacion del Documento"); // Titulo actualizado

        btnGuardarDocumentoReg.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGuardarDocumentoReg.setText("Agregar");
        btnGuardarDocumentoReg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarDocumentoRegActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(29, 29, 29)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))) // Ajustado
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(128, 128, 128)
                                                .addComponent(btnGuardarDocumentoReg, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnGuardarDocumentoReg)
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Accion boton "Guardar" o "Actualizar". Recolecta datos, valida y llama servicio.
     */
    private void btnGuardarDocumentoRegActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnGuardarDocumentoRegActionPerformed
        // Guarda o actualiza documento.
        String titulo = txtTituloReg.getText().trim();
        String autor = txtAutorReg.getText().trim();
        String editorial = txtEditorialReg.getText().trim();
        String tipoDocNombre = cmbTipoDocumentoReg.getSelectedItem() != null ? cmbTipoDocumentoReg.getSelectedItem().toString() : "";
        Integer anioPub = (Integer) spnAnioPublicacionReg.getValue();

        // Validaciones
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El titulo es obligatorio.", "Validacion", JOptionPane.WARNING_MESSAGE);
            txtTituloReg.requestFocus();
            return;
        }
        if (tipoDocNombre.isEmpty() || "No hay tipos".equals(tipoDocNombre) || "Error".equals(tipoDocNombre)) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de documento valido.", "Validacion", JOptionPane.WARNING_MESSAGE);
            cmbTipoDocumentoReg.requestFocus();
            return;
        }

        try {
            TipoDocumento tipoSeleccionado = tipoDocumentoDAO.obtenerPorNombre(tipoDocNombre);
            if (tipoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "El tipo de documento seleccionado no es valido.", "Error de Datos", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Documento docAGuardar;
            if (documentoParaEditar != null) { // Modo Editar
                docAGuardar = documentoParaEditar;
            } else { // Modo Agregar
                docAGuardar = new Documento();
            }

            docAGuardar.setTitulo(titulo);
            docAGuardar.setAutor(autor.isEmpty() ? null : autor); // Guardar null si vacio
            docAGuardar.setEditorial(editorial.isEmpty() ? null : editorial); // Guardar null si vacio
            docAGuardar.setIdTipoDocumento(tipoSeleccionado.getId());
            docAGuardar.setAnioPublicacion(anioPub); // Spinner ya devuelve Integer

            if (servicioBib == null || usuarioActual == null) {
                JOptionPane.showMessageDialog(this, "Error de inicializacion. No se puede guardar.", "Error Interno", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean resultado;
            String accionVerb; // Para mensaje

            if (documentoParaEditar == null) { // Agregar nuevo
                // Al registrar nuevo, siempre crea con lista vacia de ejemplares,
                // luego se pueden agregar desde otra ventana.
                resultado = servicioBib.registrarNuevoDocumentoConEjemplares(docAGuardar, new ArrayList<Ejemplar>());
                accionVerb = "registrado";
            } else { // Actualizar existente
                // Actualizacion de ejemplares se haria desde otra interfaz o anadir logica aqui.
                // Por ahora, solo actualizamos datos del Documento.
                resultado = servicioBib.actualizarDatosDocumento(docAGuardar); // Necesita este metodo en BibliotecaService y su Impl
                accionVerb = "actualizado";
            }

            if (resultado) {
                LogsError.info(this.getClass(), "Documento '" + titulo + "' " + accionVerb + " por: " + usuarioActual.getCorreo());
                JOptionPane.showMessageDialog(this, "Documento " + accionVerb + " exitosamente.", "Operacion Exitosa", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Cierra esta ventana

            } else {
                JOptionPane.showMessageDialog(this, "No se pudo " + (documentoParaEditar == null ? "registrar" : "actualizar") + " el documento.", "Error en Operacion", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            LogsError.error(this.getClass(), "Error SQL al guardar/actualizar documento.", e);
            JOptionPane.showMessageDialog(this, "Error de base de datos: " + e.getMessage(), "Error de Conexion", JOptionPane.ERROR_MESSAGE);
        } catch (BibliotecaException e) {
            LogsError.warn(this.getClass(), "Error de negocio al guardar/actualizar documento: " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Validacion", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarDocumentoRegActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardarDocumentoReg;
    private javax.swing.JComboBox<String> cmbTipoDocumentoReg;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner spnAnioPublicacionReg;
    private javax.swing.JTextField txtAutorReg;
    private javax.swing.JTextField txtEditorialReg;
    private javax.swing.JTextField txtTituloReg;
    // End of variables declaration//GEN-END:variables
}