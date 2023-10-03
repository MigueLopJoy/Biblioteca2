import { generateAuthorsTableContent } from "./cataloging.js"
import { generateBookworksTableContent } from "./cataloging.js"
import { generateNewBookeditionTableContent } from "./cataloging.js"
import { generateBookeditionsTableContent } from "./registering.js"
import { generateNewBookcopyTableContent } from "./registering.js"
import { author } from "./cataloging.js"
import { bookwork } from "./cataloging.js"
import { bookedition } from "./registering.js"


const d = document;

/* Global methods*/

const enableWindowNavLinkBtns = () => {
    const pageLinks = d.querySelectorAll(".page_link")

    pageLinks.forEach(pageLink => {
        pageLink.addEventListener("click", e => {
            if (e.target.classList.contains("enabled")) {
                e.preventDefault();
                showPage(e.target.classList[1]);
            }
        });
    })
}

const showPage = pageOption => {
    const pages = d.querySelectorAll(".page"),
        pageLinks = d.querySelectorAll(".page_link")

    pages.forEach(page => {
        page.classList.add("hidden")
    });
    pageLinks.forEach(pageLink => {
        pageLink.classList.remove("active");
    })
    d.querySelector(`.page_link.${pageOption}`).classList.add("active")
    d.querySelector(`.page.${pageOption}`).classList.remove("hidden")

    enablePreviousPageBtn()
}


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


/* Display results methods */

const showSearchResults = (table, operation) => {
    const modal = d.getElementById("modal")

    modal.classList.remove("hidden")
    table.classList.remove("hidden")

    generaTableContent()

    if (operation === "search") {
        d.querySelector(".modal_btns_container .select_btn").classList.remove("hidden")

        if (table.classList.contains(".authors_results_table")) {
            selectResultBtn.textContent = "Select author";
        } else if (table.classList.contains(".bookworks_results_table")) {
            selectResultBtn.textContent = "Select book work";
        } else if (table.classList.contains(".newEdition_results_table")) {
            selectResultBtn.textContent = "Save new edition";
        } else if (table.classList.contains(".editions_results_table")) {
            selectResultBtn.textContent = "Select book edition";
        } else if (table.classList.contains(".newBookCopy_results_table")) {
            selectResultBtn.textContent = "Select book edition";
        }
    } else if (operation === "create") {
        d.querySelector(".modal_btns_container .create_btns").classList.remove("hidden")
    }
}

const generaTableContent = table => {
    if (table === authorsResultsTable) {
    } else if (table === bookworksResultsTable) {
    } else if (table === bookEditionTable) {
    } 

    if (table.classList.contains(".authors_results_table")) {
        generateAuthorsTableContent()
    } else if (table.classList.contains(".bookworks_results_table")) {
        generateBookworksTableContent()
    } else if (table.classList.contains(".newEdition_results_table")) {
        generateNewBookeditionTableContent()
    } else if (table.classList.contains(".editions_results_table")) {
        selectResultBtn.textContent = "Select book edition";
    } else if (table.classList.contains(".newBookCopy_results_table")) {
        selectResultBtn.textContent = "Select book edition";
    }
}

const closeModal = () => {
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


export { enableWindowNavLinkBtns }
export { showPage }
export { findCurrentPage }

export { toggleNextPageChanging }
export { clearFormsData }
export { clearPrintedReults }

export { fetchRequest }

export { handleErrorMessages }
export { clearErrorMessages } 

export { showSearchResults }
export { closeModal }
