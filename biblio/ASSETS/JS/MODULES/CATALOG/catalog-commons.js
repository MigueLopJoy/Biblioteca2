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
import { getNewEdition } from "./cataloging.js"
import { reasigneAuthorValue } from "./cataloging.js"
import { reasigneBookworkValue } from "./cataloging.js"
import { reasigneNewEditionValue } from "./cataloging.js"


const d = document,
    selectResultBtn = d.querySelector(".modal_btns_container .select_result_btn"),
    confirmBtn = d.querySelector(".modal_btns_container .confirm_btn"),
    editBtn = d.querySelector(".modal_btns_container .edit_btn"),
    closeSymbol = modal.querySelector(".close_symbol")



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

const toggleNextPageChanging = resultsType => {
    const nextPageBtn = findCurrentPage().querySelector(".change_page_container .next_page"),
        pageLinks = d.querySelectorAll(".page_link")

    const togglePageLink = (index, enable) => {
        if (pageLinks[index]) {
            if (enable) {
                pageLinks[index].classList.add("enabled")
            } else {
                pageLinks[index].classList.remove("enabled")
            }
        }
    }

    const toggleNextPageBtn = enable => {
        if (nextPageBtn) {
            if (enable) {
                if (!nextPageBtn.classList.contains("disabled")) {
                    nextPageBtn.classList.add("disabled")
                }
            } else {
                if (nextPageBtn.classList.contains("disabled")) {
                    nextPageBtn.classList.remove("disabled")
                }
            }
        }
    }

    const checkCondition = (type, value) => {
        toggleNextPageBtn(!value)
    }

    console.log(resultsType)

    if (resultsType === "author") {
        checkCondition("author", getAuthor())
    } else if (resultsType === "bookwork") {
        checkCondition("bookwork", getBookwork())
    } else if (resultsType === "bookedition") {
        checkCondition("bookedition", getNewEdition())
    }

    const clickHandler = () => {
        showPage(nextPageBtn.classList[2])
        enablePreviousPageBtn()
        nextPageBtn.removeEventListener("click", clickHandler)
    }

    if (nextPageBtn && !nextPageBtn.classList.contains("disabled")) {
        nextPageBtn.addEventListener("click", clickHandler)
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

const showSearchResults = (operation, table) => {
    const modal = d.getElementById("modal")

    modal.classList.remove("hidden")
    table.classList.remove("hidden")

    generaTableContent(table)

    if (operation === "search") {
        d.querySelector(".modal_btns_container .select_btn").classList.remove("hidden")

        if (table.classList.contains("authors_results_table")) {
            selectResultBtn.textContent = "Select author";
        } else if (table.classList.contains("bookworks_results_table")) {
            selectResultBtn.textContent = "Select book work";
        } else if (table.classList.contains("newEdition_results_table")) {
            selectResultBtn.textContent = "Save new edition";
        } else if (table.classList.contains("editions_results_table")) {
            selectResultBtn.textContent = "Select book edition";
        } else if (table.classList.contains("newBookCopy_results_table")) {
            selectResultBtn.textContent = "Save new copy";
        }
    } else if (operation === "create") {
        d.querySelector(".modal_btns_container .create_btns").classList.remove("hidden")
    }
}

const generaTableContent = table => {
    if (table.classList.contains("authors_results_table")) {
        generateAuthorsTableContent()
    } else if (table.classList.contains("bookworks_results_table")) {
        generateBookworksTableContent()
    } else if (table.classList.contains("newEdition_results_table")) {
        generateNewBookeditionTableContent()
    } else if (table.classList.contains("editions_results_table")) {
        generateBookeditionsTableContent()
    } else if (table.classList.contains("newBookCopy_results_table")) {
        generateNewBookcopyTableContent()
    }
}

const enableModalActions = (results, resultsType, operation, table) => {
    console.log("ENABLE MODAL ACTIONS")
    if (operation === "search") {
        console.log("SEARCH")
        enableOptionChangigng()
        let i = 0
        enableCloseModalBtn(resultsType, operation, table, i)
        i++
        enableSelectResultBtn(results, resultsType, operation, table)
    } else if (operation === "create") {
        disableCloseModalBtn()
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
    endProcess(results, resultsType, operation, table)
    selectResultBtn.removeEventListener("click", selectResultBtnClickHandler)
}

const executeConfirmBtnListener = (results, resultsType, operation, table) => {
    saveResult(results, resultsType)
    endProcess(results, resultsType, operation, table)
    confirmBtn.removeEventListener("click", confirmBtnClickHandler)
}

const executeEditBtnListener = (results, resultsType, operation, table) => {
    saveResult(results, resultsType)
    prepareEditonProcess(results, resultsType, operation, table)
    editBtn.removeEventListener("click", editBtnClickHandler)
}

const saveResult = (results, resultsType) => {
    if (resultsType === "author") {
        reasigneAuthorValue(results[findSelectedResult()])
    } else if (resultsType === "bookwork") {
        reasigneBookworkValue(results[findSelectedResult()])
    } else if (resultsType === "edition") {
        reasigneNewEditionValue(results[findSelectedResult()])
    }
}

const prepareEditonProcess = (results, resultsType, operation, table) => {
    console.log(table)
    const tableResultsRow = table.querySelector(".results_row"),
        cells = tableResultsRow.querySelectorAll("td")

    clearPrintedReults(resultsType)

    if (resultsType === "author") {
        prepareAuthorEditionProcess(cells)
    } else if (resultsType = "bookwork") {
        prepareBookworkEditionProcess(cells)
    } else if (resultsType === "edition") {
        prepareNewEditionProcess(cells)
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
        } else if (resultsType === "edition") {
            results = await getEditNewEditionResults(editedFields)
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
        saveResult(results, resultsType)
        endProcess(results, resultsType, operation, table)
        confirmBtn.removeEventListener("click", confirmBtnClickHandler)
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

const endProcess = (results, resultsType, operation, table) => {
    printSelectedResult(resultsType)
    closeModal(resultsType, operation, table)

    results = "",
        resultsType = "",
        operation = ""
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


let closeModalBtnClickHandler

const enableCloseModalBtn = (resultsType, operation, table, i) => {
    closeModalBtnClickHandler = function () {
        if (closeSymbol.classList.contains("active")) {
            closeModal(resultsType, operation, table)
        }
    }

    closeSymbol.classList.add("active")
    closeSymbol.addEventListener("click", closeModalBtnClickHandler)
}

const disableCloseModalBtn = () => {
    closeSymbol.classList.remove("active")
}

const closeModal = (resultsType, operation, table) => {
    console.log("CLOSING")
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

    closeSymbol.removeEventListener("click", closeModalBtnClickHandler)
    toggleNextPageChanging(resultsType)
    clearFormsData(findCurrentPage())
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