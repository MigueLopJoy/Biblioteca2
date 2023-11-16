import {
    clearForms,
    setCreationValues,
    setSearchValues,
    showSearchResults,
    showCatalogCard,
    findSelectedResult,
    closeModal
} from "./../modules_commons.js"


import {
    fetchRequest,
} from "./../requests.js"

import {
    handleErrorMessages,
    clearErrorMessages
} from "../api_messages_handler.js"


const d = document

let library, results, error, resultsType, operation, table, catalogCard

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("libraries_section")) {
        sendLibraryForm(undefined, e.target)
    }
})

const sendLibraryForm = async (library, form) => {
    if (!form) form = setFormInputsValues(library)

    clearErrorMessages()
    await runLibraryProcess(form)

    if (error) {
        handleErrorMessages(error, form)
        error = null
        clearForms()
    } else {
        if (operation === "search") {
            showSearchResults(table)
            setSearchValues(results, resultsType, operation, table)
        } else if (operation === "create") {
            showCatalogCard(results, resultsType, catalogCard)
            setCreationValues(results, resultsType, catalogCard)
        }
    }
}

const setFormInputsValues = library => {
    let searchLibraryForm = d.querySelector(".form.library_form.search"),
        libraryName = library.libraryName,
        city = library.city,
        province = library.province

    searchLibraryForm.library_name.value = libraryName ? libraryName : library
    searchLibraryForm.city.value = city ? city : ""
    searchLibraryForm.province.value = province ? province : ""

    return searchLibraryForm
}

const runLibraryProcess = async form => {
    library = ""
    resultsType = "library"

    if (form.classList.contains("search")) {
        table = d.querySelector(".results_table.libraries_results_table")
        operation = "search"
        results = await searchLibrary(form)
    }
}

const displayLibrariesSelectionTable = async () => {
    const libraryForm = d.querySelector(".form.library_form.search")

    let libraryName = d.querySelector(".bookedition_form.search .edition_library").value

    libraryForm.library_name.value = libraryName
    libraryForm.province.value = ""
    libraryForm.city.value = ""

    await sendLibraryForm(libraryName, libraryForm)
    try {
        getLibraryResults()
        changeSelectBtn()
    } catch (ex) {
        error = ex
        handleErrorMessages(error, d.querySelector(".form.create"))
    }
}

const changeSelectBtn = () => {
    const selectResultBtn = d.querySelector(".select_results_btn")

    if (selectResultBtn.classList.contains("select_edition_library")) {
        selectResultBtn.textContent = "Select Book Edition"
        selectResultBtn.classList.remove("select_edition_library")
    } else {
        selectResultBtn.textContent = "Select Library"
        selectResultBtn.classList.add("select_edition_library")
    }
}

const selectEditionLibrary = () => {
    changeSelectBtn()
    library = getLibraryResults()[findSelectedResult()]
    closeModal(d.querySelector(".form.create"))
    manageInputValues()
}

const manageInputValues = () => {
    const libraryNameInput = d.querySelector(".form.search .edition_library")

    if (library) {
        libraryNameInput.value = library.libraryName
    } else libraryNameInput.value = ""
}

const searchLibrary = async form => {
    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/library/search-library",
            {
                libraryName: form.library_name.value,
                city: form.city.value,
                province: form.province.value
            }
        )
    } catch (ex) {
        error = ex
    }
}

const editLibrary = async (idBookEdition, editedFields) => {
    try {
        return await fetchRequest(
            "PUT",
            `http://localhost:8080/general-catalog/edit-bookedition/${idBookEdition}`,
            {
                originalBookEditionId: idBookEdition,
                isbn: editedFields[0],
                editor: editedFields[1],
                editionYear: editedFields[2],
                language: editedFields[3],
            }
        )
    } catch (ex) {
        throw ex
    }
}


const generateLibraryTableContent = () => {
    for (let i = 0; i < results.length; i++) {
        let library = results[i]

        let newRow = d.createElement("tr")
        newRow.classList.add("results_row")

        let libraryName = d.createElement("td")
        libraryName.textContent = library.libraryName

        let libraryAddress = d.createElement("td")
        libraryAddress.textContent = library.libraryAddress

        let city = d.createElement("td")
        city.textContent = library.city

        let province = d.createElement("td")
        province.textContent = library.province

        let selectBookedition = d.createElement("td"),
            checkbox = d.createElement("input")
        checkbox.type = "checkbox"
        checkbox.name = `select_bookedition`
        checkbox.classList.add('result_option')
        checkbox.classList.add('bookedition_result_option')
        checkbox.value = i
        selectBookedition.appendChild(checkbox)

        newRow.appendChild(libraryName)
        newRow.appendChild(libraryAddress)
        newRow.appendChild(city)
        newRow.appendChild(province)
        newRow.appendChild(selectBookedition)

        table.querySelector(".results_table_body").appendChild(newRow);
    }
}

const generateLibraryCatalogCard = async results => {
    let library = results,
        catalogCard = d.querySelector(".catalog_card.library_catalog_card")

    catalogCard.classList.remove("hidden")

    catalogCard.querySelector(".library_name").value = library.libraryName
    catalogCard.querySelector(".library_address").value = library.libraryAddress
    catalogCard.querySelector(".city").value = library.city
    catalogCard.querySelector(".province").value = library.province
    catalogCard.querySelector(".postal_code").value = library.postalCode
    catalogCard.querySelector(".library_phone_number").value = library.libraryPhoneNumber
    catalogCard.querySelector(".library_email").value = library.libraryEmail
}

const getLibraryResults = () => {
    if (error) throw error
    return results
}


const setLibraryValue = newLibraryValue => {
    library = newLibraryValue
}

const getLibrary = () => {
    return library
}

export {
    sendLibraryForm,
    displayLibrariesSelectionTable,
    selectEditionLibrary,
    editLibrary,
    generateLibraryTableContent,
    generateLibraryCatalogCard,
    setLibraryValue,
    getLibrary
}