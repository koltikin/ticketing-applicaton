const newPasswordInput = document.getElementById('new_password');
const newPasswordToggleIcon = document.getElementById('new-password-toggle-icon');

newPasswordToggleIcon.addEventListener('click', () => {
    if (newPasswordInput.type === 'password') {
        newPasswordInput.type = 'text';
        newPasswordToggleIcon.classList.remove('fa-eye-slash');
        newPasswordToggleIcon.classList.add('fa-eye');
    } else {
        newPasswordInput.type = 'password';
        newPasswordToggleIcon.classList.remove('fa-eye');
        newPasswordToggleIcon.classList.add('fa-eye-slash');
    }
});

const rePasswordInput = document.getElementById('re-password');
const rePasswordToggleIcon = document.getElementById('re-password-toggle-icon');

rePasswordToggleIcon.addEventListener('click', () => {
    if (rePasswordInput.type === 'password') {
        rePasswordInput.type = 'text';
        rePasswordToggleIcon.classList.remove('fa-eye-slash');
        rePasswordToggleIcon.classList.add('fa-eye');
    } else {
        rePasswordInput.type = 'password';
        rePasswordToggleIcon.classList.remove('fa-eye');
        rePasswordToggleIcon.classList.add('fa-eye-slash');
    }
});