package bibliotecaudb.gui;

import bibliotecaudb.modelo.usuario.Usuario;
import bibliotecaudb.servicios.BibliotecaService;
import bibliotecaudb.servicios.MoraService; // Para calcular la mora
import bibliotecaudb.servicios.impl.MoraServiceImpl; // Para instanciar MoraService
import bibliotecaudb.modelo.biblioteca.Prestamo; // Para obtener datos del prestamo
import bibliotecaudb.modelo.biblioteca.Devolucion; // Para el resultado
import bibliotecaudb.excepciones.BibliotecaException;
import bibliotecaudb.conexion.LogsError;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar; // Para el SpinnerDateModel
import java.util.Date;
import java.math.BigDecimal; // Para la mora

public class Devoluciones extends javax.swing.JFrame {

    private Usuario usuarioQueRegistra; // El docente que opera
    private BibliotecaService servicioBib;
    private MoraService servicioMora; // Para calcular la mora
    private Prestamo prestamoSeleccionadoParaDevolver; // Guarda el prestamo que se esta devolviendo

    /**
     * Constructor por defecto.
     */
    public Devoluciones() {
        initComponents();

        this.setTitle("Registrar Devolucion - Error de Contexto");
        btnCalcularMora.setEnabled(false); //  boton para calcular mora
        btnConfirmarDevolucion.setEnabled(false);
    }

    /**
     * Constructor principal.
     * @param usuario El usuario (docente).
     * @param service El servicio de biblioteca.
     */
    public Devoluciones(Usuario usuario, BibliotecaService service) {
        initComponents();
        this.usuarioQueRegistra = usuario;
        this.servicioBib = service;
        this.servicioMora = new MoraServiceImpl(); // Instanciamos servicio de mora

        this.setLocationRelativeTo(null);
        this.setTitle("Registrar Devolucion - Operador: " + (usuario != null ? usuario.getNombre() : "N/D"));
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        configurarSpinnerFecha();
        lblMoraCalculadaValor.setText("0.00"); // Inicializar mora a 0
    }

    /**
     * Configura JSpinner para seleccionar fecha de devolucion.
     */
    private void configurarSpinnerFecha() {
        // Configura spinner para seleccionar fecha facilmente.
        // spnFechaDevolucion es el nombre del spinner de fecha.
        // Quitamos spinners individuales de dia, mes, anio y usamos uno solo.
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        spnFechaDevolucion.setModel(dateModel); // Suponiendo JSpinner llamado spnFechaDevolucion
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnFechaDevolucion, "dd/MM/yyyy");
        spnFechaDevolucion.setEditor(dateEditor);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTituloDevoluciones = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblIdPrestamoEtiqueta = new javax.swing.JLabel();
        txtIdPrestamoDevolucion = new javax.swing.JTextField();
        lblFechaDevolucionEtiqueta = new javax.swing.JLabel();
        lblMoraPagarEtiqueta = new javax.swing.JLabel();
        lblMoraCalculadaValor = new javax.swing.JLabel();
        spnFechaDevolucion = new javax.swing.JSpinner();
        btnCalcularMora = new javax.swing.JButton();
        btnConfirmarDevolucion = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblTituloDevoluciones.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblTituloDevoluciones.setText("Registrar Devoluciones");

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        lblIdPrestamoEtiqueta.setText("ID Prestamo a Devolver (*):");

        lblFechaDevolucionEtiqueta.setText("Fecha de Devolucion:");

        lblMoraPagarEtiqueta.setText("Mora a Pagar Estimada:");

        lblMoraCalculadaValor.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMoraCalculadaValor.setText("0.00");

        spnFechaDevolucion.setModel(new javax.swing.SpinnerDateModel());

