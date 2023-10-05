import { generateAuthorsTableContent } from "./cataloging.js"
import { generateBookworksTableContent } from "./cataloging.js"
import { generateNewBookeditionTableContent } from "./cataloging.js"

import { generateBookeditionsTableContent } from "./registering.js"
import { generateNewBookcopyTableContent } from "./registering.js"

import { showPage } from "./pages_navigation.js"

import { author } from "./cataloging.js"
import { bookwork } from "./cataloging.js"
import { bookedition } from "./registering.js"


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

    if (previousPageBtn) {
        previousPageBtn.addEventListener("click", () => {
            showPage(previousPageBtn.classList[2])
            enablePreviousPageBtn()
        })
    }
}

const toggleNextPageChanging = resultsType => {
    const nextPageBtn = findCurrentPage().querySelector(".change_page_container .next_page"),
        pageLinks = d.querySelectorAll(".page_link")

    const togglePageLink = (index, enable) => {
        if (pageLinks[index]) {
            if (enable) {
                pageLinks[index].classList.add("enabled");
            } else {
                pageLinks[index].classList.remove("enabled");
            }
        }
    };

    const toggleNextPageBtn = enable => {
        if (nextPageBtn) {
            if (enable) {
                nextPageBtn.classList.remove("disabled");
            } else {
                nextPageBtn.classList.add("disabled");
            }
        }
    };

    const checkCondition = (type, value) => {
        if (!value) {
            toggleNextPageBtn(!nextPageBtn.classList.contains("disabled"));
            togglePageLink(1);
            togglePageLink(2);
        } else {
            toggleNextPageBtn(nextPageBtn && nextPageBtn.classList.contains("disabled"));
            togglePageLink(type === "bookedition" ? 1 : 2, !nextPageBtn.classList.contains("disabled"));
            if (!pageLinks[1].classList.contains("enabled") && type !== "bookedition") {
                pageLinks[1].classList.add("enabled");
            }
        }
    };

    if (resultsType === "author") {
        checkCondition("author", author);
    } else if (resultsType === "bookwork") {
        checkCondition("bookwork", bookwork);
    } else if (resultsType === "bookedition") {
        checkCondition("bookedition", bookedition);
    }

    if (nextPageBtn && !nextPageBtn.classList.contains("disabled")) {
        nextPageBtn.addEventListener("click", () => {
            showPage(nextPageBtn.classList[2]);
            enablePreviousPageBtn()
        });
    }
};


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
            
            console.log(res.ok)

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

const handleErrorMessages = layer => {
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

    console.log(table)
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
    console.log(table)
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
    if (operation === "search") {
        enableOptionChangigng()
        enableCloseModalBtn(operation, table)
        enableSelectResultBtn(results, resultsType)
    } else if (operation === "create") {
        disableCloseModalBtn()
        enableCreateBtns(results, resultsType)
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

const enableSelectResultBtn = (results, resultsType) => {
    selectResultBtn.addEventListener("click", () => {
        executeSelectResultBtnListener(results, resultsType)
    })
    
}

const enableCreateBtns = (results, resultsType) => {
    if (confirmBtn) {
        confirmBtn.addEventListener("click", () => {
            executeConfirmBtnListener(results, resultsType)
        })
    }
    if (editBtn) {
        editBtn.addEventListener("click", () => {
            executeEditBtnListener(results, resultsType)
        })
    }
}

const executeSelectResultBtnListener = (results, resultsType) => {    
    saveResult(results, resultsType)
    endProcess()
    selectResultBtn.removeEventListener("click", executeSelectResultBtnListener)
}


const executeConfirmBtnListener = (results, resultsType) => {
    saveResult(results, resultsType)
    endProcess()
    confirmBtn.removeEventListener("click", executeConfirmBtnListener)
    
}

const executeEditBtnListener = (results, resultsType) => {
    saveResult(results, resultsType)
    prepareEditonProcess()
    editBtn.removeEventListener("click", executeEditBtnListener)
}

const saveResult = (results, resultsType) => {
    if (resultsType === "author") {
        console.log(author)
        author = results[findSelectedResult()]
    } else if (resultsType === "bookwork") {
        bookwork = results[findSelectedResult()]
    } else if (resultsType === "edition") {
        newEdition = results[findSelectedResult()]
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
    printSelectedResult()
    closeModal(operation, table)
    toggleNextPageChanging(resultsType)

    results = "",
        resultsType = "",
        operation = ""
}

const enableCloseModalBtn = (operation, table) => {
    closeSymbol.classList.add("active")

    closeSymbol.addEventListener("click", () => {
        if (closeSymbol.classList.contains("active")) {
            closeModal(operation, table)
        }
    })
}

const disableCloseModalBtn = () => {
    modal.querySelector(".close_symbol").classList.remove("active")
}


const closeModal = (operation, table) => {
    let tableBody = table.querySelector(".results_table_body"),
        errorMessageRow = tableBody.querySelector(".error_message_row")

    tableBody.innerHTML = ""
    tableBody.appendChild(errorMessageRow)

    table.classList.add("hidden")

    if (table.querySelector("th.select_column")) {
        table.querySelector("th.select_column").remove()
    }

    table = ""

    modal.classList.add("hidden")
    selectResultBtn.setAttribute("disabled", true)

    if (operation === "search") {
        d.querySelector(".modal_btns_container .select_btn").classList.add("hidden")
    } else if (operation === "create") {
        d.querySelector(".modal_btns_container .create_btns").classList.add("hidden")
    }

    toggleNextPageChanging()
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
