import { findCurrentPage } from "./../modules_commons.js"
import { fetchRequest } from "./../modules_commons.js"
import { handleErrorMessages } from "./../modules_commons.js"
import { clearErrorMessages } from "./../modules_commons.js"
import { clearFormsData } from "./../modules_commons.js"
import { enableModalActions } from "./../modules_commons.js"
import { enableCreateModalActions } from "./../modules_commons.js"

import { toggleNextPageChanging } from "./catalog-commons.js"
import { clearPrintedReults } from "./catalog-commons.js"
import { showSearchResults } from "./catalog-commons.js"
import { showCatalogCard } from "./catalog-commons.js"


const d = document,
    editionsResultsTable = d.querySelector(".results_table.bookeditions_results_table"),
    bookCopyCatalogCard = d.querySelector(".catalog_card.bookcopy_catalog_card")

let bookedition, newBookcopy, results, error, table, catalogCard, resultsType, operation

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
            if (operation === "search") {
                console.log(table)
                showSearchResults(resultsType, table)
                enableModalActions(results, resultsType, operation, table)
            } else if (operation === "create") {
                showCatalogCard(resultsType, catalogCard)
                enableCreateModalActions(results, resultsType, operation, catalogCard)
            }
        }
    }
})

const runBookEditionProcess = async form => {
    bookedition = ""
    resultsType = "bookedition"
    table = editionsResultsTable
    clearPrintedReults(resultsType)
    operation = "search"
    await getSearchBookeditionResults(form)
}

const runBookCopyProcess = async form => {
    newBookcopy = ""
    resultsType = "newBookcopy"
    catalogCard = bookCopyCatalogCard
    clearPrintedReults(resultsType)
    operation = "create"
    await getCreateBookCopyResults(form)
}

const getSearchBookeditionResults = async form => {
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
        console.log(            
            {
            originalBookCopyId: results[0].idBookCopy,
            signature: editedFields[0],
            registrationNumber: editedFields[1],
            status: newBookcopy.bookCopyStatus,
            borrowed: newBookcopy.borrowed
            }
        )
        results = [await fetchRequest(
            "PUT",
            `http://localhost:8080/bookcopies/edit-bookcopy/${results[0].idBookCopy}`,
            {
                originalBookCopyId: results[0].idBookCopy,
                registrationNumber: editedFields[1],
                signature: editedFields[0],
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

const generateBookCopyCatalogCard = async () => {
    let bookCopy = results[0],
        bookEdition = bookCopy.bookEdition, 
        bookWork = bookEdition.bookWork,
        author = bookWork.author,
        authorName = `${author.firstName} ${author.lastName}`

    bookCopyCatalogCard.classList.remove("hidden")

    bookCopyCatalogCard.querySelector(".bookwork_title").value = bookWork.title
    bookCopyCatalogCard.querySelector(".bookwork_author").value = authorName
    bookCopyCatalogCard.querySelector(".bookedition_isbn").value = bookEdition.isbn
    bookCopyCatalogCard.querySelector(".bookedition_editor").value = bookEdition.editor
    bookCopyCatalogCard.querySelector(".bookedition_edition_year").value = bookEdition.editionYear
    bookCopyCatalogCard.querySelector(".bookedition_language").value = bookEdition.language
    bookCopyCatalogCard.querySelector(".bookCopy_barCode").value = bookCopy.barCode
    bookCopyCatalogCard.querySelector(".bookCopy_signature").value = bookCopy.signature
    bookCopyCatalogCard.querySelector(".bookCopy_registration_date").value = bookCopy.registrationDate
    bookCopyCatalogCard.querySelector(".bookCopy_registration_number").value = bookCopy.registrationNumber
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
export { generateBookeditionsTableContent }
export { generateBookCopyCatalogCard }
export { getBookedition }
export { getNewBookcopy }
export { reasigneBookeditionValue }
export { reasigneNewBookcopyValue }