document.getElementById('form-producto').addEventListener('submit', async (e) => {
  e.preventDefault();
  const stockMinimo = document.getElementById('producto-stockMinimo').value;

  const dto = {
    codigo: document.getElementById('producto-codigo').value.trim(),
    nombre: document.getElementById('producto-nombre').value.trim(),
    categoria: document.getElementById('producto-categoria').value.trim(),
    precio: parseFloat(document.getElementById('producto-precio').value),
    stockActual: parseInt(document.getElementById('producto-stock').value, 10),
    stockMinimo: stockMinimo ? parseInt(stockMinimo, 10) : null
  };

  try {
    await api.productos.registrar(dto);
    mostrarMensaje('Producto guardado correctamente.');
    e.target.reset();
    cargarProductos();
  } catch (err) {
    mostrarMensaje(err.message, 'error');
  }
});

async function cargarProductos() {
  try {
    const productos = await api.productos.listar();

    document.getElementById('tabla-productos').innerHTML = productos.length
      ? productos.map(p => `
        <tr>
          <td>${p.codigo}</td>
          <td>${p.nombre}</td>
          <td>${p.categoria ?? '-'}</td>
          <td>${formatearMoneda(p.precio)}</td>
          <td>${p.stockActual}</td>
          <td>${p.stockMinimo}</td>
          <td>${p.stockBajo
            ? '<span class="etiqueta etiqueta-alerta">Stock bajo</span>'
            : '<span class="etiqueta etiqueta-ok">OK</span>'}</td>
        </tr>`).join('')
      : `<tr><td colspan="7">Aún no hay productos registrados.</td></tr>`;
  } catch (err) {
    mostrarMensaje('No se pudieron cargar los productos: ' + err.message, 'error');
  }
}
