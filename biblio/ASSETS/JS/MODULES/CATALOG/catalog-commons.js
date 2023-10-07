import { getEditAuthorResults } from "./cataloging.js"
import { getEditBookworkResults } from "./cataloging.js"
import { getEditNewEditionResults } from "./cataloging.js"

import { generateAuthorsTableContent } from "./cataloging.js"
import { generateBookworksTableContent } from "./cataloging.js"
import { generateNewBookeditionTableContent } from "./cataloging.js"

import { generateBookeditionsTableContent } from "./registering.js"
import { generateNewBookcopyTableContent } from "./registering.js"

import { showPage } from "./pages_navigation.js"

import { prepareAuthorEditionProcess } from "./cataloging.js"
import { prepareBookworkEditionProcess } from "./cataloging.js"
import { prepareNewEditionProcess } from "./cataloging.js"

import { getAuthor } from "./cataloging.js"
import { getBookwork } from "./cataloging.js"
import { reasigneAuthorValue } from "./cataloging.js"
import { reasigneBookworkValue } from "./cataloging.js"
import { reasigneNewEditionValue } from "./cataloging.js"


const d = document,
    selectResultBtn = d.querySelector(".modal_btns_container .select_result_btn"),
    confirmBtn = d.querySelector(".modal_btns_container .confirm_btn"),
    editBtn = d.querySelector(".modal_btns_container .edit_btn"),
    closeSymbol = d.querySelector(".close_symbol")


/* Global methods*/

const findCurrentPage = () => {
    const pages = d.querySelectorAll(".page")

    let currentPage,
        i = 0, found = false;

    do {
        if (!pages[i].classList.contains("hidden")) {
            currentPage = pages[i]
            found = true
        } else {
            i++
        }
    } while (!found && i < pages.length)

    return currentPage
}


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

