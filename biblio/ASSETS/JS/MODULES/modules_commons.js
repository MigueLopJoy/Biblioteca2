import {
    loadContent,
    displayCatalogingMainPage,
    displayRegisteringMainPage,
    displayReadersRegisteringMainPage
} from "./CATALOG/display_pages.js"

import {
    printSelectedResult,
    showCatalogCard,
    showSearchResults,
    toggleNextPageChanging,
    clearPrintedReults
} from "./CATALOG/catalog-commons.js"

import {
    getEditAuthorResults,
    getEditBookworkResults,
    getEditNewEditionResults,
    deleteAuthor,
    deleteBookwork,
    deleteNewBookedition,
    reasigneAuthorValue,
    reasigneBookworkValue,
    reasigneNewEditionValue
} from "./CATALOG/cataloging.js"

import {
    getEditNewCopyResults,
    deleteNewCopy,
    reasigneBookeditionValue,
    reasigneNewBookcopyValue
} from "./CATALOG/registering.js"

import {
    getEditNewReaderResults,
    reasigneNewReaderValue
} from "./READERS/readers_registering.js"

import {
    getEditBrowseAuthorResults,
    reasigneBrowseAuthorValue
} from "./CATALOG/BROWSE/authors_catalog.js"

import {
    sendBookWorkForm,
    getEditBrowseBookworkResults,
    reasigneBrowseBookWorkValue
} from "./CATALOG/BROWSE/bookworks_catalog.js"

import {
    sendBookEditionForm,
    getEditBrowseBookEditionResults,
    reasigneBrowseBookeditionValue
} from "./CATALOG/BROWSE/bookeditions_catalog.js"

/* Global methods */

const d = document

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

const enableSearchModalActions = (results, resultsType, operation, table) => {
    let selectResultBtn = d.querySelector(".modal_btns_container .select_result_btn")

    enableOptionChangigng()
    toggleCloseModalSymbol(resultsType, operation, table, true)
    toggleBtnState(selectResultBtn, true)
    enableSelectResultBtn(selectResultBtn, results, resultsType, operation, table)
}

