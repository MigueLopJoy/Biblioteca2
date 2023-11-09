import { loadContent } from "./display_pages.js";

import { fetchRequest } from "./MODULES/requests.js";

import {
    handleErrorMessages,
    clearErrorMessages
} from "./MODULES/api_messages_handler.js";

import { clearForms } from "./MODULES/modules_commons.js";

const d = document

d.addEventListener("DOMContentLoaded", e => {
    loadContent("./ASSETS/HTML/login.html", d.querySelector(".page.login_page .page_element_container"))
})

d.addEventListener("click", e => {
    if (e.target.matches(".page_element_container .create-account")) {
        loadContent("./ASSETS/HTML/register.html", d.querySelector(".page.login_page .page_element_container"))
    }
})

d.addEventListener("submit", async e => {

    e.preventDefault()
    clearErrorMessages()

    try {
        console.log(e.target)
        console.log(e.target.classList.contains(".form"))
        console.log(e.target.matches(".form .registration_form"))
        console.log(e.target === d.querySelector(".form .registration_form"))
        if (e.target.matches(".form.login_form")) {
            await loginUser()
        } else if (e.target.matches(".form.registration_form")) {
            console.log("AAA")
            await registerLibrary()
        }
    } catch (ex) {
        handleErrorMessages(ex, form)
        clearForms()
    }
})

const loginUser = async () => {
    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/auth/register",
            {
                userName: document.getElementById('userEmail').value,
                password: document.getElementById('userPassword').value
            }
        )
    } catch (ex) {
        throw ex
    }
}

const registerLibrary = async () => {
    try {
        console.log(
            {
                libraryName: d.getElementById('libraryName').value,
                libraryPhoneNumber: d.getElementById('libraryPhoneNumber').value,
                libraryEmail: d.getElementById('libraryEmail').value,
                city: d.getElementById('city').value,
                province: d.getElementById('province').value,
                postalCode: d.getElementById('postalCode').value,
                firstName: d.getElementById('firstName').value,
                lastName: d.getElementById('lastName').value,
                gender: d.getElementById('gender').value,
                birthYear: d.getElementById('birth_year').value,
                email: d.getElementById('email').value,
                password: d.getElementById('password').value,
                phoneNumber: d.getElementById('phoneNumber').value,
            }
        )
        return await fetchRequest(
            "POST",
            "http://localhost:8080/auth/register",
            {
                libraryName: document.getElementById('libraryName').value,
                libraryPhoneNumber: document.getElementById('libraryPhoneNumber').value,
                libraryEmail: document.getElementById('libraryEmail').value,
                city: document.getElementById('city').value,
                province: document.getElementById('province').value,
                postalCode: document.getElementById('postalCode').value,
                firstName: document.getElementById('firstName').value,
                lastName: document.getElementById('lastName').value,
                email: document.getElementById('email').value,
                password: document.getElementById('password').value,
                phoneNumber: document.getElementById('phoneNumber').value,
            }
        )
    } catch (ex) {
        throw ex
    }
}
