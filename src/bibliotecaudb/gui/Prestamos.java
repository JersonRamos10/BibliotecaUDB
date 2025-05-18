package bibliotecaudb.gui;

import bibliotecaudb.modelo.usuario.Usuario;
import bibliotecaudb.servicios.BibliotecaService;
import bibliotecaudb.excepciones.BibliotecaException;
import bibliotecaudb.conexion.LogsError;
import bibliotecaudb.modelo.biblioteca.Prestamo;

import javax.swing.JOptionPane;
import java.sql.SQLException;
// import java.time.LocalDate;
import javax.swing.JFrame;

public class Prestamos extends javax.swing.JFrame {

    private Usuario usuarioQueRegistra; // Docente que registra
    private BibliotecaService servicioBib;
    private int idEjemplarAPrestar;   // NUEVO: ID del ejemplar a prestar

    /**
     * Constructor por defecto - evitar usar directamente.
     */
    public Prestamos() {
        initComponents();
        this.setTitle("Registrar Prestamo - Error de Contexto");

        txtIdEjemplarPrestamo.setEditable(false);
        txtIdUsuarioPrestatario.setEnabled(false);
        btnRegistrarPrestamo.setEnabled(false);
    }

    /**
     * Constructor principal para registrar prestamo.
     * @param usuarioOperador Usuario (docente) que opera sistema.
     * @param service Servicio de biblioteca.
     * @param idEjemplar ID del ejemplar seleccionado para prestar.
     */
    public Prestamos(Usuario usuarioOperador, BibliotecaService service, int idEjemplar) {
        initComponents();
        this.usuarioQueRegistra = usuarioOperador;
        this.servicioBib = service;
        this.idEjemplarAPrestar = idEjemplar;

        this.setLocationRelativeTo(null);
        this.setTitle("Registrar Prestamo para Ejemplar ID: " + this.idEjemplarAPrestar);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Asumir componentes:
        // txtIdEjemplarPrestamo (mostrar ID ejemplar)
        // txtIdUsuarioPrestatario (docente ingresa ID alumno/usuario)
        // btnRegistrarPrestamo (confirmar)

        txtIdEjemplarPrestamo.setText(String.valueOf(this.idEjemplarAPrestar));
        txtIdEjemplarPrestamo.setEditable(false); // ID ejemplar ya dado, no se edita

        // Foco en campo para ingresar ID usuario que recibira prestamo
        txtIdUsuarioPrestatario.requestFocus();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTituloPrestamo = new javax.swing.JLabel();
        txtIdEjemplarPrestamo = new javax.swing.JTextField();
        txtIdUsuarioPrestatario = new javax.swing.JTextField();
        lblIdEjemplarEtiqueta = new javax.swing.JLabel();
        lblIdUsuarioEtiqueta = new javax.swing.JLabel();
        btnRegistrarPrestamo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblTituloPrestamo.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblTituloPrestamo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloPrestamo.setText("Registrar Prestamo");

        lblIdEjemplarEtiqueta.setText("ID Ejemplar a Prestar:");

        lblIdUsuarioEtiqueta.setText("ID Usuario Prestatario (*):");

        btnRegistrarPrestamo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRegistrarPrestamo.setText("Confirmar Prestamo");
        btnRegistrarPrestamo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarPrestamoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(50, 50, 50)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblIdEjemplarEtiqueta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblIdUsuarioEtiqueta, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtIdEjemplarPrestamo, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                                        .addComponent(txtIdUsuarioPrestatario)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(120, 120, 120)
                                                .addComponent(btnRegistrarPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(90, 90, 90)
                                                .addComponent(lblTituloPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(lblTituloPrestamo)
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtIdEjemplarPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblIdEjemplarEtiqueta))
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtIdUsuarioPrestatario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblIdUsuarioEtiqueta))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                                .addComponent(btnRegistrarPrestamo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Accion boton "Confirmar Prestamo". Valida ID usuario y llama servicio.
     */
    private void btnRegistrarPrestamoActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnRegistrarPrestamoActionPerformed
        // Registra prestamo.
        String idUsuarioPrestatarioStr = txtIdUsuarioPrestatario.getText().trim();

        if (idUsuarioPrestatarioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El ID del usuario prestatario es obligatorio.", "Dato Requerido", JOptionPane.WARNING_MESSAGE);
            txtIdUsuarioPrestatario.requestFocus();
            return;
        }

        if (servicioBib == null || usuarioQueRegistra == null || idEjemplarAPrestar <= 0) {
            JOptionPane.showMessageDialog(this, "Error de inicializacion o no se selecciono un ejemplar.", "Error Interno", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int idUsuarioPrestatario = Integer.parseInt(idUsuarioPrestatarioStr);

            // Servicio BibliotecaService.realizarPrestamo se encarga de validaciones
            Prestamo prestamoRealizado = servicioBib.realizarPrestamo(idUsuarioPrestatario, this.idEjemplarAPrestar);

            LogsError.info(this.getClass(), "Prestamo ID " + prestamoRealizado.getId() + " (Ejemplar ID: " + this.idEjemplarAPrestar + ") registrado para Usuario ID: " + idUsuarioPrestatario + " por: " + usuarioQueRegistra.getCorreo());
            JOptionPane.showMessageDialog(this, "Prestamo registrado exitosamente!\nID Prestamo: " + prestamoRealizado.getId() + "\nFecha Limite: " + prestamoRealizado.getFechaLimite(), "Prestamo Exitoso", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); // Cierra esta ventana

        } catch (NumberFormatException e) {
            LogsError.warn(this.getClass(), "ID de usuario prestatario no es un numero valido: " + idUsuarioPrestatarioStr, e);
            JOptionPane.showMessageDialog(this, "El ID del usuario prestatario debe ser un numero valido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (BibliotecaException e) {
            LogsError.warn(this.getClass(), "Error de negocio al realizar prestamo: " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error al Realizar Prestamo", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            LogsError.error(this.getClass(), "Error SQL al realizar prestamo.", e);
            JOptionPane.showMessageDialog(this, "Error de base de datos al registrar el prestamo: " + e.getMessage(), "Error de Conexion", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRegistrarPrestamoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegistrarPrestamo;
    private javax.swing.JLabel lblIdEjemplarEtiqueta;
    private javax.swing.JLabel lblIdUsuarioEtiqueta;
    private javax.swing.JLabel lblTituloPrestamo;
    private javax.swing.JTextField txtIdEjemplarPrestamo;
    private javax.swing.JTextField txtIdUsuarioPrestatario;
    // End of variables declaration//GEN-END:variables
}