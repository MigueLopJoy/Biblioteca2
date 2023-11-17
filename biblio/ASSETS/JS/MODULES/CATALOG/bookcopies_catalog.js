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

import { getAuthor, setAuthorValue } from "./authors_catalog.js"
import { getBookWork, setBookworkValue } from "./bookworks_catalog.js"
import { getBookEdition, setBookeditionValue } from "./bookeditions_catalog.js"

const d = document

let bookcopy, results, error, resultsType, operation, table, catalogCard

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("bookcopies_section")) {
        sendBookCopyForm(undefined, e.target)
    }
})

const sendBookCopyForm = async (bookedition, form) => {
    if (!form) form = setFormInputsValues(bookedition)

    error = undefined
    clearErrorMessages()

    await runBookCopyProcess(form)

    if (error) {
        handleErrorMessages(error, form)
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

const setFormInputsValues = bookCopy => {
    const searchBookCopyForm = d.querySelector(".form.bookcopies_form.search")

    let bookEdition = bookCopy.bookEdition,
        bookWork = bookEdition.bookWork,
        author = bookEdition.author

    searchBookCopyForm.title.value = bookWork.title
    searchBookCopyForm.author.value = `${author.firstName} ${author.lastName}`
    searchBookCopyForm.isbn.value = bookEdition.isbn
    searchBookCopyForm.bar_code.value = bookCopy.barCode
    searchBookCopyForm.min_registration_number.value = bookCopy.registrationNumber
    searchBookCopyForm.max_registration_number.value = bookCopy.registrationNumber
    searchBookCopyForm.min_registration_date.value = bookCopy.registrationDate
    searchBookCopyForm.max_registration_date.value = bookCopy.registrationDate
    searchBookCopyForm.signature.value = bookCopy.signature

    return searchBookCopyForm
}

const runBookCopyProcess = async form => {
    bookcopy = ""
    resultsType = "bookcopy"

    if (form.classList.contains("search")) {
        table = d.querySelector(".results_table.bookcopies_results_table")
        operation = "search"
        results = await searchBookCopies(form)
    } else if (form.classList.contains("create")) {
        catalogCard = d.querySelector(".catalog_card.bookcopy_catalog_card")
        operation = "create"
        results = await createBookCopy(form)
    }
}

const displayRegistrationNumberRange = () => {
    d.querySelector(".form > .registration_number").classList.add("hidden")
    d.querySelector(".form > .registration_number_range").classList.remove("d-none")
    d.querySelector(".form > .registration_number_range").classList.add("d-flex")
}

const displayRegistrationDateRange = () => {
    d.querySelector(".form > .registration_date").classList.add("hidden")
    d.querySelector(".form > .registration_date_range").classList.remove("d-none")
    d.querySelector(".form > .registration_date_range").classList.add("d-flex")
}

const searchBookCopies = async form => {
    const rangeRegNumContainer = form.querySelector(".registration_number_range"),
        rangeRegDateContainer = form.querySelector(".registration_date_range")

    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/bookcopies/search-bookcopies",
            {
                title: form.title.value,
                author: form.author.value,
                isbn: form.isbn.value,
                editor: "",
                language: "",
                barCode: form.bar_code.value,
                minRegistrationNumber:
                    containerContainsClass(rangeRegNumContainer) ?
                        form.registration_number.value : form.min_registration_number.value,
                maxRegistrationNumber:
                    containerContainsClass(rangeRegNumContainer) ?
                        form.registration_number.value : form.max_registration_number.value,
                minRegistrationDate:
                    containerContainsClass(rangeRegDateContainer) ?
                        form.registration_date.value : form.min_registration_date.value,
                maxRegistrationDate:
                    containerContainsClass(rangeRegDateContainer) ?
                        form.registration_date.value : form.max_registration_date.value,
                signature: form.signature.value
            }
        )
    } catch (ex) {
        error = ex
    }
}

const createBookCopy = async form => {
    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/bookcopies/save-bookcopy",
            {
                libraryId: 1,
                registrationNumber: form.registration_number.value,
                signature: form.signature.value,
                bookEdition: {
                    isbn: getBookEdition() ? getBookEdition().isbn : "",
                    editor: getBookEdition() ? getBookEdition().editor : "",
                    editionYear: getBookEdition() ? getBookEdition().editionYear : "",
                    language: getBookEdition() ? getBookEdition().language : "",
                    bookWork: {
                        title: getBookWork() ? getBookWork().title : "",
                        author: {
                            firstName: getAuthor() ? getAuthor().firstName : "",
                            lastName: getAuthor() ? getAuthor().lastName : ""
                        },
                        publicationYear: getBookWork() ? getBookWork().publicationYear : "",
                    }
                }
            }
        )
    } catch (ex) {
        setBookeditionValue(undefined)
        setBookworkValue(undefined)
        setAuthorValue(undefined)
        error = ex
    }
}

