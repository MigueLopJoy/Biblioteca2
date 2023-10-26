import { findCurrentPage } from "./../modules_commons.js"
import { fetchRequest } from "./../modules_commons.js"
import { joinParamsToURL } from "./../modules_commons.js"
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
    authorsResultsTable = d.querySelector(".results_table.authors_results_table"),
    bookworksResultsTable = d.querySelector(".results_table.bookworks_results_table"),
    authorCatalogCard = d.querySelector(".author_catalog_card"),
    bookworkCatalogCard = d.querySelector(".bookwork_catalog_card"),
    bookeditionCatalogCard = d.querySelector(".bookedition_catalog_card")

let author, bookwork, newEdition, results, error, table, catalogCard, resultsType, operation

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
            await runNewEditionProcess(e.target)
        }

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

const runAuthorProcess = async form => {
    author = ""
    resultsType = "author"
    clearPrintedReults(resultsType)

    if (form.classList.contains("search")) {
        table = authorsResultsTable
        operation = "search"
        await getSearchAuthorResults(form)
    } else if (form.classList.contains("create")) {
        catalogCard = authorCatalogCard
        operation = "create"
        await getCreateAuthorResults(form)
    }
}

const runBookworkProcess = async form => {
    bookwork = ""
    resultsType = "bookwork"
    clearPrintedReults(resultsType)

    if (form.classList.contains("bookwork_form") &&
        form.classList.contains("search")
    ) {
        operation = "search"
        table = bookworksResultsTable
        await getSearchBookworkResults(form)
    } else if (form.classList.contains("bookwork_form") &&
        form.classList.contains("create")
    ) {
        operation = "create"
        catalogCard = bookworkCatalogCard
        await getCreatehBookworkResults(form)
    }
}

const runNewEditionProcess = async form => {
    newEdition = ""
    resultsType = "newEdition"
    catalogCard = bookeditionCatalogCard
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
    } catch (ex) {
        error = ex
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
    } catch (ex) {
        error = ex
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
        return results
    } catch (ex) {
        throw ex
    }
}

const deleteAuthor = async authorId => {
    try {
        await fetchRequest(
            "DELETE",
            `http://localhost:8080/authors-catalog/delete-author/${authorId}`,
        )
        results = ""
    } catch (ex) {
        error = ex
    }
}

const getAuthorBookWorks = async authorId => {
    try {
        return await fetchRequest(
            "GET",
            `http://localhost:8080/bookworks-catalog/get-author-bookworks/${authorId}`,
        )
    } catch (ex) {
        throw ex
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

const deleteBookwork = async bookworkId => {
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
    } catch (ex) {
        error = ex
    }
}

const getEditionCopies = async bookEditionId => {
    try {
        return await fetchRequest(
            "GET",
            `http://localhost:8080/bookcopies/get-bookwork-editions/${bookEditionId}`,
        )
    } catch (ex) {
        throw ex
    }
}

const deleteNewBookedition = async bookeditionId => {
    try {
        await fetchRequest(
            "DELETE",
            `http://localhost:8080/general-catalog/delete-bookedition/${bookeditionId}`,
        )
        results = ""
    } catch (ex) {
        error = ex
    }
}

const getEditNewEditionResults = async editedFields => {
    try {
        results = [await fetchRequest(
            "PUT",
            `http://localhost:8080/general-catalog/edit-bookedition/${results[0].idBookEdition}`,
            {
                idOriginalBookEdition: results[0].idBookEdition,
                isbn: editedFields[0],
                editor: editedFields[1],
                editionYear: editedFields[2],
                language: editedFields[3],
            }
        )]
        return results
    } catch (ex) {
        throw ex
    }
}

const generateAuthorsTableContent = () => {
    for (let i = 0; i < results.length; i++) {

        let result = results[i]

        let newRow = d.createElement("tr")
        newRow.classList.add("results_row")

        let firstName = d.createElement("td")
        firstName.textContent = result.firstName

        let lastName = d.createElement("td")
        lastName.textContent = result.lastName

        let selectAuthor = d.createElement("td"),
            checkbox = d.createElement("input")
        checkbox.type = "checkbox"
        checkbox.name = `select_author`
        checkbox.classList.add('result_option')
        checkbox.classList.add('author_result_option')
        checkbox.value = i;
        selectAuthor.appendChild(checkbox)

        newRow.appendChild(firstName)
        newRow.appendChild(lastName)
        newRow.appendChild(selectAuthor)

        const tableBody = table.querySelector(".results_table_body")

        if (tableBody.firstChild) {
            tableBody.insertBefore(newRow, tableBody.firstChild);
            tableBody.querySelector(".error_message_row > td").setAttribute("colspan", 3)
        } else {
            tableBody.appendChild(newRow);
        }
    }
}

const generateAuthorCatalogCard = async () => {
    let author = results[0],
        authorBookWorksMessage

    authorCatalogCard.classList.remove("hidden")

    authorCatalogCard.querySelector(".author_firstName").value = author.firstName
    authorCatalogCard.querySelector(".author_lastName").value = author.lastName
    try {
        let authorBookWorks = await getAuthorBookWorks(author.idAuthor)
        authorBookWorksMessage = `Author Book Works: ${authorBookWorks.length}`
    } catch (error) {
        authorBookWorksMessage = error.message
    }
    authorCatalogCard.querySelector(".author_bookworks").value = authorBookWorksMessage
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
            tableBody.querySelector(".error_message_row > td").setAttribute("colspan", 3)
        } else {
            tableBody.appendChild(newRow)
        }
    }
}

