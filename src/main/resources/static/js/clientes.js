document.getElementById('form-cliente').addEventListener('submit', async (e) => {
  e.preventDefault();
  const dto = {
    dniRuc: document.getElementById('cliente-dniRuc').value.trim(),
    nombres: document.getElementById('cliente-nombres').value.trim(),
    telefono: document.getElementById('cliente-telefono').value.trim(),
    email: document.getElementById('cliente-email').value.trim()
  };

  try {
    await api.clientes.registrar(dto);
    mostrarMensaje('Cliente guardado correctamente.');
    e.target.reset();
    cargarClientes();
  } catch (err) {
    mostrarMensaje(err.message, 'error');
  }
});

async function cargarClientes() {
  try {
    const [clientes, ranking] = await Promise.all([
      api.clientes.listar(),
      api.clientes.ranking()
    ]);

    document.getElementById('tabla-clientes').innerHTML = clientes.length
      ? clientes.map(c => `
        <tr>
          <td>${c.dniRuc}</td>
          <td>${c.nombres}</td>
          <td>${c.telefono ?? '-'}</td>
          <td>${c.email ?? '-'}</td>
          <td>${c.puntosAcumulados}</td>
        </tr>`).join('')
      : `<tr><td colspan="5">Aún no hay clientes registrados.</td></tr>`;

    document.getElementById('tabla-ranking').innerHTML = ranking.length
      ? ranking.map((r, i) => `
        <tr>
          <td>${i + 1}</td>
          <td>${r.nombres}</td>
          <td>${r.dniRuc}</td>
          <td>${r.puntosAcumulados}</td>
          <td>${formatearMoneda(r.totalComprado)}</td>
        </tr>`).join('')
      : `<tr><td colspan="5">Sin datos todavía.</td></tr>`;
  } catch (err) {
    mostrarMensaje('No se pudieron cargar los clientes: ' + err.message, 'error');
  }
}
