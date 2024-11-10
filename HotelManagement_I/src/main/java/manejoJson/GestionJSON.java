package manejoJson;

import enums.EstadoHabitacion;
import models.Habitacion.*;
import models.Usuarios.Administrador;
import models.Usuarios.Cliente;
import models.Usuarios.Conserje;
import models.Usuarios.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//Se realiza el mapeo de los 3 Json del proyecto
public class GestionJSON {

    // --------------------------------- MAPEO HABITACIONES-----------------------------------------------------//
    // --------------------------------- MAPEO HABITACIONES-----------------------------------------------------//
    // --------------------------------- MAPEO HABITACIONES-----------------------------------------------------//
    public static List<Habitacion> mapeoHabitaciones(String archivoJson) {

        List<Habitacion> habitaciones = new ArrayList<>();

        try {
            JSONArray JHabitaciones = new JSONArray(JSONUtiles.leer(archivoJson));

            for (int i = 0; i < JHabitaciones.length(); i++) {

                JSONObject JHabitacion = JHabitaciones.getJSONObject(i);

                if (JHabitacion.getString("tipo").equals("Apartamento")) {
                    Apartamento a = new Apartamento();
                    a.setAmbientes(JHabitacion.getInt("ambientes"));
                    a.setCocina(JHabitacion.getBoolean("cocina"));
                    mapeoHabitacion(JHabitacion, a);
                    habitaciones.add(a);
                } else if (JHabitacion.getString("tipo").equals("Doble")) {
                    Habitacion d = new Doble();
                    mapeoHabitacion(JHabitacion, d);
                    habitaciones.add(d);
                } else if (JHabitacion.getString("tipo").equals("Individual")) {
                    Habitacion ind = new Individual();
                    mapeoHabitacion(JHabitacion, ind);
                    habitaciones.add(ind);
                } else if (JHabitacion.getString("tipo").equals("Presidencial")) {
                    Presidencial p = new Presidencial();
                    JSONArray JAdicionales = JHabitacion.getJSONArray("adicionales");
                    List<String> adicionales = new ArrayList<>();
                    for (int z = 0; z < JAdicionales.length(); z++){
                        adicionales.add(JAdicionales.getString(z));
                    }
                    p.setAdicionales(adicionales);
                    p.setDimension(JHabitacion.getDouble("dimension"));
                    mapeoHabitacion(JHabitacion, p);
                    habitaciones.add(p);
                } else if (JHabitacion.getString("tipo").equals("Suite")) {
                    Suite s = new Suite();
                    s.setBalcon(JHabitacion.getBoolean("balcon"));
                    s.setComedor(JHabitacion.getBoolean("comedor"));
                    mapeoHabitacion(JHabitacion, s);
                    habitaciones.add(s);
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return habitaciones;
    }

    public static void mapeoHabitacion(JSONObject JHabitacion, Habitacion habitacion) {

        try {

            habitacion.setTipo(JHabitacion.getString("tipo"));
            habitacion.setNumero(JHabitacion.getInt("numero"));
            habitacion.setCapacidad(JHabitacion.getInt("capacidad"));

            JSONArray JCamas = JHabitacion.getJSONArray("camas");
            List<String> camas = new ArrayList<>();
            for (int i = 0; i < JCamas.length(); i++) {
                camas.add(JCamas.getString(i));
            }
            habitacion.setCamas(camas);

            habitacion.setDisponible(JHabitacion.getBoolean("disponible"));
            habitacion.setEstado(EstadoHabitacion.valueOf(JHabitacion.getString("estado").toUpperCase()));
            habitacion.setDetalleEstado(JHabitacion.getString("detalleEstado"));

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //Metodo para poder guardar una habitacion pasada por parametro en el archivo Json
    public static JSONObject guardarHabitacionEnJson(Habitacion habitacion) throws JSONException {
        JSONObject jsonHabitacion = new JSONObject();
        jsonHabitacion.put("tipo", habitacion.getTipo());
        jsonHabitacion.put("numero", habitacion.getNumero());
        jsonHabitacion.put("capacidad", habitacion.getCapacidad());

        if (habitacion instanceof Apartamento) {
            Apartamento apartamento = (Apartamento) habitacion;
            jsonHabitacion.put("ambientes", apartamento.getAmbientes());
            jsonHabitacion.put("cocina", apartamento.isCocina());
        }
        else if (habitacion instanceof Presidencial) {
            Presidencial presidencial = (Presidencial) habitacion;
            jsonHabitacion.put("adicionales", presidencial.getAdicionales());
            jsonHabitacion.put("dimension", presidencial.getDimension());
        }
        else if (habitacion instanceof Suite) {
            Suite suite = (Suite) habitacion;
            jsonHabitacion.put("balcon", suite.isBalcon());
            jsonHabitacion.put("comedor", suite.isComedor());
        }

        jsonHabitacion.put("camas", habitacion.getCamas());
        jsonHabitacion.put("disponible", habitacion.isDisponible());
        jsonHabitacion.put("estado", habitacion.getEstado());
        jsonHabitacion.put("detalleEstado", habitacion.getDetalleEstado());

        return jsonHabitacion;
    }

    // --------------------------------- FIN HABITACIONES-----------------------------------------------------//
    // --------------------------------- FIN HABITACIONES-----------------------------------------------------//
    // --------------------------------- FIN HABITACIONES-----------------------------------------------------//


    // --------------------------------- MAPEO USUARIOS -----------------------------------------------------//
    // --------------------------------- MAPEO USUARIOS -----------------------------------------------------//
    // --------------------------------- MAPEO USUARIOS-----------------------------------------------------//

    // Metodo que mapea el JSON de usuarios y devuelve una lista de usuarios
    public static List<Usuario> mapeoUsuariosJson(String fileName) throws JSONException {
        List<Usuario> usuarios = new ArrayList<>();

        try {
            // Leer el archivo JSON
            FileReader fileReader = new FileReader(fileName);
            StringBuilder jsonContent = new StringBuilder();
            int ch;
            while ((ch = fileReader.read()) != -1) {
                jsonContent.append((char) ch);
            }
            fileReader.close();

            // Convertir el contenido del archivo a un JSONArray
            JSONArray jsonArrayUsuarios = new JSONArray(jsonContent.toString());

            // Iterar sobre el JSONArray y mapear los objetos a usuarios
            for (int i = 0; i < jsonArrayUsuarios.length(); i++) {
                JSONObject jsonUsuario = jsonArrayUsuarios.getJSONObject(i);
                Usuario usuario = convertirJsonAUsuario(jsonUsuario);
                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    // Metodo para convertir un JSONObject a un Usuario (Cliente, Conserje o Administrador)
    private static Usuario convertirJsonAUsuario(JSONObject jsonUsuario) throws JSONException {
        String tipoUsuario = jsonUsuario.getString("tipoUsuario");

        switch (tipoUsuario) {
            case "CLIENTE":
                return convertirJsonACliente(jsonUsuario);
            case "CONSERJE":
                return convertirJsonAConserje(jsonUsuario);
            case "ADMINISTRADOR":
                return convertirJsonAAdministrador(jsonUsuario);
            default:
                return null;
        }
    }

    // Metodo para convertir un JSONObject a un Cliente
    private static Cliente convertirJsonACliente(JSONObject jsonUsuario) throws JSONException {
        String nombre = jsonUsuario.getString("nombre");
        String apellido = jsonUsuario.getString("apellido");
        String dni = jsonUsuario.getString("dni");
        String password = jsonUsuario.getString("contrasenia");
        String correoElectronico = jsonUsuario.getString("correoElectronico");
        String direccion = jsonUsuario.getString("direccion");
        String telefono = jsonUsuario.getString("telefono");

        int puntosFidelidad = jsonUsuario.getInt("puntosFidelidad");

        // Crear y devolver un nuevo Cliente
        return new Cliente(nombre, apellido, dni, password, correoElectronico, direccion, telefono, new ArrayList<>(), puntosFidelidad, null);
    }

    // Metodo para convertir un JSONObject a un Conserje
    private static Conserje convertirJsonAConserje(JSONObject jsonUsuario) throws JSONException {
        String nombre = jsonUsuario.getString("nombre");
        String apellido = jsonUsuario.getString("apellido");
        String dni = jsonUsuario.getString("dni");
        String password = jsonUsuario.getString("contrasenia");
        String correoElectronico = jsonUsuario.getString("correoElectronico");
        String turno = jsonUsuario.getString("turno");
        String numeroEmpleado = jsonUsuario.getString("numeroEmpleado");
        String areaResponsable = jsonUsuario.getString("areaResponsable");
        String telefono = jsonUsuario.getString("telefono");
        String estadoTrabajo = jsonUsuario.getString("estadoTrabajo");

        // Crear y devolver un nuevo Conserje
        return new Conserje(nombre, apellido, dni, password, correoElectronico, turno, numeroEmpleado, null, areaResponsable, telefono, estadoTrabajo);
    }

    // Metodo para convertir un JSONObject a un Administrador
    private static Administrador convertirJsonAAdministrador(JSONObject jsonUsuario) throws JSONException {
        String nombre = jsonUsuario.getString("nombre");
        String apellido = jsonUsuario.getString("apellido");
        String dni = jsonUsuario.getString("dni");
        String password = jsonUsuario.getString("contrasenia");
        String correoElectronico = jsonUsuario.getString("correoElectronico");

        // Crear y devolver un nuevo Administrador
        return new Administrador(nombre, apellido, dni, password, correoElectronico);
    }

    // Metodo para convertir Usuario a JSONObject
    public static JSONObject convertirUsuarioAJson(Usuario usuario) throws JSONException {
        JSONObject jsonUsuario = new JSONObject();

        // Llenamos los campos comunes
        jsonUsuario.put("tipoUsuario", usuario.getTipoUsuario());
        jsonUsuario.put("nombre", usuario.getNombre());
        jsonUsuario.put("apellido", usuario.getApellido());
        jsonUsuario.put("dni", usuario.getDni());
        jsonUsuario.put("contrasenia", usuario.getContrasenia());
        jsonUsuario.put("correoElectronico", usuario.getCorreoElectronico());

        // Dependiendo del tipo de usuario, agregamos campos adicionales
        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            jsonUsuario.put("direccion", cliente.getDireccion());
            jsonUsuario.put("telefono", cliente.getTelefono());
            jsonUsuario.put("puntosFidelidad", cliente.getPuntosFidelidad());
            // Verificar si la fecha de nacimiento es nula antes de convertirla a String
            if (cliente.getFechaNacimiento() != null) {
                jsonUsuario.put("fechaNacimiento", cliente.getFechaNacimiento().toString());
            } else {
                jsonUsuario.put("fechaNacimiento", "Fecha no disponible");
            }
        } else if (usuario instanceof Conserje) {
            Conserje conserje = (Conserje) usuario;
            jsonUsuario.put("turno", conserje.getTurno());
            jsonUsuario.put("numeroEmpleado", conserje.getNumeroEmpleado());
            jsonUsuario.put("areaResponsable", conserje.getAreaResponsable());
            jsonUsuario.put("estadoTrabajo", conserje.getEstadoTrabajo());
            jsonUsuario.put("telefono", conserje.getTelefono());
            // Verificar si la fecha de ingreso es nula antes de convertirla a String
            if (conserje.getFechaIngreso() != null) {
                jsonUsuario.put("fechaIngreso", conserje.getFechaIngreso().toString());
            } else {
                jsonUsuario.put("fechaIngreso", "Fecha no disponible");
            }
        } else if (usuario instanceof Administrador) {
            // Agregar campos específicos del administrador si es necesario
        }

        return jsonUsuario;
    }

    // --------------------------------- FIN USUARIOS -----------------------------------------------------//
    // --------------------------------- FIN USUARIOS -----------------------------------------------------//
    // --------------------------------- FIN USUARIOS -----------------------------------------------------//

}
