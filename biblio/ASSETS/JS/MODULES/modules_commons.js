import { displayCatalogingMainPage } from "./CATALOG/display_pages.js"
import { displayRegisteringMainPage } from "./CATALOG/display_pages.js"
import { displayReadersRegisteringMainPage } from "./CATALOG/display_pages.js"

import { printSelectedResult } from "./CATALOG/catalog-commons.js"
import { toggleNextPageChanging } from "./CATALOG/catalog-commons.js"
import { clearPrintedReults } from "./CATALOG/catalog-commons.js"

import { getEditAuthorResults } from "./CATALOG/cataloging.js"
import { getEditBookworkResults } from "./CATALOG/cataloging.js"
import { getEditNewEditionResults } from "./CATALOG/cataloging.js"
import { deleteAuthor } from "./CATALOG/cataloging.js"
import { deleteBookwork } from "./CATALOG/cataloging.js"
import { deleteNewBookedition } from "./CATALOG/cataloging.js"
import { prepareAuthorEditionProcess } from "./CATALOG/cataloging.js"
import { prepareBookworkEditionProcess } from "./CATALOG/cataloging.js"
import { prepareNewEditionEditionProcess } from "./CATALOG/cataloging.js"
import { reasigneAuthorValue } from "./CATALOG/cataloging.js"
import { reasigneBookworkValue } from "./CATALOG/cataloging.js"
import { reasigneNewEditionValue } from "./CATALOG/cataloging.js"

import { getEditNewCopyResults, getNewBookcopy } from "./CATALOG/registering.js"
import { deleteNewCopy } from "./CATALOG/registering.js"
import { prepareNewBookcopyEditionProcess } from "./CATALOG/registering.js"
import { reasigneBookeditionValue } from "./CATALOG/registering.js"
import { reasigneNewBookcopyValue } from "./CATALOG/registering.js"

import { getEditNewReaderResults } from "./READERS/readers_registering.js"
import { prepareNewReaderEditionProcess } from "./READERS/readers_registering.js"
import { reasigneNewReaderValue } from "./READERS/readers_registering.js"

import { initializeBrowseAuthorsFormSubmit } from "./CATALOG/BROWSE/authors_catalog.js"
import { prepareBrowseAuthorEditionProcess } from "./CATALOG/BROWSE/authors_catalog.js"
import { getEditBrowseAuthorResults } from "./CATALOG/BROWSE/authors_catalog.js"
import { deleteBrowseAuthor } from "./CATALOG/BROWSE/authors_catalog.js"
import { reasigneBrowseAuthorValue } from "./CATALOG/BROWSE/authors_catalog.js"


/* Global methods */

const d = document,
    selectBtnContainer = d.querySelector(".modal_btns_container .select_btn"),
    createBtnContainer = d.querySelector(".modal_btns_container .create_btn"),
    endingBtnContainer = d.querySelector(".modal_btns_container .ending_btn"),
    confirmBtn = d.querySelector(".modal_btns_container .confirm_btn"),
    endingBtn = d.querySelector(".modal_btns_container .ending_btn"),
    selectResultBtn = d.querySelector(".modal_btns_container .select_result_btn"),
    editSymbol = d.querySelector(".icons_container .edit_symbol"),
    deleteSymbol = d.querySelector(".icons_container .delete_symbol"),
    inspectSymbol = d.querySelector(".icons_container .inspect_symbol"),
    closeSymbol = d.querySelector(".close_symbol")

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
        toggleBtnState(selectResultBtn, true)
        enableSelectResultBtn(results, resultsType, operation, table)
    } else if (operation === "create") {
        toggleCloseModalBtn(resultsType, operation, table, false)
        toggleDeletetSymbol(results, resultsType, operation, table, true)
        toggleEditSymbol(results, resultsType, operation, table, true)
        enableCreateBtn(results, resultsType, operation, table)
    } else if (operation === "manage") {
        toggleCloseModalBtn(resultsType, operation, table, true)
        toggleDeletetSymbol(true)
        toggleEditSymbol(results, resultsType, operation, table, true)
        toggleInspectSymbol(true)
        enableCreateBtn(results, resultsType, operation, table)
        confirmBtn.setAttribute("disabled", true)
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
            })
        })
    })
}

