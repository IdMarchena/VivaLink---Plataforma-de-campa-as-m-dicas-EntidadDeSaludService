package mapper;

import dto.EntidadDeSaludDto;
import dto.UsuarioDto;
import modelo.EntidadDeSalud;
import modelo.Usuario;
import java.util.List;
import java.util.stream.Collectors;
import factory.EntidadDeSaludFactory;

public class EntidadDeSaludMapper {

    // Convertir de Modelo a DTO
    public EntidadDeSaludDto toDto(EntidadDeSalud entity) {
        if (entity == null) {
            return null;
        }
        
        // Convertir Usuario a UsuarioDto
        UsuarioDto usuarioDto = toUsuarioDto(entity.getUsuarioGerente());
        
        // Crear y devolver el DTO de EntidadDeSalud
        return new EntidadDeSaludDto(
                entity.getTipo(),
                entity.getNombre(),
                entity.getDireccion(),
                entity.getTelefono(),
                entity.getTipo(),
                usuarioDto
        );
    }

    // Convertir de DTO a Modelo
    public EntidadDeSalud toModel(EntidadDeSaludDto dto) {
        if (dto == null) {
            return null;
        }
        
        // Convertir UsuarioDto a Usuario
        Usuario usuario = toUsuarioModel(dto.usuarioDto());
        
        // Crear y devolver el modelo de EntidadDeSalud
        return EntidadDeSaludFactory.crearEntidad(
                dto.tipo(),         
                dto.nombre(),       
                dto.direccion(),   
                dto.telefono(),      
                usuario,            
                dto.identificador() 
        );
    }

    // Convertir lista de Modelos a lista de DTOs
    public List<EntidadDeSaludDto> toDtoList(List<EntidadDeSalud> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        return entities.stream()
                       .map(this::toDto)
                       .collect(Collectors.toList());
    }

    // Convertir lista de DTOs a lista de Modelos
    public List<EntidadDeSalud> toModelList(List<EntidadDeSaludDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return null;
        }
        return dtos.stream()
                   .map(this::toModel)
                   .collect(Collectors.toList());
    }

    // Convertir Usuario a UsuarioDto
    public UsuarioDto toUsuarioDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return new UsuarioDto(
                usuario.getId(),
                usuario.getTelefono(),
                usuario.getSalarioBase(),
                usuario.getFechaNacimient(),
                usuario.getSexo(),
                usuario.getDireccion()
        );
    }

    // Convertir UsuarioDto a Usuario
    public Usuario toUsuarioModel(UsuarioDto usuarioDto) {
        if (usuarioDto == null) {
            return null;
        }
        return new Usuario(
                usuarioDto.id(),
                usuarioDto.telefono(),
                usuarioDto.SalarioBase(),
                usuarioDto.fechaNacimient(),
                usuarioDto.sexo(),
                usuarioDto.direccion()
        );
    }
}
