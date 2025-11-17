package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.EntidadDeSalud;
import modelo.Usuario;
import modelo.EntidadDeSaludPublica;
import modelo.EntidadDeSaludPrivada;
import dao.conexion.DatabaseConnection;
import factory.EntidadDeSaludFactory;
import factory.DatabaseConnectionFactory;

public class EntidadDeSaludDaoPostgres implements EntidadDeSaludDao {

    private final Connection conn;

    public EntidadDeSaludDaoPostgres(DatabaseConnection connection) throws SQLException {
        DatabaseConnection db = DatabaseConnectionFactory.connection("postgres");
        this.conn = db.getConnection();
    }

    // -----------------------------------------------------------
    //  BUSCAR POR ID
    // -----------------------------------------------------------
    @Override
    public EntidadDeSalud buscarEntidadDeSaludPorId(int id) {
        String sql = """
            SELECT 
                e.id,
                e.nombre,
                e.direccion,
                e.telefono,
                e.id_usuario_gerente,
                e.tipo,
                ep.nit_empresa,
                epub.codigo_ministerio,
                u.id AS usuario_id,
                u.telefono AS usuario_telefono,
                u.salario_base AS usuario_salario_base,
                u.fecha_nacimiento AS usuario_fecha_nacimiento,
                u.sexo AS usuario_sexo,
                u.direccion AS usuario_direccion
            FROM entidades_salud e
            LEFT JOIN entidades_salud_privadas ep ON e.id = ep.id_entidad
            LEFT JOIN entidades_salud_publicas epub ON e.id = epub.id_entidad
            LEFT JOIN usuarios u ON u.id = e.id_usuario_gerente
            WHERE e.id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            Usuario usuarioGerente = new Usuario(
                    rs.getInt("usuario_id"),
                    rs.getString("usuario_telefono"),
                    rs.getInt("usuario_salario_base"),
                    rs.getDate("usuario_fecha_nacimiento").toLocalDate(),
                    rs.getString("usuario_sexo"),
                    rs.getString("usuario_direccion")
            );

            String tipo = rs.getString("tipo");
            String identificador = tipo.equals("privada")
                    ? rs.getString("nit_empresa")
                    : rs.getString("codigo_ministerio");

            return EntidadDeSaludFactory.crearEntidad(
                    tipo,
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    usuarioGerente,
                    identificador
            );

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando entidad: " + e.getMessage(), e);
        }
    }

    // -----------------------------------------------------------
    //  VERIFICAR EXISTENCIA
    // -----------------------------------------------------------
    @Override
    public boolean verificarSiEntidadDeSaludExiste(int id) {
        return buscarEntidadDeSaludPorId(id) != null;
    }

    // -----------------------------------------------------------
    //  GUARDAR - CORREGIDO
    // -----------------------------------------------------------
    @Override
    public void guardar(EntidadDeSalud entidad, String tipo) {
        String sqlEntidad = """
            INSERT INTO entidades_salud(nombre, direccion, telefono, id_usuario_gerente, tipo)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id
        """;
        String sqlPublica = """
            INSERT INTO entidades_salud_publicas(id_entidad, codigo_ministerio)
            VALUES (?, ?)
        """;
        String sqlPrivada = """
            INSERT INTO entidades_salud_privadas(id_entidad, nit_empresa)
            VALUES (?, ?)
        """;

        try {
            conn.setAutoCommit(false); // Iniciar transacción

            try (PreparedStatement ps1 = conn.prepareStatement(sqlEntidad)) {
                ps1.setString(1, entidad.getNombre());
                ps1.setString(2, entidad.getDireccion());
                ps1.setString(3, entidad.getTelefono());
                ps1.setInt(4, entidad.getUsuarioGerente().getId());
                ps1.setString(5, tipo);
                
                ResultSet rs = ps1.executeQuery();
                rs.next();
                int idEntidad = rs.getInt("id");

                if (tipo.equals("publica")) {
                    try (PreparedStatement ps2 = conn.prepareStatement(sqlPublica)) {
                        ps2.setInt(1, idEntidad);
                        // CORRECCIÓN: Usar getCodigoMinisterio() en lugar de getTipo()
                        ps2.setString(2, ((EntidadDeSaludPublica) entidad).getCodigoMinisterio());
                        ps2.executeUpdate();
                    }
                } else {
                    try (PreparedStatement ps2 = conn.prepareStatement(sqlPrivada)) {
                        ps2.setInt(1, idEntidad);
                        // CORRECCIÓN: Usar getNitEmpresa() en lugar de getTipo()
                        ps2.setString(2, ((EntidadDeSaludPrivada) entidad).getNitEmpresa());
                        ps2.executeUpdate();
                    }
                }
            }

            conn.commit(); // Confirmar transacción

        } catch (SQLException e) {
            try {
                conn.rollback(); // Revertir en caso de error
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Error en rollback: " + rollbackEx.getMessage(), rollbackEx);
            }
            throw new RuntimeException("Error guardando entidad: " + e.getMessage(), e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                // Log error but don't throw
            }
        }
    }

    // -----------------------------------------------------------
    //  LISTAR TODAS
    // -----------------------------------------------------------
    @Override
    public List<EntidadDeSalud> listarTodasLasEntidadDeSalud() {
        List<EntidadDeSalud> lista = new ArrayList<>();

        String sql = """
            SELECT 
                e.id,
                e.nombre,
                e.direccion,
                e.telefono,
                e.id_usuario_gerente,
                e.tipo,
                ep.nit_empresa,
                epub.codigo_ministerio,
                u.id AS usuario_id,
                u.telefono AS usuario_telefono,
                u.salario_base AS usuario_salario_base,
                u.fecha_nacimiento AS usuario_fecha_nacimiento,
                u.sexo AS usuario_sexo,
                u.direccion AS usuario_direccion
            FROM entidades_salud e
            LEFT JOIN entidades_salud_privadas ep ON e.id = ep.id_entidad
            LEFT JOIN entidades_salud_publicas epub ON e.id = epub.id_entidad
            LEFT JOIN usuarios u ON u.id = e.id_usuario_gerente
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usuarioGerente = new Usuario(
                        rs.getInt("usuario_id"),
                        rs.getString("usuario_telefono"),
                        rs.getInt("usuario_salario_base"),
                        rs.getDate("usuario_fecha_nacimiento").toLocalDate(),
                        rs.getString("usuario_sexo"),
                        rs.getString("usuario_direccion")
                );

                String tipo = rs.getString("tipo");
                String identificador = tipo.equals("privada")
                        ? rs.getString("nit_empresa")
                        : rs.getString("codigo_ministerio");

                EntidadDeSalud entidad = EntidadDeSaludFactory.crearEntidad(
                        tipo,
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        usuarioGerente,
                        identificador
                );

                lista.add(entidad);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar: " + e.getMessage(), e);
        }

