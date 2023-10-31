import {
    loadContent,
    displayCatalogingMainPage,
    displayRegisteringMainPage,
} from "../display_pages.js"

import {
    printSelectedResult,
    showCatalogCard,
    // toggleNextPageChanging,
    clearPrintedReults
} from "./CATALOG/catalog-commons.js"

import {
    handleErrorMessages,
    clearErrorMessages,
    displaySuccessMessage,
    hiddeSuccessMessage
} from "./api_messages_handler.js"

import {
    editAuthor,
    deleteAuthor,
    setAuthorValue
} from "./CATALOG/authors_catalog.js"

import {
    sendBookWorkForm,
    editBookwork,
    deleteBookwork,
    setBookworkValue
} from "./CATALOG/bookworks_catalog.js"

import {
    sendBookEditionForm,
    editBookEdition,
    deleteBookedition,
    setBookeditionValue
} from "./CATALOG/bookeditions_catalog.js"

import {
    sendBookCopyForm,
    editBookcopy,
    deleteBookCopy,
    setBookCopyValue
} from "./CATALOG/bookcopies_catalog.js"


const d = document

let results, resultsType, operation, resultsContainer

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

const setSearchValues = (oResults, oResultsType, table) => {
    results = selectResultObject(oResults)
    resultsType = oResultsType
    operation = "search"
    resultsContainer = table

    toggleCloseModalSymbol(true)
}

const setCreationValues = (oResults, oResultsType, catalogCard) => {
    results = selectResultObject(oResults)
    resultsType = oResultsType
    operation = "create"
    resultsContainer = catalogCard

    toggleDeletetSymbol(true)
    toggleEditSymbol(true)
}

const selectResultObject = results => {
    if (results.successMessage) {
        if (results.author) return results.author
        else if (results.bookWork) return results.bookWork
        else if (results.bookEdition) return results.bookEdition
        else if (results.bookCopy) return results.bookCopy
    } else return results
}

const toggleEditSymbol = (enable = false) => {
    const editSymbol = d.querySelector(".icons_container .edit_symbol")
    toggleSymbol(editSymbol, enable)
}

const toggleDeletetSymbol = (enable = false) => {
    const deleteSymbol = d.querySelector(".icons_container .delete_symbol")
    toggleSymbol(deleteSymbol, enable)
}

