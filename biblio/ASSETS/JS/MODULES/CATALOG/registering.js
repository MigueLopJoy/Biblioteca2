import { findCurrentPage } from "./catalog-commons.js"

import { toggleNextPageChanging } from "./catalog-commons.js"
import { clearFormsData } from "./catalog-commons.js"
import { clearPrintedReults } from "./catalog-commons.js"

import { fetchRequest } from "./catalog-commons.js"
import { joinParamsToURL } from "./catalog-commons.js"

import { handleErrorMessages } from "./catalog-commons.js"
import { clearErrorMessages } from "./catalog-commons.js"

import { showSearchResults } from "./catalog-commons.js"
import { enableModalActions } from "./catalog-commons.js"

const d = document,
    searchEditionForm = d.querySelector(".form.edition_form.search"),
    createBookcopyForm = d.querySelector(".form.bookcopy_form.create"),
    editionsResultsTable = d.querySelector(".results_table.editions_results_table"),
    bookcopiesResultsTable = d.querySelector(".results_table.newBookkcopy_results_table")

let bookedition, newBookcopy, results, error, table, resultsType, operation

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("registering_section")) {
        let currentPage = findCurrentPage()

        clearErrorMessages()

        if (currentPage.classList.contains("bookedition_page")) {
            await runBookEditionProcess(e.target)
        } else if (currentPage.classList.contains("bookcopy_page")) {
            await runBookCopyProcess(e.target)
        }

        if (error) {
            handleErrorMessages(e.target)
            error = null
            toggleNextPageChanging(resultsType)
            clearFormsData()
        } else {
            showSearchResults(table, operation)
            enableModalActions(operation)
        }
    }
})

const runBookEditionProcess = async form => {
    bookedition = ""
    table = editionsResultsTable
    resultsType = "bookedition"
    clearPrintedReults(resultsType)
    operation = "search"
    await getSearchBookeditionResults(form)
}

const runBookCopyProcess = async form => {
    newBookcopy = ""
    table = bookcopiesResultsTable
    resultsType = "bookcopies"
    clearPrintedReults(resultsType)
    operation = "create"
    await getCreateBookCopyResults(form)
}

const getSearchBookeditionResults = async form => {
    try {
        results = fetchRequest(
            "POST",
            "http://localhost:8080/general-catalog/search-bookeditions",
            {
                title: form.title,
                author: form.author,
                isbn: form.isbn.value,
                editor: form.editor_name.value,
                language: form.edition_language.value,
            }
        )
    } catch (error) {
        error = error
    }
}

const getCreateBookCopyResults = async form => {
    try {
        results = await fetchRequest(
            "POST",
            "http://localhost:8080/bookcopies/save-bookcopy",
            {
                registrationNumber: form.registration_number.value,
                signature: form.signature.value,
                bookedition: {
                    isbn: bookedition.isbn.value,
                    editor: bookedition.editor.value,
                    editionYear: bookedition.editionYear.value,
                    language: bookedition.language.value,
                    bookWork: {
                        title: bookedition.bookwork.title,
                        author: {
                            firstName: bookedition.bookwork.author.firstName,
                            lastName: bookedition.bookwork.author.lastName
                        },
                        publicationYear: bookedition.bookwork.publicationYear
                    }
                }
            }
        )
    } catch (error) {
        error = error
    }
}

const getEditNewCopyResults = async editedFields => {
    try {
        results = [await fetchRequest(
            "PUT",
            `http://localhost:8080/bookcopies/edit-bookcopy/${results[0].idBookCopy}`,
            {
                signature: editedFields[0],
                registrationNuber: editedFields[1],
                status: editedFields[2],
            }
        )]
        return results
    } catch (ex) {
        throw ex
    }
}

const generateBookeditionsTableContent = () => {

    if (!table.querySelector("th.select_column")) {
        let selectColumn = d.createElement("th")
        selectColumn.textContent = "Select book edition"
        selectColumn.classList.add("select_column")
        table.querySelector("thead tr").appendChild(selectColumn)
    }

    for (let i = 0; i < results.length; i++) {

        let result = results[i],
            bookwork = result.bookwork,
            author = bookwork.author

        let newRow = d.createElement("tr")

        let title = d.createElement("td")
        title.textContent = bookwork.title

        let bookAuthor = d.createElement("td")
        bookAuthor.textContent = `${author.firstName} ${author.lastName}`

        let isbn = d.createElement("td")
        isbn.textContent = result.isbn

        let editor = d.createElement("td")
        editor.textContent = result.editor

        let editionYear = d.createElement("td")
        editionYear.textContent = result.editionYear

        let language = d.createElement("td")
        language.textContent = result.language

        let selectBookedition, checkbox
        if (operation === "search") {
            selectBookedition = d.createElement("td")
            checkbox = d.createElement("input")
            checkbox.type = "checkbox"
            checkbox.name = `select_bookedition`
            checkbox.classList.add('result_option')
            checkbox.classList.add('bookedition_result_option')
            checkbox.value = i;
            selectAuthor.appendChild(checkbox)
        }

        newRow.appendChild(title);
        newRow.appendChild(bookAuthor);
        newRow.appendChild(isbn);
        newRow.appendChild(editor);
        newRow.appendChild(editionYear);
        newRow.appendChild(language);
        if (selectBookedition) {
            newRow.appendChild(selectBookedition)
        }

        table.querySelector(".results_table_body").appendChild(newRow);
    }
}

const generateNewBookcopyTableContent = () => {
    for (let i = 0; i < results.length; i++) {

        let result = results[i],
            bookedition = result.bookedition,
            bookwork = result.bookwork,
            author = result.author

        let newRow = d.createElement("tr")

        let title = d.createElement("td")
        title.textContent = bookwork.title

        let bookAuthor = d.createElement("td")
        bookAuthor.textContent = `${author.firstName} ${author.lastName}`

        let isbn = d.createElement("td")
        isbn.textContent = bookedition.isbn

        let editor = d.createElement("td")
        editor.textContent = bookedition.editor

        let registrationNuber = d.createElement("td")
        editionYear.textContent = result.registrationNuber

        let signature = d.createElement("td")
        language.textContent = result.signature

        newRow.appendChild(title);
        newRow.appendChild(bookAuthor);
        newRow.appendChild(isbn);
        newRow.appendChild(editor);
        newRow.appendChild(registrationNuber);
        newRow.appendChild(signature);

        table.querySelector(".results_table_body").appendChild(newRow);
    }
}

const prepareBookeditionEditionProcess = cells => {
    cells[0].innerHTML = `<input type="text" class="edition" value="${author.firstName}" >`
    cells[1].innerHTML = `<input type="text" class="edition"value="${author.lastName}" >`
}

const prepareNewBookCopyEditionProcess = cells => {
    cells[0].innerHTML = `<input type="text" class="edition" value="${bookwork.title}" >`
    cells[2].innerHTML = `<input type="number" class="edition" value="${bookwork.publicationYear}" >`
}


const getBookedition = () => {
    return bookedition
}

const getNewBookcopy = () => {
    return newBookcopy
}

const reasigneBookeditionValue = newBookeditionValue => {
    author = newAuthorValue
}

const reasigneNewBookcopyValue = newBookworkValue => {
    newBookcopy = newBookworkValue
}

export { getEditNewCopyResults }
export { prepareBookeditionEditionProcess }
export { prepareNewBookCopyEditionProcess }
export { generateBookeditionsTableContent }
export { generateNewBookcopyTableContent }
export { getBookedition }
export { getNewBookcopy }
export { reasigneBookeditionValue }
export { reasigneNewBookcopyValue }