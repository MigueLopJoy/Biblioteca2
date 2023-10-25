import { findCurrentPage } from "../../modules_commons.js"
import { fetchRequest } from "../../modules_commons.js"
import { joinParamsToURL } from "../../modules_commons.js"
import { handleErrorMessages } from "../../modules_commons.js"
import { clearErrorMessages } from "../../modules_commons.js"
import { clearFormsData } from "../../modules_commons.js"
import { enableModalActions } from "../../modules_commons.js"
import { enableCreateModalActions } from "../../modules_commons.js"

import { toggleNextPageChanging } from "../catalog-commons.js"
import { showSearchResults } from "../catalog-commons.js"
import { showCatalogCard } from "../catalog-commons.js"

const d = document,
    authorsResultsTable = d.querySelector(".results_table.b_authors_results_table"),
    authorCatalogCard = d.querySelector(".catalog_card.author_catalog_card")

let author, results, error, table, resultsType, operation

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("b_authors_section")) {
        initializeBrowseAuthorsFormSubmit(e.target)
    }
})

const initializeBrowseAuthorsFormSubmit = async form => {

    if (!form) {
        form = setFormInputsValues()
        operation = "manage"
    } else {
        if (form.classList.contains("search")) operation = "search"
        else operation = "create"
    }

    clearErrorMessages()

    if (findCurrentPage().classList.contains("b_authors_page")) {
        if (operation === "search")
            await runAuthorProcess(form)
    }

    if (error) {
        handleErrorMessages(error, form)
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

const setFormInputsValues = () => {
    authorsForm.author_name.value = `${author.firstName} ${author.lastName}`
    return authorsForm
}

const runAuthorProcess = async form => {
    author = ""
    resultsType = "b_author"
    clearPrintedReults(resultsType)

    if (form.classList.contains("search")) {
        table = authorsResultsTable
        operation = "search"
        await getSearchAuthorResults(form)
    }
    else {
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
    if (!table.querySelector("th.select_column")) {
        let selectColumn = d.createElement("th")
        selectColumn.textContent = "Select book work"
        selectColumn.classList.add("select_column")
        table.querySelector("thead tr").appendChild(selectColumn)
    }
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

const generateBrowseAuthorCatalogCard = async () => {
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

const getBrowseAuthor = () => {
    return author
}

const reasigneBrowseAuthorValue = newAuthorValue => {
    author = newAuthorValue
}

export { initializeBrowseAuthorsFormSubmit }
export { getEditBrowseAuthorResults }
export { deleteBrowseAuthor }
export { generateBrowseAuthorsTableContent }
export { generateBrowseAuthorCatalogCard }
export { prepareBrowseAuthorEditionProcess }
export { getBrowseAuthor }
export { reasigneBrowseAuthorValue }