        return lista;
    }

    // -----------------------------------------------------------
    //  ACTUALIZAR - CORREGIDO
    // -----------------------------------------------------------
    @Override
    public void actualizarEntidadDeSalud(int id, EntidadDeSalud entidad, String tipo) {
        String sqlEntidad = """
            UPDATE entidades_salud 
            SET nombre = ?, direccion = ?, telefono = ?, id_usuario_gerente = ?, tipo = ?
            WHERE id = ?
        """;
        String sqlPublica = """
            UPDATE entidades_salud_publicas 
            SET codigo_ministerio = ?
            WHERE id_entidad = ?
        """;
        String sqlPrivada = """
            UPDATE entidades_salud_privadas 
            SET nit_empresa = ?
            WHERE id_entidad = ?
        """;

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlEntidad)) {
                ps1.setString(1, entidad.getNombre());
                ps1.setString(2, entidad.getDireccion());
                ps1.setString(3, entidad.getTelefono());
                ps1.setInt(4, entidad.getUsuarioGerente().getId());
                ps1.setString(5, tipo);
                ps1.setInt(6, id);
                ps1.executeUpdate();
            }

            if (tipo.equals("publica")) {
                try (PreparedStatement ps2 = conn.prepareStatement(sqlPublica)) {
                    ps2.setString(1, ((EntidadDeSaludPublica) entidad).getCodigoMinisterio());
                    ps2.setInt(2, id);
                    ps2.executeUpdate();
                }
            } else {
                try (PreparedStatement ps2 = conn.prepareStatement(sqlPrivada)) {
                    ps2.setString(1, ((EntidadDeSaludPrivada) entidad).getNitEmpresa());
                    ps2.setInt(2, id);
                    ps2.executeUpdate();
                }
            }

            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Error en rollback: " + rollbackEx.getMessage(), rollbackEx);
            }
            throw new RuntimeException("Error actualizando entidad: " + e.getMessage(), e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                // Log error
            }
        }
    }

    // -----------------------------------------------------------
    //  ELIMINAR
    // -----------------------------------------------------------
    @Override
    public void eliminarEntidadDeSalud(int id) {
        // No necesitamos eliminar manualmente de las tablas hijas por el ON DELETE CASCADE
        String sql = "DELETE FROM entidades_salud WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows == 0) {
                throw new RuntimeException("No se encontró la entidad con ID: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando entidad: " + e.getMessage(), e);
        }
    }

    // Los métodos restantes (buscar por nombre, dirección, gerente) se mantienen igual
    // ya que solo son consultas y no tienen el problema de inserción/actualización

    @Override
    public List<EntidadDeSalud> buscarEntidadDeSaludPorNombre(String nombre) {
        List<EntidadDeSalud> lista = new ArrayList<>();
        String sql = """
            SELECT 
                e.id,
                e.nombre,
                e.direccion,
                e.telefono,
                e.id_usuario_gerente,
                e.tipo,
                ep.nit_empresa,
                epub.codigo_ministerio,
                u.id AS usuario_id,
                u.telefono AS usuario_telefono,
                u.salario_base AS usuario_salario_base,
                u.fecha_nacimiento AS usuario_fecha_nacimiento,
                u.sexo AS usuario_sexo,
                u.direccion AS usuario_direccion
            FROM entidades_salud e
            LEFT JOIN entidades_salud_privadas ep ON e.id = ep.id_entidad
            LEFT JOIN entidades_salud_publicas epub ON e.id = epub.id_entidad
            LEFT JOIN usuarios u ON u.id = e.id_usuario_gerente
            WHERE LOWER(e.nombre) LIKE LOWER(?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usuarioGerente = new Usuario(
                        rs.getInt("usuario_id"),
                        rs.getString("usuario_telefono"),
                        rs.getInt("usuario_salario_base"),
                        rs.getDate("usuario_fecha_nacimiento").toLocalDate(),
                        rs.getString("usuario_sexo"),
                        rs.getString("usuario_direccion")
                );

                String tipo = rs.getString("tipo");
                String identificador = tipo.equals("privada")
                        ? rs.getString("nit_empresa")
                        : rs.getString("codigo_ministerio");

                lista.add(
                        EntidadDeSaludFactory.crearEntidad(
                                tipo,
                                rs.getString("nombre"),
                                rs.getString("direccion"),
                                rs.getString("telefono"),
                                usuarioGerente,
                                identificador
                        )
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando por nombre: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public List<EntidadDeSalud> buscarEntidadDeSaludPorDireccion(String direccion) {
        List<EntidadDeSalud> lista = new ArrayList<>();
        String sql = """
            SELECT 
                e.id,
                e.nombre,
                e.direccion,
                e.telefono,
                e.id_usuario_gerente,
                e.tipo,
                ep.nit_empresa,
                epub.codigo_ministerio,
                u.id AS usuario_id,
                u.telefono AS usuario_telefono,
                u.salario_base AS usuario_salario_base,
                u.fecha_nacimiento AS usuario_fecha_nacimiento,
                u.sexo AS usuario_sexo,
                u.direccion AS usuario_direccion
            FROM entidades_salud e
            LEFT JOIN entidades_salud_privadas ep ON e.id = ep.id_entidad
            LEFT JOIN entidades_salud_publicas epub ON e.id = epub.id_entidad
            LEFT JOIN usuarios u ON u.id = e.id_usuario_gerente
            WHERE LOWER(e.direccion) LIKE LOWER(?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + direccion + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usuarioGerente = new Usuario(
                        rs.getInt("usuario_id"),
                        rs.getString("usuario_telefono"),
                        rs.getInt("usuario_salario_base"),
                        rs.getDate("usuario_fecha_nacimiento").toLocalDate(),
                        rs.getString("usuario_sexo"),
                        rs.getString("usuario_direccion")
                );

                String tipo = rs.getString("tipo");
                String identificador = tipo.equals("privada")
                        ? rs.getString("nit_empresa")
                        : rs.getString("codigo_ministerio");

                lista.add(
                        EntidadDeSaludFactory.crearEntidad(
                                tipo,
                                rs.getString("nombre"),
                                rs.getString("direccion"),
                                rs.getString("telefono"),
                                usuarioGerente,
                                identificador
                        )
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando por dirección: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public List<EntidadDeSalud> buscarEntidadDeSaludPorGerente(int idGerente) {
        List<EntidadDeSalud> lista = new ArrayList<>();
        String sql = """
            SELECT 
                e.id,
                e.nombre,
                e.direccion,
                e.telefono,
                e.id_usuario_gerente,
                e.tipo,
                ep.nit_empresa,
                epub.codigo_ministerio,
                u.id AS usuario_id,
                u.telefono AS usuario_telefono,
                u.salario_base AS usuario_salario_base,
                u.fecha_nacimiento AS usuario_fecha_nacimiento,
                u.sexo AS usuario_sexo,
                u.direccion AS usuario_direccion
            FROM entidades_salud e
            LEFT JOIN entidades_salud_privadas ep ON e.id = ep.id_entidad
            LEFT JOIN entidades_salud_publicas epub ON e.id = epub.id_entidad
            LEFT JOIN usuarios u ON u.id = e.id_usuario_gerente
            WHERE e.id_usuario_gerente = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idGerente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Usuario usuarioGerente = new Usuario(
                        rs.getInt("usuario_id"),
                        rs.getString("usuario_telefono"),
                        rs.getInt("usuario_salario_base"),
                        rs.getDate("usuario_fecha_nacimiento").toLocalDate(),
                        rs.getString("usuario_sexo"),
                        rs.getString("usuario_direccion")
                );

                String tipo = rs.getString("tipo");
                String identificador = tipo.equals("privada")
                        ? rs.getString("nit_empresa")
                        : rs.getString("codigo_ministerio");

                lista.add(
                        EntidadDeSaludFactory.crearEntidad(
                                tipo,
                                rs.getString("nombre"),
                                rs.getString("direccion"),
                                rs.getString("telefono"),
                                usuarioGerente,
                                identificador
                        )
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando por gerente: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public Usuario buscarUsuarioPorIdEntidadDeSalud(int id) {
                String sql = """
            SELECT 
                e.id,
                e.nombre,
                e.direccion,
                e.telefono,
                e.id_usuario_gerente,
                e.tipo,
                ep.nit_empresa,
                epub.codigo_ministerio,
                u.id AS usuario_id,
                u.telefono AS usuario_telefono,
                u.salario_base AS usuario_salario_base,
                u.fecha_nacimiento AS usuario_fecha_nacimiento,
                u.sexo AS usuario_sexo,
                u.direccion AS usuario_direccion
            FROM entidades_salud e
            LEFT JOIN entidades_salud_privadas ep ON e.id = ep.id_entidad
            LEFT JOIN entidades_salud_publicas epub ON e.id = epub.id_entidad
            LEFT JOIN usuarios u ON u.id = e.id_usuario_gerente
            WHERE e.id = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }
            Usuario usuarioGerente = new Usuario(
                    rs.getInt("usuario_id"),
                    rs.getString("usuario_telefono"),
                    rs.getInt("usuario_salario_base"),
                    rs.getDate("usuario_fecha_nacimiento").toLocalDate(),
                    rs.getString("usuario_sexo"),
                    rs.getString("usuario_direccion")
            );
            return usuarioGerente;

        } catch (SQLException e) {
            throw new RuntimeException("Error buscando entidad: " + e.getMessage(), e);
        }
    }
}