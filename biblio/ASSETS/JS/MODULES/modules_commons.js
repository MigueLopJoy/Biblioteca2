import { displayCatalogingMainPage } from "./CATALOG/display_pages.js"
import { displayRegisteringMainPage } from "./CATALOG/display_pages.js"
import { displayReadersRegisteringMainPage } from "./CATALOG/display_pages.js"
import { displaySearchReadersMainPage } from "./CATALOG/display_pages.js"

import { printSelectedResult } from "./CATALOG/catalog-commons.js"
import { toggleNextPageChanging } from "./CATALOG/catalog-commons.js"
import { clearPrintedReults } from "./CATALOG/catalog-commons.js"

import { getEditAuthorResults } from "./CATALOG/cataloging.js"
import { getEditBookworkResults } from "./CATALOG/cataloging.js"
import { getEditNewEditionResults } from "./CATALOG/cataloging.js"
import { prepareAuthorEditionProcess } from "./CATALOG/cataloging.js"
import { prepareBookworkEditionProcess } from "./CATALOG/cataloging.js"
import { prepareNewEditionEditionProcess } from "./CATALOG/cataloging.js"
import { reasigneAuthorValue } from "./CATALOG/cataloging.js"
import { reasigneBookworkValue } from "./CATALOG/cataloging.js"
import { reasigneNewEditionValue } from "./CATALOG/cataloging.js"

import { getEditNewCopyResults } from "./CATALOG/registering.js"
import { prepareNewBookcopyEditionProcess } from "./CATALOG/registering.js"
import { reasigneBookeditionValue } from "./CATALOG/registering.js"
import { reasigneNewBookcopyValue } from "./CATALOG/registering.js"

import { getEditNewReaderResults } from "./READERS/readers_registering.js"
import { prepareNewReaderEditionProcess } from "./READERS/readers_registering.js"


/* Global methods */

const d = document,
    selectBtnContainer = d.querySelector(".modal_btns_container .select_btn"),
    createBtnsContainer = d.querySelector(".modal_btns_container .create_btns"),
    endingBtnContainer = d.querySelector(".modal_btns_container .ending_btn"),
    confirmBtn = d.querySelector(".modal_btns_container .confirm_btn"),
    editBtn = d.querySelector(".modal_btns_container .edit_btn"),
    endingBtn = d.querySelector(".modal_btns_container .ending_btn"),
    selectResultBtn = d.querySelector(".modal_btns_container .select_result_btn"),
    closeSymbol = d.querySelector(".close_symbol"),
    successMessage = d.querySelector(".success_message")

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

/* Modal actions */

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

let closeSymbolClickHandler
const toggleCloseModalBtn = (resultsType, operation, table, enable) => {

    closeSymbol.removeEventListener("click", closeSymbolClickHandler)

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
    if (resultsType !== "newEdition" && resultsType !== "newCopy" && resultsType != "newReader") {
        endProcess(resultsType, operation, table)
    } else {
        displaySuccessMessage(resultsType)
    }
}

const executeEditBtnListener = (results, resultsType, operation, table) => {
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
    } else if (resultsType === "bookedition") {
        reasigneBookeditionValue(results[findSelectedResult()])
    } else if (resultsType === "newBookcopy") {
        reasigneNewBookcopyValue(results[findSelectedResult()])
    } else if (resultsType === "newReader") {
        reasigneNewReaderValue(results[findSelectedResult()])
    }
}

const prepareEditonProcess = (results, resultsType, operation, table) => {
    const tableResultsRow = table.querySelector(".results_row"),
        cells = tableResultsRow.querySelectorAll("td")

    clearPrintedReults(resultsType)

    if (resultsType === "author") {
        prepareAuthorEditionProcess(cells)
    } else if (resultsType === "bookwork") {
        prepareBookworkEditionProcess(cells)
    } else if (resultsType === "newEdition") {
        prepareNewEditionEditionProcess(cells)
    } else if (resultsType === "bookedition") {
        prepareBookworkEditionProcess(cells)
    } else if (resultsType === "newBookCopy") {
        prepareNewBookcopyEditionProcess(cells)
    } else if (resultsType === "newReader") {
        prepareNewReaderEditionProcess(cells)
    }

    confirmBtn.removeEventListener("click", confirmBtnClickHandler)

    confirmBtnClickHandler = function () {
        confirmEdition(results, resultsType, operation, table)
    }
    confirmBtn.addEventListener("click", confirmBtnClickHandler)
}



