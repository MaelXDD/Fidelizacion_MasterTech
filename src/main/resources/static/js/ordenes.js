const ESTADOS_ORDEN = ['En Diagnóstico', 'En Reparación', 'Reparado', 'Entregado', 'No Reparable'];

document.getElementById('form-orden').addEventListener('submit', async (e) => {
  e.preventDefault();
  const dto = {
    dniRucCliente: document.getElementById('orden-dniRuc').value.trim(),
    nombreUsuarioTecnico: document.getElementById('orden-tecnico').value,
    dispositivo: document.getElementById('orden-dispositivo').value.trim(),
    diagnostico: document.getElementById('orden-diagnostico').value.trim()
  };

  try {
    await api.ordenes.registrar(dto);
    mostrarMensaje('Orden de servicio registrada correctamente.');
    e.target.reset();
    cargarOrdenes();
  } catch (err) {
    mostrarMensaje(err.message, 'error');
  }
});

async function cambiarEstadoOrden(idOrden, nuevoEstado) {
  try {
    const costo = nuevoEstado === 'Entregado' ? prompt('Costo final del servicio (S/):', '0') : null;
    await api.ordenes.actualizarEstado(idOrden, {
      nuevoEstado,
      costo: costo !== null && costo !== '' ? parseFloat(costo) : null
    });
    mostrarMensaje(`Orden #${idOrden} actualizada a "${nuevoEstado}".`);
    cargarOrdenes();
  } catch (err) {
    mostrarMensaje(err.message, 'error');
  }
}

async function cargarOrdenes() {
  try {
    await cargarUsuarios();
    const ordenes = await api.ordenes.listar();

    document.getElementById('tabla-ordenes').innerHTML = ordenes.length
      ? ordenes.slice().reverse().map(o => `
        <tr>
          <td>${o.idOrden}</td>
          <td>${o.clienteNombres}</td>
          <td>${o.tecnico}</td>
          <td>${o.dispositivo}</td>
          <td>
            <select onchange="cambiarEstadoOrden(${o.idOrden}, this.value)">
              ${ESTADOS_ORDEN.map(estado => `<option value="${estado}" ${estado === o.estado ? 'selected' : ''}>${estado}</option>`).join('')}
            </select>
          </td>
          <td>${formatearMoneda(o.costo)}</td>
          <td>${formatearFecha(o.fechaIngreso)}</td>
        </tr>`).join('')
      : `<tr><td colspan="7">Aún no hay órdenes de servicio registradas.</td></tr>`;
  } catch (err) {
    mostrarMensaje('No se pudo cargar el módulo de órdenes: ' + err.message, 'error');
  }
}
