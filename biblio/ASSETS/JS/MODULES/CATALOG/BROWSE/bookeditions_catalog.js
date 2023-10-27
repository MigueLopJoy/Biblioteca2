import {
    fetchRequest,
    handleErrorMessages,
    clearErrorMessages,
    clearFormsData,
    enableSearchModalActions,
} from "../../modules_commons.js"

import {
    showSearchResults,
} from "../catalog-commons.js"

const d = document

let bookedition, results, error, table, resultsType, operation

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("b_bookeditions_section")) {
        sendBookEditionForm(undefined, e.target)
    }
})

const sendBookEditionForm = async (bookwork, form) => {

    console.log(form)

    if (!form) form = setFormInputsValues(bookwork)

    clearErrorMessages()
    await runBookEditionProcess(form)

    if (error) {
        handleErrorMessages(error, form)
        error = null
        clearFormsData()
    } else {
        if (operation === "search") {
            showSearchResults(resultsType, table)
            enableSearchModalActions(results, resultsType, operation, table)
        }
    }
}

const setFormInputsValues = bookwork => {
    let searchBookEditionForm = d.querySelector(".form.b_bookedition_form.search"),
        author = bookwork.author

    searchBookEditionForm.title.value = bookwork.title
    searchBookEditionForm.author.value = `${author.firstName} ${author.lastName}`

    console.log(searchBookEditionForm)

    return searchBookEditionForm
}

const runBookEditionProcess = async form => {
    bookedition = ""
    resultsType = "b_bookedition"

    table = d.querySelector(".results_table.b_bookeditions_results_table")
    console.log(table)
    operation = "search"
    await getSearchBookeditionResults(form)
}

const getSearchBookeditionResults = async form => {
    try {
        console.log(
            {
                title: form.title.value,
                author: form.author.value,
                isbn: form.isbn.value,
                editor: form.editor_name.value,
                language: form.edition_language.value,
            }
        )
        results = await fetchRequest(
            "POST",
            "http://localhost:8080/general-catalog/search-bookeditions",
            {
                title: form.title.value,
                author: form.author.value,
                isbn: form.isbn.value,
                editor: form.editor_name.value,
                language: form.edition_language.value,
            }
        )
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

const deleteBrowseBookedition = async bookeditionId => {
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

const getEditBrowseBookEditionResults = async editedFields => {
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


const generateBrowseBookEditionsTableContent = (base = results, tab) => {
    console.log(tab)
    console.log(table)
    for (let i = 0; i < results.length; i++) {
        let result = base[i],
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

const generateBrowseBookEditionCatalogCard = async () => {
    let bookEdition = results[0],
        bookwork = bookEdition.bookwork,
        author = bookwork.author,
        authorName = `${author.firstName} ${author.lastName}`,
        editionCopiesMessage, bookEditionCopies,
        catalogCard = d.querySelector(".catalog_card.bookedition_catalog_card")
    
    catalogCard.classList.remove("hidden")

    catalogCard.querySelector(".bookwork_title").value = bookwork.title
    catalogCard.querySelector(".bookwork_author").value = authorName
    catalogCard.querySelector(".bookedition_isbn").value = bookEdition.isbn
    catalogCard.querySelector(".bookedition_editor").value = bookEdition.editor
    catalogCard.querySelector(".bookedition_edition_year").value = bookEdition.editionYear
    catalogCard.querySelector(".bookedition_language").value = bookEdition.language

    try {
        bookEditionCopies = await getEditionCopies(bookEdition.idBookEdition)
        editionCopiesMessage = `Author Book Works: ${bookEditionCopies.length}`
    } catch (error) {
        editionCopiesMessage = error.message
    }
    catalogCard.querySelector(".bookedition_copies").value = editionCopiesMessage
}

const getBrowseBookWork = () => {
    return author
}

const reasigneBrowseBookeditionValue = newBookEditionValue => {
    bookedition = newBookEditionValue
}

export { sendBookEditionForm }
export { getEditBrowseBookEditionResults }
export { deleteBrowseBookedition }
export { generateBrowseBookEditionsTableContent }
export { generateBrowseBookEditionCatalogCard }
export { getBrowseBookWork }
export { reasigneBrowseBookeditionValue }