        btnCalcularMora.setText("Buscar Prestamo y Calcular Mora");
        btnCalcularMora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularMoraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnCalcularMora, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblIdPrestamoEtiqueta, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblFechaDevolucionEtiqueta, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lblMoraPagarEtiqueta, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtIdPrestamoDevolucion, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                                        .addComponent(spnFechaDevolucion)
                                                        .addComponent(lblMoraCalculadaValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(30, 30, 30))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtIdPrestamoDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblIdPrestamoEtiqueta))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblFechaDevolucionEtiqueta)
                                        .addComponent(spnFechaDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(btnCalcularMora)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblMoraPagarEtiqueta)
                                        .addComponent(lblMoraCalculadaValor))
                                .addContainerGap(20, Short.MAX_VALUE))
        );

        btnConfirmarDevolucion.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnConfirmarDevolucion.setText("Confirmar Devolucion");
        btnConfirmarDevolucion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarDevolucionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblTituloDevoluciones)
                                .addGap(95, 95, 95))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(125, 125, 125)
                                .addComponent(btnConfirmarDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(lblTituloDevoluciones)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnConfirmarDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Accion boton "Buscar Prestamo y Calcular Mora". Busca prestamo y calcula mora.
     */
    private void btnCalcularMoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularMoraActionPerformed
        // Busca prestamo y calcula mora.
        String idPrestamoStr = txtIdPrestamoDevolucion.getText().trim();
        if (idPrestamoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el ID del prestamo.", "Dato Requerido", JOptionPane.WARNING_MESSAGE);
            txtIdPrestamoDevolucion.requestFocus();
            prestamoSeleccionadoParaDevolver = null; // Limpiar seleccion previa
            lblMoraCalculadaValor.setText("0.00");
            return;
        }

        if (servicioBib == null || servicioMora == null) {
            JOptionPane.showMessageDialog(this, "Error de inicializacion de servicios.", "Error Interno", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int idPrestamo = Integer.parseInt(idPrestamoStr);
            // Usar metodo del servicio biblioteca para obtener prestamo
            prestamoSeleccionadoParaDevolver = servicioBib.obtenerDetallesPrestamoParaDevolucion(idPrestamo);

            if (prestamoSeleccionadoParaDevolver == null) {
                JOptionPane.showMessageDialog(this, "Prestamo con ID " + idPrestamo + " no encontrado o ya devuelto.", "Prestamo No Encontrado", JOptionPane.WARNING_MESSAGE);
                lblMoraCalculadaValor.setText("0.00");
                return;
            }
            if (prestamoSeleccionadoParaDevolver.getFechaDevolucion() != null) {
                JOptionPane.showMessageDialog(this, "Este prestamo (ID: "+idPrestamo+") ya fue devuelto el " + prestamoSeleccionadoParaDevolver.getFechaDevolucion(), "Ya Devuelto", JOptionPane.INFORMATION_MESSAGE);
                lblMoraCalculadaValor.setText(prestamoSeleccionadoParaDevolver.getMora() != null ? String.format("%.2f", prestamoSeleccionadoParaDevolver.getMora()) : "0.00");
                return;
            }

            Date fechaDevSpinner = (Date) spnFechaDevolucion.getValue();
            LocalDate fechaDevolucionSeleccionada = fechaDevSpinner.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (fechaDevolucionSeleccionada.isBefore(prestamoSeleccionadoParaDevolver.getFechaPrestamo())) {
                JOptionPane.showMessageDialog(this, "La fecha de devolucion no puede ser anterior a la fecha del prestamo.", "Fecha Invalida", JOptionPane.WARNING_MESSAGE);
                lblMoraCalculadaValor.setText("0.00");
                return;
            }

            BigDecimal mora = servicioMora.calcularMoraParaPrestamo(prestamoSeleccionadoParaDevolver, fechaDevolucionSeleccionada);
            lblMoraCalculadaValor.setText(String.format("%.2f", mora)); // Muestra mora con 2 decimales
            LogsError.info(this.getClass(), "Mora calculada para prestamo ID " + idPrestamo + ": " + mora);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID del prestamo debe ser un numero.", "Formato Invalido", JOptionPane.ERROR_MESSAGE);
            prestamoSeleccionadoParaDevolver = null;
            lblMoraCalculadaValor.setText("0.00");
        } catch (SQLException e) {
            LogsError.error(this.getClass(), "Error SQL al buscar prestamo o calcular mora.", e);
            JOptionPane.showMessageDialog(this, "Error de base de datos: " + e.getMessage(), "Error de Conexion", JOptionPane.ERROR_MESSAGE);
            prestamoSeleccionadoParaDevolver = null;
            lblMoraCalculadaValor.setText("0.00");
        } catch (BibliotecaException e) {
            LogsError.warn(this.getClass(), "Error de negocio al calcular mora: " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Calculo", JOptionPane.WARNING_MESSAGE);
            prestamoSeleccionadoParaDevolver = null;
            lblMoraCalculadaValor.setText("0.00");
        }
    }//GEN-LAST:event_btnCalcularMoraActionPerformed

    /**
     * Accion boton "Confirmar Devolucion". Registra devolucion en sistema.
     */
    private void btnConfirmarDevolucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarDevolucionActionPerformed
        // Confirma y registra devolucion.
        if (prestamoSeleccionadoParaDevolver == null) {
            JOptionPane.showMessageDialog(this, "Primero busque el prestamo y calcule la mora.", "Accion Requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (prestamoSeleccionadoParaDevolver.getFechaDevolucion() != null){
            JOptionPane.showMessageDialog(this, "Este prestamo ya fue devuelto.", "Ya Devuelto", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (servicioBib == null || usuarioQueRegistra == null) {
            JOptionPane.showMessageDialog(this, "Error de inicializacion. No se puede registrar la devolucion.", "Error Interno", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Date fechaDevSpinner = (Date) spnFechaDevolucion.getValue();
            LocalDate fechaDevolucionConfirmada = fechaDevSpinner.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (fechaDevolucionConfirmada.isBefore(prestamoSeleccionadoParaDevolver.getFechaPrestamo())) {
                JOptionPane.showMessageDialog(this, "La fecha de devolucion no puede ser anterior a la fecha del prestamo.", "Fecha Invalida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Recalcular mora por si usuario cambio fecha despues de "Calcular Mora".
            // O confiar en valor de lblMoraCalculadaValor.
            // Por seguridad, recalcular o asegurar no cambiar fecha despues de calcular.
            BigDecimal moraFinal = servicioMora.calcularMoraParaPrestamo(prestamoSeleccionadoParaDevolver, fechaDevolucionConfirmada);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Desea registrar la devolucion del prestamo ID " + prestamoSeleccionadoParaDevolver.getId() +
                            "\nCon fecha: " + fechaDevolucionConfirmada +
                            "\nMora a registrar: " + String.format("%.2f", moraFinal) + "?",
                    "Confirmar Devolucion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                Devolucion devolucionRealizada = servicioBib.registrarDevolucion(prestamoSeleccionadoParaDevolver.getId(), fechaDevolucionConfirmada);

                LogsError.info(this.getClass(), "Devolucion ID " + devolucionRealizada.getId() + " registrada por: " + usuarioQueRegistra.getCorreo());
                JOptionPane.showMessageDialog(this, "Devolucion registrada exitosamente!\nID Devolucion: " + devolucionRealizada.getId() + "\nMora Registrada: " + String.format("%.2f", devolucionRealizada.getMoraPagada()), "Devolucion Exitosa", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Cierra esta ventana
            }

        } catch (BibliotecaException e) {
            LogsError.warn(this.getClass(), "Error de negocio al registrar devolucion: " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error al Devolver", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            LogsError.error(this.getClass(), "Error SQL al registrar devolucion.", e);
            JOptionPane.showMessageDialog(this, "Error de base de datos al registrar la devolucion.", "Error de Conexion", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnConfirmarDevolucionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalcularMora;
    private javax.swing.JButton btnConfirmarDevolucion;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblFechaDevolucionEtiqueta;
    private javax.swing.JLabel lblIdPrestamoEtiqueta;
    private javax.swing.JLabel lblMoraCalculadaValor;
    private javax.swing.JLabel lblMoraPagarEtiqueta;
    private javax.swing.JLabel lblTituloDevoluciones;
    private javax.swing.JSpinner spnFechaDevolucion;
    private javax.swing.JTextField txtIdPrestamoDevolucion;
    // End of variables declaration//GEN-END:variables
}