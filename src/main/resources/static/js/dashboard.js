async function cargarDashboard() {
  try {
    const [clientes, productos, ventas, alertas] = await Promise.all([
      api.clientes.listar(),
      api.productos.listar(),
      api.ventas.listar(),
      api.productos.alertasStockBajo()
    ]);

    document.getElementById('dash-clientes').textContent = clientes.length;
    document.getElementById('dash-productos').textContent = productos.length;
    document.getElementById('dash-ventas').textContent = ventas.length;
    document.getElementById('dash-alertas').textContent = alertas.length;

    const cuerpo = document.getElementById('tabla-alertas');
    cuerpo.innerHTML = alertas.length
      ? alertas.map(p => `
        <tr>
          <td>${p.codigo}</td>
          <td>${p.nombre}</td>
          <td>${p.categoria ?? '-'}</td>
          <td>${p.stockActual}</td>
          <td>${p.stockMinimo}</td>
        </tr>`).join('')
      : `<tr><td colspan="5">No hay productos con stock bajo.</td></tr>`;
  } catch (e) {
    mostrarMensaje('No se pudo cargar el dashboard: ' + e.message, 'error');
  }
}
