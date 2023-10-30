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
    joinParamsToURL
} from "./../requests.js"

import {
    handleErrorMessages,
    clearErrorMessages
} from "../api_messages_handler.js"

const d = document

let author, results, error, resultsType, operation, table, catalogCard

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("authors_section")) {
        sendAuthorForm(undefined, e.target)
    }
})

const sendAuthorForm = async (author, form) => {
    if (!form) form = setFormInputsValues(author)

    clearErrorMessages()

    await runAuthorProcess(form)
    if (error) {
        handleErrorMessages(error, form)
        error = null
        clearForms()
    } else {
        if (operation === "search") {
            showSearchResults(table)
            setSearchValues(results, resultsType, table)
        } else if (operation === "create") {
            showCatalogCard(results, resultsType, catalogCard)
            setCreationValues(results.author, resultsType, catalogCard)
        }
    }
}

const setFormInputsValues = author => {
    let searchAuthorForm = d.querySelector(".form.author_form.search")
    if (author.firstName && author.lastName) {
        searchAuthorForm.author_name.value = `${author.firstName} ${author.lastName}`
    } else searchAuthorForm.author_name.value = author
    return searchAuthorForm
}

const runAuthorProcess = async form => {
    author = ""
    resultsType = "author"

    if (form.classList.contains("search")) {
        table = d.querySelector(".results_table.authors_results_table")
        operation = "search"
        results = await getAuthors(form)
    } else if (form.classList.contains("create")) {
        catalogCard = d.querySelector(".catalog_card.author_catalog_card")
        operation = "create"
        results = await createAuthor(form)
    }
}

const getAuthors = async form => {
    try {
        return await fetchRequest(
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

const createAuthor = async form => {
    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/authors-catalog/save-author",
            {
                firstName: form.firstName.value.trim(),
                lastName: form.lastName.value.trim()
            }
        )
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

const editAuthor = async (idAuthor, editedFields) => {
    try {
        return await fetchRequest(
            "PUT",
            `http://localhost:8080/authors-catalog/edit-author/${idAuthor}`,
            {
                firstName: editedFields[0],
                lastName: editedFields[1]
            }
        )
    } catch (ex) {
        throw ex
    }
}

const deleteAuthor = async idAuthor => {
    try {
        return await fetchRequest(
            "DELETE",
            `http://localhost:8080/authors-catalog/delete-author/${idAuthor}`
        )
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
        checkbox.value = i;
        selectAuthor.appendChild(checkbox)


        newRow.appendChild(firstName)
        newRow.appendChild(lastName)
        newRow.appendChild(selectAuthor)
        table.querySelector(".results_table_body").appendChild(newRow)
    }
}

const generateAuthorCatalogCard = async results => {
    let author = results,
        authorBookWorks, authorBookWorksMessage,
        catalogCard = d.querySelector(".catalog_card.author_catalog_card")

    catalogCard.classList.remove("hidden")

    catalogCard.querySelector(".author_firstName").value = author.firstName
    catalogCard.querySelector(".author_lastName").value = author.lastName
    try {
        authorBookWorks = await getAuthorBookWorks(author.idAuthor)
        authorBookWorksMessage = `Author Book Works: ${authorBookWorks.length}`
    } catch (error) {
        authorBookWorksMessage = error.message
    }
    catalogCard.querySelector(".author_bookworks").value = authorBookWorksMessage
    return catalogCard
}

const getAuthor = () => {
    return author
}

const setAuthorValue = newAuthorValue => {
    author = newAuthorValue
}

export {
    sendAuthorForm,
    deleteAuthor,
    editAuthor,
    generateAuthorsTableContent,
    generateAuthorCatalogCard,
    getAuthor,
    setAuthorValue
}
