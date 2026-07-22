const selectCategoria = document.getElementById('producto-categoria-select');
const inputCategoriaNueva = document.getElementById('producto-categoria-nueva');

selectCategoria.addEventListener('change', () => {
  if (selectCategoria.value === '__nueva__') {
    selectCategoria.classList.add('oculto');
    inputCategoriaNueva.classList.remove('oculto');
    inputCategoriaNueva.required = true;
    inputCategoriaNueva.focus();
  }
});

inputCategoriaNueva.addEventListener('blur', () => {
  if (!inputCategoriaNueva.value.trim()) {
    volverAlSelect();
  }
});

function volverAlSelect() {
  inputCategoriaNueva.classList.add('oculto');
  inputCategoriaNueva.required = false;
  inputCategoriaNueva.value = '';
  selectCategoria.classList.remove('oculto');
  selectCategoria.value = '';
}

function actualizarOpcionesCategoria(productos, categoriaSeleccionar) {
  const categorias = [...new Set(
      productos.map(p => p.categoria).filter(c => c && c.trim())
  )].sort((a, b) => a.localeCompare(b, 'es'));

  const valorPrevio = categoriaSeleccionar ?? selectCategoria.value;

  selectCategoria.innerHTML = `
    <option value=""></option>
    ${categorias.map(c => `<option value="${c}">${c}</option>`).join('')}
    <option value="__nueva__">+ Agregar nueva categoría...</option>
  `;

  if (valorPrevio && valorPrevio !== '__nueva__' && categorias.includes(valorPrevio)) {
    selectCategoria.value = valorPrevio;
  }
}

document.getElementById('form-producto').addEventListener('submit', async (e) => {
  e.preventDefault();
  const stockMinimo = document.getElementById('producto-stockMinimo').value;

  const categoria = selectCategoria.value === '__nueva__'
      ? inputCategoriaNueva.value.trim()
      : selectCategoria.value.trim();

  if (selectCategoria.value === '__nueva__' && !categoria) {
    mostrarMensaje('Escribe el nombre de la nueva categoría.', 'error');
    return;
  }

  const dto = {
    codigo: document.getElementById('producto-codigo').value.trim(),
    nombre: document.getElementById('producto-nombre').value.trim(),
    categoria,
    precio: parseFloat(document.getElementById('producto-precio').value),
    stockActual: parseInt(document.getElementById('producto-stock').value, 10),
    stockMinimo: stockMinimo ? parseInt(stockMinimo, 10) : null
  };

  try {
    await api.productos.registrar(dto);
    mostrarMensaje('Producto guardado correctamente.');
    e.target.reset();
    volverAlSelect();
    cargarProductos(categoria);
  } catch (err) {
    mostrarMensaje(err.message, 'error');
  }
});

async function cargarProductos(categoriaSeleccionar) {
  try {
    const productos = await api.productos.listar();

    actualizarOpcionesCategoria(productos, categoriaSeleccionar);

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