let checkboxClickHandler
const toggleBtnState = (btn, enable) => {
    const checkboxes = document.querySelectorAll('input.result_option');

    checkboxClickHandler = function () {
        if (findSelectedResult() !== -1) {
            btn.removeAttribute("disabled")
        } else {
            btn.setAttribute("disabled", true)
        }
    }

    checkboxes.forEach(checkbox => {
        if (enable) {
            checkbox.addEventListener("change", checkboxClickHandler)
        } else {
            checkbox.removeEventListener("change", checkboxClickHandler)
        }
    })
}



let editSymbolClickHandler, deleteSymbolClickHandler, inspectSymbolClickHandler, closeSymbolClickHandler

const toggleEditSymbol = (results, resultsType, operation, table, enable = false) => {
    editSymbolClickHandler = function () {
        executeEditSymbolListener(results, resultsType, operation, table)
    }
    toggleSymbol(editSymbol, enable)
    toggleListener(editSymbol, editSymbolClickHandler, enable)
}

const toggleDeletetSymbol = (results, resultsType, operation, table, enable = false) => {
    deleteSymbolClickHandler = function () {
        executeDeleteSymbolListener(results, resultsType, operation, table)
    }
    toggleSymbol(deleteSymbol, enable)
    toggleListener(deleteSymbol, deleteSymbolClickHandler, enable)
}

const toggleInspectSymbol = (enable = false) => {
    inspectSymbolClickHandler = function () {

    }
    toggleSymbol(inspectSymbol, enable)
    toggleListener(inspectSymbol, inspectSymbolClickHandler, enable)
}

const toggleCloseModalBtn = (resultsType, operation, table, enable = false) => {
    closeSymbolClickHandler = function () {
        closeModal(resultsType, operation, table)
    }
    toggleSymbol(closeSymbol, enable)
    toggleListener(closeSymbol, closeSymbolClickHandler, enable)
}

const toggleListener = (symbol, listener, enable) => {
    if (enable) {
        symbol.addEventListener("click", listener)
    } else {
        symbol.removeEventListener("click", listener)
    }
}

const toggleSymbol = (symbol, enable) => {
    if (enable) {
        if (!symbol.classList.contains("active")) {
            symbol.classList.add("active")
        }
    } else {
        symbol.classList.remove("active")
    }
}

let selectResultBtnClickHandler, confirmBtnClickHandler

const enableSelectResultBtn = (results, resultsType, operation, table) => {
    if (findCurrentPage().classList[1].startsWith("b_")) {
        selectResultBtnClickHandler = function () {
            executeSelectResultBtnListener(results, resultsType, operation, table)
            initializeBrowseAuthorsFormSubmit()
        }
    } else {
        selectResultBtnClickHandler = function () {
            toggleBtnState(false)
            executeSelectResultBtnListener(results, resultsType, operation, table)
        }
    }
    selectResultBtn.addEventListener("click", selectResultBtnClickHandler)
}

const enableCreateBtn = (results, resultsType, operation, table) => {
    if (confirmBtn) {
        confirmBtnClickHandler = function () {
            if (resultsType === "newEdition" || resultsType === "newBookcopy" || resultsType === "newReader") {
                saveResult(results, resultsType)
                prepareEndingBtn(resultsType)
            } else {
                executeConfirmBtnListener(results, resultsType, operation, table)
            }
        }
        confirmBtn.addEventListener("click", confirmBtnClickHandler)
    }
}

const executeSelectResultBtnListener = (results, resultsType, operation, table) => {
    saveResult(results, resultsType)
    endProcess(resultsType, operation, table)
}

const executeConfirmBtnListener = (results, resultsType, operation, table, crud) => {
    saveResult(results, resultsType)
    hiddeSuccessMessage()
    endProcess(resultsType, operation, table)
}

