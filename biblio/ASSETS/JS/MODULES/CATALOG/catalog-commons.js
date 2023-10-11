import { findCurrentPage } from "./../modules_commons.js"
import { fetchRequest } from "./../modules_commons.js"
import { joinParamsToURL } from "./../modules_commons.js"
import { handleErrorMessages } from "./../modules_commons.js"
import { clearErrorMessages } from "./../modules_commons.js"
import { clearFormsData } from "./../modules_commons.js"

import { getEditAuthorResults } from "./cataloging.js"
import { getEditBookworkResults } from "./cataloging.js"
import { getEditNewEditionResults } from "./cataloging.js"

import { generateAuthorsTableContent } from "./cataloging.js"
import { generateBookworksTableContent } from "./cataloging.js"
import { generateNewBookeditionTableContent } from "./cataloging.js"

import { generateBookeditionsTableContent } from "./registering.js"
import { generateNewBookcopyTableContent } from "./registering.js"

import { prepareAuthorEditionProcess } from "./cataloging.js"
import { prepareBookworkEditionProcess } from "./cataloging.js"
import { prepareNewEditionProcess } from "./cataloging.js"

import { getAuthor } from "./cataloging.js"
import { getBookwork } from "./cataloging.js"
import { reasigneAuthorValue } from "./cataloging.js"
import { reasigneBookworkValue } from "./cataloging.js"
import { reasigneNewEditionValue } from "./cataloging.js"

import { showPage } from "./display_pages.js"
import { displayCatalogingMainPage } from "./display_pages.js"
import { displayRegisteringMainPage } from "./display_pages.js"

const d = document,
    selectBtnContainer = d.querySelector(".modal_btns_container .select_btn"),
    createBtnsContainer = d.querySelector(".modal_btns_container .create_btns"),
    selectResultBtn = d.querySelector(".modal_btns_container .select_result_btn")



/* Page's interaction methods*/

/* --- Page change btns methods*/

const enablePreviousPageBtn = () => {
    let previousPageBtn = findCurrentPage().querySelector(".change_page_container .previous_page")

    const clickHandler = () => {
        showPage(previousPageBtn.classList[2])
        previousPageBtn.removeEventListener("click", clickHandler)
        enablePreviousPageBtn()
    }

    if (previousPageBtn) {
        previousPageBtn.addEventListener("click", clickHandler)
    }
}

let nextPageBtnClickHandler
const toggleNextPageBtn = (nextPageBtn, object) => {
    if (nextPageBtn) {

        nextPageBtn.removeEventListener("click", nextPageBtnClickHandler)

        const isDisabled = () => {
            return nextPageBtn.classList.contains("disabled")
        }

        nextPageBtnClickHandler = function () {
            if (!isDisabled()) {
                showPage(nextPageBtn.classList[2])
                enablePreviousPageBtn()
            }
        }

        if (!object) {
            if (!isDisabled()) {
                nextPageBtn.classList.add("disabled")
            }
        } else {
            if (isDisabled()) {
                nextPageBtn.classList.remove("disabled")
                nextPageBtn.addEventListener("click", nextPageBtnClickHandler)
            }
        }
    }
}

const togglePageLinks = (pageLinks, object) => {
    for (let i = 0; i < pageLinks.length; i++) {
        let pageLink = pageLinks[i],
            condition = pageLink.classList.contains("enabled")

        if (!object) {
            if (condition) {
                pageLink.classList.remove("enabled")
            }
        } else {
            if (!condition) {
                pageLink.classList.add("enabled")
            }
        }
    }
}

const toggleNextPageChanging = resultsType => {
    const nextPageBtn = findCurrentPage().querySelector(".change_page_container .next_page"),
        pageLinks = d.querySelectorAll(".page_link")

    if (resultsType === "author") {
        toggleNextPageBtn(nextPageBtn, getAuthor())
        if (!getAuthor()) {
            let links = [pageLinks[1], pageLinks[2]]
            togglePageLinks(links, getAuthor())
        } else {
            togglePageLinks([pageLinks[1]], getAuthor())
        }
    } else if (resultsType === "bookwork") {
        toggleNextPageBtn(nextPageBtn, getBookwork())
        togglePageLinks([pageLinks[2]])
    } else if (resultsType === "bookedition") {
        toggleNextPageBtn(nextPageBtn, getBookEdition())
        togglePageLinks([pageLinks[1]], getBookEdition())
    }
}

const clearPrintedReults = resultsType => {
    if (resultsType === "author") {
        d.querySelectorAll(".selected_author").forEach(el => {
            if (el.hasAttribute("readonly")) {
                el.value = ""
            } else {
                el.textContent = "Author: "
            }
        })
    } else if (resultsType === "bookwork") {
        d.querySelectorAll(".selected_bookwork").forEach(el => {
            if (el.hasAttribute("readonly")) {
                el.value = ""
            } else {
                el.textContent = "Book work: "
            }
        })
    } else if (resultsType === "bookedition") {
        d.querySelectorAll(".selected_bookedition").forEach(el => {
            if (el.hasAttribute("readonly")) {
                el.value = ""
            } else {
                el.textContent = "Book work: "
            }
        })
    }
}

/* Handle results methods */

const tableContainsClass = (table, className) => {
    return table.classList.contains(className)
}

const showSearchResults = (operation, table) => {
    const modal = d.getElementById("modal")

    modal.classList.remove("hidden")
    table.classList.remove("hidden")

    generaTableContent(table)

    if (operation === "search") {
        selectBtnContainer.classList.remove("hidden")

        if (tableContainsClass(table, "authors_results_table")) {
            selectResultBtn.textContent = "Select author"
        } else if (tableContainsClass(table, "bookworks_results_table")) {
            selectResultBtn.textContent = "Select book work"
        } else if (tableContainsClass(table, "newEdition_results_table")) {
            selectResultBtn.textContent = "Save new edition"
        } else if (tableContainsClass(table, "editions_results_table")) {
            selectResultBtn.textContent = "Select book edition"
        } else if (tableContainsClass(table, "newBookCopy_results_table")) {
            selectResultBtn.textContent = "Save new copy"
        } 
    } else if (operation === "create") {
        createBtnsContainer.classList.remove("hidden")
    }
}

const generaTableContent = table => {
    if (tableContainsClass(table, "authors_results_table")) {
        generateAuthorsTableContent()
    } else if (tableContainsClass(table, "bookworks_results_table")) {
        generateBookworksTableContent()
    } else if (tableContainsClass(table, "newEdition_results_table")) {
        generateNewBookeditionTableContent()
    } else if (tableContainsClass(table, "editions_results_table")) {
        generateBookeditionsTableContent()
    } else if (tableContainsClass(table, "newBookCopy_results_table")) {
        generateNewBookcopyTableContent()
    }
}


const printSelectedResult = resultsType => {
    if (resultsType === "author") {
        d.querySelectorAll(".selected_result_holder .selected_author").forEach(el => {
            el.textContent += `${getAuthor().firstName} ${getAuthor().lastName}`
        })
        d.querySelector(".form .selected_author").value = `${getAuthor().firstName} ${getAuthor().lastName}`
    } else if (resultsType === "bookwork") {
        d.querySelectorAll(".selected_result_holder .selected_bookwork").forEach(el => {
            el.textContent += `${getBookwork().title}`
        })
        d.querySelector(".form .selected_bookwork").value = getBookwork().title
    }
}


export { enablePreviousPageBtn }
export { toggleNextPageChanging }
export { clearFormsData }
export { clearPrintedReults }
export { printSelectedResult }
export { showSearchResults }
