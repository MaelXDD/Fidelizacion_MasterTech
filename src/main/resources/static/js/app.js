const TITULOS = {
  dashboard: 'Dashboard',
  clientes: 'Clientes',
  productos: 'Productos',
  ventas: 'Ventas',
  ordenes: 'Órdenes de Servicio Técnico',
  usuarios: 'Usuarios del sistema'
};

function mostrarSeccion(nombre) {
  document.querySelectorAll('.seccion').forEach(s => s.classList.add('oculto'));
  document.getElementById(`seccion-${nombre}`).classList.remove('oculto');

  document.querySelectorAll('.menu-item').forEach(b => b.classList.remove('activo'));
  document.querySelector(`.menu-item[data-seccion="${nombre}"]`).classList.add('activo');

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

document.addEventListener('DOMContentLoaded', () => {
  mostrarSeccion('dashboard');
});
