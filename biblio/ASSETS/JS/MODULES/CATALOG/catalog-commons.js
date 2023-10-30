import {
    findCurrentPage,
    toggleSymbol
} from "./../modules_commons.js"

import { displaySuccessMessage } from "../api_messages_handler.js"

import { showPage } from "../../display_pages.js"

import {
    getAuthor,
    generateAuthorCatalogCard,
    generateAuthorsTableContent
} from "./authors_catalog.js"

import {
    generateBookWorkCatalogCard,
    generateBookworksTableContent,
} from "./bookworks_catalog.js"

import {
    generateBookEditionsTableContent,
    generateBookEditionCatalogCard,
} from "./bookeditions_catalog.js"

import { generateBookCopyCatalogCard } from "./bookcopies_catalog.js"


const d = document

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

/* 
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
*/

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

const showSearchResults = table => {
    renderModal()
    generaTableContent(table)
    table.classList.remove("hidden")
    d.querySelector(".select_results_container").classList.remove("hidden")
}

const showCatalogCard = (results, resultsType, catalogCard) => {
    console.log(results)
    renderModal()
    if (results.author) generateCatalogCard(results.author, resultsType)
    else generateCatalogCard(results, resultsType)
    displaySuccessMessage(results)
    catalogCard.classList.remove("hidden")
    d.querySelector(".confirm_creation_container").classList.remove("hidden")
}

const renderModal = () => {
    const modal = d.getElementById("modal")
    modal.classList.remove("hidden")
    modal.style.display = "flex"
    modal.style.justifyContent = "center"
    modal.style.alignItems = "center"
}

const generaTableContent = async table => {
    if (table.classList.contains("authors_results_table")) {
        generateAuthorsTableContent()
    } else if (table.classList.contains("bookworks_results_table")) {
        generateBookworksTableContent()
    } else if (table.classList.contains("bookeditions_results_table")) {
        generateBookEditionsTableContent()
    }
}

const generateCatalogCard = (results, resultsType) => {
    switch (resultsType) {
        case "author":
            generateAuthorCatalogCard(results)
            break
        case "bookwork":
            generateBookWorkCatalogCard(results)
            break
        case "bookedition":
            generateBookEditionCatalogCard(results)
            break
        case "bookcopy":
            generateBookCopyCatalogCard(results)
            break
        default:
            break
    }
    toggleSearchRelatedObjectsSymbolActivation()
}

const toggleSearchRelatedObjectsSymbolActivation = () => {
    const searchRelatedObjectsSymbol = d.querySelector(".search_related_objects"),
        inputs = d.querySelectorAll(".catalog_card .form input")

    if (inputs[inputs.length - 1].value.substring(0, 2) !== "No") {
        toggleSymbol(searchRelatedObjectsSymbol, true)
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
// export { toggleNextPageChanging }
export { clearPrintedReults }
export { printSelectedResult }
export { showSearchResults }
export { showCatalogCard }
