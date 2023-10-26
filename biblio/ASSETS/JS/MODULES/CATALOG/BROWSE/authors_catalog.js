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
    authorsResultsTable = d.querySelector(".results_table.b_authors_results_table"),
    authorCatalogCard = d.querySelector(".catalog_card.author_catalog_card")

let author, results, error, table, catalogCard, resultsType, operation

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("b_authors_section")) {
        clearErrorMessages()
        await runAuthorProcess(e.target)

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
    resultsType = "b_author"

    if (form.classList.contains("search")) {
        table = authorsResultsTable
        operation = "search"
        await getSearchAuthorResults(form)
    } else {
        catalogCard = authorCatalogCard
        operation = "create"
        await getCreateAuthorResults(form)
    }
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

const getEditBrowseAuthorResults = async editedFields => {
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

const deleteBrowseAuthor = async () => {
    try {
        results = [await fetchRequest(
            "PUT",
            `http://localhost:8080/authors-catalog/delete-author/${results[0].idAuthor}`
        )]
        return results
    } catch (ex) {
        throw ex
    }
}

const generateBrowseAuthorsTableContent = (base = results) => {
    for (let i = 0; i < results.length; i++) {

        let result = base[i]

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
        table.querySelector(".results_table_body").appendChild(newRow)
    }
}

const generateRelatedBookWorksTableContent = (results, table) => {
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

const generateBrowseAuthorCatalogCard = async (browseResults = results) => {
    let author = browseResults[0],
        authorBookWorks, authorBookWorksMessage

    authorCatalogCard.classList.remove("hidden")

    authorCatalogCard.querySelector(".author_firstName").value = author.firstName
    authorCatalogCard.querySelector(".author_lastName").value = author.lastName
    try {
        authorBookWorks = await getAuthorBookWorks(author.idAuthor)
        authorBookWorksMessage = `Author Book Works: ${authorBookWorks.length}`
    } catch (error) {
        authorBookWorksMessage = error.message
    }
    authorCatalogCard.querySelector(".author_bookworks").value = authorBookWorksMessage
}

const getBrowseAuthor = () => {
    return author
}

const reasigneBrowseAuthorValue = newAuthorValue => {
    author = newAuthorValue
}

export { getEditBrowseAuthorResults }
export { deleteBrowseAuthor }
export { generateBrowseAuthorsTableContent }
export { generateBrowseAuthorCatalogCard }
export { getBrowseAuthor }
export { reasigneBrowseAuthorValue }
export { generateRelatedBookWorksTableContent }
export { getAuthorBookWorks }