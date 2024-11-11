package manejoJson;

import enums.EstadoHabitacion;
import exceptions.AtributoFaltanteException;
import models.Habitacion.*;
import models.Usuarios.Administrador;
import models.Usuarios.Cliente;
import models.Usuarios.Conserje;
import models.Usuarios.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//Clase para la manipulacion de los Json de habitaciones, clientes y reservas
public class GestionJSON {

    // --------------------------------- MAPEO HABITACIONES-----------------------------------------------------//
    // --------------------------------- MAPEO HABITACIONES-----------------------------------------------------//
    // --------------------------------- MAPEO HABITACIONES-----------------------------------------------------//

    //Metodo que guarda en una lista las habitaciones que se encuentran en un archivo json
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

    //Metodo que carga un objeto Json con los datos de una habitacion
    public static JSONObject convertirHabitacionAJson(Habitacion habitacion) throws JSONException {
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

    //Metodo que actualiza el Json de las habitaciones con la nueva información
    public static void guardarHabitacionesJson(List<Habitacion> habitaciones, String filePath) throws JSONException, IOException {
        //Creamos un JSONArray que contendrá todas las habitaciones
        JSONArray habitacionesArray = new JSONArray();

        //Convertimos cada habitacion en un JSONObject y lo agregamos al JSONArray
        for (Habitacion habitacion : habitaciones) {
            JSONObject jsonHabitacion = convertirHabitacionAJson(habitacion);
            habitacionesArray.put(jsonHabitacion);
        }

        //Llamamos a la función grabar para escribir el JSONArray en el archivo JSON especificado
        JSONUtiles.grabar(habitacionesArray, filePath);
    }

    // --------------------------------- FIN HABITACIONES-----------------------------------------------------//
    // --------------------------------- FIN HABITACIONES-----------------------------------------------------//
    // --------------------------------- FIN HABITACIONES-----------------------------------------------------//


    // --------------------------------- MAPEO USUARIOS -----------------------------------------------------//
    // --------------------------------- MAPEO USUARIOS -----------------------------------------------------//
    // --------------------------------- MAPEO USUARIOS-----------------------------------------------------//

    // Metodo que mapea el JSON de usuarios y devuelve una lista de usuarios
    public static List<Usuario> mapeoUsuariosJson(String archivoJson) {
        List<Usuario> usuarios = new ArrayList<>();

        try {

            // Convertir el contenido del archivo a un JSONArray
            JSONArray jsonArrayUsuarios = new JSONArray(JSONUtiles.leer(archivoJson));

            // Iterar sobre el JSONArray y mapear los objetos a usuarios
            for (int i = 0; i < jsonArrayUsuarios.length(); i++) {
                JSONObject jsonUsuario = jsonArrayUsuarios.getJSONObject(i);
                Usuario usuario = null;
                try {
                    usuario = convertirJsonAUsuario(jsonUsuario, i);
                } catch (AtributoFaltanteException e) {
                    throw new RuntimeException(e);
                }
                if (usuario != null) {
                    usuarios.add(usuario);
                }
                else {
                    System.err.println("Error en el índice: " + i);
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return usuarios;
    }

    // Metodo para convertir un JSONObject a un Usuario (Cliente, Conserje o Administrador)
    private static Usuario convertirJsonAUsuario(JSONObject jsonUsuario,int indice) throws JSONException, AtributoFaltanteException {

        // Validamos el tipo de usuario
        if (!jsonUsuario.has("tipoUsuario")) {
            throw new AtributoFaltanteException("El atributo 'tipoUsuario' está ausente.");
        }
        // Validamos si los atributos comunes existen en el JSON
        if (!jsonUsuario.has("nombre") || !jsonUsuario.has("apellido") || !jsonUsuario.has("dni") ||
                !jsonUsuario.has("contrasenia") || !jsonUsuario.has("correoElectronico")) {
            throw new AtributoFaltanteException("Faltan atributos comunes en el Usuario.");
        }

        String tipoUsuario = jsonUsuario.getString("tipoUsuario");

        Usuario usuario = null;
        switch (tipoUsuario) {
            case "CLIENTE":
                usuario = convertirJsonACliente(jsonUsuario);
                break;
            case "CONSERJE":
                usuario = convertirJsonAConserje(jsonUsuario);
                break;
            case "ADMINISTRADOR":
                usuario = convertirJsonAAdministrador(jsonUsuario);
                break;
            default:
                throw new AtributoFaltanteException("Tipo de usuario desconocido: " + tipoUsuario);
        }

        return usuario;
    }

    // Metodo para convertir un JSONObject a un Cliente
    private static Cliente convertirJsonACliente(JSONObject jsonUsuario) throws JSONException, AtributoFaltanteException {

        // Verificar que los atributos específicos de Cliente estén presentes
        if (!jsonUsuario.has("direccion") || !jsonUsuario.has("telefono") ||
                !jsonUsuario.has("puntosFidelidad") || !jsonUsuario.has("fechaNacimiento")) {
            throw new AtributoFaltanteException("Faltan atributos específicos en Cliente.");
        }

        String nombre = jsonUsuario.getString("nombre");
        String apellido = jsonUsuario.getString("apellido");
        String dni = jsonUsuario.getString("dni");
        String password = jsonUsuario.getString("contrasenia");
        String correoElectronico = jsonUsuario.getString("correoElectronico");
        String direccion = jsonUsuario.getString("direccion");
        String telefono = jsonUsuario.getString("telefono");
        int puntosFidelidad = jsonUsuario.getInt("puntosFidelidad");

        // Obtener la fecha de nacimiento como String desde el JSON
        String fechaNacimientoString = jsonUsuario.getString("fechaNacimiento");

        Date fechaNacimiento = null;
        if (fechaNacimientoString != null && !fechaNacimientoString.equalsIgnoreCase("Fecha no disponible")) {
            try {
                // Intentar parsear la fecha usando el formato adecuado
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Formato de la fecha en el JSON
                fechaNacimiento = dateFormat.parse(fechaNacimientoString); // Convertir el String a Date
            } catch (ParseException e) {
                e.printStackTrace(); // Si ocurre un error al parsear la fecha
            }
        }

        // Crear y devolver un nuevo Cliente con la fecha de nacimiento convertida
        return new Cliente(nombre, apellido, dni, password, correoElectronico, direccion, telefono, new ArrayList<>(), puntosFidelidad, fechaNacimiento);
    }


    // Metodo para convertir un JSONObject a un Conserje
    private static Conserje convertirJsonAConserje(JSONObject jsonUsuario) throws JSONException, AtributoFaltanteException {
        // Verificar que los atributos específicos de Conserje estén presentes
        if (!jsonUsuario.has("fechaIngreso") || !jsonUsuario.has("telefono") || !jsonUsuario.has("estadoTrabajo")) {
            throw new AtributoFaltanteException("Faltan atributos específicos en Conserje.");
        }

        // Crear y devolver el objeto Conserje
        Conserje conserje = new Conserje();

        // Asignar los atributos comunes de Usuario
        conserje.setNombre(jsonUsuario.getString("nombre"));
        conserje.setApellido(jsonUsuario.getString("apellido"));
        conserje.setDni(jsonUsuario.getString("dni"));
        conserje.setContrasenia(jsonUsuario.getString("contrasenia"));
        conserje.setCorreoElectronico(jsonUsuario.getString("correoElectronico"));

        // Asignar los atributos específicos de Conserje
        conserje.setFechaIngreso(parseDate(jsonUsuario.getString("fechaIngreso")));
        conserje.setTelefono(jsonUsuario.getString("telefono"));
        conserje.setEstadoTrabajo(jsonUsuario.getString("estadoTrabajo"));

        return conserje;
    }

    // Metodo auxiliar parseDate para convertir fechas
    private static Date parseDate(String fechaString) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formato.parse(fechaString);
        } catch (ParseException e) {
            System.err.println("Error al parsear la fecha: " + e.getMessage());
            return null;
        }
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

    public static void guardarUsuariosJson(List<Usuario> usuarios, String filePath) throws JSONException, IOException {
        JSONArray usuariosArray = new JSONArray();

        for (Usuario usuario : usuarios) {
            JSONObject jsonUsuario = convertirUsuarioAJson(usuario);
            usuariosArray.put(jsonUsuario);
        }

        JSONUtiles.grabar(usuariosArray, filePath);
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
            // Formatear la fecha a "yyyy-MM-dd" antes de guardarla
            if (cliente.getFechaNacimiento() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String fechaNacimientoString = dateFormat.format(cliente.getFechaNacimiento());
                jsonUsuario.put("fechaNacimiento", fechaNacimientoString);
            } else {
                jsonUsuario.put("fechaNacimiento", "Fecha no disponible");
            }
        } else if (usuario instanceof Conserje) {
            Conserje conserje = (Conserje) usuario;
            jsonUsuario.put("estadoTrabajo", conserje.getEstadoTrabajo());
            jsonUsuario.put("telefono", conserje.getTelefono());
            // Formatear la fecha a "yyyy-MM-dd" antes de guardarla
            if (conserje.getFechaIngreso() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String fechaNacimientoString = dateFormat.format(conserje.getFechaIngreso());
                jsonUsuario.put("fechaIngreso", fechaNacimientoString);
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