const executeConfirmCrudOperationBtn = (results, resultsType, operation, table, crud) => {
    saveResult(results, resultsType)
    displaySuccessMessage(resultsType, crud)
    confirmBtn.setAttribute("disabled", true)
    toggleCloseModalBtn(resultsType, operation, table, true)
    setTimeout(() => {
        confirmBtn.removeAttribute("disabled")
        hiddeSuccessMessage()
        if (crud === "edit") endProcess(resultsType, operation, table)
        else closeModal(resultsType, operation, table)
    }, 2000)
}

const executeEditSymbolListener = (results, resultsType, operation, table) => {
    if (operation !== "manage") saveResult(results, resultsType)

    confirmBtn.removeAttribute("disabled")
    toggleDeletetSymbol(false)
    toggleInspectSymbol(false)
    prepareEditonProcess(results, resultsType, operation, table)
    toggleEditSymbol(results, resultsType, operation, table, false)
}

const executeInspectSymbolListener = () => {

}

const executeDeleteSymbolListener = (results, resultsType, operation, table) => {
    confirmBtn.removeEventListener("click", confirmBtnClickHandler)
    toggleDeletetSymbol(false)
    toggleInspectSymbol(false)
    toggleEditSymbol(false)

    confirmBtn.textContent = "Click to confirm deletion"
    confirmBtnClickHandler = function () {
        clearPrintedReults(resultsType)
        deleteCreatedObject(results, resultsType)
        confirmBtn.textContent = "Confirm"
        results = ""
        executeConfirmCrudOperationBtn(results, resultsType, operation, table, "delete")
    }
    confirmBtn.addEventListener("click", confirmBtnClickHandler)
}

const deleteCreatedObject = (results, resultsType) => {
    switch (resultsType) {
        case "author":
            deleteAuthor(results[findSelectedResult()].idAuthor)
            break;
        case "bookwork":
            deleteBookwork(results[findSelectedResult()].idBookWork)
            break;
        case "newEdition":
            deleteNewBookedition(results[findSelectedResult()].idBookEdition)
            break;
        case "bookedition":
            break;
        case "newBookcopy":
            deleteNewCopy(results[findSelectedResult()].idBookCopy)
            break;
        case "newReader":
            break;
        case "b_author":
            break;
        default:
            break;
    }
}

const saveResult = (results, resultsType) => {
    switch (resultsType) {
        case "author":
            reasigneAuthorValue(results[findSelectedResult()])
            break;
        case "bookwork":
            reasigneBookworkValue(results[findSelectedResult()])
            break;
        case "newEdition":
            reasigneNewEditionValue(results[findSelectedResult()])
            break;
        case "bookedition":
            reasigneBookeditionValue(results[findSelectedResult()])
            break;
        case "newBookcopy":
            reasigneNewBookcopyValue(results[findSelectedResult()])
            break;
        case "newReader":
            reasigneNewReaderValue(results[findSelectedResult()])
            break;
        case "b_author":
            reasigneBrowseAuthorValue(results[findSelectedResult()])
            break;
        default:
            break;
    }
}

const prepareEditonProcess = (results, resultsType, operation, table) => {
    const tableResultsRow = table.querySelector(".results_row"),
        cells = tableResultsRow.querySelectorAll("td")

    clearPrintedReults(resultsType)

    switch (resultsType) {
        case "author":
            prepareAuthorEditionProcess(cells)
            break;
        case "bookwork":
            prepareBookworkEditionProcess(cells)
            break;
        case "newEdition":
            prepareNewEditionEditionProcess(cells)
            break;
        case "newBookcopy":
            prepareNewBookcopyEditionProcess(cells)
            break;
        case "newReader":
            prepareNewReaderEditionProcess(cells)
            break;
        case "b_author":
            prepareBrowseAuthorEditionProcess(cells)
            break;
        default:
            break;
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
        } else if (resultsType === "newBookcopy") {
            results = await getEditNewCopyResults(editedFields)
        } else if (resultsType === "newReader") {
            results = await getEditNewReaderResults(editedFields)
        } else if (resultsType === "b_author") {
            results = await getEditBrowseAuthorResults(editedFields)
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

        switch (resultsType) {
            case "newBookedition":
            case "newBookcopy":
                displaySuccessMessage(resultsType, "edit")
                prepareEndingBtn(resultsType)
                break;
            default:
                executeConfirmCrudOperationBtn(results, resultsType, operation, table, "edit")
                break;
        }
    }
}

