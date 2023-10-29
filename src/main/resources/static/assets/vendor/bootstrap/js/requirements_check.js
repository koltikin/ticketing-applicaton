
const passwordField = document.getElementById("new_password");

const passwordRequirements = document.getElementById("password-requirements");

if (passwordField.value) {
    checkPasswordRequirements();
}

passwordField.addEventListener("input", checkPasswordRequirements);


function checkPasswordRequirements() {

    const lengthRegex = /^.{8,}$/;
    const lowercaseRegex = /[a-z]/;
    const uppercaseRegex = /[A-Z]/;
    const numberRegex = /[0-9]/;
    const specialCharRegex = /[\W_]/;

    const password = passwordField.value;

    // Update the password requirements in the HTML
    const requirements = [
        // Requirements checking logic
        'Your password must contain:',
        lengthRegex.test(password) ? '<p class="met"> At least 8 characters</p>' : '<p class="not-met"> At least 8 characters</p>',
        "<p>At least 3 of the following:</p>",
        lowercaseRegex.test(password) ? '<p class="met-3"> Lower case letters (a-z)</p>' : '<p class="not-met-3"> Lower case letters (a-z)</p>',
        uppercaseRegex.test(password) ? '<p class="met-3"> Upper case letters (A-Z)</p>' : '<p class="not-met-3"> Upper case letters (A-Z)</p>',
        numberRegex.test(password) ? '<p class="met-3"> Numbers (0-9)</p>' : '<p class="not-met-3"> Numbers (0-9)',
        specialCharRegex.test(password) ? '<p class="met"> Special characters</p>' : '<p class="not-met-3"> Special characters</p>'
    ];

    passwordRequirements.innerHTML = requirements.join("\n");
}