const containerContainsClass = inputContainer =>
    inputContainer.classList.contains("d-none")

const editBookcopy = async (idBookCopy, editedFields) => {
    try {
        return await fetchRequest(
            "PUT",
            `http://localhost:8080/bookcopies/edit-bookcopy/${idBookCopy}`,
            {
                originalBookCopyId: idBookCopy,
                registrationNumber: editedFields[1],
                signature: editedFields[0],
                status: bookcopy.bookCopyStatus,
                borrowed: bookcopy.borrowed
            }
        )
    } catch (ex) {
        throw ex
    }
}

const deleteBookCopy = async bookcopyId => {
    try {
        return await fetchRequest(
            "DELETE",
            `http://localhost:8080/bookcopies/delete-bookcopy/${bookcopyId}`,
        )
    } catch (ex) {
        throw ex
    }
}


const generateBookCopiesTableContent = () => {
    for (let i = 0; i < results.length; i++) {
        let result = results[i],
            bookEdition = result.bookEdition,
            bookWork = bookEdition.bookWork,
            author = bookWork.author

        let newRow = d.createElement("tr")
        newRow.classList.add("results_row")

        let title = d.createElement("td")
        title.textContent = bookWork.title

        let bookAuthor = d.createElement("td")
        bookAuthor.textContent = `${author.firstName} ${author.lastName}`

        let isbn = d.createElement("td")
        isbn.textContent = bookEdition.isbn

        let barCode = d.createElement("td")
        barCode.textContent = result.barCode

        let registrationNumber = d.createElement("td")
        registrationNumber.textContent = result.registrationNumber

        let signature = d.createElement("td")
        signature.textContent = result.signature

        let selectedBookCopy = d.createElement("td"),
            checkbox = d.createElement("input")
        checkbox.type = "checkbox"
        checkbox.name = `select_bookcopy`
        checkbox.classList.add('result_option')
        checkbox.classList.add('bookcopy_result_option')
        checkbox.value = i
        selectedBookCopy.appendChild(checkbox)

        newRow.appendChild(title)
        newRow.appendChild(bookAuthor)
        newRow.appendChild(isbn)
        newRow.appendChild(barCode)
        newRow.appendChild(registrationNumber)
        newRow.appendChild(signature)
        newRow.appendChild(selectedBookCopy)

        table.querySelector(".results_table_body").appendChild(newRow);
    }
}

const generateBookCopyCatalogCard = async results => {
    const catalogCard = d.querySelector(".bookcopy_catalog_card")
    
    let bookCopy = results,
        bookEdition = bookCopy.bookEdition,
        bookWork = bookEdition.bookWork,
        author = bookWork.author,
        authorName = `${author.firstName} ${author.lastName}`

    catalogCard.classList.remove("hidden")

    catalogCard.querySelector(".bookwork_title").value = bookWork.title
    catalogCard.querySelector(".bookwork_author").value = authorName
    catalogCard.querySelector(".bookedition_isbn").value = bookEdition.isbn
    catalogCard.querySelector(".bookedition_editor").value = bookEdition.editor
    catalogCard.querySelector(".bookedition_edition_year").value = bookEdition.editionYear
    catalogCard.querySelector(".bookedition_language").value = bookEdition.language
    catalogCard.querySelector(".bookCopy_barCode").value = bookCopy.barCode
    catalogCard.querySelector(".bookCopy_signature").value = bookCopy.signature
    catalogCard.querySelector(".bookCopy_registration_date").value = bookCopy.registrationDate
    catalogCard.querySelector(".bookCopy_registration_number").value = bookCopy.registrationNumber
}

const geBookCopy = () => {
    return bookcopy
}

const setBookCopyValue = newBookCopyValue => {
    bookcopy = newBookCopyValue
}


export {
    sendBookCopyForm,
    displayRegistrationNumberRange,
    displayRegistrationDateRange,
    editBookcopy,
    deleteBookCopy,
    generateBookCopyCatalogCard,
    generateBookCopiesTableContent,
    geBookCopy,
    setBookCopyValue
}
