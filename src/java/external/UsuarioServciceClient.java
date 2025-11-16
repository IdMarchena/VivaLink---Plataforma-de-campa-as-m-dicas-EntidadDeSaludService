package external;

import dto.UsuarioDto;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class UsuarioServciceClient {
    private static final String BASE_URL = "http://localhost:8080/UsuarioService/UsuarioServlet";

    // Método para verificar si el usuario existe y tiene el rol Gerente
 public boolean verificarUsuarioPorRol(String nombre, String rol) {
        try {
            // Preparar los parámetros
            String params = String.format("nombre=%s&rol=%s", 
                URLEncoder.encode(nombre, "UTF-8"), 
                URLEncoder.encode(rol, "UTF-8")
            );

            // URL del endpoint
            URL url = new URL(BASE_URL + "?action=verificarUsuarioPorNombreYrol&" + params);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Leer la respuesta del servidor
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    String response = content.toString();

                    // Procesar la respuesta JSON, por ejemplo, '{"valido":true}'
                    if (response.contains("\"valido\":true")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                System.out.println("Error al verificar usuario. Código: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;  // Si hay algún error, devolvemos 'false'
    }
}