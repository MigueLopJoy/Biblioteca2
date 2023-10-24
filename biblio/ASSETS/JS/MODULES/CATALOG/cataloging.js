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
    bookEditionTable = d.querySelector(".results_table.newEdition_results_table"),
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
                console.log(catalogCard)
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
        console.log(authorCatalogCard)
        console.log(catalogCard)
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
    table = bookEditionTable
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
    let authorBookWorks
    try {
        authorBookWorks = await fetchRequest(
            "GET",
            `http://localhost:8080/bookworks-catalog/get-author-bookworks/${authorId}`,
        )
        return authorBookWorks
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
    let bookWorkEditions
    try {
        bookWorkEditions = await fetchRequest(
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

    if (!table.querySelector("th.select_column")) {
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
}

const generateAuthorCatalogCard = () => {
    let author = results[0],
        authorName = `${author.firstName} ${author.lastName}`,
        authorBookWorks = getAuthorBookWorks().length,
        authorBookWorksMessage,
        authorCatalogCard = d.querySelector(".author_catalog_card")
    authorCatalogCard.classList.remove("hidden")

    if (authorBookWorks > 0) {
        authorBookWorksMessage = `Author Book Works: ${authorBookWorks}`
    } else {
        authorBookWorksMessage = "No Associated Book Works"
    }

    authorCatalogCard.querySelector(".author_bookworks").value = authorBookWorksMessage
    authorCatalogCard.querySelector(".author_name").value = authorName
}

const generateBookWorkCatalogCard = () => {
    let bookWork = results[0],
        author = bookWork.author,
        authorName = `${author.firstName} ${author.lastName}`,
        title = bookwork.title,
        publicationYear = bookWork.publicationYear,
        bookWorkEditions = getBookWorkEditions().length,
        bookWorkEditionsMessage,
        bookWorkCatalogCard = d.querySelector(".bookwork_catalog_card")
    bookWorkCatalogCard.classList.remove("hidden")

    if (bookWorkEditions > 0) {
        bookWorkEditionsMessage = `Book Work Editions: ${bookWorkEditions}`
    } else {
        bookWorkEditionsMessage = "No Associated Book Editions"
    }
    bookWorkCatalogCard.querySelector(".title").value = title
    bookWorkCatalogCard.querySelector(".author").value = authorName
    bookWorkCatalogCard.querySelector(".publication_year").value = publicationYear
    bookWorkCatalogCard.querySelector(".bookwork_editions").value = bookWorkEditionsMessage
}

const generateBookEditionCatalogCard = () => {
    let author = results[0],
        authorName = `${author.firstName} ${author.lastName}`,
        authorBookWorks = getAuthorBookWorks().length,
        authorBookWorksMessage,
        authorCatalogCard = d.querySelector(".author_catalog_card")
    authorCatalogCard.classList.remove("hidden")

    if (authorBookWorks > 0) {
        authorBookWorksMessage = `Author Book Works: ${countAuthorBookWorks}`
    } else {
        authorBookWorksMessage = "No Associated Book Copies"
    }

    authorCatalogCard.querySelector(".author_bookworks").value = authorBookWorksMessage
    authorCatalogCard.querySelector(".author_name").value = authorName
}


const generateBookworksTableContent = () => {
    if (!table.querySelector("th.select_column")) {
        let selectColumn = d.createElement("th")
        selectColumn.textContent = "Select author"
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

        let result = results[i],
            bookWork = result.bookWork,
            author = result.bookWork.author

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

        let language = d.createElement("td")
        language.textContent = result.language

        newRow.appendChild(title)
        newRow.appendChild(bookAuthor)
        newRow.appendChild(isbn)
        newRow.appendChild(editor)
        newRow.appendChild(editionYear)
        newRow.appendChild(language)

        let tableBody = table.querySelector(".results_table_body")

        if (tableBody.firstChild) {
            tableBody.insertBefore(newRow, tableBody.firstChild)

            let errorMessageTd = tableBody.querySelector(".error_message_row > td")
            errorMessageTd.setAttribute("colspan", 6)
        } else {
            tableBody.appendChild(newRow)
        }
    }
}

const prepareAuthorEditionProcess = cells => {
    cells[0].innerHTML = `<input type="text" class="edition" value="${author.firstName}" >`
    cells[1].innerHTML = `<input type="text" class="edition"value="${author.lastName}" >`
}

const prepareBookworkEditionProcess = cells => {
    cells[0].innerHTML = `<input type="text" class="edition" value="${bookwork.title}" >`
    cells[2].innerHTML = `<input type="number" class="edition" value="${bookwork.publicationYear}" >`
}

const prepareNewEditionEditionProcess = cells => {
    cells[2].innerHTML = `<input type="text" class="edition" value="${newEdition.isbn}" >`
    cells[3].innerHTML = `<input type="text" class="edition" value="${newEdition.editor}" >`
    cells[4].innerHTML = `<input type="number" class="edition" value="${newEdition.editionYear}" >`
    cells[5].innerHTML = `<input type="text" class="edition" value="${newEdition.language}" >`
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
export { generateNewBookeditionTableContent }
export { generateAuthorCatalogCard }
export { generateBookWorkCatalogCard }
export { generateBookEditionCatalogCard }

export { prepareAuthorEditionProcess }
export { prepareBookworkEditionProcess }
export { prepareNewEditionEditionProcess }

export { getAuthor }
export { getBookwork }
export { getNewEdition }
export { reasigneAuthorValue }
export { reasigneBookworkValue }
export { reasigneNewEditionValue }