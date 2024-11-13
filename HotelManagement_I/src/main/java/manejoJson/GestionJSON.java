package manejoJson;

import enums.EstadoHabitacion;
import exceptions.AtributoFaltanteException;
import models.Habitacion.*;
import models.Pasajero;
import models.Reserva;
import models.Usuarios.Administrador;
import models.Usuarios.Cliente;
import models.Usuarios.Conserje;
import models.Usuarios.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import services.GestionHabitaciones;
import services.GestionReservas;
import services.GestionUsuario;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
                    for (int z = 0; z < JAdicionales.length(); z++) {
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
        } else if (habitacion instanceof Presidencial) {
            Presidencial presidencial = (Presidencial) habitacion;
            jsonHabitacion.put("adicionales", presidencial.getAdicionales());
            jsonHabitacion.put("dimension", presidencial.getDimension());
        } else if (habitacion instanceof Suite) {
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

    // Metodo para mapear un Apartamento
    public static Apartamento mapeoApartamento(JSONObject JHabitacion) throws JSONException {
        Apartamento apartamento = new Apartamento();
        mapeoHabitacionComunes(JHabitacion, apartamento);
        apartamento.setAmbientes(JHabitacion.getInt("ambientes"));
        apartamento.setCocina(JHabitacion.getBoolean("cocina"));
        return apartamento;
    }

    // Metodo para mapear una habitación Doble
    public static Doble mapeoDoble(JSONObject JHabitacion) {
        Doble doble = new Doble();
        mapeoHabitacionComunes(JHabitacion, doble);
        return doble;
    }

    // Metodo para mapear una habitación Presidencial
    public static Presidencial mapeoPresidencial(JSONObject JHabitacion) throws JSONException {
        Presidencial presidencial = new Presidencial();
        mapeoHabitacionComunes(JHabitacion, presidencial);
        JSONArray JAdicionales = JHabitacion.getJSONArray("adicionales");
        List<String> adicionales = new ArrayList<>();
        for (int i = 0; i < JAdicionales.length(); i++) {
            adicionales.add(JAdicionales.getString(i));
        }
        presidencial.setAdicionales(adicionales);
        presidencial.setDimension(JHabitacion.getDouble("dimension"));
        return presidencial;
    }

    // Metodo para mapear una habitación Suite
    public static Suite mapeoSuite(JSONObject JHabitacion) throws JSONException {
        Suite suite = new Suite();
        mapeoHabitacionComunes(JHabitacion, suite);
        suite.setBalcon(JHabitacion.getBoolean("balcon"));
        suite.setComedor(JHabitacion.getBoolean("comedor"));
        return suite;
    }

    // Metodo para mapear las propiedades comunes de cualquier tipo de habitación
    public static void mapeoHabitacionComunes(JSONObject JHabitacion, Habitacion habitacion) {
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
                    usuario = convertirJsonAUsuario(jsonUsuario);
                } catch (AtributoFaltanteException e) {
                    throw new RuntimeException(e);
                }
                if (usuario != null) {
                    usuarios.add(usuario);
                } else {
                    System.err.println("Error en el índice: " + i);
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return usuarios;
    }

    // Metodo para convertir un JSONObject a un Usuario (Cliente, Conserje o Administrador)
    private static Usuario convertirJsonAUsuario(JSONObject jsonUsuario) throws JSONException, AtributoFaltanteException {

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

        LocalDate fechaNacimiento = null;
        if (fechaNacimientoString != null && !fechaNacimientoString.equalsIgnoreCase("Fecha no disponible")) {
            try {
                // Intentar parsear la fecha usando el formato adecuado
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Formato de la fecha en el JSON
                fechaNacimiento = LocalDate.parse(fechaNacimientoString, formatter); // Convertir el String a LocalDate
            } catch (DateTimeParseException e) {
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
        String fechaIngresoString = jsonUsuario.getString("fechaIngreso");
        if (fechaIngresoString != null && !fechaIngresoString.equalsIgnoreCase("Fecha no disponible")) {
            conserje.setFechaIngreso(parseDate(fechaIngresoString)); // Convertir fecha a LocalDate
        }

        conserje.setTelefono(jsonUsuario.getString("telefono"));
        conserje.setEstadoTrabajo(jsonUsuario.getString("estadoTrabajo"));

        return conserje;
    }


    // Metodo auxiliar para convertir un String en LocalDate
    private static LocalDate parseDate(String fechaString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(fechaString, formatter); // Convertir el String a LocalDate
        } catch (DateTimeParseException e) {
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String fechaNacimientoString = cliente.getFechaNacimiento().format(formatter);
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String fechaIngresoString = conserje.getFechaIngreso().format(formatter);
                jsonUsuario.put("fechaIngreso", fechaIngresoString);
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


    // --------------------------------- MAPEO RESERVAS -----------------------------------------------------//
    // --------------------------------- MAPEO RESERVAS -----------------------------------------------------//
    // --------------------------------- MAPEO RESERVAS -----------------------------------------------------//

    // Metodo que mapea las reservas desde un archivo JSON
    public static List<Reserva> mapeoReservasJson(String archivoJson) {
        List<Reserva> reservas = new ArrayList<>();

        try {
            // Convertir el contenido del archivo JSON en un JSONArray
            JSONArray jsonArrayReservas = new JSONArray(JSONUtiles.leer(archivoJson));

            // Obtener las instancias de GestionUsuario y GestionHabitaciones
            GestionUsuario gestionUsuario = GestionUsuario.getInstancia("ruta/a/tu/archivo_usuarios.json");
            GestionHabitaciones gestionHabitaciones = GestionHabitaciones.getInstancia("ruta/a/tu/archivo_habitaciones.json");

            // Iterar sobre el JSONArray y mapear los objetos a reservas
            for (int i = 0; i < jsonArrayReservas.length(); i++) {
                JSONObject jsonReserva = jsonArrayReservas.getJSONObject(i);

                // Extraemos la información de la reserva
                LocalDate fechaReserva = parseDate(jsonReserva.getString("fechaReserva"));
                LocalDate fechaEntrada = parseDate(jsonReserva.getString("fechaEntrada"));
                LocalDate fechaSalida = parseDate(jsonReserva.getString("fechaSalida"));
                String estadoReserva = jsonReserva.getString("estadoReserva");
                String comentario = jsonReserva.getString("comentario");
                int cantidadPersonas = jsonReserva.getInt("cantidadPersonas");

                // Mapeamos los pasajeros
                JSONArray jsonPasajeros = jsonReserva.getJSONArray("pasajeros");
                List<Pasajero> pasajeros = mapeoPasajeros(jsonPasajeros);

                // Mapeamos los servicios adicionales
                JSONArray jsonServiciosAdicionales = jsonReserva.getJSONArray("serviciosAdicionales");
                List<String> serviciosAdicionales = mapeoServiciosAdicionales(jsonServiciosAdicionales);

                // Obtener el DNI del cliente
                String dniCliente = jsonReserva.getString("dniCliente");

                // Buscar el usuario por DNI en la lista de usuarios
                Usuario usuario = gestionUsuario.buscarPorId(dniCliente);

                // Verificar que el usuario esté disponible (si es null, no seguimos con esta reserva)
                if (usuario == null) {
                    System.err.println("No se encontró el usuario con DNI: " + dniCliente);
                    continue;
                }

                // Obtener el número de la habitación desde el JSON
                int numeroHabitacion = jsonReserva.getInt("habitacionNumero");

                // Buscar la habitación correspondiente a ese número
                Habitacion habitacion = gestionHabitaciones.buscarPorIdInt(numeroHabitacion);

                // Verificar que la habitación esté disponible (si es null, no seguimos con esta reserva)
                if (habitacion == null) {
                    System.err.println("No se encontró la habitación con número: " + numeroHabitacion);
                    continue;
                }

                // Crear la reserva
                Reserva reserva = new Reserva(
                        fechaReserva,
                        fechaEntrada,
                        fechaSalida,
                        estadoReserva,
                        comentario,
                        cantidadPersonas,
                        pasajeros,
                        serviciosAdicionales,
                        usuario,
                        habitacion
                );

                reservas.add(reserva);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservas;
    }

    // Metodo que mapea los pasajeros desde un JSONArray
    private static List<Pasajero> mapeoPasajeros(JSONArray jsonPasajeros) throws JSONException {
        List<Pasajero> pasajeros = new ArrayList<>();
        for (int i = 0; i < jsonPasajeros.length(); i++) {
            JSONObject jsonPasajero = jsonPasajeros.getJSONObject(i);

            // Extraer los datos del pasajero
            String nombre = jsonPasajero.getString("nombre");
            String apellido = jsonPasajero.getString("apellido");
            String dni = jsonPasajero.getString("dni");

            // Crear el objeto Pasajero y agregarlo a la lista
            pasajeros.add(new Pasajero(nombre, apellido, dni));
        }
        return pasajeros;
    }

    // Metodo que mapea los servicios adicionales desde un JSONArray
    private static List<String> mapeoServiciosAdicionales(JSONArray jsonServiciosAdicionales) throws JSONException {
        List<String> serviciosAdicionales = new ArrayList<>();
        for (int i = 0; i < jsonServiciosAdicionales.length(); i++) {
            serviciosAdicionales.add(jsonServiciosAdicionales.getString(i));
        }
        return serviciosAdicionales;
    }


    // Metodo que busca una habitación por su número
    private static Habitacion obtenerHabitacionPorNumero(int numeroHabitacion, List<Habitacion> habitaciones) {
        for (Habitacion habitacion : habitaciones) {
            if (habitacion.getNumero() == numeroHabitacion) {
                return habitacion;
            }
        }
        return null; // Si no se encuentra, devuelve null o lanza una excepción si lo prefieres
    }

    // Metodo para convertir una lista de reservas a JSON y guardar en el archivo
    public static void guardarReservasJson(List<Reserva> reservas, String filePath) throws JSONException, IOException {
        JSONArray reservasArray = new JSONArray();

        for (Reserva reserva : reservas) {
            JSONObject jsonReserva = convertirReservaAJson(reserva);
            reservasArray.put(jsonReserva);
        }

        JSONUtiles.grabar(reservasArray, filePath);
    }

    public static JSONObject convertirReservaAJson(Reserva reserva) throws JSONException {
        JSONObject jsonReserva = new JSONObject();

        jsonReserva.put("id", reserva.getId());
        jsonReserva.put("fechaReserva", reserva.getFechaReserva().toString());
        jsonReserva.put("fechaEntrada", reserva.getFechaEntrada().toString());
        jsonReserva.put("fechaSalida", reserva.getFechaSalida().toString());
        jsonReserva.put("estadoReserva", reserva.getEstadoReserva());
        jsonReserva.put("comentario", reserva.getComentario());
        jsonReserva.put("cantidadPersonas", reserva.getCantidadPersonas());

        // Agregar los pasajeros
        JSONArray JPasajeros = new JSONArray();
        for (Pasajero pasajero : reserva.getPasajeros()) {
            JSONObject jsonPasajero = new JSONObject();
            jsonPasajero.put("nombre", pasajero.getNombre());
            jsonPasajero.put("apellido", pasajero.getApellido());
            jsonPasajero.put("dni", pasajero.getDni());
            JPasajeros.put(jsonPasajero);
        }
        jsonReserva.put("pasajeros", JPasajeros);

        // Agregar los servicios adicionales
        JSONArray JServiciosAdicionales = new JSONArray(reserva.getServiciosAdicionales());
        jsonReserva.put("serviciosAdicionales", JServiciosAdicionales);

        // Agregar los detalles completos del usuario
        JSONObject jsonUsuario = new JSONObject();
        jsonUsuario.put("nombre", reserva.getUsuario().getNombre());
        jsonUsuario.put("apellido", reserva.getUsuario().getApellido());
        jsonUsuario.put("dni", reserva.getUsuario().getDni());
        jsonUsuario.put("correoElectronico", reserva.getUsuario().getCorreoElectronico());
        jsonUsuario.put("contrasenia", reserva.getUsuario().getContrasenia());
        jsonUsuario.put("tipoUsuario", reserva.getUsuario().getTipoUsuario().toString());
        jsonUsuario.put("habilitacion", reserva.getUsuario().getHabilitacion());

        // En caso de ser Cliente, agregar atributos específicos
        if (reserva.getUsuario() instanceof Cliente) {
            Cliente cliente = (Cliente) reserva.getUsuario();
            jsonUsuario.put("direccion", cliente.getDireccion());
            jsonUsuario.put("telefono", cliente.getTelefono());
            jsonUsuario.put("puntosFidelidad", cliente.getPuntosFidelidad());
            jsonUsuario.put("fechaNacimiento", cliente.getFechaNacimiento().toString());
        }
        // En caso de ser Conserje, agregar atributos específicos
        else if (reserva.getUsuario() instanceof Conserje) {
            Conserje conserje = (Conserje) reserva.getUsuario();
            jsonUsuario.put("fechaIngreso", conserje.getFechaIngreso().toString());
            jsonUsuario.put("telefono", conserje.getTelefono());
            jsonUsuario.put("estadoTrabajo", conserje.getEstadoTrabajo());
        }
        jsonReserva.put("usuario", jsonUsuario);

        // Agregar los detalles completos de la habitación
        JSONObject jsonHabitacion = new JSONObject();
        jsonHabitacion.put("tipo", reserva.getHabitacion().getTipo());
        jsonHabitacion.put("numero", reserva.getHabitacion().getNumero());
        jsonHabitacion.put("capacidad", reserva.getHabitacion().getCapacidad());
        jsonHabitacion.put("camas", new JSONArray(reserva.getHabitacion().getCamas()));
        jsonHabitacion.put("disponible", reserva.getHabitacion().isDisponible());
        jsonHabitacion.put("estado", reserva.getHabitacion().getEstado().toString());
        jsonHabitacion.put("detalleEstado", reserva.getHabitacion().getDetalleEstado());

        // Agregar atributos específicos según el tipo de habitación
        if (reserva.getHabitacion() instanceof Apartamento) {
            Apartamento apartamento = (Apartamento) reserva.getHabitacion();
            jsonHabitacion.put("ambientes", apartamento.getAmbientes());
            jsonHabitacion.put("cocina", apartamento.isCocina());
        } else if (reserva.getHabitacion() instanceof Doble) {
            // No se necesitan atributos extra para Doble
        } else if (reserva.getHabitacion() instanceof Individual) {
            // No se necesitan atributos extra para Individual
        } else if (reserva.getHabitacion() instanceof Presidencial) {
            Presidencial presidencial = (Presidencial) reserva.getHabitacion();
            jsonHabitacion.put("adicionales", new JSONArray(presidencial.getAdicionales()));
            jsonHabitacion.put("dimension", presidencial.getDimension());
        } else if (reserva.getHabitacion() instanceof Suite) {
            Suite suite = (Suite) reserva.getHabitacion();
            jsonHabitacion.put("balcon", suite.isBalcon());
            jsonHabitacion.put("comedor", suite.isComedor());
        }
        jsonReserva.put("habitacion", jsonHabitacion);

        return jsonReserva;
    }
}