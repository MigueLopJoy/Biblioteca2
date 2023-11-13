import { loadContent } from "./display_pages.js";

import { fetchRequest } from "./MODULES/requests.js";

import {
    handleErrorMessages,
    clearErrorMessages,
    displaySuccessMessage
} from "./MODULES/api_messages_handler.js";

import { clearForms } from "./MODULES/modules_commons.js";

const d = document,
    pageContainer = d.querySelector(".page.login_page .page_element_container")

d.addEventListener("DOMContentLoaded", e => {
    loadContent("./ASSETS/HTML/login.html", pageContainer)
})

d.addEventListener("click", e => {
    if (e.target.matches(".page_element_container .create-account")) {
        loadContent("./ASSETS/HTML/register.html", pageContainer)
    }
})

d.addEventListener("submit", async e => {

    e.preventDefault()
    clearErrorMessages()

    try {
        if (e.target.matches(".form.login_form")) {
            localStorage.clear()
            let tokens = await loginUser()
            localStorage.setItem("jwtToken", tokens.access_token)
            window.location.replace('http://localhost/biblio/ASSETS/HTML/program-container.html');
            console.log(localStorage.getItem("jwtToken"))
        } else if (e.target.matches(".form.registration_form")) {
            let newAccount = await registerLibrary()
            backToLoginPage(newAccount)
        }
    } catch (ex) {
        handleErrorMessages(ex, e.target)
        clearForms()
    }
})

const backToLoginPage = newAccount => {
    loadContent("./ASSETS/HTML/login.html", pageContainer)
    displaySuccessMessage(newAccount)
}

const loginUser = async () => {
    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/auth/authenticate",
            {
                email: document.getElementById('userEmail').value,
                password: document.getElementById('userPassword').value
            }
        )
    } catch (ex) {
        throw ex
    }
}

const registerLibrary = async () => {
    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/auth/register",
            {
                library: {
                    libraryName: d.getElementById('libraryName').value,
                    libraryPhoneNumber: d.getElementById('libraryPhoneNumber').value,
                    libraryEmail: d.getElementById('libraryEmail').value,
                    libraryAddress: d.getElementById('library_address').value,
                    city: d.getElementById('city').value,
                    province: d.getElementById('province').value,
                    postalCode: d.getElementById('postalCode').value,
                },
                librarian: {
                    firstName: d.getElementById('firstName').value,
                    lastName: d.getElementById('lastName').value,
                    gender: d.getElementById('gender').value,
                    birthYear: d.getElementById('birth_year').value,
                    email: d.getElementById('email').value,
                    password: d.getElementById('password').value,
                    phoneNumber: d.getElementById('phoneNumber').value,
                }
            }
        )
    } catch (ex) {
        throw ex
    }
}


