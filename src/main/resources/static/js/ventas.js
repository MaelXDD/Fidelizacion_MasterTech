let productosDisponibles = [];

function opcionesProductos() {
  return productosDisponibles
    .map(p => `<option value="${p.codigo}">${p.codigo} - ${p.nombre} (${formatearMoneda(p.precio)})</option>`)
    .join('');
}

function agregarLineaVenta() {
  const contenedor = document.getElementById('lineas-venta');
  const fila = document.createElement('div');
  fila.className = 'linea-venta';
  fila.innerHTML = `
    <select class="linea-producto">${opcionesProductos()}</select>
    <input type="number" class="linea-cantidad" min="1" value="1" placeholder="Cantidad">
    <button type="button" class="boton-quitar">Quitar</button>
  `;
  fila.querySelector('.boton-quitar').addEventListener('click', () => fila.remove());
  contenedor.appendChild(fila);
}

document.getElementById('btn-agregar-linea').addEventListener('click', agregarLineaVenta);

document.getElementById('form-venta').addEventListener('submit', async (e) => {
  e.preventDefault();

  const items = [...document.querySelectorAll('#lineas-venta .linea-venta')].map(fila => ({
    codigoProducto: fila.querySelector('.linea-producto').value,
    cantidad: parseInt(fila.querySelector('.linea-cantidad').value, 10)
  }));

  if (!items.length) {
    mostrarMensaje('Agrega al menos un producto a la venta.', 'error');
    return;
  }

  const dto = {
    dniRucCliente: document.getElementById('venta-dniRuc').value.trim(),
    nombreUsuarioVendedor: document.getElementById('venta-vendedor').value,
    items
  };

  try {
    const venta = await api.ventas.registrar(dto);
    mostrarComprobante(venta);
    mostrarMensaje('Venta registrada correctamente.');
    e.target.reset();
    document.getElementById('lineas-venta').innerHTML = '';
    agregarLineaVenta();
    cargarVentas();
  } catch (err) {
    mostrarMensaje(err.message, 'error');
  }
});

function mostrarComprobante(venta) {
  const el = document.getElementById('comprobante');
  el.classList.remove('oculto');
  el.innerHTML = `
    <h3>🧾 Comprobante de venta #${venta.idVenta}</h3>
    <p><strong>Cliente:</strong> ${venta.clienteNombres} (${venta.clienteDniRuc})</p>
    <ul>
      ${venta.detalles.map(d => `<li>${d.cantidad} x ${d.nombreProducto} — ${formatearMoneda(d.subtotalLinea)}</li>`).join('')}
    </ul>
    <p>Subtotal: ${formatearMoneda(venta.subtotal)} · Descuento: ${formatearMoneda(venta.descuento)} · <strong>Total: ${formatearMoneda(venta.total)}</strong></p>
    <p>Puntos acumulados del cliente: <strong>${venta.puntosAcumuladosCliente}</strong></p>
  `;
}

document.getElementById('form-reporte').addEventListener('submit', async (e) => {
  e.preventDefault();
  const params = { tipo: document.getElementById('reporte-tipo').value };
  const fecha = document.getElementById('reporte-fecha').value;
  const categoria = document.getElementById('reporte-categoria').value.trim();
  if (fecha) params.fecha = fecha;
  if (categoria) params.categoria = categoria;

  try {
    const reporte = await api.ventas.reporte(params);
    document.getElementById('resultado-reporte').innerHTML = `
      Periodo <strong>${reporte.periodo}</strong>${reporte.categoria ? ` · Categoría <strong>${reporte.categoria}</strong>` : ''}:
      <strong>${reporte.cantidadVentas}</strong> venta(s) por un total de <strong>${formatearMoneda(reporte.totalVendido)}</strong>.
    `;
  } catch (err) {
    mostrarMensaje(err.message, 'error');
  }
});

async function cargarVentas() {
  try {
    await cargarUsuarios();
    productosDisponibles = await api.productos.listar();

    if (!document.querySelector('#lineas-venta .linea-venta')) {
      agregarLineaVenta();
    } else {
      document.querySelectorAll('#lineas-venta .linea-producto').forEach(sel => sel.innerHTML = opcionesProductos());
    }

    const ventas = await api.ventas.listar();
    document.getElementById('tabla-ventas').innerHTML = ventas.length
      ? ventas.slice().reverse().map(v => `
        <tr>
          <td>${v.idVenta}</td>
          <td>${formatearFecha(v.fecha)}</td>
          <td>${v.clienteNombres}</td>
          <td>${formatearMoneda(v.subtotal)}</td>
          <td>${formatearMoneda(v.descuento)}</td>
          <td>${formatearMoneda(v.total)}</td>
        </tr>`).join('')
      : `<tr><td colspan="6">Aún no hay ventas registradas.</td></tr>`;
  } catch (err) {
    mostrarMensaje('No se pudo cargar el módulo de ventas: ' + err.message, 'error');
  }
}
