import {
    fetchRequest,
    joinParamsToURL,
    handleErrorMessages,
    clearErrorMessages,
    clearFormsData,
    enableModalActions,
    enableCreateModalActions,
} from "../../modules_commons.js"

import {
    showSearchResults,
    showCatalogCard
} from "../catalog-commons.js"

const d = document,
    bookWorksResultsTable = d.querySelector(".results_table.b_bookworks_results_table"),
    bookWorkCatalogCard = d.querySelector(".catalog_card.bookwork_catalog_card")

let bookwork, results, error, table, catalogCard, resultsType, operation

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("b_bookworks_section")) {
        clearErrorMessages()
        await runBookWorkProcess(e.target)

        if (error) {
            handleErrorMessages(error, e.target)
            error = null
            toggleNextPageChanging(resultsType)
            clearFormsData()
        } else {
            if (operation === "search") {
                showSearchResults(resultsType, table)
                enableModalActions(results, resultsType, operation, table)
            } else if (operation === "create") {
                showCatalogCard(resultsType, catalogCard)
                enableCreateModalActions(results, resultsType, operation, catalogCard)
            }
        }
    }
})

const runBookWorkProcess = async form => {
    bookwork = ""
    resultsType = "b_bookwork"

    if (form.classList.contains("search")) {
        table = bookWorksResultsTable
        operation = "search"
        await getSearchBookworkResults(form)
    } else {
        catalogCard = bookWorkCatalogCard
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

const generateBrowseBookworksTableContent = () => {
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

const generateRelatedEditionsTableContent = (results, table) => {
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

const generateBrowseBookWorkCatalogCard = async (browseResults = results) => {
    let bookwork = browseResults[0],
        bookWorkEditions, bookWorkEditionsMessage,
        author = bookwork.author,
        authorName = `${author.firstName} ${author.lastName}`,
        title = bookwork.title,
        publicationYear = bookwork.publicationYear,
        bookWorkCatalogCard = d.querySelector(".bookwork_catalog_card")
    bookWorkCatalogCard.classList.remove("hidden")

    bookWorkCatalogCard.querySelector(".bookwork_title").value = title
    bookWorkCatalogCard.querySelector(".bookwork_author").value = authorName
    bookWorkCatalogCard.querySelector(".bookwork_publication_year").value = publicationYear

    try {
        bookWorkEditions = await getBookWorkEditions(bookwork.idBookWork)
        bookWorkEditionsMessage = `Book Work Editions: ${bookWorkEditions.length}`
    } catch (error) {
        bookWorkEditionsMessage = error.message
    }
    bookWorkCatalogCard.querySelector(".bookwork_editions").value = bookWorkEditionsMessage
}

const getBrowseBookWork = () => {
    return author
}

const reasigneBrowseAuthorValue = newAuthorValue => {
    author = newAuthorValue
}

export { getEditBrowseBookworkResults }
export { deleteBrowseBookwork }
export { generateBrowseBookworksTableContent }
export { generateBrowseBookWorkCatalogCard }
export { getBrowseBookWork }
export { reasigneBrowseAuthorValue }
export { generateRelatedEditionsTableContent }
export { getBookWorkEditions }