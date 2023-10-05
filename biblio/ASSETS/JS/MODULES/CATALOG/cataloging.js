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
import { saveResult } from "./catalog-commons.js"
import { endProcess } from "./catalog-commons.js"

const d = document,
    searchAuthorForm = d.querySelector(".form.author_form.search"),
    createAuthorForm = d.querySelector(".form.author_form.create"),
    searchBookworkForm = d.querySelector(".form.bookwork_form.search"),
    createBookworkForm = d.querySelector(".form.bookwork_form.create"),
    createBookEditionForm = d.querySelector(".form.edition_form.create"),
    authorsResultsTable = d.querySelector(".results_table.authors_results_table"),
    bookworksResultsTable = d.querySelector(".results_table.bookworks_results_table"),
    bookEditionTable = d.querySelector(".results_table.newEdition_results_table")

let author, bookwork, newEdition, results, error, table, resultsType, operation

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("cataloging_section")) {
        let currentPage = findCurrentPage()

        clearErrorMessages()

        if (currentPage.classList.contains("author_page")) {
            await runAuthorProcess(e.target)
        } else if (currentPage.classList.contains("bookwork_page")) {
            await runBookworkProcess(e.target)
        } else if (currentPage.classList.contains("edition_page")) {
            await runBookeditionProcess(e.target)
        }

        if (error) {
            handleErrorMessages(e.target)
            error = null
            toggleNextPageChanging(resultsType)
            clearFormsData()
        } else {
            console.log("1")
            showSearchResults(operation, table)
            enableModalActions(results, resultsType, operation, table)
        }
    }
})

const runAuthorProcess = async form => {
    author = ""
    table = authorsResultsTable
    resultsType = "author"
    clearPrintedReults(resultsType)

    console.log(form)

    if (form === searchAuthorForm) {
        operation = "search"
        await getSearchAuthorResults(form)
    } else if (form === createAuthorForm) {
        operation = "create"
        await getCreateAuthorResults(form)
    }
    console.log(results)
}

const runBookworkProcess = async form => {
    bookwork = ""
    table = bookworksResultsTable
    resultsType = "bookwork"
    clearPrintedReults(resultsType)

    if (form === searchBookworkForm) {
        operation = "search"
        await getSearchBookworkResults(form)
    } else if (form === createBookworkForm) {
        operation = "create"
        await getCreatehBookworkResults(form)
    }
}

const runBookeditionProcess = async form => {
    newEdition = ""
    resultsType = "edition";
    table = bookEditionTable;
    operation = "create"

    await getCreateBookeditionResults(form)
}

const getSearchAuthorResults = async form => {
    try {
        results = await fetchRequest(
            "GET",
            joinParamsToURL(
                "http://localhost:8080/authors-catalog/search-author",
                {
                    author_name: form.author_name.value
                }
            )
        )
    } catch (error) {
        error = error
    }
}

const getCreateAuthorResults = async form => {
    try {
        results = [await fetchRequest(
            "POST",
            "http://localhost:8080/authors-catalog/save-author",
            {
                firstName: form.firstName.value.trim(),
                lastName: form.lastName.value.trim()
            }
        )]
    } catch (error) {
        error = error
    }
}


const getEditAuthorResults = async editedFields => {
    try {
        results = [await fetchRequest(
            "PUT",
            `http://localhost:8080/authors-catalog/edit-author/${results[0].idAuthor}`,
            {
                firstName: editedFields[0],
                lastName: editedFields[1]
            }
        )]
    } catch (error) {
        error = error
    }
}

const getSearchBookworkResults = async form => {
    try {
        results = await fetchRequest(
            "POST",
            "http://localhost:8080/bookworks-catalog/search-bookwork",
            {
                title: form.title.value,
                author: `${author.firstName} ${author.lastName}`
            }
        )
    } catch (error) {
        error = error
    }
}