const toggleCloseModalSymbol = (enable = false) => {
    let closeSymbol = d.querySelector(".close_symbol")
    toggleSymbol(closeSymbol, enable)
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

const executeSelectResultBtnListener = () => {
    results = results[findSelectedResult()]
    saveResult()
    closeModal()
    browseObject()
}

const executeConfirmCreationBtnListener = () => {
    results = results[findSelectedResult()]
    saveResult()
    hiddeSuccessMessage()
    closeModal()
}

const executeEditSymbolListener = () => {
    executeCrudSymbolsListeners()
    d.querySelector(".confirm_edition_container").classList.remove("hidden")
    prepareEditonProcess()
}

const executeDeleteSymbolListener = () => {
    executeCrudSymbolsListeners()
    d.querySelector(".confirm_deletion_container").classList.remove("hidden")
}

const executeCrudSymbolsListeners = () => {
    hiddeModalBtns()
    hiddeSuccessMessage()
    disableAllSymbols()
}

const executeConfirmCrudOperationBtn = btn => {
    saveResult()
    displaySuccessMessage(results)
    btn.setAttribute("disabled", true)
    toggleCloseModalSymbol(true)

    setTimeout(() => {
        btn.removeAttribute("disabled")
        hiddeSuccessMessage()
        closeModal()
    }, 2000)
}

const executeSearchRelatedObjectsListener = async () => {
    closeModal()
    switch (resultsType) {
        case "b_author":
            await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/bookworks_catalog.html", d.getElementById("main-content"))
            sendBookWorkForm(results)
            break;
        case "b_bookwork":
            await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/bookeditions_catalog.html", d.getElementById("main-content"))
            sendBookEditionForm(results)
            break;
        case "b_bookedition":
            await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/bookcopies_catalog.html", d.getElementById("main-content"))
            sendBookCopyForm(results)
            break;
        default:
            break;
    }
}

const deleteObject = async () => {
    switch (resultsType) {
        case "author":
            results = await deleteAuthor(results.idAuthor)
            break
        case "bookwork":
            results = await deleteBookwork(results.idBookWork)
            break
        case "bookedition":
            results = await deleteBookedition(results.idBookEdition)
            break
        case "bookcopy":
            results = await deleteBookCopy(results.idBookCopy)
            break
        default:
            break
    }
}

const saveResult = () => {
    switch (resultsType) {
        case "author":
            setAuthorValue(results)
            break
        case "bookwork":
            setBookworkValue(results)
            break
        case "bookedition":
            setBookeditionValue(results)
            break
        case "bookcopy":
            setBookCopyValue(results)
            break
        default:
            break;
    }
}

const browseObject = () => {
    resultsContainer = d.querySelector(".catalog_card")
    showCatalogCard(results, resultsType, resultsContainer)
    setCreationValues(results, resultsType, resultsContainer)
    toggleCloseModalSymbol(true)
}

const prepareEditonProcess = () => {
    const inputs = d.querySelectorAll(".form input.card_info")

    for (const input of inputs) {
        input.removeAttribute("readonly")
        input.classList.add("editing")
    }
    clearPrintedReults(resultsType)
}

const confirmEdition = async btn => {
    const editedInputs = [...d.querySelectorAll(".form input.editing")].map(input => input.value)

    clearErrorMessages()
    try {

        results = await getEditionResults(editedInputs)

        d.querySelectorAll(".form input.card_info").forEach(input => {
            if (input.classList.contains("editing")) {
                input.classList.remove("editing")
            }
        })
        switch (resultsType) {
            // case "newBookedition":
            // case "newBookcopy":
            // displaySuccessMessage(resultsType, "edit")
            // prepareEndingBtn(resultsType, operation, catalogCard)
            // break;
            default:
                executeConfirmCrudOperationBtn(btn)
                break;
        }
    } catch (ex) {
        handleErrorMessages(ex, resultsContainer)
    }
}

const confirmDeletion = async btn => {
    clearPrintedReults(resultsType)
    await deleteObject()
    executeConfirmCrudOperationBtn(btn)
}

const getEditionResults = async editedInputs => {
    switch (resultsType) {
        case "author":
            return await editAuthor(results.idAuthor, editedInputs)
        case "bookwork":
            return await editBookwork(results.idBookWork, editedInputs)
        case "bookedition":
            return await editBookEdition(results.idBookEdition, editedInputs)
        case "bookcopy":
            return await editBookcopy(results.idBookCopy, editedInputs)
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

const closeModal = () => {
    disableSelectResultBtn()
    hiddeModalBtns()
    hiddeResultsContainers()
    disableAllSymbols()
    // toggleNextPageChanging(resultsType)
    clearForms()
    modal.classList.add("hidden")
}

const disableSelectResultBtn = () => {
    const selectResultBtn = d.querySelector(".select_results_btn")
    if (!selectResultBtn.hasAttribute("disabled")) {
        selectResultBtn.setAttribute("disabled", true)
    }
    if (selectResultBtn.classList.length > 2) {
        selectResultBtn.classList.remove(selectResultBtn.classList[2])
    }
}

const hiddeModalBtns = () => {
    const modalBtnsContainers = d.querySelectorAll(".modal_btns_container > div")

    modalBtnsContainers.forEach(modalBtnContainer => {
        if (!modalBtnContainer.classList.contains("hidden")) {
            modalBtnContainer.classList.add("hidden")
        }
    })
}

const hiddeResultsContainers = () => {
    hiddeTable(d.querySelectorAll(".results_table"))
    hiddeCatalogCard(d.querySelector(".catalog_card"))
}

const hiddeTable = tables => {
    for (const table of tables) {
        const tableBody = table.querySelector(".results_table_body"),
            errorMessageRow = tableBody.querySelector(".error_message_row")

        if (tableBody) {
            tableBody.innerHTML = ""
            if (errorMessageRow) {
                tableBody.appendChild(errorMessageRow)
            }
        }
        if (!table.classList.contains("hidden")) {
            table.classList.add("hidden")
        }
    }
}

const hiddeCatalogCard = catalogCard => {
    if (catalogCard) {
        if (!catalogCard.classList.contains("hidden")) {
            catalogCard.classList.add("hidden")
        }
    }
}

const disableAllSymbols = () => {
    toggleCloseModalSymbol()
    toggleEditSymbol()
    toggleDeletetSymbol()
}

let endingBtnClickHandler
const prepareEndingBtn = (resultsType, operation, catalogCard) => {
    let createBtnContainer = d.querySelector(".modal_btns_container .confirm_creation_btn"),
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

const clearForms = () => {
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


export {
    findCurrentPage,
    findSelectedResult,
    selectResultObject,
    setSearchValues,
    setCreationValues,
    closeModal,
    executeSelectResultBtnListener,
    executeEditSymbolListener,
    executeDeleteSymbolListener,
    executeConfirmCreationBtnListener,
    confirmEdition,
    executeSearchRelatedObjectsListener,
    confirmDeletion,
    toggleSymbol,
    clearForms,
}