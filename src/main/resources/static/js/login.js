// ===== Logica de la pagina login.html =====

document.addEventListener('DOMContentLoaded', () => {
    // Si ya hay sesion activa (login previo), no tiene sentido mostrar
    // el formulario de nuevo: se va directo al panel.
    if (obtenerSesionGuardada()) {
        window.location.href = 'index.html';
    }
});


function mostrarErrorLogin(mensaje) {
    const el = document.getElementById('login-error');
    el.textContent = mensaje;
    el.classList.remove('oculto');
}

function ocultarErrorLogin() {
    document.getElementById('login-error').classList.add('oculto');
}

document.getElementById('form-login').addEventListener('submit', async (e) => {
    e.preventDefault();
    ocultarErrorLogin();

    const dto = {
        nombreUsuario: document.getElementById('login-usuario').value.trim(),
        contrasena: document.getElementById('login-contrasena').value
    };

    const boton = e.target.querySelector('button[type="submit"]');
    boton.disabled = true;

    try {
        const usuario = await api.auth.login(dto);
        guardarSesion({ idUsuario: usuario.idUsuario, nombreUsuario: usuario.nombreUsuario, rol: usuario.rol });
        window.location.href = 'index.html';
    } catch (err) {
        mostrarErrorLogin(err.message || 'Usuario o contraseña incorrectos.');
        boton.disabled = false;
    }
});