const getCreatehBookworkResults = async form => {
    try {
        results = [await fetchRequest(
            "POST",
            "http://localhost:8080/bookworks-catalog/save-bookwork",
            {
                title: form.title.value,
                author: {
                    firstName: author.firstName,
                    lastName: author.lastName
                },
                publicationYear: form.publication_year.value
            }
        )]
    } catch (error) {
        error = error
    }
}

const getEditBookworkResults = async editedFields => {
    try {
        results = [await fetchRequest(
            "PUT",
            `http://localhost:8080/bookworks-catalog/edit-bookwork/${results[0].idBookWork}`,
            {
                title: editedFields[0],
                publicationYear: editedFields[1]
            }
        )]
    } catch (error) {
        error = error
    }
}

const getCreateBookeditionResults = async form => {
    try {
        results = [await fetchRequest(
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
        )]
    } catch (error) {
        error = error
    }
}

const getEditBookeditionResults = async editedFields => {
    try {
        results = [await fetchRequest(
            "PUT",
            `http://localhost:8080/general-catalog/save-bookedition/${results[0].idBookEdition}`,
            {
                isbn: editedFields[2],
                editor: editedFields[3],
                editionYear: editedFields[4],
                language: editedFields[5],
            }
        )]
    } catch (error) {
        error = error
    }
}

const generateAuthorsTableContent = () => {
    console.log("AAA")
    if (operation === "search" && !table.querySelector("th.select_column")) {
        let selectColumn = d.createElement("th")
        selectColumn.textContent = "Select Autor"
        selectColumn.classList.add("select_column")
        table.querySelector("thead tr").appendChild(selectColumn)
    }

    for (let i = 0; i < results.length; i++) {

        let result = results[i]

        let newRow = d.createElement("tr")
        newRow.classList.add("results_row")

        let firstName = d.createElement("td")
        firstName.textContent = result.firstName

        let lastName = d.createElement("td")
        lastName.textContent = result.lastName

        let selectAuthor, checkbox
        if (operation === "search") {
            selectAuthor = d.createElement("td")
            checkbox = d.createElement("input")
            checkbox.type = "checkbox"
            checkbox.name = `select_author`
            checkbox.classList.add('result_option')
            checkbox.classList.add('author_result_option')
            checkbox.value = i;
            selectAuthor.appendChild(checkbox)
        }

        newRow.appendChild(firstName)
        newRow.appendChild(lastName)
        if (selectAuthor) {
            newRow.appendChild(selectAuthor)
        }

        let tableBody = table.querySelector(".results_table_body")

        if (tableBody.firstChild) {
            tableBody.insertBefore(newRow, tableBody.firstChild);

            let errorMessageTd = tableBody.querySelector(".error_message_row > td")
            if (selectAuthor) {
                errorMessageTd.setAttribute("colspan", 3)
            } else {
                errorMessageTd.setAttribute("colspan", 2)
            }
        } else {
            tableBody.appendChild(newRow);
        }
    }
    console.log(table)
}

const generateBookworksTableContent = () => {

    if (operation === "search" && !table.querySelector("th.select_column")) {
        let selectColumn = d.createElement("th")
        selectColumn.textContent = "Select book work"
        selectColumn.classList.add("select_column")
        table.querySelector("thead tr").appendChild(selectColumn)
    }

    for (let i = 0; i < results.length; i++) {

        let result = results[i],
            author = result.author

        let newRow = d.createElement("tr")
        newRow.classList.add("results_row")

        let title = d.createElement("td")
        title.textContent = result.title;

        let bookAuthor = d.createElement("td")
        bookAuthor.textContent = `${author.firstName} ${author.lastName}`

        let publicationYear = d.createElement("td")
        publicationYear.textContent = `${result.publicationYear ? result.publicationYear : "Unknown"}`


        let selectBookwork, checkbox
        if (operation === "search") {
            selectBookwork = d.createElement("td"),
                checkbox = d.createElement("input")
            checkbox.type = "checkbox"
            checkbox.name = `select-bookwork`
            checkbox.classList.add('result_option')
            checkbox.classList.add('bookwork_result_option')
            checkbox.value = i
            selectBookwork.appendChild(checkbox)
        }

        newRow.appendChild(title)
        newRow.appendChild(bookAuthor)
        newRow.appendChild(publicationYear)
        if (selectBookwork) {
            newRow.appendChild(selectBookwork)
        }

        let tableBody = table.querySelector(".results_table_body")

        if (tableBody.firstChild) {
            tableBody.insertBefore(newRow, tableBody.firstChild)

            let errorMessageTd = tableBody.querySelector(".error_message_row > td")
            if (selectBookwork) {
                errorMessageTd.setAttribute("colspan", 3)
            } else {
                errorMessageTd.setAttribute("colspan", 3)
            }
        } else {
            tableBody.appendChild(newRow)
        }
    }
}

