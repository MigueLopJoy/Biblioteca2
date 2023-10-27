import {
    fetchRequest,
    handleErrorMessages,
    clearErrorMessages,
    clearFormsData,
    enableSearchModalActions,
    enableCreateModalActions,
} from "../../modules_commons.js"

import {
    showSearchResults,
    showCatalogCard
} from "../catalog-commons.js"

const d = document

let bookwork, results, error, table, catalogCard, resultsType, operation

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("b_bookworks_section")) {
        sendBookWorkForm(undefined, e.target)
    }
})

const sendBookWorkForm = async (author, form) => {

    console.log("SENDING BOOK WORK FORM")

    if (!form) form = setFormInputsValues(author)

    clearErrorMessages()
    await runBookWorkProcess(form)

    if (error) {
        handleErrorMessages(error, form)
        error = null
        clearFormsData()
    } else {
        if (operation === "search") {
            showSearchResults(resultsType, table)
            enableSearchModalActions(results, resultsType, operation, table)
        } else if (operation === "create") {
            showCatalogCard(resultsType, catalogCard)
            enableCreateModalActions(results, resultsType, operation, catalogCard)
        }
    }
}

const setFormInputsValues = author => {
    let searchBookWorkForm = d.querySelector(".form.b_bookwork_form.search")
    searchBookWorkForm.author_name.value = `${author.firstName} ${author.lastName}`
    return searchBookWorkForm
}

const runBookWorkProcess = async form => {
    bookwork = ""
    resultsType = "b_bookwork"

    if (form.classList.contains("search")) {
        table = d.querySelector(".results_table.b_bookworks_results_table")
        operation = "search"
        await getSearchBookworkResults(form)
    } else {
        catalogCard = d.querySelector(".catalog_card.bookwork_catalog_card")
        operation = "create"
        await getCreatehBookworkResults(form)
    }
}

const getSearchBookworkResults = async form => {
    try {
        results = await fetchRequest(
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
    } catch (ex) {
        error = ex
    }
}

const getEditBrowseBookworkResults = async editedFields => {
    try {
        results = [await fetchRequest(
            "PUT",
            `http://localhost:8080/bookworks-catalog/edit-bookwork/${results[0].idBookWork}`,
            {
                title: editedFields[0],
                publicationYear: editedFields[1]
            }
        )]
        return results
    } catch (ex) {
        throw ex
    }
}

const deleteBrowseBookwork = async bookworkId => {
    try {
        await fetchRequest(
            "DELETE",
            `http://localhost:8080/bookworks-catalog/delete-bookwork/${bookworkId}`,
        )
        results = ""
    } catch (ex) {
        error = ex
    }
}

const generateBrowseBookworksTableContent = (base = results, table) => {
    for (let i = 0; i < results.length; i++) {
        let result = base[i],
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

const generateBrowseBookWorkCatalogCard = async (browseResults = results) => {
    let bookwork = browseResults[0],
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

const getBrowseBookWork = () => {
    return author
}

const reasigneBrowseBookWorkValue = newBookWorkValue => {
    bookwork = newBookWorkValue
}

export { sendBookWorkForm }
export { getEditBrowseBookworkResults }
export { deleteBrowseBookwork }
export { generateBrowseBookworksTableContent }
export { generateBrowseBookWorkCatalogCard }
export { getBrowseBookWork }
export { reasigneBrowseBookWorkValue }