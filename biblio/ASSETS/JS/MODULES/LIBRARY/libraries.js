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

import { getBookWork } from "./bookworks_catalog.js"

const d = document

let library, results, error, resultsType, operation, table, catalogCard

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("bookeditions_section")) {
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
    let searchLibraryForm = d.querySelector(".form.library_form.search")

    searchLibraryForm.library_name.value = library.libraryName
    searchLibraryForm.city.value = library.city
    searchLibraryForm.province.value = library.province

    return searchLibraryForm
}

const runLibraryProcess = async form => {
    library = ""
    resultsType = "library"

    if (form.classList.contains("search")) {
        table = d.querySelector(".results_table.library_results_table")
        operation = "search"
        results = await getLibrary(form)
    }
}

const displayLibrariesSelectionTable = async () => {
    const libraryForm = d.querySelector(".form.library_form.search")

    let libraryName = d.querySelector(".form.search .edition_library").value

    libraryForm.editor_name.value = libraryName
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
        selectResultBtn.textContent = "Select Library"
        selectResultBtn.classList.remove("select_edition_library")
    } else {
        selectResultBtn.textContent = "Select Book Edition"
        selectResultBtn.classList.add("select_edition_library")
    }
}

const selectEditionLibrary = () => {
    changeSelectBtn()
    library = getLibraryResults()[findSelectedResult()]
    closeModal(d.querySelector(".form.create"))
}

const getLibrary = async form => {
    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/library/search-bookeditions",
            {
                title: form.bookwork_title.value,
                author: form.author_name.value,
                isbn: form.bookedition_isbn.value,
                editor: form.editor_name.value,
                language: form.edition_language.value,
            }
        )
    } catch (ex) {
        error = ex
    }
}

const createBookEdition = async form => {
    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/general-catalog/save-bookedition",
            {
                isbn: form.isbn.value,
                editor: form.editor_name.value,
                editionYear: form.edition_year.value,
                language: form.edition_language.value,
                bookWork: {
                    title: getBookWork() ? getBookWork().title : "",
                    author: {
                        firstName: getBookWork() ? getBookWork().author.firstName : "",
                        lastName: getBookWork() ? getBookWork().author.lastName : ""
                    },
                    publicationYear: getBookWork() ? getBookWork().publicationYear : ""
                }
            }
        )
    } catch (ex) {
        error = ex
    }
}

const getEditionCopies = async bookEditionId => {
    try {
        return await fetchRequest(
            "GET",
            `http://localhost:8080/bookcopies/get-bookwork-editions/${bookEditionId}`,
        )
    } catch (ex) {
        throw ex
    }
}

const deleteBookedition = async bookeditionId => {
    try {
        return await fetchRequest(
            "DELETE",
            `http://localhost:8080/general-catalog/delete-bookedition/${bookeditionId}`,
        )
    } catch (ex) {
        throw ex
    }
}

const editBookEdition = async (idBookEdition, editedFields) => {
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


const generateBookEditionsTableContent = () => {
    for (let i = 0; i < results.length; i++) {
        let result = results[i],
            bookWork = result.bookWork,
            author = bookWork.author

        let newRow = d.createElement("tr")
        newRow.classList.add("results_row")

        let title = d.createElement("td")
        title.textContent = bookWork.title

        let bookAuthor = d.createElement("td")
        bookAuthor.textContent = `${author.firstName} ${author.lastName}`

        let isbn = d.createElement("td")
        isbn.textContent = result.isbn

        let editor = d.createElement("td")
        editor.textContent = result.editor

        let editionYear = d.createElement("td")
        editionYear.textContent = result.editionYear

        let selectBookedition = d.createElement("td"),
            checkbox = d.createElement("input")
        checkbox.type = "checkbox"
        checkbox.name = `select_bookedition`
        checkbox.classList.add('result_option')
        checkbox.classList.add('bookedition_result_option')
        checkbox.value = i
        selectBookedition.appendChild(checkbox)

        newRow.appendChild(title)
        newRow.appendChild(bookAuthor)
        newRow.appendChild(isbn)
        newRow.appendChild(editor)
        newRow.appendChild(editionYear)
        newRow.appendChild(selectBookedition)

        table.querySelector(".results_table_body").appendChild(newRow);
    }
}

const generateBookEditionCatalogCard = async results => {
    let bookEdition = results,
        bookwork = bookEdition.bookWork

    let author = bookwork.author,
        authorName = `${author.firstName} ${author.lastName}`,
        editionCopiesMessage, bookEditionCopies,
        catalogCard = d.querySelector(".catalog_card.bookedition_catalog_card")

    catalogCard.classList.remove("hidden")

    catalogCard.querySelector(".bookwork_title").value = bookwork.title
    catalogCard.querySelector(".bookwork_author").value = authorName
    catalogCard.querySelector(".bookedition_isbn").value = bookEdition.isbn
    catalogCard.querySelector(".bookedition_editor").value = bookEdition.editor
    catalogCard.querySelector(".bookedition_edition_year").value = bookEdition.editionYear
    catalogCard.querySelector(".bookedition_language").value = bookEdition.language

    try {
        bookEditionCopies = await getEditionCopies(bookEdition.idBookEdition)
        editionCopiesMessage = `Book Edition Copies: ${bookEditionCopies.length}`
    } catch (error) {
        editionCopiesMessage = error.message
    }
    catalogCard.querySelector(".bookedition_copies").value = editionCopiesMessage
}

const getLibraryResults = () => {
    if (error) throw error
    return results
}


const setBookeditionValue = newBookEditionValue => {
    bookedition = newBookEditionValue
}

export {
    sendBookEditionForm,
    displayBookEditionSelectionTable,
    displayLibrariesSelectionTable,
    selectEditionLibrary,
    editBookEdition,
    deleteBookedition,
    generateBookEditionCatalogCard,
    generateBookEditionsTableContent,
    getBrowseBookWork,
    setBookeditionValue
}