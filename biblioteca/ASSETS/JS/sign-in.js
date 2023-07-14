const d = document;

let libraryName, libraryPhoneNumber, libraryEmail, libraryAdressName, libraryAdressNumber, libraryLocality, libraryProvince, libraryPostalCode,
    userFirstName, userLastName, userPhoneNumber, userEmail, userPassword;

d.addEventListener("click", e => {
    if (e.target.matches(".sub-option.continue-option > a")) {
        saveLibraryData();
    } else if (e.target.matches("#sign-in")) {
        saveUserData();
    }
})

const saveLibraryData = () => {
    libraryName = d.getElementById("library_name").value,
        libraryPhoneNumber = d.getElementById("library_phone_number").value,
        libraryEmail = d.getElementById("library_email").value,
        libraryAdressName = d.getElementById("address_name").value,
        libraryAdressNumber = d.getElementById("address_number").value,
        libraryLocality = d.getElementById("address_locality").value,
        libraryProvince = d.getElementById("address_province").value,
        libraryPostalCode = d.getElementById("address_postal_code").value;
}

const saveUserData = () => {
    firstName = d.getElementById("first_name").value,
        lastName = d.getElementById("last_name").value,
        userPhoneNumber = d.getElementById("phone_number").value,
        userEmail = d.getElementById("email").value,
        userPassword = d.getElementById("password").value;
}

const saveAccount = async () => {
    try {
        const reqBody = {
            libraryName,
            libraryPhoneNumber,
            libraryEmail,
            libraryAddressDTO: {
                libraryAdressName,
                libraryAdressNumber,
                libraryLocality,
                libraryProvince,
                libraryPostalCode,
            },
            libraryManagerDTO: {
                firstName,
                lastName,
                userPhoneNumber,
                userEmail,
                userPassword
            }
        };

        const options = {
            method: "POST",
            headers: {
                "Content-type": "application/json",
            },
            body: JSON.stringify(reqBody),
        };

        const res = await fetch("http://localhost:8080/accounts/create", options);

        if (!res.ok) throw { status: res.status, statusText: res.statusText };

    } catch (err) {

        const message = err.statusText || "An error occurred";
        d.getElementById("form").insertAdjacentHTML(
            "afterend",
            `<p><b>Error ${err.status}: ${message}</b></p>`

        )
    }
};