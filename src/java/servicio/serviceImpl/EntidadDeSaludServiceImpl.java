package servicio.serviceImpl;

import dto.EntidadDeSaludDto;
import dto.UsuarioDto;
import external.UsuarioServciceClient;
import servicio.service.EntidadDeSaludService;
import modelo.EntidadDeSalud;
import modelo.Usuario;
import repositorio.EntidadDeSaludRepository;
import mapper.EntidadDeSaludMapper;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

public class EntidadDeSaludServiceImpl implements EntidadDeSaludService {
    private final UsuarioServciceClient usuarioServiceClient;
    private final EntidadDeSaludRepository repository;
    private final EntidadDeSaludMapper mapper;

    public EntidadDeSaludServiceImpl(String tipoDb) throws SQLException {
        this.usuarioServiceClient = new UsuarioServciceClient();
        this.repository = new EntidadDeSaludRepository(tipoDb);
        this.mapper = new EntidadDeSaludMapper(); // Inicializamos el Mapper
    }

    @Override
    public void crearEntidadDeSalud(EntidadDeSaludDto entidadDto) {
        // Verificar si el usuario es válido y tiene el rol de Gerente
        if (!usuarioServiceClient.verificarUsuarioPorRol(entidadDto.usuarioDto().telefono(), "Gerente")) {
            throw new RuntimeException("El usuario no tiene el rol adecuado para crear una entidad de salud.");
        }

        // Verificar si ya existe una entidad de salud con ese nombre
        if (existeEntidadConNombre(entidadDto.nombre())) {
            throw new RuntimeException("Ya existe una entidad de salud con ese nombre.");
        }

        // Convertir el DTO a la entidad y guardar en el repositorio
        EntidadDeSalud entidad = mapper.toModel(entidadDto);
        repository.guardar(entidad, entidadDto.tipo());
    }

    @Override
    public void actualizarEntidadDeSalud(int id, EntidadDeSaludDto entidadDto) {
        // Verificar si el usuario es válido y tiene el rol adecuado
        if (!usuarioServiceClient.verificarUsuarioPorRol(entidadDto.usuarioDto().telefono(), "Gerente")) {
            throw new RuntimeException("El usuario no tiene el rol adecuado para actualizar la entidad de salud.");
        }

        // Verificar si la entidad existe
        if (!repository.verificarSiEntidadDeSaludExiste(id)) {
            throw new RuntimeException("La entidad de salud con el ID proporcionado no existe.");
        }

        // Convertir el DTO a la entidad y actualizar
        EntidadDeSalud entidad = mapper.toModel(entidadDto);
        repository.actualizarEntidadDeSalud(id, entidad, entidadDto.tipo());
    }

    @Override
    public void eliminarEntidadDeSalud(int id) {
        // Verificar si la entidad existe
        if (!repository.verificarSiEntidadDeSaludExiste(id)) {
            throw new RuntimeException("La entidad de salud con el ID proporcionado no existe.");
        }

        // Eliminar la entidad de salud
        repository.eliminarEntidadDeSalud(id);
    }

    @Override
    public EntidadDeSaludDto buscarEntidadDeSalud(int id) {
        // Buscar la entidad en el DAO
        EntidadDeSalud entidad = repository.buscarEntidadDeSaludPorId(id);
        if (entidad == null) {
            throw new RuntimeException("La entidad de salud con el ID proporcionado no existe.");
        }

        // Convertir a DTO
        return mapper.toDto(entidad);
    }

    @Override
    public List<EntidadDeSaludDto> listarEntidadeDeSalud() {
        List<EntidadDeSalud> entidades = repository.listarTodasLasEntidadDeSalud();
        return entidades.stream()
                        .map(mapper::toDto)
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public List<EntidadDeSaludDto> listarEntidadDeSaludPorTipo(String tipo) {
        List<EntidadDeSalud> entidades = repository.buscarEntidadDeSaludPorNombre(tipo);
        return entidades.stream()
                        .map(mapper::toDto)
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public List<EntidadDeSaludDto> listarEntidadDeSaludPorDireccion(String direccion) {
        // Implementación para listar entidades de salud por ciudad
        List<EntidadDeSalud> entidades = repository.buscarEntidadDeSaludPorDireccion(direccion);
        return entidades.stream()
                        .map(mapper::toDto)
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public List<EntidadDeSaludDto> buscarEntidadDeSaludPorGerente(int idUsuarioGerente) {
        // Implementación para buscar entidades de salud por gerente
        List<EntidadDeSalud> entidades = repository.buscarEntidadDeSaludPorGerente(idUsuarioGerente);
        return entidades.stream()
                        .map(mapper::toDto)
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public boolean existeEntidadConNombre(String nombre) {
        return !repository.buscarEntidadDeSaludPorNombre(nombre).isEmpty();
    }

    @Override
    public int contarEntidadesPorTipo(String tipo) {
        return listarEntidadDeSaludPorTipo(tipo).size();
    }

    @Override
    public void registrarEntidadConUsuario(EntidadDeSaludDto entidadDto) {
        // Reutilizar el método de creación
        crearEntidadDeSalud(entidadDto);
    }
}