const confirmEdition = async (results, resultsType, operation, table) => {
    const tbody = table.querySelector(".results_table_body"),
        errorMessageRow = tbody.querySelector(".error_message_row"),
        editedFields = [...tbody.querySelectorAll("td input")].map(input => input.value)

    let error
    try {
        if (resultsType === "author") {
            results = await getEditAuthorResults(editedFields)
        } else if (resultsType === "bookwork") {
            results = await getEditBookworkResults(editedFields)
        } else if (resultsType === "newEdition") {
            results = await getEditNewEditionResults(editedFields)
        } else if (resultsType === "newBookCopy") {
            results = await getEditNewCopyResults(editedFields)
        } else if (resultsType === "newReader") {
            results = await getEditNewReaderResults(editedFields)
        }

    } catch (ex) {
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
        executeConfirmBtnListener(results, resultsType, operation, table)
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

const closeModal = (resultsType, operation, table) => {
    if (table) {
        let tableBody = table.querySelector(".results_table_body"),
            errorMessageRow = tableBody.querySelector(".error_message_row")

        tableBody.innerHTML = ""
        if (errorMessageRow) {
            tableBody.appendChild(errorMessageRow)
        }

        table.classList.add("hidden")

        if (table.querySelector("th.select_column")) {
            table.querySelector("th.select_column").remove()
        }

        table = ""
    }

    modal.classList.add("hidden")
    selectResultBtn.setAttribute("disabled", true)

    if (operation === "search") {
        selectBtnContainer.classList.add("hidden")
    } else if (operation === "create") {
        createBtnsContainer.classList.add("hidden")
    } else if (operation === "ending") {
        endingBtnContainer.classList.add("hidden")
    }

    removeModalElementsListeners()
    toggleCloseModalBtn(resultsType, operation, table, false)
    toggleNextPageChanging(resultsType)
    clearFormsData(findCurrentPage())
}

let endingBtnClickHandler
const displaySuccessMessage = (resultsType) => {
    successMessage.classList.add("active")
    if (resultsType === "newEdition") {
        successMessage.textContent = "New Edition Created Successfully"
    } else if (resultsType === "newCopy") {
        successMessage.textContent = "New Copy Created Successfully"
    } else if (resultsType === "newReader") {
        successMessage.textContent = "New Reader Created Successfully"
    }

    createBtnsContainer.classList.add("hidden")
    endingBtnContainer.classList.remove("hidden")
    endingBtnClickHandler = function () {
        d.getElementById("main-content").textContent = ""
        if (resultsType === "newEdition") {
            displayCatalogingMainPage()
        } else if (resultsType === "newCopy") {
            displayRegisteringMainPage()
        } else if (resultsType === "newReader") {
            displayReadersRegisteringMainPage()
        }
    }
    endingBtn.addEventListener("click", endingBtnClickHandler)
}

const removeModalElementsListeners = () => {
    closeSymbol.removeEventListener("click", closeSymbolClickHandler)
    selectResultBtn.removeEventListener("click", selectResultBtnClickHandler)
    confirmBtn.removeEventListener("click", confirmBtnClickHandler)
    editBtn.removeEventListener("click", editBtnClickHandler)
    endingBtn.removeEventListener("click", endingBtnClickHandler)
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
    console.log(error)
    if (error.status === 422) {
        error.validationErrors.forEach(er => {
            let validationErrorMessage = layer.querySelector(`.error_message.${er.field}`)
            validationErrorMessage.classList.add("active")
            validationErrorMessage.textContent = er.message
        })
    } else {
        let generalErrorMessage = layer.querySelector('.error_message.general_error')
        generalErrorMessage.classList.add("active")
        generalErrorMessage.textContent = error.message
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


export { findCurrentPage }
export { enableModalActions }
export { fetchRequest }
export { joinParamsToURL }
export { handleErrorMessages }
export { clearErrorMessages }
export { clearFormsData }