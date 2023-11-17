import {
    clearForms,
    setCreationValues,
    setSearchValues,
    showSearchResults,
    showCatalogCard
} from "./../modules_commons.js"

import {
    fetchRequest,
} from "./../requests.js"

import {
    handleErrorMessages,
    clearErrorMessages
} from "./../api_messages_handler.js"

const d = document

let librarian, results, error, resultsType, operation, table, catalogCard

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("librarians_section")) {
        sendLibrarianForm(undefined, e.target)
    }
})

const sendLibrarianForm = async (librarian, form) => {
    error = undefined
    clearErrorMessages()

    if (!form) form = setFormInputsValues(librarian)
    await runLibrarianProcess(form)

    if (error) {
        handleErrorMessages(error, form)
        clearForms()
    } else {
        if (operation === "search") {
            showSearchResults(table)
            setSearchValues(results, resultsType, table)
        } else if (operation === "create") {
            showCatalogCard(results, resultsType, catalogCard)
            setCreationValues(results.librarian, resultsType, catalogCard)
        }
    }
}

const setFormInputsValues = librarian => {
    let searchLibrarianForm = d.querySelector(".form.librarian_form.search")
    searchLibrarianForm.librarian_email.value = librarian.email
    return searchLibrarianForm
}

const runLibrarianProcess = async form => {
    librarian = ""
    resultsType = "librarian"

    if (form.classList.contains("search")) {
        table = d.querySelector(".results_table.librarians_results_table")
        operation = "search"
        results = await getLibrarians(form)
    } else if (form.classList.contains("create")) {
        catalogCard = d.querySelector(".catalog_card.librarian_catalog_card")
        operation = "create"
        results = await createLibrarian(form)
    }
}

const getLibrarians = async form => {
    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/librarians/search-librarians",
            {
                librarianName: form.librarian_name.value,
                email: form.librarian_email.value,
            }
        )
    } catch (ex) {
        error = ex
    }
}

const createLibrarian = async form => {
    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/librarians/save-librarian",
            {
                idLibrary: 1,
                firstName: form.librarian_firstname.value,
                lastName: form.librarian_lastname.value,
                email: form.librarian_email.value,
                phoneNumber: form.librarian_phone_number.value,
                password: form.provisional_password.value,
                gender: form.librarian_gender.value,
                birthYear: form.birth_year.value,
                authorities: getRoleSet()
            }
        )
    } catch (ex) {
        error = ex
    }
}

const editLibrarian = async (idLibrarian, editedFields) => {
    try {
        return await fetchRequest(
            "PUT",
            `http://localhost:8080/librarians/edit-librarian/${idLibrarian}`,
            {
                originalLibrarianId: idLibrarian,
                firstName: editedFields[0],
                lastName: editedFields[1],
                email: editedFields[3],
                phoneNumber: editedFields[4],
                authorities: getRoleSet()
            }
        )
    } catch (ex) {
        throw ex
    }
}

const getRoleSet = () => {
    const roleSelect = d.querySelector(".librarian_role"),
        roleSet = []

    if (roleSelect.selectedIndex === 0) {
        roleSet.push(roleSelect.options[0])
        roleSet.push(roleSelect.options[1])
        roleSet.push(roleSelect.options[2])
    } else if (roleSelect.selectedIndex === 1) {
        roleSet.push(roleSelect.options[1])
        roleSet.push(roleSelect.options[2])
    } else if (roleSelect.selectedIndex === 2) {
        roleSet.push(roleSelect.options[2])
    }
}

const deleteLibrarian = async idLibrarian => {
    try {
        return await fetchRequest(
            "DELETE",
            `http://localhost:8080/librarians/delete-librarian/${idLibrarian}`
        )
    } catch (ex) {
        throw ex
    }
}

const generateLibrariansTableContent = () => {
    for (let i = 0; i < results.length; i++) {
        let result = results[i]

        let newRow = d.createElement("tr")
        newRow.classList.add("results_row")

        let librarianName = d.createElement("td")
        librarianName.textContent = `${result.firstName} ${result.lastName}`

        let librarianEmail = d.createElement("td")
        email.textContent = result.email

        let mainAuthority = d.createElement("td")
        mainAuthority.textContent = result.authorities[0]

        let selectLibrarian = d.createElement("td"),
            checkbox = d.createElement("input")
        checkbox.type = "checkbox"
        checkbox.name = `select_librarian`
        checkbox.classList.add('result_option')
        checkbox.value = i;
        selectLibrarian.appendChild(checkbox)

        newRow.appendChild(librarianName)
        newRow.appendChild(librarianEmail)
        newRow.appendChild(mainAuthority)
        newRow.appendChild(selectLibrarian)
        newRow.appendChild(d.createElement("td")) // Library
        table.querySelector(".results_table_body").appendChild(newRow)
    }
}

const generateLibrarianCatalogCard = async results => {
    let librarian = results,
        catalogCard = d.querySelector(".catalog_card.librarian_catalog_card")

    catalogCard.classList.remove("hidden")

    catalogCard.querySelector(".librarian_firstname").value = librarian.firstName
    catalogCard.querySelector(".librarian_lastname").value = librarian.lastName
    catalogCard.querySelector(".librarian_email").value = librarian.email
    catalogCard.querySelector(".librarian_phone_number").value = librarian.phoneNumber
    catalogCard.querySelector(".librarian_role").value = librarian.role[0]

    return catalogCard
}

const getLibrarian = () => {
    return librarian
}

const getLibrarianResults = () => {
    if (error) throw error
    return results
}

const setLibrarianValue = newLibrarianValue => {
    librarian = newLibrarianValue
}

export {
    sendLibrarianForm,
    deleteLibrarian,
    editLibrarian,
    generateLibrariansTableContent,
    generateLibrarianCatalogCard,
    getLibrarian,
    setLibrarianValue,
    getLibrarianResults
}
