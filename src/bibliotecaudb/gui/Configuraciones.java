package bibliotecaudb.gui;

import bibliotecaudb.modelo.biblioteca.ConfiguracionSistema;
import bibliotecaudb.modelo.biblioteca.PoliticasPrestamo;
import bibliotecaudb.modelo.usuario.Usuario;
import bibliotecaudb.modelo.usuario.TipoUsuario;
import bibliotecaudb.servicios.BibliotecaService;
import bibliotecaudb.servicios.impl.BibliotecaServiceImpl;
import bibliotecaudb.dao.usuario.TipoUsuarioDAO;
import bibliotecaudb.dao.usuario.impl.TipoUsuarioDAOImpl;
import bibliotecaudb.excepciones.BibliotecaException;
import bibliotecaudb.conexion.LogsError;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class Configuraciones extends javax.swing.JFrame {

    private Usuario usuarioLogueado;
    private BibliotecaService bibliotecaService;
    private ConfiguracionSistema configActualSistema; // Para configuracion global
    private TipoUsuarioDAO tipoUsuarioDAO; //  Para cargar tipos usuario en combo
    private DefaultTableModel modeloTablaPoliticas; // Modelo para tabla politicas
    private List<TipoUsuario> listaTiposUsuario; //  Para mapear nombre tipo a ID
    private List<PoliticasPrestamo> listaPoliticasActuales; //  Para tener politicas cargadas


    public Configuraciones(Usuario admin) {
        initComponents(); // Asegurar que nuevos componentes esten declarados
        this.usuarioLogueado = admin;
        this.bibliotecaService = new BibliotecaServiceImpl();
        this.tipoUsuarioDAO = new TipoUsuarioDAOImpl();
        this.listaTiposUsuario = new ArrayList<>();
        this.listaPoliticasActuales = new ArrayList<>();

        this.setLocationRelativeTo(null);
        this.setTitle("Configuraciones del Sistema - Biblioteca UDB");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        if (this.usuarioLogueado == null || !"Administrador".equals(this.usuarioLogueado.getTipoUsuario().getTipo())) {
            JOptionPane.showMessageDialog(this, "Acceso no autorizado.", "Error de Permiso", JOptionPane.ERROR_MESSAGE);
            // Deshabilitar todos los controles
            btnActualizarConfigGlobal.setEnabled(false); // Asume boton original
            spnMaxEjemplaresGlobal.setEnabled(false);
            spnMoraDiariaGlobal.setEnabled(false);
            // Deshabilitar nuevos componentes politicas
            tblPoliticas.setEnabled(false); // Asume nueva tabla
            cmbTipoUsuarioPolitica.setEnabled(false);
            spnMaxEjemplaresPorTipo.setEnabled(false);
            spnDiasPrestamoPorTipo.setEnabled(false);
            btnGuardarPoliticaTipoUsuario.setEnabled(false);
        } else {
            // Configuracion Global (existente)
            configurarSpinnersGlobales();
            cargarConfiguracionGlobal();

            // NUEVO: Configuracion Politicas Prestamo
            inicializarTablaPoliticas();
            cargarTiposUsuarioEnComboBox(); // Carga tipos en combo
            cargarPoliticasEnTabla();      // Carga politicas existentes en tabla
            configurarSpinnersPoliticas();  // Configura spinners para editar politicas

            // Listener para ComboBox tipo usuario para politicas
            cmbTipoUsuarioPolitica.addActionListener(evt -> cargarDatosPoliticaSeleccionada());
        }
    }

    // --- METODOS PARA CONFIGURACION GLOBAL (EXISTENTES) ---
    private void configurarSpinnersGlobales() {
        spnMaxEjemplaresGlobal.setModel(new SpinnerNumberModel(1, 0, 100, 1));
        spnMoraDiariaGlobal.setModel(new SpinnerNumberModel(0.0, 0.0, 100.0, 0.01));
        JSpinner.NumberEditor editorMora = new JSpinner.NumberEditor(spnMoraDiariaGlobal, "#,##0.00");
        spnMoraDiariaGlobal.setEditor(editorMora);
    }

    private void cargarConfiguracionGlobal() {
        try {
            configActualSistema = bibliotecaService.obtenerConfiguracionGlobal();
            if (configActualSistema != null) {
                spnMaxEjemplaresGlobal.setValue(configActualSistema.getMaximoEjemplaresGlobal() != null ? configActualSistema.getMaximoEjemplaresGlobal() : 0);
                spnMoraDiariaGlobal.setValue(configActualSistema.getMoraDiariaGlobal() != null ? configActualSistema.getMoraDiariaGlobal().doubleValue() : 0.0);
            } else {
                LogsError.warn(Configuraciones.class, "No se pudo cargar la configuracion global del sistema.");
                // Establecer valores por defecto o mostrar error
            }
        } catch (SQLException e) {
            LogsError.error(Configuraciones.class, "Error SQL al cargar configuracion global.", e);
            JOptionPane.showMessageDialog(this, "Error al cargar configuracion global.", "Error de Conexion", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- NUEVOS METODOS PARA POLITICAS DE PRESTAMO ---
    /**
     * Prepara tabla para mostrar politicas de prestamo.
     */
    private void inicializarTablaPoliticas() {
        String[] columnas = {"ID Tipo Usuario", "Tipo Usuario", "Max. Ejemplares", "Dias Prestamo"};
        modeloTablaPoliticas = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable directamente
            }
        };
        tblPoliticas.setModel(modeloTablaPoliticas); // Asume JTable se llama tblPoliticas
    }

    /**
     * Carga tipos de usuario en ComboBox para seleccionar politica a editar.
     */
    private void cargarTiposUsuarioEnComboBox() {
        try {
            listaTiposUsuario = tipoUsuarioDAO.obtenerTodos();
            DefaultComboBoxModel<String> modeloCombo = new DefaultComboBoxModel<>();
            if (listaTiposUsuario == null || listaTiposUsuario.isEmpty()) {
                modeloCombo.addElement("Error: Sin tipos");
            } else {
                modeloCombo.addElement("Seleccione un Tipo de Usuario..."); // Item por defecto
                for (TipoUsuario tipo : listaTiposUsuario) {
                    modeloCombo.addElement(tipo.getTipo());
                }
            }
            cmbTipoUsuarioPolitica.setModel(modeloCombo); // Asume JComboBox se llama cmbTipoUsuarioPolitica
        } catch (SQLException e) {
            LogsError.error(this.getClass(), "Error SQL al cargar tipos de usuario para ComboBox de politicas.", e);
            cmbTipoUsuarioPolitica.setModel(new DefaultComboBoxModel<>(new String[]{"Error al cargar"}));
        }
    }

    /**
     * Carga politicas de prestamo existentes en JTable.
     */
    private void cargarPoliticasEnTabla() {
        modeloTablaPoliticas.setRowCount(0); // Limpiar tabla
        try {
            listaPoliticasActuales = bibliotecaService.obtenerTodasLasPoliticasPrestamo();
            if (listaPoliticasActuales != null) {
                for (PoliticasPrestamo politica : listaPoliticasActuales) {
                    String nombreTipoUsuario = "N/D";
                    if (politica.getTipoUsuario() != null) { // DAO deberia cargar esto
                        nombreTipoUsuario = politica.getTipoUsuario().getTipo();
                    } else { // Fallback si TipoUsuario no viene cargado
                        TipoUsuario tu = listaTiposUsuario.stream()
                                .filter(t -> t.getId() == politica.getIdTipoUsuario())
                                .findFirst().orElse(null);
                        if(tu != null) nombreTipoUsuario = tu.getTipo();
                    }
                    modeloTablaPoliticas.addRow(new Object[]{
                            politica.getIdTipoUsuario(),
                            nombreTipoUsuario,
                            politica.getMaxEjemplaresPrestamo(),
                            politica.getDiasPrestamoDefault()
                    });
                }
            }
        } catch (SQLException e) {
            LogsError.error(this.getClass(), "Error SQL al cargar politicas de prestamo en la tabla.", e);
            JOptionPane.showMessageDialog(this, "Error al cargar politicas de prestamo.", "Error de Conexion", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Configura spinners para editar valores de una politica.
     */
    private void configurarSpinnersPoliticas() {
        // Asume JSpinners se llaman spnMaxEjemplaresPorTipo y spnDiasPrestamoPorTipo
        spnMaxEjemplaresPorTipo.setModel(new SpinnerNumberModel(1, 1, 100, 1)); // Min 1 ejemplar
        spnDiasPrestamoPorTipo.setModel(new SpinnerNumberModel(1, 1, 365, 1)); // Min 1 dia
    }

    /**
     * Al seleccionar tipo usuario en ComboBox, carga datos politica en spinners.
     */
    private void cargarDatosPoliticaSeleccionada() {
        // Activa cuando admin selecciona tipo usuario de ComboBox.
        int indiceSeleccionado = cmbTipoUsuarioPolitica.getSelectedIndex();
        if (indiceSeleccionado > 0) { // > 0 porque indice 0 es "Seleccione..."
            // Indice en listaTiposUsuario es indiceSeleccionado - 1
            TipoUsuario tipoSeleccionado = listaTiposUsuario.get(indiceSeleccionado - 1);

            PoliticasPrestamo politicaExistente = listaPoliticasActuales.stream()
                    .filter(p -> p.getIdTipoUsuario() == tipoSeleccionado.getId())
                    .findFirst()
                    .orElse(null);

            if (politicaExistente != null) {
                spnMaxEjemplaresPorTipo.setValue(politicaExistente.getMaxEjemplaresPrestamo());
                spnDiasPrestamoPorTipo.setValue(politicaExistente.getDiasPrestamoDefault());
            } else {
                // Si no existe politica (raro si tabla cargo bien), poner valores por defecto
                spnMaxEjemplaresPorTipo.setValue(1);
                spnDiasPrestamoPorTipo.setValue(7);
                LogsError.warn(this.getClass(), "No se encontro politica precargada para tipo: " + tipoSeleccionado.getTipo());
            }
        } else { // Si se selecciona "Seleccione..."
            spnMaxEjemplaresPorTipo.setValue(1);
            spnDiasPrestamoPorTipo.setValue(7);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTituloConfiguraciones = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane(); // Usaremos pestañas
        panelConfigGlobal = new javax.swing.JPanel();
        lblMaxEjemplares = new javax.swing.JLabel();
        spnMaxEjemplaresGlobal = new javax.swing.JSpinner();
        lblMoraDiaria = new javax.swing.JLabel();
        spnMoraDiariaGlobal = new javax.swing.JSpinner();
        btnActualizarConfigGlobal = new javax.swing.JButton();
        panelPoliticasPrestamo = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPoliticas = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cmbTipoUsuarioPolitica = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        spnMaxEjemplaresPorTipo = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        spnDiasPrestamoPorTipo = new javax.swing.JSpinner();
        btnGuardarPoliticaTipoUsuario = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblTituloConfiguraciones.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTituloConfiguraciones.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTituloConfiguraciones.setText("Configuraciones del Sistema");

        panelConfigGlobal.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuracion Global"));

        lblMaxEjemplares.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaxEjemplares.setText("Max. Ejemplares Global (Fallback):");

        spnMaxEjemplaresGlobal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lblMoraDiaria.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMoraDiaria.setText("Mora Diaria Global (Fallback):");

        spnMoraDiariaGlobal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnActualizarConfigGlobal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnActualizarConfigGlobal.setText("Actualizar Configuracion Global");
        btnActualizarConfigGlobal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarConfigGlobalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelConfigGlobalLayout = new javax.swing.GroupLayout(panelConfigGlobal);
        panelConfigGlobal.setLayout(panelConfigGlobalLayout);
        panelConfigGlobalLayout.setHorizontalGroup(
                panelConfigGlobalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelConfigGlobalLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelConfigGlobalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnActualizarConfigGlobal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(panelConfigGlobalLayout.createSequentialGroup()
                                                .addGroup(panelConfigGlobalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblMaxEjemplares)
                                                        .addComponent(lblMoraDiaria))
                                                .addGap(18, 18, 18)
                                                .addGroup(panelConfigGlobalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(spnMaxEjemplaresGlobal, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                                        .addComponent(spnMoraDiariaGlobal))))
                                .addContainerGap())
        );
        panelConfigGlobalLayout.setVerticalGroup(
                panelConfigGlobalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelConfigGlobalLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(panelConfigGlobalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblMaxEjemplares)
                                        .addComponent(spnMaxEjemplaresGlobal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(panelConfigGlobalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblMoraDiaria)
                                        .addComponent(spnMoraDiariaGlobal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(btnActualizarConfigGlobal, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Global", panelConfigGlobal);

        panelPoliticasPrestamo.setBorder(javax.swing.BorderFactory.createTitledBorder("Politicas de Prestamo por Tipo de Usuario"));

        tblPoliticas.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {}, new String [] {}
        ));
        jScrollPane1.setViewportView(tblPoliticas);

        jLabel1.setText("Editar Politica para Tipo Usuario:");

        cmbTipoUsuarioPolitica.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione..." }));

        jLabel2.setText("Max. Ejemplares a Prestar:");

        jLabel3.setText("Dias de Prestamo por Defecto:");

        btnGuardarPoliticaTipoUsuario.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGuardarPoliticaTipoUsuario.setText("Guardar Politica para Tipo Seleccionado");
        btnGuardarPoliticaTipoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarPoliticaTipoUsuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPoliticasPrestamoLayout = new javax.swing.GroupLayout(panelPoliticasPrestamo);
        panelPoliticasPrestamo.setLayout(panelPoliticasPrestamoLayout);
        panelPoliticasPrestamoLayout.setHorizontalGroup(
                panelPoliticasPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelPoliticasPrestamoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelPoliticasPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(btnGuardarPoliticaTipoUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(panelPoliticasPrestamoLayout.createSequentialGroup()
                                                .addGroup(panelPoliticasPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel3))
                                                .addGap(18, 18, 18)
                                                .addGroup(panelPoliticasPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(cmbTipoUsuarioPolitica, 0, 180, Short.MAX_VALUE)
                                                        .addComponent(spnMaxEjemplaresPorTipo)
                                                        .addComponent(spnDiasPrestamoPorTipo))))
                                .addContainerGap())
        );
        panelPoliticasPrestamoLayout.setVerticalGroup(
                panelPoliticasPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelPoliticasPrestamoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(panelPoliticasPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(cmbTipoUsuarioPolitica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelPoliticasPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(spnMaxEjemplaresPorTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelPoliticasPrestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(spnDiasPrestamoPorTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(btnGuardarPoliticaTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Politicas por Tipo Usuario", panelPoliticasPrestamo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblTituloConfiguraciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTabbedPane1))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblTituloConfiguraciones)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTabbedPane1)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Accion boton "Actualizar Configuracion Global". Guarda cambios pestaña config global.
     */
    private void btnActualizarConfigGlobalActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnActualizarConfigGlobalActionPerformed
        // Guarda configuracion global del sistema.
        if (configActualSistema == null) {
            configActualSistema = new ConfiguracionSistema();
            configActualSistema.setId(1); // Asume ID 1 para fila config
            LogsError.warn(Configuraciones.class, "configActualSistema era null, se crea nueva instancia para guardar.");
        }

        try {
            Object maxEjGlobalObj = spnMaxEjemplaresGlobal.getValue();
            configActualSistema.setMaximoEjemplaresGlobal((Integer) maxEjGlobalObj);

            Object moraGlobalObj = spnMoraDiariaGlobal.getValue();
            configActualSistema.setMoraDiariaGlobal(BigDecimal.valueOf((Double) moraGlobalObj));

            if (configActualSistema.getMaximoEjemplaresGlobal() < 0 || configActualSistema.getMoraDiariaGlobal().compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Los valores de configuracion global no pueden ser negativos.", "Dato Invalido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean actualizado = bibliotecaService.actualizarConfiguracionGlobal(configActualSistema);
            if (actualizado) {
                LogsError.info(this.getClass(), "Configuracion global actualizada por: " + (usuarioLogueado != null ? usuarioLogueado.getCorreo() : "N/A"));
                JOptionPane.showMessageDialog(this, "Configuracion global actualizada exitosamente.", "Actualizacion Exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la configuracion global.", "Error de Actualizacion", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) { // Captura generica para errores conversion o BibliotecaException
            LogsError.error(Configuraciones.class, "Error al actualizar configuracion global: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, "Error al procesar configuracion global: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnActualizarConfigGlobalActionPerformed

    /**
     * Accion boton "Guardar Politica para Tipo Seleccionado". Guarda cambios politica prestamo.
     */
    private void btnGuardarPoliticaTipoUsuarioActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnGuardarPoliticaTipoUsuarioActionPerformed
        // Guarda cambios en politica para tipo usuario seleccionado.
        int indiceSeleccionadoCombo = cmbTipoUsuarioPolitica.getSelectedIndex();
        if (indiceSeleccionadoCombo <= 0) { // 0 es "Seleccione..."
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un tipo de usuario para modificar su politica.", "Seleccion Requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        TipoUsuario tipoUsuarioSeleccionado = listaTiposUsuario.get(indiceSeleccionadoCombo - 1);
        int maxEjemplaresNuevos = (Integer) spnMaxEjemplaresPorTipo.getValue();
        int diasPrestamoNuevos = (Integer) spnDiasPrestamoPorTipo.getValue();

        if (maxEjemplaresNuevos <= 0 || diasPrestamoNuevos <= 0) {
            JOptionPane.showMessageDialog(this, "El maximo de ejemplares y los dias de prestamo deben ser mayores a cero.", "Datos Invalidos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (bibliotecaService == null || usuarioLogueado == null) {
            JOptionPane.showMessageDialog(this, "Error de inicializacion. No se puede guardar.", "Error Interno", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Busca si ya existe politica para este tipo usuario para actualizarla,
            // o crea nueva si no existe.
            PoliticasPrestamo politicaAGuardar = listaPoliticasActuales.stream()
                    .filter(p -> p.getIdTipoUsuario() == tipoUsuarioSeleccionado.getId())
                    .findFirst()
                    .orElse(new PoliticasPrestamo()); // Si no existe, crea nueva instancia

            politicaAGuardar.setIdTipoUsuario(tipoUsuarioSeleccionado.getId());
            politicaAGuardar.setMaxEjemplaresPrestamo(maxEjemplaresNuevos);
            politicaAGuardar.setDiasPrestamoDefault(diasPrestamoNuevos);
            // DAO actualiza por id_tipo_usuario, debe hacer INSERT si no encuentra, o UPDATE si encuentra.
            // (O servicio decide si insertar o actualizar)


            PoliticasPrestamo politicaExistente = bibliotecaService.obtenerPoliticaPorTipoUsuario(tipoUsuarioSeleccionado.getId()); // Necesita este metodo en servicio

            boolean resultado;
            if (politicaExistente != null) {
                politicaExistente.setMaxEjemplaresPrestamo(maxEjemplaresNuevos);
                politicaExistente.setDiasPrestamoDefault(diasPrestamoNuevos);
                resultado = bibliotecaService.actualizarPoliticaPrestamo(politicaExistente);
            } else {
                // Crear y guardar nueva politica (necesita bibliotecaService.crearPoliticaPrestamo)
                politicaAGuardar.setIdTipoUsuario(tipoUsuarioSeleccionado.getId());
                // resultado = bibliotecaService.crearPoliticaPrestamo(politicaAGuardar); // Metodo hipotetico
                JOptionPane.showMessageDialog(this, "Funcionalidad para crear nueva politica no implementada completamente en servicio.", "Pendiente", JOptionPane.INFORMATION_MESSAGE);
                return; // Salir por ahora
            }


            if (resultado) {
                LogsError.info(this.getClass(), "Politica de prestamo para tipo " + tipoUsuarioSeleccionado.getTipo() + " guardada por: " + usuarioLogueado.getCorreo());
                JOptionPane.showMessageDialog(this, "Politica para '" + tipoUsuarioSeleccionado.getTipo() + "' guardada exitosamente.", "Guardado Exitoso", JOptionPane.INFORMATION_MESSAGE);
                cargarPoliticasEnTabla(); // Refrescar tabla
                // Limpiar seleccion combo para evitar re-guardado accidental
                cmbTipoUsuarioPolitica.setSelectedIndex(0);
                spnMaxEjemplaresPorTipo.setValue(1);
                spnDiasPrestamoPorTipo.setValue(7);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar la politica para '" + tipoUsuarioSeleccionado.getTipo() + "'.", "Error al Guardar", JOptionPane.ERROR_MESSAGE);
            }

        } catch (BibliotecaException e) {
            LogsError.warn(this.getClass(), "Error de negocio al guardar politica: " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Validacion", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            LogsError.error(this.getClass(), "Error SQL al guardar politica.", e);
            JOptionPane.showMessageDialog(this, "Error de base de datos al guardar la politica.", "Error de Conexion", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarPoliticaTipoUsuarioActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarConfigGlobal;
    private javax.swing.JButton btnGuardarPoliticaTipoUsuario;
    private javax.swing.JComboBox<String> cmbTipoUsuarioPolitica;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblMaxEjemplares;
    private javax.swing.JLabel lblMoraDiaria;
    private javax.swing.JLabel lblTituloConfiguraciones;
    private javax.swing.JPanel panelConfigGlobal;
    private javax.swing.JPanel panelPoliticasPrestamo;
    private javax.swing.JSpinner spnDiasPrestamoPorTipo;
    private javax.swing.JSpinner spnMaxEjemplaresGlobal;
    private javax.swing.JSpinner spnMaxEjemplaresPorTipo;
    private javax.swing.JSpinner spnMoraDiariaGlobal;
    private javax.swing.JTable tblPoliticas;
    // End of variables declaration//GEN-END:variables
}