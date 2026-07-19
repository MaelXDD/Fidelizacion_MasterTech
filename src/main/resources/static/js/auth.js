// ===== Sesion y permisos por rol (modulo compartido) =====
// esto controla la interfaz que ve cada rol.

const SESION_STORAGE_KEY = 'mt_sesion';

const PERMISOS_POR_ROL = {
    ADMIN: ['dashboard', 'clientes', 'productos', 'ventas', 'ordenes', 'usuarios'],
    TECNICO: ['ordenes'],
    //VENDEDOR: ['ventas']
    VENTAS: ['ventas']
};

const ETIQUETAS_ROL = {
    ADMIN: 'Administrador',
    TECNICO: 'Técnico',
    // VENDEDOR: 'Vendedor'
    VENTAS: 'Vendedor'
};

function tienePermiso(rol, seccion) {
    return (PERMISOS_POR_ROL[rol] || []).includes(seccion);
}

function primeraSeccionPermitida(rol) {
    return (PERMISOS_POR_ROL[rol] || [])[0] || 'dashboard';
}

function obtenerSesionGuardada() {
    try {
        const bruto = localStorage.getItem(SESION_STORAGE_KEY);
        return bruto ? JSON.parse(bruto) : null;
    } catch (_) {
        return null;
    }
}

function guardarSesion(sesion) {
    localStorage.setItem(SESION_STORAGE_KEY, JSON.stringify(sesion));
}

function borrarSesion() {
    localStorage.removeItem(SESION_STORAGE_KEY);
}
