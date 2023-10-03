import { enableWindowNavLinkBtns } from "./catalog-commons.js"
import { showPage } from "./catalog-commons.js"
import { findCurrentPage } from "./catalog-commons.js"

import { toggleNextPageChanging } from "./catalog-commons.js"
import { clearFormsData } from "./catalog-commons.js"
import { clearPrintedReults } from "./catalog-commons.js"

import { fetchRequest } from "./catalog-commons.js"

import { handleErrorMessages } from "./catalog-commons.js"
import { clearErrorMessages } from "./catalog-commons.js"


const d = document,
    searchEditionForm = d.querySelector(".form.edition_form.search"),
    createBookcopyForm = d.querySelector(".form.bookcopy_form.create"),
    editionsResultsTable = d.querySelector(".results_table.editions_results_table"),
    bookcopiesResultsTable = d.querySelector(".results_table.bookcopies_results_table"),
    modal = d.getElementById("modal"),
    selectResultBtn = d.querySelector(".modal_btns_container .select_result_btn"),
    confirmBtn = d.querySelector(".modal_btns_container .confirm_btn"),
    editBtn = d.querySelector(".modal_btns_container .edit_btn")

let bookedition, results, error, table, resultsType, operation

d.addEventListener("submit", async e => {
    e.preventDefault();

    let currentPage = findCurrentPage()

    clearErrorMessages()

    if (currentPage.classList.contains("bookedition_page")) {
        await runBookEditionProcess(e.target)
    } else if (currentPage.classList.contains("bookcopy_page")) {
        await runBookCopyProcess(e.target)
    }

    if (error) {
        handleErrorMessages(e.target)
        error = null
        toggleNextPageChanging()
        clearFormsData(findCurrentPage())
    } else {
        showSearchResults()
        enableModalActions()
    }
})

const runBookEditionProcess = async form => {
    author = ""
    table = authorsResultsTable;
    resultsType = "author";
    clearPrintedReults(resultsType)

    if (form === searchAuthorForm) {
        operation = "search"
        await getSearchAuthorResults(form)
    } else if (form === createAuthorForm) {
        operation = "create"
        await getCreateAuthorResults(form)
    }
}

const getSearchBookeditionResults = async form => {
    await fetchRequest(
        "POST",
        "http://localhost:8080/general-catalog/search-bookeditions",
        {
            title: form.title,
            author: form.author,
            isbn: form.isbn.value,
            editor: form.editor_name.value,
            language: form.edition_language.value,
        }
    )
}

const getCreateBookCopyResults = async form => {
    await fetchRequest(
        "POST",
        "http://localhost:8080/bookcopies/save-bookcopy",
        {
            registrationNumber: form.registration_number.value,
            signature: form.signature.value,
            bookedition: {
                isbn: bookedition.isbn.value,
                editor: bookedition.editor.value,
                editionYear: bookedition.editionYear.value,
                language: bookedition.language.value,
                bookWork: {
                    title: bookedition.bookwork.title,
                    author: {
                        firstName: bookedition.bookwork.author.firstName,
                        lastName: bookedition.bookwork.author.lastName
                    },
                    publicationYear: bookedition.bookwork.publicationYear
                }
            }
        }
    )
}

enableWindowNavLinkBtns()
showPage("bookedition_page")
