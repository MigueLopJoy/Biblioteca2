import { findCurrentPage } from "./../modules_commons.js"
import { fetchRequest } from "./../modules_commons.js"
import { handleErrorMessages } from "./../modules_commons.js"
import { clearErrorMessages } from "./../modules_commons.js"
import { clearFormsData } from "./../modules_commons.js"
import { enableModalActions } from "./../modules_commons.js"

import { toggleNextPageChanging } from "./catalog-commons.js"
import { clearPrintedReults } from "./catalog-commons.js"

import { showSearchResults } from "./catalog-commons.js"

const d = document,
    editionsResultsTable = d.querySelector(".results_table.bookeditions_results_table"),
    bookcopiesResultsTable = d.querySelector(".results_table.newBookcopy_results_table")

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
            handleErrorMessages(error, e.target)
            error = null
            toggleNextPageChanging(resultsType)
            clearFormsData()
        } else {
            showSearchResults(resultsType, operation, table)
            enableModalActions(results, resultsType, operation, table)
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
    resultsType = "newBookcopy"
    clearPrintedReults(resultsType)
    operation = "create"
    await getCreateBookCopyResults(form)
}

const getSearchBookeditionResults = async form => {
    console.log(
        {
            title: form.title.value,
            author: form.author.value,
            isbn: form.isbn.value,
            editor: form.editor_name.value,
            language: form.edition_language.value,
        }
    )

    try {
        results = await fetchRequest(
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
        console.log(results)
    } catch (ex) {
        error = ex
    }
}

const getCreateBookCopyResults = async form => {
    try {
        results = [await fetchRequest(
            "POST",
            "http://localhost:8080/bookcopies/save-bookcopy",
            {
                registrationNumber: form.registration_number.value,
                signature: form.signature.value,
                bookEdition: {
                    isbn: bookedition.isbn,
                    editor: bookedition.editor,
                    editionYear: bookedition.editionYear,
                    language: bookedition.language,
                    bookWork: {
                        title: bookedition.bookWork.title,
                        author: {
                            firstName: bookedition.bookWork.author.firstName,
                            lastName: bookedition.bookWork.author.lastName
                        },
                        publicationYear: bookedition.bookWork.publicationYear
                    }
                }
            }
        )]
    } catch (ex) {
        error = ex
    }
}

const getEditNewCopyResults = async editedFields => {
    try {
        results = [await fetchRequest(
            "PUT",
            `http://localhost:8080/bookcopies/edit-bookcopy/${results[0].idBookCopy}`,
            {
                originalBookCopyId: results[0].idBookCopy,
                registrationNumber: editedFields[0],
                signature: editedFields[1],
                status: newBookcopy.bookCopyStatus,
                borrowed: newBookcopy.borrowed
            }
        )]
        return results
    } catch (ex) {
        throw ex
    }
}

const deleteNewCopy = async bookcopyId => {
    try {
        await fetchRequest(
            "DELETE",
            `http://localhost:8080/bookcopies/delete-bookcopy/${bookcopyId}`,
        )
        results = ""
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
        checkbox.value = i;
        selectBookedition.appendChild(checkbox)

        newRow.appendChild(title);
        newRow.appendChild(bookAuthor);
        newRow.appendChild(isbn);
        newRow.appendChild(editor);
        newRow.appendChild(editionYear);
        newRow.appendChild(selectBookedition)

        table.querySelector(".results_table_body").appendChild(newRow);
    }
}

const generateNewBookcopyTableContent = () => {
    const tableBody = table.querySelector(".results_table_body")

    for (let i = 0; i < results.length; i++) {

        let result = results[i],
            bookedition = result.bookEdition,
            bookwork = bookedition.bookWork,
            author = bookwork.author

        let newRow = d.createElement("tr")
        newRow.classList.add("results_row")

        let title = d.createElement("td")
        title.textContent = bookwork.title

        let bookAuthor = d.createElement("td")
        bookAuthor.textContent = `${author.firstName} ${author.lastName}`

        let isbn = d.createElement("td")
        isbn.textContent = bookedition.isbn

        let editor = d.createElement("td")
        editor.textContent = bookedition.editor

        let registrationNuber = d.createElement("td")
        registrationNuber.textContent = result.registrationNumber

        let signature = d.createElement("td")
        signature.textContent = result.signature

        newRow.appendChild(title);
        newRow.appendChild(bookAuthor);
        newRow.appendChild(isbn);
        newRow.appendChild(editor);
        newRow.appendChild(registrationNuber);
        newRow.appendChild(signature);

        table.querySelector(".results_table_body").appendChild(newRow);


        if (tableBody.firstChild) {
            tableBody.insertBefore(newRow, tableBody.firstChild)

            let errorMessageTd = tableBody.querySelector(".error_message_row > td")
            errorMessageTd.setAttribute("colspan", 6)
        } else {
            tableBody.appendChild(newRow)
        }
    }
}

const prepareNewBookcopyEditionProcess = cells => {
    cells[4].innerHTML = `<input type="text" class="edition" value="${newBookcopy.registrationNumber}" >`
    cells[5].innerHTML = `<input type="text" class="edition"value="${newBookcopy.signature}" >`
}


const getBookedition = () => {
    return bookedition
}

const getNewBookcopy = () => {
    return newBookcopy
}

const reasigneBookeditionValue = newBookeditionValue => {
    bookedition = newBookeditionValue
}

const reasigneNewBookcopyValue = newBookworkValue => {
    newBookcopy = newBookworkValue
}

export { getEditNewCopyResults }
export { deleteNewCopy }
export { prepareNewBookcopyEditionProcess }
export { generateBookeditionsTableContent }
export { generateNewBookcopyTableContent }
export { getBookedition }
export { getNewBookcopy }
export { reasigneBookeditionValue }
export { reasigneNewBookcopyValue }