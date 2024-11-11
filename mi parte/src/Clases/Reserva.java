    package Clases;

    import Clases.Habitaciones.Habitacion;
    import Clases.Usuario.Cliente;
    import Clases.Usuario.Usuario;
    import org.json.JSONArray;
    import org.json.JSONObject;
    import org.json.JSONException;

    import java.util.List;

    import Exepcion.HabitacionOcupadaException;
    import Exepcion.HabitacionNoEncontradaException;
    import Exepcion.UsuarioNoEncontradoException;
    import JSON.JSONUtiles;


    public class Reserva {

        private static int contadorReservas = 1;
        private int id;
        private listaUsuarios<Usuario> listaUsuarios;
        private listaHabitaciones<Habitacion> listaHabitaciones;
        private String fechaReserva;
        private String estadoReserva;
        private boolean estado;
        private List<Cliente> pasajeros;
        private Usuario usuario;
        private Habitacion habitacion;


        public Reserva(listaUsuarios<Usuario> listaUsuarios, listaHabitaciones<Habitacion> listaHabitaciones) {
            this.listaUsuarios = listaUsuarios;
            this.listaHabitaciones = listaHabitaciones;
            this.id = contadorReservas++;
        }

        public Reserva(Usuario usuarioEncontrado, Habitacion habitacionEncontrada) {
            this.usuario = usuarioEncontrado;
            this.habitacion = habitacionEncontrada;
        }

        public Reserva(int id, String usuarioNombre, String habitacionId, String fechaReserva, String estadoReserva) {
        }

        public void añadirReserva(String nombreUsuario, String habitacionId) throws UsuarioNoEncontradoException, HabitacionNoEncontradaException, HabitacionOcupadaException {
            Usuario usuarioEncontrado = null;
            Habitacion habitacionEncontrada = null;

            // Buscar el usuario en la lista por nombre
            for (Usuario usuario : listaUsuarios.getListaUsuariso()) {
                if (usuario.getNombre().equalsIgnoreCase(nombreUsuario)) {
                    usuarioEncontrado = usuario;
                    break;
                }
            }

            if (usuarioEncontrado == null) {
                throw new UsuarioNoEncontradoException("El usuario con nombre " + nombreUsuario + " no se encuentra en la lista.");
            }

            // Buscar la habitación en la lista
            for (Habitacion habitacion : listaHabitaciones.getListaHabitaciones()) {
                if (String.valueOf(habitacion.getNumero()).equals(habitacionId)) { // Convertir int a String para comparar
                    habitacionEncontrada = habitacion;
                    break;
                }
            }

            // Si no se encuentra la habitación
            if (habitacionEncontrada == null) {
                throw new HabitacionNoEncontradaException("La habitación con ID " + habitacionId + " no se encuentra en la lista.");
            }

            // Verificar si la habitación está disponible
            if (!habitacionEncontrada.isDisponible()) {
                throw new HabitacionOcupadaException("La habitación con ID " + habitacionId + " ya está ocupada.");
            }

            habitacionEncontrada.setEstado("ocupado");
            this.usuario = usuarioEncontrado;
            this.habitacion = habitacionEncontrada;
            this.fechaReserva = obtenerFechaActual();
            this.estadoReserva = "Confirmada";


            Reserva nuevaReserva = new Reserva(usuarioEncontrado, habitacionEncontrada);
            System.out.println("Reserva agregada con éxito:");
            System.out.println("Usuario: " + usuarioEncontrado.getNombre() + " - Habitacion: " + habitacionEncontrada.getNumero());
            System.out.println("Fecha de reserva: " + this.fechaReserva);
        }

        private String obtenerFechaActual() {

            return java.time.LocalDate.now().toString();
        }

        public listaHabitaciones<Habitacion> getListaHabitaciones() {
            return listaHabitaciones;
        }

        public void setListaHabitaciones(listaHabitaciones<Habitacion> listaHabitaciones) {
            this.listaHabitaciones = listaHabitaciones;
        }

        public String getFechaReserva() {
            return fechaReserva;
        }

        public void setFechaReserva(String fechaReserva) {
            this.fechaReserva = fechaReserva;
        }

        public String getEstadoReserva() {
            return estadoReserva;
        }

        public void setEstadoReserva(String estadoReserva) {
            this.estadoReserva = estadoReserva;
        }

        public static int getContadorReservas() {
            return contadorReservas;
        }

        public static void setContadorReservas(int contadorReservas) {
            Reserva.contadorReservas = contadorReservas;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isEstado() {
            return estado;
        }

        public void setEstado(boolean estado) {
            this.estado = estado;
        }

        public List<Cliente> getPasajeros() {
            return pasajeros;
        }

        public void setPasajeros(List<Cliente> pasajeros) {
            this.pasajeros = pasajeros;
        }

        public Clases.listaUsuarios<Usuario> getListaUsuarios() {
            return listaUsuarios;
        }

        public void setListaUsuarios(Clases.listaUsuarios<Usuario> listaUsuarios) {
            this.listaUsuarios = listaUsuarios;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public Habitacion getHabitacion() {
            return habitacion;
        }

        public void setHabitacion(Habitacion habitacion) {
            this.habitacion = habitacion;
        }

        @Override
        public String toString() {
            return "Reserva{" +
                    "fechaReserva='" + fechaReserva + '\'' +
                    ", estadoReserva='" + estadoReserva + '\'' +
                    '}';
        }
    }

