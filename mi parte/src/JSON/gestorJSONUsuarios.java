package JSON;

import Clases.*;
import Clases.Usuario.Usuario;
import org.json.JSONArray;
import org.json.JSONObject;

import Clases.Usuario.Cliente;
import Clases.Usuario.Administrador;
import Clases.Usuario.Conserje;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class gestorJSONUsuarios {

        public static listaUsuarios<Usuario> cargaUsuarios() {
        listaUsuarios<Usuario> listaUsuarios = new listaUsuarios<>();

        try {
            JSONArray jsonUsuarios = new JSONArray(JSONUtiles.leer("usuarios.json"));
            listaUsuarios.setListaUsuariso(listaUsuarios(jsonUsuarios));
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el archivo JSON", e);
        }
        return listaUsuarios;
    }

        public static List<Usuario> listaUsuarios(JSONArray jsonArray) {
        List<Usuario> usuarios = new ArrayList<>();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String tipoUsuario = jsonObject.getString("tipoUsuario");

                switch (tipoUsuario) {
                    case "CLIENTE":
                        Cliente cliente = new Cliente();
                        // Convertir la fecha de nacimiento a Date
                        String fechaNacimientoStr = jsonObject.optString("fechaNacimiento", "Fecha no disponible");
                        if (!fechaNacimientoStr.equals("Fecha no disponible")) {
                            cliente.setFechaNacimiento(formatoFecha.parse(fechaNacimientoStr));
                        }
                        cliente.setTelefono(jsonObject.getString("telefono"));
                        cliente.setDireccion(jsonObject.getString("direccion"));
                        cliente.setPuntosFidelidad(jsonObject.getInt("puntosFidelidad"));
                        mapeoUsuarioBase(jsonObject, cliente);
                        usuarios.add(cliente);
                        break;

                    case "CONSERJE":
                        Conserje conserje = new Conserje();
                        // Convertir la fecha de ingreso a Date
                        String fechaIngresoStr = jsonObject.optString("fechaIngreso", "Fecha no disponible");
                        if (!fechaIngresoStr.equals("Fecha no disponible")) {
                            conserje.setFechaIngreso(formatoFecha.parse(fechaIngresoStr));  // Comvierte el formato de la fecha
                        }
                        conserje.setNumeroEmpleado(jsonObject.getString("numeroEmpleado"));
                        conserje.setTurno(jsonObject.getString("turno"));
                        conserje.setEstadoTrabajo(jsonObject.getString("estadoTrabajo"));
                        conserje.setAreaResponsable(jsonObject.getString("areaResponsable"));
                        mapeoUsuarioBase(jsonObject, conserje);
                        usuarios.add(conserje);
                        break;

                    case "ADMINISTRADOR":
                        Administrador administrador = new Administrador();
                        mapeoUsuarioBase(jsonObject, administrador);
                        usuarios.add(administrador);
                        break;

                    default:
                        throw new RuntimeException("Tipo de usuario desconocido: " + tipoUsuario);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error al procesar el usuario", e);
            }
        }
        return usuarios;
    }

        public static void mapeoUsuarioBase(JSONObject jsonObject, Usuario usuario) {
        try {
            usuario.setNombre(jsonObject.getString("nombre"));
            usuario.setApellido(jsonObject.getString("apellido"));
            usuario.setEmail(jsonObject.getString("correoElectronico"));
            usuario.setPassword(jsonObject.getString("contrasenia"));
            usuario.setDni(jsonObject.getString("dni"));
        } catch (Exception e) {
            throw new RuntimeException("Error al mapear el usuario", e);
        }
    }

}
