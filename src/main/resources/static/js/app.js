const TITULOS = {
  dashboard: 'Dashboard',
  clientes: 'Clientes',
  productos: 'Productos',
  ventas: 'Ventas',
  ordenes: 'Órdenes de Servicio Técnico',
  usuarios: 'Usuarios del sistema'
};

function mostrarSeccion(nombre) {
  // si el rol de la sesion actual no tiene permiso sobre esta seccion, no se muestra.
  if (window.sesionActual && !tienePermiso(window.sesionActual.rol, nombre)) {
    return;
  }

  document.querySelectorAll('.seccion').forEach(s => s.classList.add('oculto'));
  document.getElementById(`seccion-${nombre}`).classList.remove('oculto');

  document.querySelectorAll('.menu-item').forEach(b => b.classList.remove('activo'));
  document.querySelector(`.menu-item[data-seccion="${nombre}"]`)?.classList.add('activo');

  document.getElementById('titulo-seccion').textContent = TITULOS[nombre];

  // Recarga los datos de la seccion cada vez que se visita
  if (nombre === 'dashboard') cargarDashboard();
  if (nombre === 'clientes') cargarClientes();
  if (nombre === 'productos') cargarProductos();
  if (nombre === 'ventas') cargarVentas();
  if (nombre === 'ordenes') cargarOrdenes();
  if (nombre === 'usuarios') cargarUsuarios();
}

document.querySelectorAll('.menu-item').forEach(boton => {
  boton.addEventListener('click', () => mostrarSeccion(boton.dataset.seccion));
});

/** Oculta del menu lateral las secciones que el rol de la sesion no puede usar. */
function aplicarPermisosEnMenu(rol) {
  document.querySelectorAll('.menu-item').forEach(boton => {
    boton.classList.toggle('oculto', !tienePermiso(rol, boton.dataset.seccion));
  });
}

document.getElementById('btn-logout').addEventListener('click', () => {
  borrarSesion();
  window.location.href = 'login.html';
});

document.addEventListener('DOMContentLoaded', () => {
  const sesion = obtenerSesionGuardada();
  if (!sesion) {
    window.location.href = 'login.html';
    return;
  }

  window.sesionActual = sesion;
  aplicarPermisosEnMenu(sesion.rol);

  document.getElementById('sesion-info').innerHTML = `
    <strong>${sesion.nombreUsuario}</strong>
    <span class="sesion-rol">${ETIQUETAS_ROL[sesion.rol] || sesion.rol}</span>
  `;

  mostrarSeccion(primeraSeccionPermitida(sesion.rol));
});
