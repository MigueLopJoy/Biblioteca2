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

import { sendAuthorForm, generateAuthorsTableContent } from "./authors_catalog.js"

const d = document

let bookwork, results, error, table, catalogCard, resultsType, operation

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("bookworks_section")) {
        sendBookWorkForm(undefined, e.target)
    }
})

const sendBookWorkForm = async (author, form) => {
    if (!form) form = setFormInputsValues(author)

    clearErrorMessages()
    await runBookWorkProcess(form)

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
            setCreationValues(results, resultsType, operation, catalogCard)
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
    } else {
        catalogCard = d.querySelector(".catalog_card.bookwork_catalog_card")
        operation = "create"
        results = await createBookwork(form)
    }
}

const bookWorkAuthorSelection = () => {
    let authorName = d.querySelector(".bookwork_form.create .author_name").value
    console.log(authorName)
    sendAuthorForm(authorName)
}

const getBookworks = async form => {
    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/bookworks-catalog/search-bookwork",
            {
                title: form.title.value,
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
                    firstName: author.firstName,
                    lastName: author.lastName
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
        error = ex
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

const generateBookWorkCatalogCard = async (results) => {
    let bookwork = results.bookWork,
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
    return author
}

const setBookworkValue = newBookWorkValue => {
    bookwork = newBookWorkValue
}

export {
    sendBookWorkForm,
    editBookwork,
    deleteBookwork,
    generateBookworksTableContent,
    generateBookWorkCatalogCard,
    getBookWork,
    setBookworkValue,
    bookWorkAuthorSelection
}