const toggleNextPageBtn = (nextPageBtn, object) => {
    if (nextPageBtn) {

        const clonedBtn = nextPageBtn.cloneNode(true)
        nextPageBtn.parentNode.replaceChild(clonedBtn, nextPageBtn)

        const isDisabled = () => {
            return clonedBtn.classList.contains("disabled")
        }

        const nextPageBtnClickHandler = () => {
            if (!isDisabled()) {
                showPage(nextPageBtn.classList[2])
                enablePreviousPageBtn()
            }
        }

        if (!object) {
            if (!isDisabled()) {
                clonedBtn.classList.add("disabled")
            }
        } else {
            if (isDisabled()) {
                clonedBtn.classList.remove("disabled")
                clonedBtn.addEventListener("click", nextPageBtnClickHandler)
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


/* --- Clear layers methods */

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

const clearFormsData = () => {
    findCurrentPage().querySelectorAll(".form").forEach(form => {
        form.querySelectorAll("input").forEach(input => {
            if (input.type !== "submit" &&
                !input.hasAttribute("readonly")
            ) {
                input.value = ""
            }
        })
    })
}


/* API interaction methods */

/* --- Fetch method */

const fetchRequest = async (method, url, bodyContent) => {
    try {
        let options = {
            method: method,
            headers: {
                "Content-type": "application/json; charset=utf-8"
            },
            body: bodyContent ? JSON.stringify(bodyContent) : null
        },
            res = await fetch(url, options)

        if (!res.ok) throw res

        return await res.json()

    } catch (ex) {
        const errorData = await ex.json(),
            errorResponse = {
                status: errorData.status,
                statusText: errorData.statusText,
                message: errorData.message,
                validationErrors: errorData.validationErrors
            }
        throw errorResponse
    }
}

const joinParamsToURL = (baseURL, params) => {
    let queryParams = Object.keys(params)
        .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
        .join('&')

    return `${baseURL}?${queryParams}`
}

/* Error handling methods*/

const handleErrorMessages = (error, layer) => {
    if (error.status === 422) {
        error.validationErrors.forEach(er => {
            layer.querySelector(`.error_message.${er.field}`).classList.add("active")
            layer.querySelector(`.error_message.${er.field}`).textContent = er.message
        })
    } else {
        layer.querySelector('.error_message.general_error').classList.add("active")
        layer.querySelector('.error_message.general_error').textContent = error.message
    }
}

const clearErrorMessages = () => {
    d.querySelectorAll(".error_message").forEach(er => {
        if (er.classList.contains("active")) {
            er.classList.remove("active")
        }
        er.textContent = ""
    })
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
        d.querySelector(".modal_btns_container .select_btn").classList.remove("hidden")

        if (tableContainsClass(table, "authors_results_table")) {
            selectResultBtn.textContent = "Select author";
        } else if (tableContainsClass(table, "bookworks_results_table")) {
            selectResultBtn.textContent = "Select book work";
        } else if (tableContainsClass(table, "newEdition_results_table")) {
            selectResultBtn.textContent = "Save new edition";
        } else if (tableContainsClass(table, "editions_results_table")) {
            selectResultBtn.textContent = "Select book edition";
        } else if (tableContainsClass(table, "newBookCopy_results_table")) {
            selectResultBtn.textContent = "Save new copy";
        }
    } else if (operation === "create") {
        d.querySelector(".modal_btns_container .create_btns").classList.remove("hidden")
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

const enableModalActions = (results, resultsType, operation, table) => {

    if (operation === "search") {
        enableOptionChangigng()
        toggleCloseModalBtn(resultsType, operation, table, true)
        enableSelectResultBtn(results, resultsType, operation, table)
    } else if (operation === "create") {
        toggleCloseModalBtn(resultsType, operation, table, false)
        enableCreateBtns(results, resultsType, operation, table)
    }
}

const enableOptionChangigng = () => {
    const checkboxes = document.querySelectorAll('input.result_option');

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener("change", e => {
            checkboxes.forEach(cb => {
                if (cb !== checkbox) {
                    cb.checked = false;
                }
            });
            changeBtnState(e.target);
        });
    });
}

const changeBtnState = checkbox => {
    if (checkbox.checked === true) {
        selectResultBtn.removeAttribute("disabled")
    } else {
        selectResultBtn.setAttribute("disabled", true)
    }
}

let selectResultBtnClickHandler, confirmBtnClickHandler, editBtnClickHandler

const enableSelectResultBtn = (results, resultsType, operation, table) => {
    selectResultBtnClickHandler = function () {
        executeSelectResultBtnListener(results, resultsType, operation, table)
    }

    selectResultBtn.addEventListener("click", selectResultBtnClickHandler)

}

const enableCreateBtns = (results, resultsType, operation, table) => {
    if (confirmBtn) {

        confirmBtnClickHandler = function () {
            executeConfirmBtnListener(results, resultsType, operation, table)
        }

        confirmBtn.addEventListener("click", confirmBtnClickHandler)
    }
    if (editBtn) {

        editBtnClickHandler = function () {
            executeEditBtnListener(results, resultsType, operation, table)
        }

        editBtn.addEventListener("click", editBtnClickHandler)
    }
}

const executeSelectResultBtnListener = (results, resultsType, operation, table) => {
    saveResult(results, resultsType)
    endProcess(resultsType, operation, table)
}

const executeConfirmBtnListener = (results, resultsType, operation, table) => {
    saveResult(results, resultsType)
    endProcess(resultsType, operation, table)
}

const executeEditBtnListener = (results, resultsType, operation, table) => {
    console.log("Executing edit btn listener")
    saveResult(results, resultsType)
    prepareEditonProcess(results, resultsType, operation, table)
}

const saveResult = (results, resultsType) => {
    if (resultsType === "author") {
        reasigneAuthorValue(results[findSelectedResult()])
    } else if (resultsType === "bookwork") {
        reasigneBookworkValue(results[findSelectedResult()])
    } else if (resultsType === "newEdition") {
        reasigneNewEditionValue(results[findSelectedResult()])
    }
}

const prepareEditonProcess = (results, resultsType, operation, table) => {
    const tableResultsRow = table.querySelector(".results_row"),
        cells = tableResultsRow.querySelectorAll("td")

    console.log(cells)

    clearPrintedReults(resultsType)

    if (resultsType === "author") {
        prepareAuthorEditionProcess(cells)
    } else if (resultsType === "bookwork") {
        prepareBookworkEditionProcess(cells)
    } else if (resultsType === "newEdition") {
        prepareNewEditionProcess(cells)
    }

    confirmBtn.removeEventListener("click", confirmBtnClickHandler)

    confirmBtnClickHandler = function () {
        console.log("AAAA")
        confirmEdition(results, resultsType, operation, table)
    }
    confirmBtn.addEventListener("click", confirmBtnClickHandler)
}

const confirmEdition = async (results, resultsType, operation, table) => {
    const tbody = table.querySelector(".results_table_body"),
        errorMessageRow = tbody.querySelector(".error_message_row"),
        editedFields = [...tbody.querySelectorAll("td input")].map(input => input.value)

    console.log(editedFields)
    console.log("CONFIRMING")

    let error
    try {
        if (resultsType === "author") {
            results = await getEditAuthorResults(editedFields)
        } else if (resultsType === "bookwork") {
            results = await getEditBookworkResults(editedFields)
        } else if (resultsType === "newEdition") {
            results = await getEditNewEditionResults(editedFields)
        }
        console.log(results)
    } catch (ex) {
        console.log(error)
        error = ex
    }

    clearErrorMessages()

    if (error) {
        errorMessageRow.classList.remove("hidden")
        handleErrorMessages(error, tbody)
    } else {
        if (!errorMessageRow.classList.contains("hidden")) {
            errorMessageRow.classList.add("hidden")
        }
        saveResult(results, resultsType)
        endProcess(resultsType, operation, table)
    }
}

const findSelectedResult = () => {
    const checkboxes = d.querySelectorAll('input.result_option');

    if (checkboxes.length > 0) {
        for (const checkbox of checkboxes) {
            if (checkbox.checked === true) {
                return checkbox.value
            }
        }
    } else {
        return 0
    }
}

const endProcess = (resultsType, operation, table) => {
    printSelectedResult(resultsType)
    closeModal(resultsType, operation, table)
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

let closeSymbolClickHandler

const toggleCloseModalBtn = (resultsType, operation, table, enable) => {
    closeSymbolClickHandler = function () {
        closeModal(resultsType, operation, table)
    }

    if (enable) {
        if (!closeSymbol.classList.contains("active")) {
            closeSymbol.classList.add("active")
            closeSymbol.addEventListener("click", closeSymbolClickHandler)
        }
    } else {
        closeSymbol.classList.remove("active")
    }
}


const closeModal = (resultsType, operation, table) => {
    if (table) {
        let tableBody = table.querySelector(".results_table_body"),
            errorMessageRow = tableBody.querySelector(".error_message_row")

        tableBody.innerHTML = ""
        tableBody.appendChild(errorMessageRow)

        table.classList.add("hidden")

        if (table.querySelector("th.select_column")) {
            table.querySelector("th.select_column").remove()
        }

        table = ""
    }

    modal.classList.add("hidden")
    selectResultBtn.setAttribute("disabled", true)

    if (operation === "search") {
        d.querySelector(".modal_btns_container .select_btn").classList.add("hidden")
    } else if (operation === "create") {
        d.querySelector(".modal_btns_container .create_btns").classList.add("hidden")
    }

    removeModalElementsListeners()
    toggleCloseModalBtn(resultsType, operation, table, false)
    toggleNextPageChanging(resultsType)
    clearFormsData(findCurrentPage())
}

const removeModalElementsListeners = () => {
    closeSymbol.removeEventListener("click", closeSymbolClickHandler)
    selectResultBtn.removeEventListener("click", selectResultBtnClickHandler)
    confirmBtn.removeEventListener("click", confirmBtnClickHandler)
    editBtn.removeEventListener("click", editBtnClickHandler)
}

export { findCurrentPage }

export { enablePreviousPageBtn }
export { toggleNextPageChanging }
export { clearFormsData }
export { clearPrintedReults }

export { fetchRequest }
export { joinParamsToURL }

export { handleErrorMessages }
export { clearErrorMessages }

export { showSearchResults }
export { enableModalActions }
export { saveResult }
export { endProcess }