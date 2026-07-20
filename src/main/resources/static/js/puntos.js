async function cargarModuloPuntos() {
    try {
        const premios = await api.fidelizacion.listarPremios();
        const select = document.getElementById('canje-producto');

        select.innerHTML = '<option value="">-- Seleccione un producto de fidelización --</option>';

        if (premios.length === 0) {
            select.innerHTML += '<option value="" disabled>No hay premios con stock disponible</option>';
            return;
        }

        premios.forEach(p => {
            select.innerHTML += `
        <option value="${p.idPremio}">
          ${p.nombre} (${p.puntosRequeridos} pts) - Stock: ${p.stockActual} u.
        </option>`;
        });
    } catch (err) {
        mostrarMensaje('Error al cargar catálogo de premios: ' + err.message, 'error');
    }
}

document.getElementById('form-canje').addEventListener('submit', async (e) => {
    e.preventDefault();

    const dto = {
        dniRucCliente: document.getElementById('canje-dni').value.trim(),
        idPremio: parseInt(document.getElementById('canje-producto').value, 10)
    };

    if (!dto.idPremio) {
        mostrarMensaje('Por favor seleccione un artículo para el canje.', 'error');
        return;
    }

    const boton = e.target.querySelector('button[type="submit"]');
    boton.disabled = true;

    try {
        await api.fidelizacion.canjear(dto);
        mostrarMensaje('🎉 ¡Canje realizado con éxito en el módulo!');
        e.target.reset();
        await cargarModuloPuntos(); // Recarga stock y opciones
    } catch (err) {
        // Si salta la excepción de puntos insuficientes o stock de Spring Boot, se captura aquí de forma limpia
        mostrarMensaje(err.message, 'error');
    } finally {
        boton.disabled = false;
    }
});