const enableCreateModalActions = (results, resultsType, operation, catalogCard) => {
    if (resultsType.substring(0, 2) !== "b_") {
        toggleCloseModalSymbol(resultsType, operation, catalogCard, false)
    } else {
        toggleCloseModalSymbol(resultsType, operation, catalogCard, true)
    }
    toggleDeletetSymbol(results, resultsType, operation, catalogCard, true)
    toggleEditSymbol(results, resultsType, operation, catalogCard, true)
    enableCreateBtn(results, resultsType, operation, catalogCard)
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

let editSymbolClickHandler, deleteSymbolClickHandler, closeSymbolClickHandler, searchRelatedObjectsClickHandler

const toggleEditSymbol = (results, resultsType, operation, catalogCard, enable = false) => {
    let editSymbol = d.querySelector(".icons_container .edit_symbol")

    editSymbolClickHandler = function () {
        executeEditSymbolListener(results, resultsType, operation, catalogCard)
    }
    toggleSymbol(editSymbol, enable)
    toggleListener(editSymbol, editSymbolClickHandler, enable)
}

const toggleDeletetSymbol = (results, resultsType, operation, resultsContainer, enable = false) => {
    let deleteSymbol = d.querySelector(".icons_container .delete_symbol")

    deleteSymbolClickHandler = function () {
        executeDeleteSymbolListener(results, resultsType, operation, resultsContainer)
    }
    toggleSymbol(deleteSymbol, enable)
    toggleListener(deleteSymbol, deleteSymbolClickHandler, enable)
}

const toggleCloseModalSymbol = (resultsType, operation, resultsContainer, enable = false) => {
    let closeSymbol = d.querySelector(".close_symbol")

    closeSymbolClickHandler = function () {
        closeModal(resultsType, operation, resultsContainer)
    }
    toggleSymbol(closeSymbol, enable)
    toggleListener(closeSymbol, closeSymbolClickHandler, enable)
}

const toggleSearchRelatedObjectsSymbol = (results, resultsType, catalogCard, searchRelatedObjectsSymbol, enable = false) => {
    searchRelatedObjectsClickHandler = function () {
        executeSearchRelatedObjectsListener(results, resultsType, catalogCard)
    }
    toggleSymbol(searchRelatedObjectsSymbol, enable)
    toggleListener(searchRelatedObjectsSymbol, searchRelatedObjectsClickHandler, enable)
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

const enableSelectResultBtn = (selectResultBtn, results, resultsType, operation, table) => {
    selectResultBtnClickHandler = function () {
        toggleBtnState(false)
        executeSelectResultBtnListener(results, resultsType, operation, table)
    }
    selectResultBtn.addEventListener("click", selectResultBtnClickHandler)
}

const enableCreateBtn = (results, resultsType, operation, resultsContainer) => {
    let confirmBtn = d.querySelector(".modal_btns_container .confirm_btn")

    if (confirmBtn) {
        confirmBtnClickHandler = function () {
            if (resultsType === "newEdition" || resultsType === "newBookcopy" || resultsType === "newReader") {
                saveResult(results, resultsType)
                prepareEndingBtn(resultsType, operation, resultsContainer)
            } else {
                executeConfirmBtnListener(results, resultsType, operation, resultsContainer)
            }
        }
        confirmBtn.addEventListener("click", confirmBtnClickHandler)
    }
}

const executeSelectResultBtnListener = (results, resultsType, operation, table) => {
    results = results[findSelectedResult()]
    saveResult(results, resultsType)
    endProcess(resultsType, operation, table)
    browseObject(results, resultsType)
}

const browseObject = (results, resultsType) => {
    if (resultsType.substring(0, 2) === "b_") {
        let catalogCard,
            inputs = d.querySelectorAll(".catalog_card .form input"),
            operation = "create"

        switch (resultsType) {
            case "b_author":
                catalogCard = d.querySelector(".catalog_card.author_catalog_card")
                break;
            case "b_bookwork":
                catalogCard = d.querySelector(".catalog_card.bookwork_catalog_card")
                break;
            case "b_bookedition":
                console.log("AAAAA")
                catalogCard = d.querySelector(".catalog_card.bookedition_catalog_card")
                console.log(catalogCard)
            default:
                break;
        }

        let searchRelatedObjectsSymbol = catalogCard.querySelector(".search_related_objects")

        showCatalogCard(resultsType, catalogCard, [results])
        enableCreateModalActions(results, resultsType, operation, catalogCard)

        if (inputs[inputs.length - 1].value.substring(0, 2) !== "No") {
            searchRelatedObjectsSymbol.classList.add("active")
            toggleSearchRelatedObjectsSymbol(results, resultsType, catalogCard, searchRelatedObjectsSymbol, true)
        }
    }
}

const executeConfirmBtnListener = (results, resultsType, operation, table, crud) => {
    saveResult(results, resultsType)
    hiddeSuccessMessage()
    endProcess(resultsType, operation, table)
}

const executeConfirmCrudOperationBtn = (results, resultsType, operation, resultsContainer, crud) => {
    let confirmBtn = d.querySelector(".modal_btns_container .confirm_btn")

    saveResult(results, resultsType)
    displaySuccessMessage(resultsType, crud)
    confirmBtn.setAttribute("disabled", true)
    toggleCloseModalSymbol(resultsType, operation, resultsContainer, true)
    setTimeout(() => {
        confirmBtn.removeAttribute("disabled")
        hiddeSuccessMessage()
        if (crud === "edit") endProcess(resultsType, operation, resultsContainer)
        else closeModal(resultsType, operation, resultsContainer)
    }, 2000)
}

const executeEditSymbolListener = (results, resultsType, operation, catalogCard) => {
    let confirmBtn = d.querySelector(".modal_btns_container .confirm_btn")

    if (operation !== "manage") saveResult(results, resultsType)
    hiddeSuccessMessage()
    confirmBtn.removeAttribute("disabled")
    toggleDeletetSymbol(false)
    prepareEditonProcess(results, resultsType, operation, catalogCard)
    toggleEditSymbol(results, resultsType, operation, catalogCard, false)
}

const executeDeleteSymbolListener = (results, resultsType, operation, resultsContainer) => {
    let confirmBtn = d.querySelector(".modal_btns_container .confirm_btn")

    confirmBtn.removeEventListener("click", confirmBtnClickHandler)
    toggleDeletetSymbol(false)
    toggleEditSymbol(false)
    hiddeSuccessMessage()

    confirmBtn.textContent = "Click To Confirm Deletion"
    confirmBtnClickHandler = function () {
        clearPrintedReults(resultsType)
        deleteCreatedObject(results, resultsType)
        confirmBtn.textContent = "Confirm"
        results = ""
        executeConfirmCrudOperationBtn(results, resultsType, operation, resultsContainer, "delete")
    }
    confirmBtn.addEventListener("click", confirmBtnClickHandler)
}

const executeSearchRelatedObjectsListener = async (results, resultsType, catalogCard) => {
    closeModal(resultsType, "create", catalogCard)
    switch (resultsType) {
        case "b_author":
            await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/BROWSE/bookworks_catalog.html", d.getElementById("main-content"))
            sendBookWorkForm(results)
            break;
        case "b_bookwork":
            await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/BROWSE/bookeditions_catalog.html", d.getElementById("main-content"))
            sendBookEditionForm(results)
            break;
        case "b_bookedition":
            await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/BROWSE/bookcopies_catalog.html", d.getElementById("main-content"))
            sendBookEditionForm(results)
                break;        
        default:
            break;
    }
}

const deleteCreatedObject = (results, resultsType) => {
    switch (resultsType) {
        case "author":
        case "b_author":
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
        default:
            break;
    }
}

const saveResult = (results, resultsType) => {
    switch (resultsType) {
        case "author":
            reasigneAuthorValue(results[findSelectedResult()])
            break
        case "bookwork":
            reasigneBookworkValue(results[findSelectedResult()])
            break
        case "newEdition":
            reasigneNewEditionValue(results[findSelectedResult()])
            break
        case "bookedition":
            reasigneBookeditionValue(results[findSelectedResult()])
            break
        case "newBookcopy":
            reasigneNewBookcopyValue(results[findSelectedResult()])
            break
        case "newReader":
            reasigneNewReaderValue(results[findSelectedResult()])
            break
        case "b_author":
            reasigneBrowseAuthorValue(results[findSelectedResult()])
            break
        case "b_bookwork":
            reasigneBrowseBookWorkValue(results[findSelectedResult()])
            break
        case "b_bookedition":
            reasigneBrowseBookeditionValue(results[findSelectedResult()])
            break
        default:
            break;
    }
}

const prepareEditonProcess = (results, resultsType, operation, catalogCard) => {
    const inputs = catalogCard.querySelectorAll(".form input.card_info"),
        confirmBtn = d.querySelector(".modal_btns_container .confirm_btn")

    for (const input of inputs) {
        input.removeAttribute("readonly")
        input.classList.add("editing")
    }

    clearPrintedReults(resultsType)

    confirmBtn.removeEventListener("click", confirmBtnClickHandler)
    confirmBtnClickHandler = function () {
        confirmEdition(results, resultsType, operation, catalogCard)
    }
    confirmBtn.addEventListener("click", confirmBtnClickHandler)
}

const confirmEdition = async (results, resultsType, operation, catalogCard) => {
    const editedInputs = [...catalogCard.querySelectorAll(".form input.editing")].map(input => input.value)

    clearErrorMessages()
    try {
        results = await getEditionResults(resultsType, editedInputs)

        d.querySelectorAll(".form input.card_info").forEach(input => {
            if (input.classList.contains("editing")) {
                input.classList.remove("editing")
            }
        })
        switch (resultsType) {
            case "newBookedition":
            case "newBookcopy":
                displaySuccessMessage(resultsType, "edit")
                prepareEndingBtn(resultsType, operation, catalogCard)
                break;
            default:
                executeConfirmCrudOperationBtn(results, resultsType, operation, catalogCard, "edit")
                break;
        }
    } catch (ex) {
        handleErrorMessages(ex, catalogCard)
    }
}

const getEditionResults = async (resultsType, editedInputs) => {
    switch (resultsType) {
        case "author":
            return await getEditAuthorResults(editedInputs)
        case "bookwork":
            return await getEditBookworkResults(editedInputs)            
        case "newEdition":
            return await getEditNewEditionResults(editedInputs)            
        case "newBookCopy":
            return await getEditNewCopyResults(editedInputs)            
        case "b_author":
            return await getEditBrowseAuthorResults(editedInputs)            
        case "b_bookwork":
            return await getEditBrowseBookworkResults(editedInputs)            
        case "b_bookedition":
            return await getEditBrowseBookEditionResults(editedInputs)            
        case "newReader":
            return await getEditNewReaderResults(editedInputs)            
        default:
            break;
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

const endProcess = (resultsType, operation, resultsContainer) => {
    printSelectedResult(resultsType)
    closeModal(resultsType, operation, resultsContainer)
}

const closeModal = (resultsType, operation, resultsContainer) => {
    let selectBtnContainer = d.querySelector(".modal_btns_container .select_btn"),
        createBtnContainer = d.querySelector(".modal_btns_container .create_btn"),
        endingBtnContainer = d.querySelector(".modal_btns_container .ending_btn"),
        selectResultBtn = d.querySelector(".modal_btns_container .select_result_btn")

    if (resultsContainer.classList.contains("results_table")) hiddeTable(resultsContainer)
    else if (resultsContainer.classList.contains("catalog_card")) hiddeCatalogCard(resultsContainer)

    modal.classList.add("hidden")
    selectResultBtn.setAttribute("disabled", true)

    if (operation === "search") {
        selectBtnContainer.classList.add("hidden")
    } else if (operation === "create") {
        createBtnContainer.classList.add("hidden")
    } else if (operation === "ending") {
        endingBtnContainer.classList.add("hidden")
    }

    removeModalElementsListeners()
    toggleCloseModalSymbol()
    toggleEditSymbol()
    toggleDeletetSymbol()
    toggleNextPageChanging(resultsType)
    clearFormsData(findCurrentPage())
}

const hiddeTable = table => {
    if (table) {
        let tableBody = table.querySelector(".results_table_body"),
            errorMessageRow = tableBody.querySelector(".error_message_row")

        tableBody.innerHTML = ""
        if (errorMessageRow) {
            tableBody.appendChild(errorMessageRow)
        }

        table.classList.add("hidden")

        table = ""
    }
}

const hiddeCatalogCard = catalogCard => {
    if (catalogCard) {
        if (!catalogCard.classList.contains("hidden")) {
            catalogCard.classList.add("hidden")
        }
    }
}

const displaySuccessMessage = (resultsType, crud = "create") => {
    const successMessage = d.querySelector(".success_message")
    successMessage.classList.add("active")

    switch (resultsType) {
        case "author":
        case "b_author":
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
        case "bookwork":
        case "b_bookwork":
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
            hiddeCatalogCard()
            break;
    }
}

const hiddeSuccessMessage = () => {
    const successMessage = d.querySelector(".success_message")
    successMessage.textContent = ""
    if (successMessage.classList.contains("active")) successMessage.classList.remove("active")
}

let endingBtnClickHandler
const prepareEndingBtn = (resultsType, operation, catalogCard) => {
    let createBtnContainer = d.querySelector(".modal_btns_container .create_btn"),
        endingBtnContainer = d.querySelector(".modal_btns_container .ending_btn"),
        endingBtn = d.querySelector(".modal_btns_container .ending_btn")

    createBtnContainer.classList.add("hidden")
    endingBtnContainer.classList.remove("hidden")
    endingBtnClickHandler = function () {
        closeModal(resultsType, operation, catalogCard)
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
    let selectResultBtn = d.querySelector(".modal_btns_container .select_result_btn"),
        closeSymbol = d.querySelector(".close_symbol"),
        editSymbol = d.querySelector(".icons_container .edit_symbol"),
        confirmBtn = d.querySelector(".modal_btns_container .confirm_btn"),
        endingBtn = d.querySelector(".modal_btns_container .ending_btn")

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
export { enableSearchModalActions }
export { enableCreateModalActions }
export { fetchRequest }
export { joinParamsToURL }
export { handleErrorMessages }
export { clearErrorMessages }
export { clearFormsData }
export { displaySuccessMessage }