const findSelectedResult = () => {
    const checkboxes = d.querySelectorAll('input.result_option');

    if (checkboxes.length > 0) {
        for (const checkbox of checkboxes) {
            if (checkbox.checked) {
                return checkbox.value
            }
        }
        return -1
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
    } else if (operation === "create" || operation === "manage") {
        createBtnContainer.classList.add("hidden")
    } else if (operation === "ending") {
        endingBtnContainer.classList.add("hidden")
    }

    removeModalElementsListeners()
    toggleCloseModalBtn()
    toggleEditSymbol()
    toggleDeletetSymbol()
    toggleInspectSymbol()
    toggleNextPageChanging(resultsType)
    clearFormsData(findCurrentPage())
}

const displaySuccessMessage = (resultsType, crud = "create") => {
    const successMessage = d.querySelector(".success_message")

    successMessage.classList.add("active")

    switch (resultsType) {
        case "author" || "b_author":
            switch (crud) {
                case "create":
                    successMessage.textContent = "Author Created Successfully"
                    break;
                case "edit":
                    successMessage.textContent = "Author Edited Successfully"
                    break;
                case "delete":
                    successMessage.textContent = "Author Deleted Successfully"
                    break;
                default:
                    break;
            }
            break;
        case "bookwork" || "b_bookwork":
            switch (crud) {
                case "create":
                    successMessage.textContent = "Book Work Created Successfully"
                    break;
                case "edit":
                    successMessage.textContent = "Book Work Edited Successfully"
                    break;
                case "delete":
                    successMessage.textContent = "Book Work Deleted Successfully"
                    break;
                default:
                    break;
            }
            break;
        case "newEdition":
            switch (crud) {
                case "create":
                    successMessage.textContent = "New Edition Created SuccessFully"
                    break;
                case "edit":
                    successMessage.textContent = "New Edition Edited Successfully"
                    break;
                case "delete":
                    successMessage.textContent = "New Edition Deleted Successfully"
                    break;
                default:
                    break;
            }
            break;
        case "newBookcopy":
            switch (crud) {
                case "create":
                    successMessage.textContent = "New Copy Created SuccessFully"
                    break;
                case "edit":
                    successMessage.textContent = "New Copy Edited Successfully"
                    break;
                case "delete":
                    successMessage.textContent = "New Copy Deleted Successfully"
                    break;
                default:
                    break;
            }            break;
        case "newReader":
            successMessage.textContent = "New Reader Created Successfully"
            break;
        default:
            break;
    }
}

const hiddeSuccessMessage = () => {
    const successMessage = d.querySelector(".success_message")
    successMessage.textContent = ""
    if (successMessage.classList.contains("active")) successMessage.classList.remove("active")
}

let endingBtnClickHandler
const prepareEndingBtn = resultsType => {
    createBtnContainer.classList.add("hidden")
    endingBtnContainer.classList.remove("hidden")
    endingBtnClickHandler = function () {
        closeModal()
        d.getElementById("main-content").textContent = ""
        if (resultsType === "newEdition") {
            displayCatalogingMainPage()
        } else if (resultsType === "newBookcopy") {
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
    confirmBtn.removeEventListener("click", executeConfirmBtnListener)
    editSymbol.removeEventListener("click", editSymbolClickHandler)
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

        if (method.toUpperCase() === "DELETE") return await res.text()

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
export { displaySuccessMessage }