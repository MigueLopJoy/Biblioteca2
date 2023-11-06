function submitForm(event) {
    event.preventDefault();

    // Obtener los valores del formulario
    const libraryName = document.getElementById('libraryName').value;
    const libraryPhoneNumber = document.getElementById('libraryPhoneNumber').value;
    const libraryEmail = document.getElementById('libraryEmail').value;
    // Obtén los demás valores de LibraryDTOSaveLibrary aquí

    const city = document.getElementById('city').value;
    const province = document.getElementById('province').value;
    const postalCode = document.getElementById('postalCode').value;

    const firstName = document.getElementById('firstName').value;
    const lastName = document.getElementById('lastName').value;
    const email = document.getElementById('email').value;
    // Obtén los demás valores de USaveLibrarianDTO aquí

    const password = document.getElementById('password').value;
    const phoneNumber = document.getElementById('phoneNumber').value;
    const authorities = document.getElementById('authorities').value;

    // Construir el objeto de datos para enviar
    const requestData = {
        library: {
            libraryName: libraryName,
            libraryPhoneNumber: libraryPhoneNumber,
            libraryEmail: libraryEmail,
            // Agrega los demás campos de LibraryDTOSaveLibrary aquí
            city: city,
            province: province,
            postalCode: postalCode,
        },
        librarian: {
            firstName: firstName,
            lastName: lastName,
            email: email,
            // Agrega los demás campos de USaveLibrarianDTO aquí
            password: password,
            phoneNumber: phoneNumber,
            authorities: authorities,
        }
    };

    // Enviar el formulario al endpoint auth/register (puedes usar Fetch API o axios)
    fetch('http://tu-api-url/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData),
    })
        .then(response => response.json())
        .then(data => {
            console.log('Registro exitoso:', data);
            // Aquí puedes redirigir al usuario o realizar otras acciones después del registro
        })
        .catch(error => {
            console.error('Error al registrar:', error);
            // Manejar errores aquí
        });
}
