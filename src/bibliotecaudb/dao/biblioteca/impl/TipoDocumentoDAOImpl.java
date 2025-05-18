package bibliotecaudb.dao.biblioteca.impl;

import bibliotecaudb.modelo.biblioteca.TipoDocumento;
import bibliotecaudb.dao.biblioteca.TipoDocumentoDAO;
import bibliotecaudb.conexion.ConexionBD;
import bibliotecaudb.conexion.LogsError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TipoDocumentoDAOImpl implements TipoDocumentoDAO {

    private static final String SQL_INSERT = "INSERT INTO tipo_documento (tipo) VALUES (?)";
    private static final String SQL_UPDATE = "UPDATE tipo_documento SET tipo = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM tipo_documento WHERE id = ?";
    private static final String SQL_SELECT_BY_ID = "SELECT id, tipo FROM tipo_documento WHERE id = ?";
    private static final String SQL_SELECT_BY_NAME = "SELECT id, tipo FROM tipo_documento WHERE tipo = ?";
    private static final String SQL_SELECT_ALL = "SELECT id, tipo FROM tipo_documento ORDER BY tipo";

    @Override
    public boolean insertar(TipoDocumento tipoDocumento) throws SQLException {
        // Este metodo sirve para guardar un nuevo tipo de documento en la base de datos.
        Connection conn = null; // Variable para la conexion
        PreparedStatement pstmt = null; // Variable para la consulta SQL
        ResultSet generatedKeys = null; // Para obtener el ID que se genera
        int rowsAffected = 0; // Para saber cuantas filas se afectaron
        try {
            conn = ConexionBD.getConexion(); // Obtenemos la conexion
            pstmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, tipoDocumento.getTipo()); // El nombre del tipo de documento

            LogsError.info(this.getClass(), "Ejecutando consulta para insertar tipo documento: " + SQL_INSERT + " con tipo: " + tipoDocumento.getTipo());
            rowsAffected = pstmt.executeUpdate(); // Ejecutamos la insercion
            if (rowsAffected > 0) {
                generatedKeys = pstmt.getGeneratedKeys(); // Obtenemos el ID generado
                if (generatedKeys.next()) {
                    tipoDocumento.setId(generatedKeys.getInt(1)); // Asignamos el ID al objeto
                }
                LogsError.info(this.getClass(), "TipoDocumento insertado con ID: " + tipoDocumento.getId());
            } else {
                LogsError.warn(this.getClass(), "No se inserto el TipoDocumento.");
            }
        } catch (SQLException ex) {
            LogsError.error(this.getClass(), "Error al insertar tipo de documento: " + ex.getMessage(), ex);
            // Puede ser porque el 'tipo' ya existe y es una llave unica (UNIQUE)
            throw ex; // Relanzamos el error
        } finally {
            ConexionBD.close(generatedKeys); // Cerramos el ResultSet
            ConexionBD.close(pstmt); // Cerramos el PreparedStatement
        }
        return rowsAffected > 0; // Devolvemos true si se inserto algo
    }

    @Override
    public boolean actualizar(TipoDocumento tipoDocumento) throws SQLException {
        // Este metodo sirve para actualizar un tipo de documento existente.
        Connection conn = null; // Variable para la conexion
        PreparedStatement pstmt = null; // Variable para la consulta
        int rowsAffected = 0; // Filas afectadas
        try {
            conn = ConexionBD.getConexion(); // Obtenemos la conexion
            pstmt = conn.prepareStatement(SQL_UPDATE);
            pstmt.setString(1, tipoDocumento.getTipo()); // El nuevo nombre del tipo
            pstmt.setInt(2, tipoDocumento.getId()); // El ID del tipo a actualizar (WHERE)

            LogsError.info(this.getClass(), "Ejecutando consulta para actualizar tipo documento: " + SQL_UPDATE + " para ID: " + tipoDocumento.getId());
            rowsAffected = pstmt.executeUpdate(); // Ejecutamos la actualizacion
            if (rowsAffected > 0) {
                 LogsError.info(this.getClass(), "TipoDocumento actualizado. Filas afectadas: " + rowsAffected);
            } else {
                LogsError.warn(this.getClass(), "No se encontro TipoDocumento para actualizar con ID: " + tipoDocumento.getId() + " o el valor era el mismo.");
            }
        } catch (SQLException ex) {
            LogsError.error(this.getClass(), "Error al actualizar tipo de documento: " + ex.getMessage(), ex);
            throw ex; // Relanzamos el error
        } finally {
            ConexionBD.close(pstmt); // Cerramos el PreparedStatement
        }
        return rowsAffected > 0; // Devolvemos true si se actualizo algo
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        // Este metodo sirve para eliminar un tipo de documento usando su ID.
        Connection conn = null; // Variable para la conexion
        PreparedStatement pstmt = null; // Variable para la consulta
        int rowsAffected = 0; // Filas afectadas
        try {
            conn = ConexionBD.getConexion(); // Obtenemos la conexion
            pstmt = conn.prepareStatement(SQL_DELETE);
            pstmt.setInt(1, id); // El ID del tipo a eliminar

            LogsError.info(this.getClass(), "Ejecutando consulta para eliminar tipo documento: " + SQL_DELETE + " para ID: " + id);
            rowsAffected = pstmt.executeUpdate(); // Ejecutamos la eliminacion
             if (rowsAffected > 0) {
                LogsError.info(this.getClass(), "TipoDocumento eliminado. Filas afectadas: " + rowsAffected);
            } else {
                LogsError.warn(this.getClass(), "No se encontro TipoDocumento para eliminar con ID: " + id);
            }
        } catch (SQLException ex) {
            LogsError.error(this.getClass(), "Error al eliminar tipo de documento: " + ex.getMessage(), ex);
            // Podria fallar si hay documentos que usan este tipo (error de llave foranea).
            throw ex; // Relanzamos el error
        } finally {
            ConexionBD.close(pstmt); // Cerramos el PreparedStatement
        }
        return rowsAffected > 0; // Devolvemos true si se elimino algo
    }

    // Este metodo convierte los datos de la base de datos (ResultSet) a un objeto TipoDocumento.
    private TipoDocumento mapearResultSet(ResultSet rs) throws SQLException {
        TipoDocumento td = new TipoDocumento(); // Creamos un objeto TipoDocumento vacio
        td.setId(rs.getInt("id"));
        td.setTipo(rs.getString("tipo"));
        return td; // Devolvemos el tipo de documento con sus datos
    }

    @Override
    public TipoDocumento obtenerPorId(int id) throws SQLException {
        // Este metodo busca y devuelve un tipo de documento usando su ID.
        Connection conn = null; // Variable para la conexion
        PreparedStatement pstmt = null; // Variable para la consulta
        ResultSet rs = null; // Para el resultado
        TipoDocumento tipoDocumento = null; // Variable para el tipo de documento
        try {
            conn = ConexionBD.getConexion(); // Obtenemos la conexion
            pstmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            pstmt.setInt(1, id); // El ID que buscamos
            LogsError.info(this.getClass(), "Ejecutando consulta para obtener tipo documento por ID: " + SQL_SELECT_BY_ID + " con ID: " + id);
            rs = pstmt.executeQuery(); // Ejecutamos la consulta
            if (rs.next()) { // Si encontramos el tipo
                tipoDocumento = mapearResultSet(rs); // Convertimos los datos a objeto
            } else {
                LogsError.warn(this.getClass(), "No se encontro TipoDocumento con ID: " + id);
            }
        } catch (SQLException ex) {
            LogsError.error(this.getClass(), "Error al obtener tipo de documento por ID: " + ex.getMessage(), ex);
            throw ex; // Relanzamos el error
        } finally {
            ConexionBD.close(rs); // Cerramos el ResultSet
            ConexionBD.close(pstmt); // Cerramos el PreparedStatement
        }
        return tipoDocumento; // Devolvemos el tipo (o null)
    }

    @Override
    public TipoDocumento obtenerPorNombre(String nombreTipo) throws SQLException {
        // Este metodo busca y devuelve un tipo de documento usando su nombre.
        Connection conn = null; // Variable para la conexion
        PreparedStatement pstmt = null; // Variable para la consulta
        ResultSet rs = null; // Para el resultado
        TipoDocumento tipoDocumento = null; // Variable para el tipo
        try {
            conn = ConexionBD.getConexion(); // Obtenemos la conexion
            pstmt = conn.prepareStatement(SQL_SELECT_BY_NAME);
            pstmt.setString(1, nombreTipo); // El nombre del tipo que buscamos
            LogsError.info(this.getClass(), "Ejecutando consulta para obtener tipo documento por nombre: " + SQL_SELECT_BY_NAME + " con nombre: " + nombreTipo);
            rs = pstmt.executeQuery(); // Ejecutamos la consulta
            if (rs.next()) { // Si encontramos el tipo
                tipoDocumento = mapearResultSet(rs); // Convertimos los datos a objeto
            } else {
                LogsError.warn(this.getClass(), "No se encontro TipoDocumento con nombre: " + nombreTipo);
            }
        } catch (SQLException ex) {
            LogsError.error(this.getClass(), "Error al obtener tipo de documento por nombre: " + ex.getMessage(), ex);
            throw ex; // Relanzamos el error
        } finally {
            ConexionBD.close(rs); // Cerramos el ResultSet
            ConexionBD.close(pstmt); // Cerramos el PreparedStatement
        }
        return tipoDocumento; // Devolvemos el tipo (o null)
    }

    @Override
    public List<TipoDocumento> obtenerTodos() throws SQLException {
        // Este metodo devuelve una lista con todos los tipos de documento de la base de datos.
        Connection conn = null; // Variable para la conexion
        PreparedStatement pstmt = null; // Variable para la consulta
        ResultSet rs = null; // Para los resultados
        List<TipoDocumento> tiposDocumento = new ArrayList<>(); // Lista para guardar los tipos
        try {
            conn = ConexionBD.getConexion(); // Obtenemos la conexion
            pstmt = conn.prepareStatement(SQL_SELECT_ALL);
            LogsError.info(this.getClass(), "Ejecutando consulta para obtener todos los tipos de documento: " + SQL_SELECT_ALL);
            rs = pstmt.executeQuery(); // Ejecutamos la consulta
            while (rs.next()) { // Mientras haya tipos
                tiposDocumento.add(mapearResultSet(rs)); // Agregamos el tipo a la lista
            }
        } catch (SQLException ex) {
            LogsError.error(this.getClass(), "Error al obtener todos los tipos de documento: " + ex.getMessage(), ex);
            throw ex; // Relanzamos el error
        } finally {
            ConexionBD.close(rs); // Cerramos el ResultSet
            ConexionBD.close(pstmt); // Cerramos el PreparedStatement
        }
        return tiposDocumento; // Devolvemos la lista de tipos
    }
}