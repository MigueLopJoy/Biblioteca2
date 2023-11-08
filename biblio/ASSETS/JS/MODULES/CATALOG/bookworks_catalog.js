import {
    showSearchResults,
    showCatalogCard,
    findSelectedResult,
    clearForms,
    setCreationValues,
    setSearchValues,
    closeModal
} from "./../modules_commons.js"

import {
    fetchRequest,
} from "./../requests.js"

import {
    handleErrorMessages,
    clearErrorMessages
} from "../api_messages_handler.js"

import { getAuthor } from "./authors_catalog.js"

const d = document

let bookwork, results, error, table, catalogCard, resultsType, operation

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("bookworks_section")) {
        sendBookWorkForm(undefined, e.target)
    }
})

const sendBookWorkForm = async (author, form) => {
    error = undefined
    clearErrorMessages()

    if (!form) form = setFormInputsValues(author)
    await runBookWorkProcess(form)

    if (error) {
        handleErrorMessages(error, form)
        clearForms()
    } else {
        if (operation === "search") {
            showSearchResults(table)
            setSearchValues(results, resultsType, table)
        } else if (operation === "create") {
            showCatalogCard(results, resultsType, catalogCard)
            setCreationValues(results, resultsType, catalogCard)
        }
    }
}

const setFormInputsValues = author => {
    let searchBookWorkForm = d.querySelector(".form.bookwork_form.search")
    searchBookWorkForm.author_name.value = `${author.firstName} ${author.lastName}`
    return searchBookWorkForm
}

const runBookWorkProcess = async form => {
    bookwork = ""
    resultsType = "bookwork"

    if (form.classList.contains("search")) {
        table = d.querySelector(".results_table.bookworks_results_table")
        operation = "search"
        results = await getBookworks(form)
    } else if (form.classList.contains("create")) {
        catalogCard = d.querySelector(".catalog_card.bookwork_catalog_card")
        operation = "create"
        results = await createBookwork(form)
    }
}

const displayBookWorkSelectionTable = async () => {
    const bookworkForm = d.querySelector(".form.bookwork_form.search")
    let bookWorkTitle = d.querySelector(".form.create .bookwork_title").value,
        bookWorkAuthor = d.querySelector(".form.create .author_name").value

    bookworkForm.bookwork_title.value = bookWorkTitle
    bookworkForm.author_name.value = bookWorkAuthor

    await sendBookWorkForm(bookWorkTitle, bookworkForm)
    try {
        getBookWorkResults()
        changeSelectBtn()
    } catch (ex) {
        error = ex
        handleErrorMessages(error, d.querySelector(".form.create"))
    }
}

const changeSelectBtn = () => {
    const selectResultBtn = d.querySelector(".select_results_btn")

    if (selectResultBtn.classList.contains("select_edition_bookwork")) {
        selectResultBtn.textContent = "Select Book Edition"
        selectResultBtn.classList.remove("select_edition_bookwork")
    } else {
        selectResultBtn.textContent = "Select Book Work"
        selectResultBtn.classList.add("select_edition_bookwork")
    }
}

const selectEditionBookWork = () => {
    changeSelectBtn()
    bookwork = getBookWorkResults()[findSelectedResult()]
    closeModal(d.querySelector(".form.create"))
    manageInputValues()
}

const manageInputValues = () => {
    const bookworkTitleInput = d.querySelector(".form.create .bookwork_title"),
        bookworkAuthorInput = d.querySelector(".form.create .author_name"),
        bookEditionIsbnInput = d.querySelector(".form.create .bookedition_isbn")

    if (bookwork) {
        bookworkTitleInput.value = bookwork.title
        bookworkAuthorInput.value = `${bookwork.author.firstName} ${bookwork.author.lastName}`

        if (bookEditionIsbnInput) bookEditionIsbnInput.value = ""

    } else bookworkTitleInput.value = ""
}

const getBookworks = async form => {
    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/bookworks-catalog/search-bookwork",
            {
                title: form.bookwork_title.value,
                author: form.author_name.value
            }
        )
    } catch (ex) {
        error = ex
    }
}

const getBookWorkEditions = async bookWorkId => {
    try {
        let bookWorkEditions = await fetchRequest(
            "GET",
            `http://localhost:8080/general-catalog/get-bookwork-editions/${bookWorkId}`,
        )
        return bookWorkEditions
    } catch (ex) {
        throw ex
    }
}

const createBookwork = async form => {
    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/bookworks-catalog/save-bookwork",
            {
                title: form.title.value,
                author: {
                    firstName: getAuthor() ? getAuthor().firstName : "",
                    lastName: getAuthor() ? getAuthor().lastName : ""
                },
                publicationYear: form.publication_year.value
            }
        )
    } catch (ex) {
        error = ex
    }
}

const editBookwork = async (idBookWork, editedFields) => {
    try {
        return await fetchRequest(
            "PUT",
            `http://localhost:8080/bookworks-catalog/edit-bookwork/${idBookWork}`,
            {
                title: editedFields[0],
                publicationYear: editedFields[1]
            }
        )
    } catch (ex) {
        throw ex
    }
}

const deleteBookwork = async bookworkId => {
    try {
        return await fetchRequest(
            "DELETE",
            `http://localhost:8080/bookworks-catalog/delete-bookwork/${bookworkId}`,
        )
    } catch (ex) {
        throw ex
    }
}

const generateBookworksTableContent = () => {
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


        let selectBookwork = d.createElement("td"),
            checkbox = d.createElement("input")
        checkbox.type = "checkbox"
        checkbox.name = `select-bookwork`
        checkbox.classList.add('result_option')
        checkbox.classList.add('bookwork_result_option')
        checkbox.value = i
        selectBookwork.appendChild(checkbox)

        newRow.appendChild(title)
        newRow.appendChild(bookAuthor)
        newRow.appendChild(publicationYear)
        newRow.appendChild(selectBookwork)
        table.querySelector(".results_table_body").appendChild(newRow)
    }
}

const generateBookWorkCatalogCard = async results => {
    let bookwork = results,
        bookWorkEditions, bookWorkEditionsMessage,
        author = bookwork.author,
        authorName = `${author.firstName} ${author.lastName}`,
        title = bookwork.title,
        publicationYear = bookwork.publicationYear,
        catalogCard = d.querySelector(".bookwork_catalog_card")

    catalogCard.classList.remove("hidden")

    catalogCard.querySelector(".bookwork_title").value = title
    catalogCard.querySelector(".bookwork_author").value = authorName
    catalogCard.querySelector(".bookwork_publication_year").value = publicationYear

    try {
        bookWorkEditions = await getBookWorkEditions(bookwork.idBookWork)
        bookWorkEditionsMessage = `Book Work Editions: ${bookWorkEditions.length}`
    } catch (error) {
        bookWorkEditionsMessage = error.message
    }
    catalogCard.querySelector(".bookwork_editions").value = bookWorkEditionsMessage
}

const getBookWork = () => {
    return bookwork
}

const getBookWorkResults = () => {
    if (error) throw error
    return results
}

const setBookworkValue = newBookWorkValue => {
    bookwork = newBookWorkValue
}

export {
    sendBookWorkForm,
    displayBookWorkSelectionTable,
    selectEditionBookWork,
    editBookwork,
    deleteBookwork,
    generateBookworksTableContent,
    generateBookWorkCatalogCard,
    getBookWork,
    setBookworkValue,
    getBookWorkResults
}