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

const toggleNextPageChanging = () => {
    let nextPageBtn = findCurrentPage().querySelector(".change_page_container .next_page")

    if (resultsType === "author") {
        if (!author) {
            if (nextPageBtn) {
                if (!nextPageBtn.classList.contains("disabled")) {
                    nextPageBtn.classList.add("disabled")
                }
            }
            if (pageLinks[1].classList.contains("enabled")) {
                pageLinks[1].classList.remove("enabled")
            }
            if (pageLinks[2].classList.contains("enabled")) {
                pageLinks[2].classList.remove("enabled")
            }
        } else {
            if (nextPageBtn) {
                if (nextPageBtn.classList.contains("disabled")) {
                    nextPageBtn.classList.remove("disabled")
                }
            }
            if (!pageLinks[1].classList.contains("enabled")) {
                pageLinks[1].classList.add("enabled")
            }
        }
    } else if (resultsType === "bookwork") {
        if (!bookwork) {
            if (nextPageBtn) {
                if (!nextPageBtn.classList.contains("disabled")) {
                    nextPageBtn.classList.add("disabled")
                }
            }
            if (pageLinks[2].classList.contains("enabled")) {
                pageLinks[2].classList.remove("enabled")
            }
        } else {
            if (nextPageBtn) {
                if (nextPageBtn.classList.contains("disabled")) {
                    nextPageBtn.classList.remove("disabled")
                }
            }
            if (!pageLinks[2].classList.contains("enabled")) {
                pageLinks[2].classList.add("enabled")
            }
        }
    }

    if (nextPageBtn) {
        nextPageBtn.addEventListener("click", () => {
            if (!nextPageBtn.classList.contains("disabled")) {
                showPage(nextPageBtn.classList[2])
            }
        })
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
    }
}

const clearFormsData = page => {
    page.querySelectorAll(".form").forEach(form => {
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

/* --- Fetching method */

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
        console.log(error)
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

export { enableWindowNavLinkBtns }
export { showPage }
export { findCurrentPage }

export { toggleNextPageChanging }
export { clearFormsData }
export { clearPrintedReults }

export { fetchRequest }

export { handleErrorMessages }
export { clearErrorMessages } 
