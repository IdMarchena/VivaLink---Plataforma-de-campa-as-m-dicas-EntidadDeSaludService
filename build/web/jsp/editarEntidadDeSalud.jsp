<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title>Editar Entidad de Salud</title>
</head>
<body>
    <h1>Editar Entidad de Salud</h1>

    <form action="EntidadDeSaludServlet" method="POST">
        <input type="hidden" name="action" value="actualizar" />
        <input type="hidden" name="id" value="${entidad.identificador}" />

        <label for="nombre">Nombre: </label>
        <input type="text" name="nombre" value="${entidad.nombre}" required /><br>

        <label for="direccion">Dirección: </label>
        <input type="text" name="direccion" value="${entidad.direccion}" required /><br>

        <label for="telefono">Teléfono: </label>
        <input type="text" name="telefono" value="${entidad.telefono}" required /><br>

        <label for="tipo">Tipo: </label>
        <input type="text" name="tipo" value="${entidad.tipo}" required /><br>

        <label for="idUsuario">ID del Usuario: </label>
        <input type="text" name="idUsuario" value="${entidad.usuarioDto.id}" required /><br>

        <button type="submit">Actualizar</button>
    </form>

    <a href="EntidadDeSaludServlet?action=listar">Volver a la lista</a>
</body>
</html>