const generateNewBookeditionTableContent = () => {
    for (let i = 0; i < results.length; i++) {

        let result = results[i]

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
        editionYear.textContent = result.editor

        let language = d.createElement("td")
        language.textContent = result.language

        newRow.appendChild(title);
        newRow.appendChild(bookAuthor);
        newRow.appendChild(isbn);
        newRow.appendChild(editor);
        newRow.appendChild(editionYear);
        newRow.appendChild(language);

        table.querySelector(".results_table_body").appendChild(newRow);
    }
}

const printSelectedResult = () => {
    if (resultsType === "author") {
        d.querySelectorAll(".selected_result_holder .selected_author").forEach(el => {
            el.textContent += `${author.firstName} ${author.lastName}`
        })
        d.querySelector(".form .selected_author").value = `${author.firstName} ${author.lastName}`
    } else if (resultsType === "bookwork") {
        d.querySelectorAll(".selected_result_holder .selected_bookwork").forEach(el => {
            el.textContent += `${bookwork.title}`
        })
        d.querySelector(".form .selected_bookwork").value = bookwork.title
    }
}

const prepareEditonProcess = () => {
    const tableResultsRow = table.querySelector(".results_row"),
        cells = tableResultsRow.querySelectorAll("td")

    clearPrintedReults(resultsType)

    if (resultsType === "author") {
        cells[0].innerHTML = `<input type="text" class="edition" value="${author.firstName}" >`
        cells[1].innerHTML = `<input type="text" class="edition"value="${author.lastName}" >`
    } else if (resultsType = "bookwork") {
        cells[0].innerHTML = `<input type="text" class="edition" value="${bookwork.title}" >`
        cells[2].innerHTML = `<input type="number" class="edition" value="${bookwork.publicationYear}" >`
    } else if (resultsType === "edition") {
        cells[2].innerHTML = `<input type="text" class="edition" value="${edition.isbn}" >`
        cells[3].innerHTML = `<input type="text" class="edition" value="${edition.editor}" >`
        cells[4].innerHTML = `<input type="number" class="edition" value="${edition.editionYear}" >`
        cells[5].innerHTML = `<input type="text" class="edition" value="${edition.language}" >`
    }
    confirmBtn.removeEventListener("click", executeConfirmBtnListener)
    confirmBtn.addEventListener("click", confirmEdition)

}

const confirmEdition = async () => {
    const tbody = table.querySelector(".results_table_body"),
        errorMessageRow = tbody.querySelector(".error_message_row"),
        editedFields = [...tbody.querySelectorAll("td input")].map(input => input.value)

    if (resultsType === "author") {
        await getEditAuthorResults(editedFields)
    } else if (resultsType === "bookwork") {
        await getEditBookworkResults(editedFields)
    } else if (resultsType === "edition") {
        await getEditBookeditionResults(editedFields)
    }

    clearErrorMessages()

    if (error) {
        errorMessageRow.classList.remove("hidden")
        handleErrorMessages(tbody)
        error = null
    } else {
        if (!errorMessageRow.classList.contains("hidden")) {
            errorMessageRow.classList.add("hidden")
        }
        saveResult()
        endProcess()
        confirmBtn.removeEventListener("click", confirmEdition)
    }
}

export { generateAuthorsTableContent }
export { generateBookworksTableContent }
export { generateNewBookeditionTableContent }
export { author }
export { bookwork }