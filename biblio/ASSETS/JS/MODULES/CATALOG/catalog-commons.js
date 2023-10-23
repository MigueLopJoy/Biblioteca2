import { findCurrentPage } from "./../modules_commons.js"

import { generateAuthorsTableContent } from "./cataloging.js"
import { generateBookworksTableContent } from "./cataloging.js"
import { generateNewBookeditionTableContent } from "./cataloging.js"
import { generateAuthorCatalogCard } from "./cataloging.js"

import { generateBookeditionsTableContent } from "./registering.js"
import { generateNewBookcopyTableContent } from "./registering.js"

import { generateBrowseAuthorsTableContent } from "./BROWSE/authors_catalog.js"

import { getAuthor } from "./cataloging.js"
import { getBookwork } from "./cataloging.js"
import { getBookedition } from "./registering.js"

import { showPage } from "./display_pages.js"

import { displaySuccessMessage } from "./../modules_commons.js"

const d = document,
    selectBtnContainer = d.querySelector(".modal_btns_container .select_btn"),
    createBtnContainer = d.querySelector(".modal_btns_container .create_btn"),
    selectResultBtn = d.querySelector(".modal_btns_container .select_result_btn")

/* Page's interaction methods*/

/* --- Page change btns methods*/

const enablePreviousPageBtn = () => {
    let previousPageBtn = findCurrentPage().querySelector(".change_page_container .previous_page")

    const clickHandler = () => {
        showPage(previousPageBtn.classList[2])
        previousPageBtn.removeEventListener("click", clickHandler)
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
            nextPageBtn.classList.remove("disabled")
            nextPageBtn.addEventListener("click", nextPageBtnClickHandler)
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
        togglePageLinks([pageLinks[2]], getBookwork())
    } else if (resultsType === "bookedition") {
        toggleNextPageBtn(nextPageBtn, getBookedition())
        togglePageLinks([pageLinks[1]], getBookedition())
    }
}

const clearPrintedReults = resultsType => {
    switch (resultsType) {
        case "author":
            d.querySelectorAll(".selected_author").forEach(el => {
                if (el.hasAttribute("readonly")) {
                    el.value = ""
                } else {
                    el.textContent = "Author: "
                }
            })
            break;
        case "bookwork":
            d.querySelectorAll(".selected_bookwork").forEach(el => {
                if (el.hasAttribute("readonly")) {
                    el.value = ""
                } else {
                    el.textContent = "Book work: "
                }
            })
            break;
        case "bookedition":
            d.querySelectorAll(".selected_bookedition").forEach(el => {
                if (el.hasAttribute("readonly")) {
                    el.value = ""
                } else {
                    el.textContent = "Book edition: "
                }
            })
            d.querySelector(".form .selected_title").value = ""
            d.querySelector(".form .selected_author").value = ""
            d.querySelector(".form .selected_editorAndYear").value = ""
            d.querySelector(".form .selected_isbn").value = ""
            break;
        default:
            break;
    }
}

/* Handle results methods */

const tableContainsClass = (table, className) => {
    return table.classList.contains(className)
}

const showSearchResults = (table) => {
    const modal = d.getElementById("modal")
    modal.classList.remove("hidden")
    table.classList.remove("hidden")

    generaTableContent(table)
    selectBtnContainer.classList.remove("hidden")

    if (tableContainsClass(table, "authors_results_table")) {
        selectResultBtn.textContent = "Select author"
    } else if (tableContainsClass(table, "bookworks_results_table")) {
        selectResultBtn.textContent = "Select book work"
    } else if (tableContainsClass(table, "bookeditions_results_table")) {
        selectResultBtn.textContent = "Select book edition"
    }
}

const showCatalogCard = (resultsType, operation) => {
    const modal = d.getElementById("modal")
    modal.classList.remove("hidden")

    generateCatalogCard()

    if (resultsType === "") {
        selectResultBtn.textContent = "Save New Author"
    } else if (resultsType === "newEdition") {
        selectResultBtn.textContent = "Save New Edition"
    } else if (resultsType === "newBookcopy") {
        selectResultBtn.textContent = "Save New Copy"
    } else if ()

        if (operation === "create") displaySuccessMessage(resultsType)
    createBtnContainer.classList.remove("hidden")
}

const generaTableContent = table => {
    if (tableContainsClass(table, "authors_results_table")) {
        generateAuthorsTableContent()
    } else if (tableContainsClass(table, "bookworks_results_table")) {
        generateBookworksTableContent()
    } else if (tableContainsClass(table, "newEdition_results_table")) {
        generateNewBookeditionTableContent()
    } else if (tableContainsClass(table, "bookeditions_results_table")) {
        generateBookeditionsTableContent()
    } else if (tableContainsClass(table, "newBookcopy_results_table")) {
        generateNewBookcopyTableContent()
    } else if (tableContainsClass(table, "b_authors_results_table")) {
        generateBrowseAuthorsTableContent()
    }
}

const generateCatalogCard = (form) => {
    if (form.classList.contains("author_form")) {
        generateAuthorCatalogCard()
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
    } else if (resultsType === "bookedition") {
        d.querySelectorAll(".selected_result_holder .selected_bookwork").forEach(el => {
            el.textContent +=
                `${getBookedition().bookWork.title} / 
                ${getBookedition().bookWork.author.firstName} ${getBookedition().bookWork.author.lastName}`
        })
        d.querySelectorAll(".selected_result_holder .selected_bookedition").forEach(el => {
            el.textContent +=
                `${getBookedition().editor}: ${getBookedition().editionYear} . - ${getBookedition().isbn}`
        })
        d.querySelector(".form .selected_title").value = `${getBookedition().bookWork.title}`
        d.querySelector(".form .selected_author").value =
            `${getBookedition().bookWork.author.firstName} ${getBookedition().bookWork.author.lastName}`
        d.querySelector(".form .selected_editorAndYear").value =
            `${getBookedition().editor}: ${getBookedition().editionYear}`
        d.querySelector(".form .selected_isbn").value =
            `${getBookedition().isbn}`
    }
}


export { enablePreviousPageBtn }
export { toggleNextPageChanging }
export { clearPrintedReults }
export { printSelectedResult }
export { showSearchResults }
export { showCatalogCard }
