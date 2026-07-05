document.getElementById('form-usuario').addEventListener('submit', async (e) => {
  e.preventDefault();
  const dto = {
    nombreUsuario: document.getElementById('usuario-nombre').value.trim(),
    contrasena: document.getElementById('usuario-contrasena').value,
    rol: document.getElementById('usuario-rol').value
  };

  try {
    await api.usuarios.registrar(dto);
    mostrarMensaje('Usuario guardado correctamente.');
    e.target.reset();
    cargarUsuarios();
  } catch (err) {
    mostrarMensaje(err.message, 'error');
  }
});

async function cargarUsuarios() {
  try {
    const usuarios = await api.usuarios.listar();

    document.getElementById('tabla-usuarios').innerHTML = usuarios.length
      ? usuarios.map(u => `<tr><td>${u.idUsuario}</td><td>${u.nombreUsuario}</td><td>${u.rol}</td></tr>`).join('')
      : `<tr><td colspan="3">Aún no hay usuarios registrados.</td></tr>`;

    // Rellena los selects de vendedor/tecnico usados en Ventas y Ordenes
    const opciones = usuarios.map(u => `<option value="${u.nombreUsuario}">${u.nombreUsuario} (${u.rol})</option>`).join('');
    const selectVendedor = document.getElementById('venta-vendedor');
    const selectTecnico = document.getElementById('orden-tecnico');
    if (selectVendedor) selectVendedor.innerHTML = opciones || '<option value="">Sin usuarios registrados</option>';
    if (selectTecnico) selectTecnico.innerHTML = opciones || '<option value="">Sin usuarios registrados</option>';
  } catch (err) {
    mostrarMensaje('No se pudieron cargar los usuarios: ' + err.message, 'error');
  }
}
