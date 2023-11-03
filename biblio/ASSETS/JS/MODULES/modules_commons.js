import {
    loadContent,
    displayCatalogingMainPage,
    displayRegisteringMainPage,
} from "../display_pages.js"

import {
    handleErrorMessages,
    clearErrorMessages,
    displaySuccessMessage,
    hiddeSuccessMessage
} from "./api_messages_handler.js"

import {
    editAuthor,
    deleteAuthor,
    setAuthorValue,
    generateAuthorCatalogCard,
    generateAuthorsTableContent
} from "./CATALOG/authors_catalog.js"

import {
    editReader,
    deleteReader,
    setReaderValue,
    generateReadersCatalogCard,
    generateReadersTableContent
} from "./USERS/readers.js"

import {
    sendBookWorkForm,
    editBookwork,
    deleteBookwork,
    setBookworkValue,
    generateBookWorkCatalogCard,
    generateBookworksTableContent,
} from "./CATALOG/bookworks_catalog.js"

import {
    sendBookEditionForm,
    editBookEdition,
    deleteBookedition,
    setBookeditionValue,
    generateBookEditionsTableContent,
    generateBookEditionCatalogCard
} from "./CATALOG/bookeditions_catalog.js"

import {
    sendBookCopyForm,
    editBookcopy,
    deleteBookCopy,
    setBookCopyValue,
    generateBookCopyCatalogCard,
    generateBookCopiesTableContent
} from "./CATALOG/bookcopies_catalog.js"


const d = document

let results, resultsType, operation, resultsContainer

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

const showSearchResults = table => {
    renderModal()
    generaTableContent(table)
    table.classList.remove("hidden")
    d.querySelector(".select_results_container").classList.remove("hidden")
}

const showCatalogCard = (results, resultsType, catalogCard) => {
    renderModal()
    displaySuccessMessage(results)
    results = selectResultObject(results)
    generateCatalogCard(results, resultsType)
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
    console.log(table)
    if (table.classList.contains("authors_results_table")) {
        generateAuthorsTableContent()
    } else if (table.classList.contains("bookworks_results_table")) {
        generateBookworksTableContent()
    } else if (table.classList.contains("bookeditions_results_table")) {
        console.log(table)
        generateBookEditionsTableContent()
    } else if (table.classList.contains("bookcopies_results_table")) {
        generateBookCopiesTableContent()
    } else if (table.classList.contains("readers_results_table")) {
        generateReadersTableContent()
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
        case "reader":
            generateReadersCatalogCard(results)
        default:
            break
    }
    toggleSearchRelatedObjectsSymbolActivation()
}

const toggleSearchRelatedObjectsSymbolActivation = () => {
    const searchRelatedObjectsSymbol = d.querySelector(".search_related_objects"),
        inputs = d.querySelectorAll(".catalog_card .form input")

    if (searchRelatedObjectsSymbol) {
        if (inputs[inputs.length - 1].value.substring(0, 2) !== "No") {
            toggleSymbol(searchRelatedObjectsSymbol, true)
        }
    }
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
    clearErrorMessages()
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
        case "author":
            await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/bookworks_catalog.html", d.getElementById("main-content"))
            sendBookWorkForm(results)
            break;
        case "bookwork":
            await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/bookeditions_catalog.html", d.getElementById("main-content"))
            sendBookEditionForm(results)
            break;
        case "bookedition":
            await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/bookcopies_catalog.html", d.getElementById("main-content"))
            sendBookCopyForm(results)
            break;
        default:
            break;
    }
}

const deleteObject = async () => {
    try {
        switch (resultsType) {
            case "author":
                return await deleteAuthor(results.idAuthor)
            case "bookwork":
                return await deleteBookwork(results.idBookWork)
            case "bookedition":
                return await deleteBookedition(results.idBookEdition)
            case "bookcopy":
                return await deleteBookCopy(results.idBookCopy)
            case "reader":
                return await deleteReader(results.idReader)
            default:
                break
        }
    } catch (error) {
        throw error
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
        case "reader":
            setReaderValue(results)
        default:
            break
    }
}

const browseObject = () => {
    resultsContainer = d.querySelector(".catalog_card")
    showCatalogCard(results, resultsType, resultsContainer)
    setCreationValues(results, resultsType, resultsContainer)
    toggleCloseModalSymbol(true)
    d.querySelector(".confirm_creation_container").classList.add("hidden")
}

const prepareEditonProcess = () => {
    const inputs = d.querySelectorAll(".form input.card_info")

    for (const input of inputs) {
        input.removeAttribute("readonly")
        input.classList.add("editing")
    }
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
        executeConfirmCrudOperationBtn(btn)
    } catch (ex) {
        handleErrorMessages(ex, resultsContainer)
    }
}

const confirmDeletion = async btn => {
    clearErrorMessages()
    try {
        results = await deleteObject()
        executeConfirmCrudOperationBtn(btn)
    } catch (error) {
        handleErrorMessages(error, resultsContainer)
        toggleCloseModalSymbol(true)
        toggleEditSymbol(true)
        toggleDeletetSymbol(true)
        d.querySelector(".confirm_deletion_container").classList.add("hidden")
        d.querySelector(".confirm_creation_container").classList.remove("hideen")
    }
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
        case "reader":
            return await editReader(results.idReader, editedInputs)
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

const closeModal = (form) => {
    disableSelectResultBtn()
    hiddeModalBtns()
    hiddeResultsContainers()
    disableAllSymbols()
    clearForms(form)
    // toggleNextPageChanging(resultsType)
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
        }
    }
    endingBtn.addEventListener("click", endingBtnClickHandler)
}

const clearForms = form => {

    const clearForm = form => {
        form.querySelectorAll("input").forEach(input => {
            if (input.type !== "submit" &&
                !input.hasAttribute("readonly")
            ) {
                input.value = ""
            }
        })
    }

    d.querySelectorAll(".page_element_container .form").forEach(pageForm => {
        if (!form || (form && form !== pageForm)) {
            clearForm(pageForm)
        }
    })
}


export {
    showSearchResults,
    showCatalogCard,
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