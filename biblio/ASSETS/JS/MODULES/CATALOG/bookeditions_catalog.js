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
} from "../api_messages_handler.js"

const d = document

let bookedition, results, error, resultsType, operation, table, catalogCard

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("bookeditions_section")) {
        sendBookEditionForm(undefined, e.target)
    }
})

const sendBookEditionForm = async (bookwork, form) => {
    if (!form) form = setFormInputsValues(bookwork)

    clearErrorMessages()
    await runBookEditionProcess(form)

    if (error) {
        handleErrorMessages(error, form)
        error = null
        clearForms()
    } else {
        if (operation === "search") {
            showSearchResults(table)
            setSearchValues(results, resultsType, operation, table)
        } else if (operation === "edit") {
            showCatalogCard(results, resultsType, catalogCard)
            setCreationValues()
        }
    }
}

const setFormInputsValues = bookwork => {
    let searchBookEditionForm = d.querySelector(".form.bookedition_form.search"),
        author = bookwork.author

    searchBookEditionForm.title.value = bookwork.title
    searchBookEditionForm.author.value = `${author.firstName} ${author.lastName}`

    return searchBookEditionForm
}

const runBookEditionProcess = async form => {
    bookedition = ""
    resultsType = "bookedition"

    table = d.querySelector(".results_table.bookeditions_results_table")
    operation = "search"
    results = await getBookeditions(form)
}

const getBookeditions = async form => {
    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/general-catalog/search-bookeditions",
            {
                title: form.title.value,
                author: form.author.value,
                isbn: form.isbn.value,
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
                    title: bookwork.title,
                    author: {
                        firstName: bookwork.author.firstName,
                        lastName: bookwork.author.lastName
                    },
                    publicationYear: bookwork.publicationYear
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

const getBrowseBookWork = () => {
    return author
}

const setBookeditionValue = newBookEditionValue => {
    bookedition = newBookEditionValue
}

export {
    sendBookEditionForm,
    editBookEdition,
    deleteBookedition,
    generateBookEditionCatalogCard,
    generateBookEditionsTableContent,
    getBrowseBookWork,
    setBookeditionValue
}