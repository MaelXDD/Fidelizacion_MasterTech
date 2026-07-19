// Como el frontend es servido por el propio Spring Boot (resources/static),
// las rutas son relativas y no requieren configurar CORS.
const API_BASE = '/api';

/**
 * Wrapper de fetch que arma la URL, serializa el body en JSON
 * y traduce los errores del backend (GlobalExceptionHandler) a
 * excepciones legibles para el usuario.
 */
async function apiFetch(ruta, opciones = {}) {
  const respuesta = await fetch(API_BASE + ruta, {
    headers: { 'Content-Type': 'application/json' },
    ...opciones,
    body: opciones.body ? JSON.stringify(opciones.body) : undefined
  });

  if (!respuesta.ok) {
    let mensaje = `Error ${respuesta.status}`;
    try {
      const cuerpo = await respuesta.json();
      mensaje = cuerpo.mensaje || mensaje;
    } catch (_) { /* respuesta sin cuerpo JSON */ }
    throw new Error(mensaje);
  }

  if (respuesta.status === 204) return null;
  return respuesta.json();
}

const api = {
  // // Seleccion de endpoints segun el rol del usuario
  auth: {
    login: (dto) => apiFetch('/auth/login', { method: 'POST', body: dto })
  },
  clientes: {
    listar: () => apiFetch('/clientes'),
    buscar: (dniRuc) => apiFetch(`/clientes/${encodeURIComponent(dniRuc)}`),
    registrar: (dto) => apiFetch('/clientes', { method: 'POST', body: dto }),
    ranking: () => apiFetch('/clientes/ranking')
  },
  productos: {
    listar: () => apiFetch('/productos'),
    registrar: (dto) => apiFetch('/productos', { method: 'POST', body: dto }),
    alertasStockBajo: () => apiFetch('/productos/alertas-stock')
  },
  ventas: {
    listar: () => apiFetch('/ventas'),
    registrar: (dto) => apiFetch('/ventas', { method: 'POST', body: dto }),
    reporte: (params) => apiFetch('/ventas/reportes?' + new URLSearchParams(params))
  },
  ordenes: {
    listar: () => apiFetch('/ordenes-servicio'),
    registrar: (dto) => apiFetch('/ordenes-servicio', { method: 'POST', body: dto }),
    actualizarEstado: (id, dto) => apiFetch(`/ordenes-servicio/${id}/estado`, { method: 'PUT', body: dto })
  },
  usuarios: {
    listar: () => apiFetch('/usuarios'),
    registrar: (dto) => apiFetch('/usuarios', { method: 'POST', body: dto })
  }
};

/** Muestra un mensaje de exito/error en la parte superior del panel. */
function mostrarMensaje(texto, tipo = 'exito') {
  const el = document.getElementById('mensaje-global');
  el.textContent = texto;
  el.className = `mensaje-global ${tipo}`;
  setTimeout(() => el.classList.add('oculto'), 4000);
}

function formatearMoneda(valor) {
  return 'S/ ' + Number(valor).toFixed(2);
}

function formatearFecha(iso) {
  if (!iso) return '-';
  const fecha = new Date(iso);
  return fecha.toLocaleString('es-PE', { dateStyle: 'short', timeStyle: 'short' });
}
