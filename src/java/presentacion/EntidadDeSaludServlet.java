package presentacion;

import servicio.service.EntidadDeSaludService;
import servicio.serviceImpl.EntidadDeSaludServiceImpl;
import dto.EntidadDeSaludDto;
import dto.UsuarioDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.google.gson.Gson;

@WebServlet(name = "EntidadDeSaludServlet", urlPatterns = {"/EntidadDeSaludServlet"})
public class EntidadDeSaludServlet extends HttpServlet {

    private final EntidadDeSaludService entidadDeSaludService;
    private final Gson gson;

    // Constructor
    public EntidadDeSaludServlet() throws SQLException {
        this.entidadDeSaludService = new EntidadDeSaludServiceImpl("TipoDb");
        this.gson = new Gson();
    }

    // Método para procesar las peticiones
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        System.out.println("Acción recibida: " + action);

        // Configurar respuesta como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Filtra las acciones según el parámetro "action"
            if ("crear".equals(action)) {
                crearEntidadDeSalud(request, response);
            } else if ("listar".equals(action)) {
                listarEntidades(request, response);
            } else if ("buscar".equals(action)) {
                buscarEntidadDeSalud(request, response);
            } else if ("actualizar".equals(action)) {
                actualizarEntidadDeSalud(request, response);
            } else if ("eliminar".equals(action)) {
                eliminarEntidadDeSalud(request, response);
            } else if ("listarPorTipo".equals(action)) {
                listarEntidadDeSaludPorTipo(request, response);
            } else if ("listarPorDireccion".equals(action)) {
                listarEntidadDeSaludPorDireccion(request, response);
            } else if ("buscarPorGerente".equals(action)) {
                buscarEntidadDeSaludPorGerente(request, response);
            } else if ("existeEntidadConNombre".equals(action)) {
                existeEntidadConNombre(request, response);
            } else if ("contarEntidadesPorTipo".equals(action)) {
                contarEntidadesPorTipo(request, response);
            } else if ("registrarEntidadConUsuario".equals(action)) {
                registrarEntidadConUsuario(request, response);
            } else {
                // Acción no reconocida
                JsonResponse<Object> jsonResponse = new JsonResponse<>(
                    false, 
                    "Acción no reconocida: " + action, 
                    null, 
                    HttpServletResponse.SC_BAD_REQUEST
                );
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(jsonResponse));
            }
        } catch (Exception e) {
            JsonResponse<Object> errorResponse = new JsonResponse<>(
                false, 
                "Error interno del servidor: " + e.getMessage(), 
                null, 
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(errorResponse));
        }
    }

    // Acción para crear una entidad de salud
    private void crearEntidadDeSalud(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String tipo = request.getParameter("tipo");
        String idUsuario = request.getParameter("idUsuario");

        try {
            // Validar parámetros requeridos
            if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() ||
                telefono == null || telefono.isEmpty() || tipo == null || tipo.isEmpty() ||
                idUsuario == null || idUsuario.isEmpty()) {
                
                JsonResponse<Object> jsonResponse = new JsonResponse<>(
                    false, 
                    "Todos los campos son requeridos", 
                    null, 
                    HttpServletResponse.SC_BAD_REQUEST
                );
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }

            UsuarioDto usuarioDto = obtenerUsuarioPorId(Integer.parseInt(idUsuario));
            EntidadDeSaludDto entidadDto = new EntidadDeSaludDto(tipo, nombre, direccion, telefono, idUsuario, usuarioDto);
            entidadDeSaludService.crearEntidadDeSalud(entidadDto);
            
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                true, 
                "Entidad de Salud creada con éxito", 
                null, 
                HttpServletResponse.SC_CREATED
            );
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (Exception e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "Hubo un error al crear la entidad de salud: " + e.getMessage(), 
                null, 
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }

    // Acción para listar las entidades de salud
    private void listarEntidades(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<EntidadDeSaludDto> entidades = entidadDeSaludService.listarEntidadeDeSalud();
        
        JsonResponse<List<EntidadDeSaludDto>> jsonResponse = new JsonResponse<>(
            true, 
            "Entidades de salud obtenidas exitosamente", 
            entidades, 
            HttpServletResponse.SC_OK
        );
        
        response.getWriter().write(gson.toJson(jsonResponse));
    }

    // Acción para listar entidades de salud por tipo
    private void listarEntidadDeSaludPorTipo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tipo = request.getParameter("tipo");
        
        if (tipo == null || tipo.isEmpty()) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "El parámetro tipo es requerido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }
        
        List<EntidadDeSaludDto> entidades = entidadDeSaludService.listarEntidadDeSaludPorTipo(tipo);
        
        JsonResponse<List<EntidadDeSaludDto>> jsonResponse = new JsonResponse<>(
            true, 
            "Entidades de salud por tipo obtenidas exitosamente", 
            entidades, 
            HttpServletResponse.SC_OK
        );
        
        response.getWriter().write(gson.toJson(jsonResponse));
    }

    // Acción para listar entidades de salud por dirección
    private void listarEntidadDeSaludPorDireccion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String direccion = request.getParameter("direccion");
        
        if (direccion == null || direccion.isEmpty()) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "El parámetro direccion es requerido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }
        
        List<EntidadDeSaludDto> entidades = entidadDeSaludService.listarEntidadDeSaludPorDireccion(direccion);
        
        JsonResponse<List<EntidadDeSaludDto>> jsonResponse = new JsonResponse<>(
            true, 
            "Entidades de salud por dirección obtenidas exitosamente", 
            entidades, 
            HttpServletResponse.SC_OK
        );
        
        response.getWriter().write(gson.toJson(jsonResponse));
    }

    // Acción para buscar una entidad de salud por ID
    private void buscarEntidadDeSalud(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idd = request.getParameter("id");
        
        if (idd == null || idd.isEmpty()) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "El parámetro id es requerido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }
        
        try {
            int id = Integer.parseInt(idd);
            EntidadDeSaludDto entidad = entidadDeSaludService.buscarEntidadDeSalud(id);
            
            if (entidad != null) {
                JsonResponse<EntidadDeSaludDto> jsonResponse = new JsonResponse<>(
                    true, 
                    "Entidad de salud encontrada", 
                    entidad, 
                    HttpServletResponse.SC_OK
                );
                response.getWriter().write(gson.toJson(jsonResponse));
            } else {
                JsonResponse<Object> jsonResponse = new JsonResponse<>(
                    false, 
                    "Entidad de salud no encontrada", 
                    null, 
                    HttpServletResponse.SC_NOT_FOUND
                );
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson(jsonResponse));
            }
        } catch (NumberFormatException e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "ID de entidad de salud inválido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (Exception e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "Error al buscar la entidad de salud: " + e.getMessage(), 
                null, 
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }

    // Acción para actualizar una entidad de salud
    private void actualizarEntidadDeSalud(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idd = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String tipo = request.getParameter("tipo");
        String idUsuario = request.getParameter("idUsuario");

        try {
            int id = Integer.parseInt(idd);
            
            // Validar parámetros requeridos
            if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() ||
                telefono == null || telefono.isEmpty() || tipo == null || tipo.isEmpty() ||
                idUsuario == null || idUsuario.isEmpty()) {
                
                JsonResponse<Object> jsonResponse = new JsonResponse<>(
                    false, 
                    "Todos los campos son requeridos", 
                    null, 
                    HttpServletResponse.SC_BAD_REQUEST
                );
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }

            UsuarioDto usuarioDto = obtenerUsuarioPorId(Integer.parseInt(idUsuario));
            EntidadDeSaludDto entidadDto = new EntidadDeSaludDto(tipo, nombre, direccion, telefono, idUsuario, usuarioDto);
            entidadDeSaludService.actualizarEntidadDeSalud(id, entidadDto);
            
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                true, 
                "Entidad de Salud actualizada con éxito", 
                null, 
                HttpServletResponse.SC_OK
            );
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (NumberFormatException e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "ID de entidad de salud inválido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (Exception e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "Hubo un error al actualizar la entidad de salud: " + e.getMessage(), 
                null, 
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }

    // Acción para eliminar una entidad de salud
    private void eliminarEntidadDeSalud(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idd = request.getParameter("id");

        try {
            int id = Integer.parseInt(idd);
            entidadDeSaludService.eliminarEntidadDeSalud(id);
            
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                true, 
                "Entidad de Salud eliminada con éxito", 
                null, 
                HttpServletResponse.SC_OK
            );
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (NumberFormatException e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "ID de entidad de salud inválido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (Exception e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "Hubo un error al eliminar la entidad de salud: " + e.getMessage(), 
                null, 
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }

    // Acción para buscar entidades de salud por gerente
    private void buscarEntidadDeSaludPorGerente(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idUsuarioGerente = request.getParameter("idUsuarioGerente");
        
        if (idUsuarioGerente == null || idUsuarioGerente.isEmpty()) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "El parámetro idUsuarioGerente es requerido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }
        
        try {
            List<EntidadDeSaludDto> entidades = entidadDeSaludService.buscarEntidadDeSaludPorGerente(Integer.parseInt(idUsuarioGerente));
            
            JsonResponse<List<EntidadDeSaludDto>> jsonResponse = new JsonResponse<>(
                true, 
                "Entidades de salud por gerente obtenidas exitosamente", 
                entidades, 
                HttpServletResponse.SC_OK
            );
            
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (NumberFormatException e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "ID de usuario gerente inválido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (Exception e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "Error al buscar entidades por gerente: " + e.getMessage(), 
                null, 
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }

    // Acción para verificar si existe una entidad de salud con un nombre específico
    private void existeEntidadConNombre(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nombre = request.getParameter("nombre");
        
        if (nombre == null || nombre.isEmpty()) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "El parámetro nombre es requerido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }
        
        boolean existe = entidadDeSaludService.existeEntidadConNombre(nombre);
        
        JsonResponse<Boolean> jsonResponse = new JsonResponse<>(
            true, 
            "Verificación de existencia completada", 
            existe, 
            HttpServletResponse.SC_OK
        );
        
        response.getWriter().write(gson.toJson(jsonResponse));
    }

    // Acción para contar las entidades de salud por tipo
    private void contarEntidadesPorTipo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String tipo = request.getParameter("tipo");
        
        if (tipo == null || tipo.isEmpty()) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "El parámetro tipo es requerido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
            return;
        }
        
        int cantidad = entidadDeSaludService.contarEntidadesPorTipo(tipo);
        
        JsonResponse<Integer> jsonResponse = new JsonResponse<>(
            true, 
            "Conteo de entidades por tipo completado", 
            cantidad, 
            HttpServletResponse.SC_OK
        );
        
        response.getWriter().write(gson.toJson(jsonResponse));
    }

    // Acción para registrar una entidad de salud con un usuario
    private void registrarEntidadConUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String tipo = request.getParameter("tipo");
        String idUsuario = request.getParameter("idUsuario");

        try {
            // Validar parámetros requeridos
            if (nombre == null || nombre.isEmpty() || direccion == null || direccion.isEmpty() ||
                telefono == null || telefono.isEmpty() || tipo == null || tipo.isEmpty() ||
                idUsuario == null || idUsuario.isEmpty()) {
                
                JsonResponse<Object> jsonResponse = new JsonResponse<>(
                    false, 
                    "Todos los campos son requeridos", 
                    null, 
                    HttpServletResponse.SC_BAD_REQUEST
                );
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(jsonResponse));
                return;
            }

            UsuarioDto usuarioDto = obtenerUsuarioPorId(Integer.parseInt(idUsuario));
            EntidadDeSaludDto entidadDto = new EntidadDeSaludDto(tipo, nombre, direccion, telefono, idUsuario, usuarioDto);
            entidadDeSaludService.registrarEntidadConUsuario(entidadDto);
            
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                true, 
                "Entidad de Salud registrada con éxito", 
                null, 
                HttpServletResponse.SC_CREATED
            );
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (NumberFormatException e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "ID de usuario inválido", 
                null, 
                HttpServletResponse.SC_BAD_REQUEST
            );
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (Exception e) {
            JsonResponse<Object> jsonResponse = new JsonResponse<>(
                false, 
                "Hubo un error al registrar la entidad de salud: " + e.getMessage(), 
                null, 
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(jsonResponse));
        }
    }

    // Método para obtener el UsuarioDto por ID (esto debería hacerse en tu servicio o DAO)
    private UsuarioDto obtenerUsuarioPorId(int idUsuario) throws Exception {
        return new UsuarioDto(idUsuario, "telefonoEjemplo", 5000, java.time.LocalDate.now(), "Masculino", "Direccion Ejemplo");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}