const generateBookWorkCatalogCard = async () => {
    let bookwork = results[0],
        author = bookwork.author,
        authorName = `${author.firstName} ${author.lastName}`,
        title = bookwork.title,
        publicationYear = bookwork.publicationYear,
        bookWorkEditionsMessage,
        bookWorkCatalogCard = d.querySelector(".bookwork_catalog_card")
    bookWorkCatalogCard.classList.remove("hidden")

    bookWorkCatalogCard.querySelector(".bookwork_title").value = title
    bookWorkCatalogCard.querySelector(".bookwork_author").value = authorName
    bookWorkCatalogCard.querySelector(".bookwork_publication_year").value = publicationYear

    try {
        let bookWorkEditions = await getBookWorkEditions(bookwork.idBookWork)
        bookWorkEditionsMessage = `Book Work Editions: ${bookWorkEditions.length}`
    } catch (error) {
        bookWorkEditionsMessage = error.message
    }
    bookWorkCatalogCard.querySelector(".bookwork_editions").value = bookWorkEditionsMessage
}

const generateBookEditionCatalogCard = async () => {
    let bookEdition = results[0],
        author = bookwork.author,
        bookWork = bookEdition.bookWork,
        authorName = `${author.firstName} ${author.lastName}`,
        editionCopiesMessage

    bookeditionCatalogCard.classList.remove("hidden")

    bookeditionCatalogCard.querySelector(".bookwork_title").value = bookWork.title
    bookeditionCatalogCard.querySelector(".bookwork_author").value = authorName
    bookeditionCatalogCard.querySelector(".bookedition_isbn").value = bookEdition.isbn
    bookeditionCatalogCard.querySelector(".bookedition_editor").value = bookEdition.editor
    bookeditionCatalogCard.querySelector(".bookedition_edition_year").value = bookEdition.editionYear
    bookeditionCatalogCard.querySelector(".bookedition_language").value = bookEdition.language
    try {
        let bookEditionCopies = await getEditionCopies(bookEdition.idBookEdition)
        editionCopiesMessage = `Author Book Works: ${bookEditionCopies.length}`
    } catch (error) {
        editionCopiesMessage = error.message
    }
    bookeditionCatalogCard.querySelector(".bookedition_copies").value = editionCopiesMessage
}

const getAuthor = () => {
    return author
}

const getBookwork = () => {
    return bookwork
}

const getNewEdition = () => {
    return newEdition
}

const reasigneAuthorValue = newAuthorValue => {
    author = newAuthorValue
}

const reasigneBookworkValue = newBookworkValue => {
    bookwork = newBookworkValue
}

const reasigneNewEditionValue = newEditionValue => {
    newEdition = newEditionValue
}


export { getEditAuthorResults }
export { getEditBookworkResults }
export { getEditNewEditionResults }
export { deleteAuthor }
export { deleteBookwork }
export { deleteNewBookedition }

export { generateAuthorsTableContent }
export { generateBookworksTableContent }
export { generateAuthorCatalogCard }
export { generateBookWorkCatalogCard }
export { generateBookEditionCatalogCard }

export { getAuthor }
export { getBookwork }
export { getNewEdition }
export { reasigneAuthorValue }
export { reasigneBookworkValue }
export { reasigneNewEditionValue }