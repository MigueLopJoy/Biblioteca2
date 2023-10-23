import { findCurrentPage } from "../../modules_commons.js"
import { fetchRequest } from "../../modules_commons.js"
import { joinParamsToURL } from "../../modules_commons.js"
import { handleErrorMessages } from "../../modules_commons.js"
import { clearErrorMessages } from "../../modules_commons.js"
import { clearFormsData } from "../../modules_commons.js"
import { enableModalActions } from "../../modules_commons.js"

import { toggleNextPageChanging } from "../catalog-commons.js"
import { showSearchResults } from "../catalog-commons.js"
import { showCatalogCard } from "../catalog-commons.js"

const d = document,
    searchAuthorForm = d.querySelector(".form.b_author_form.search"),
    createAuthorForm = d.querySelector(".form.b_author_form.create"),
    authorsResultsTable = d.querySelector(".results_table.b_authors_results_table")

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
        if (form === searchAuthorForm) operation === "search"
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
        if (operation === "search") showSearchResults(table)
        else showCatalogCard(operation, table)
        enableModalActions(results, resultsType, operation, table)
    }
}

const setFormInputsValues = () => {
    authorsForm.author_name.value = `${author.firstName} ${author.lastName}`
    return authorsForm
}

const runAuthorProcess = async form => {
    author = ""
    table = authorsResultsTable
    resultsType = "b_author"
    console.log(form)

    if (form = searchAuthorForm) await getSearchAuthorResults(form)
    else await getCreateAuthorResults(form)
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
    if (operation === "search" && !table.querySelector("th.select_column")) {
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

        let selectAuthor
        if (operation === "search") {
            selectAuthor = d.createElement("td")
            let checkbox = d.createElement("input")
            checkbox.type = "checkbox"
            checkbox.name = `select_author`
            checkbox.classList.add('result_option')
            checkbox.classList.add('author_result_option')
            checkbox.value = i;
            selectAuthor.appendChild(checkbox)
        }

        newRow.appendChild(firstName)
        newRow.appendChild(lastName)
        if (selectAuthor) newRow.appendChild(selectAuthor)

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

const prepareBrowseAuthorEditionProcess = cells => {
    cells[0].innerHTML = `<input type="text" class="edition" value="${cells[0].textContent}" >`
    cells[1].innerHTML = `<input type="text" class="edition"value="${cells[1].textContent}" >`
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
export { prepareBrowseAuthorEditionProcess }
export { getBrowseAuthor }
export { reasigneBrowseAuthorValue }