import {
    clearForms,
    setCreationValues,
    setSearchValues,
} from "./../modules_commons.js"

import {
    showSearchResults,
    showCatalogCard
} from "./catalog-commons.js"

import {
    fetchRequest,
} from "./../requests.js"

import {
    handleErrorMessages,
    clearErrorMessages
} from "../api_messages_handler.js"

const d = document

let bookcopy, results, error, resultsType, operation, table, catalogCard

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("b_bookcopies_section")) {
        sendBookCopyForm(undefined, e.target)
    }
})

const sendBookCopyForm = async (bookedition, form) => {
    if (!form) form = setFormInputsValues(bookedition)

    clearErrorMessages()
    await runBookCopyProcess(form)


    if (error) {
        handleErrorMessages(error, form)
        error = null
        clearForms()
    } else {
        if (operation === "search") {
            showSearchResults(resultsType, table)
            setSearchValues(results, resultsType, operation, table)
        } else if (operation === "create") {
            showCatalogCard(results, resultsType, catalogCard)
            setCreationValues()
        }
    }
}

const setFormInputsValues = bookedition => {
    let searchBookCopyForm = d.querySelector(".form.b_bookcopies_form.search")
    searchBookCopyForm.isbn.value = bookedition.isbn
    return searchBookCopyForm
}

const runBookCopyProcess = async form => {
    bookcopy = ""
    resultsType = "b_bookcopy"
    table = d.querySelector(".results_table.b_bookcopies_results_table")
    operation = "search"
    results = await getBookCopies(form)
}

const getBookCopies = async form => {
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
                        form.registration_date.value : form.max_registration_number.value,
                signature: form.signature.value,
                status: undefined,
                borrowed: undefined,
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
        )
    } catch (ex) {
        error = ex
    }
}

const containerContainsClass = inputContainer => {
    return inputContainer.classList.contains("d-none")
}

const editBookcopy = async (idBookCopy, editedFields) => {
    try {
        return await fetchRequest(
            "PUT",
            `http://localhost:8080/bookcopies/edit-bookcopy/${idBookCopy}`,
            {
                originalBookCopyId: results[0].idBookCopy,
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
            bookedition = result.bookEdition,
            bookWork = bookedition.bookWork,
            author = bookWork.author

        let newRow = d.createElement("tr")
        newRow.classList.add("results_row")

        let title = d.createElement("td")
        title.textContent = bookWork.title

        let bookAuthor = d.createElement("td")
        bookAuthor.textContent = `${author.firstName} ${author.lastName}`

        let isbn = d.createElement("td")
        isbn.textContent = result.isbn

        let barCode = d.createElement("td")
        barCode.textContent = result.barCode

        let registrationNumber = d.createElement("td")
        registrationNumber.textContent = result.registrationNumber

        let signature = d.createElement("td")
        signature.textContent = result.signature

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
        newRow.appendChild(barCode)
        newRow.appendChild(registrationNumber);
        newRow.appendChild(signature);

        table.querySelector(".results_table_body").appendChild(newRow);
    }
}

const generateBookCopyCatalogCard = async () => {
    let bookCopy = results.bookCopy,
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

const enableRegistrationNumberRange = () => {
    d.querySelector(".input_group.registration_number").style.display = "none"
    d.querySelector(".registration_number_range").classList.remove("d-none")
    d.querySelector(".registration_number_range").classList.add("d-flex")
}

const enableRegistrationDateRange = () => {
    d.querySelector(".input_group.registration_date").style.display = "none"
    d.querySelector(".registration_date_range").classList.remove("d-none")
    d.querySelector(".registration_date_range").classList.add("d-flex")
}

const geBookCopy = () => {
    return bookcopy
}

const setBookCopyValue = newBookCopyValue => {
    bookcopy = newBookCopyValue
}


export {
    sendBookCopyForm,
    editBookcopy,
    deleteBookCopy,
    generateBookCopyCatalogCard,
    generateBookCopiesTableContent,
    geBookCopy,
    setBookCopyValue
}
