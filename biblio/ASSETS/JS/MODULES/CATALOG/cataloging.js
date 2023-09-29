const d = document,
    currentPage = d.getElementById("cataloging-section"),
    pageLinks = d.querySelectorAll(".page-link"),
    pages = d.querySelectorAll(".page"),
    searchAuthorForm = d.querySelector(".form.author_form.search"),
    createAuthorForm = d.querySelector(".form.author_form.create"),
    searchBookworkForm = d.querySelector(".form.bookwork_form.search"),
    createBookworkForm = d.querySelector(".form.bookwork_form.create"),
    createBookEditionForm = d.querySelector(".form.edition_form.create"),
    authorsResultsTable = d.querySelector(".results_table.authors_results_table"),
    bookworksResultsTable = d.querySelector(".results_table.bookworks_results_table"),
    bookEditionTable = d.querySelector(".results_table.edition_results_table"),
    modal = d.getElementById("modal"),
    selectResultBtn = d.querySelector(".modal_btns_container .select_result_btn"),
    confirmBtn = d.querySelector(".modal_btns_container .confirm_btn"),
    editBtn = d.querySelector(".modal_btns_container .edit_btn")

let author, bookwork, newEdition, results, error, table, resultsType, operation;

pageLinks.forEach(pageLink => {
    pageLink.addEventListener("click", e => {
        e.preventDefault();
        showPage(e.target.classList[1]);
    });
});

d.addEventListener("submit", async e => {
    e.preventDefault();

    let currentPage = findCurrentPage()

    cleanErrorMessages(currentPage)

    if (currentPage.classList.contains("author_page")) {

        table = authorsResultsTable;
        resultsType = "author";

        if (e.target === searchAuthorForm) {
            operation = "search"
            await getSearchAuthorResults(e.target)
        } else if (e.target === createAuthorForm) {
            operation = "create"
            await getCreateAuthorResults(e.target)
        }

    } else if (currentPage.classList.contains("bookwork_page")) {
        table = bookworksResultsTable;
        resultsType = "bookwork";

        if (e.target === searchBookworkForm) {
            await getSearchAuthorResults(e.target)
        } else if (e.target === createBookworkForm) {
            await getCreatehBookworkResults(e.target)
        }
    } else if (currentPage.classList.contains("edition_page")) {
        resultsType = "edition";
        table = bookEditionTable;

        await getCreateBookeditionResults(e.target)
    }

    if (error) {
        handleErrorMessages(currentPage)
        error = null
    } else {
        showSearchResults()
        selectResult()
    }
})

const getSearchAuthorResults = async form => {
    await fetchRequest(
        "GET",
        joinParamsToURL(
            "http://localhost:8080/authors-catalog/search-author",
            {
                author_name: form.author_name.value
            }
        )
    )
}

const getCreateAuthorResults = async form => {
    await fetchRequest(
        "POST",
        "http://localhost:8080/authors-catalog/save-author",
        {
            firstName: form.firstName.value,
            lastName: form.lastName.value
        }
    )
    results = [results]
}

const getSearchBookworkResults = async form => {
    await fetchRequest(
        "POST",
        "http://localhost:8080/bookworks-catalog/search-bookwork",
        {
            title: form.title.value,
            author: `${author.firstName} ${author.lastName}`
        }
    )
}

const getCreatehBookworkResults = async form => {
    await fetchRequest(
        "POST",
        "http://localhost:8080/bookworks-catalog/save-bookwork",
        {
            title: form.title.value,
            author: {
                firstName: author.firstName,
                lastName: author.lastName
            },
            publicationYear: form.publication_year.value
        }
    )
    results = [results]
}

const getCreateBookeditionResults = async form => {
    await fetchRequest(
        "POST",
        "http://localhost:8080/general-catalog/save-bookedition",
        {
            isbn: form.isbn.value,
            editor: form.editor_name.value,
            editionYear: form.edition_year.value,
            language: form.edition_language.value,
            bookWork: {
                title: bookwork.title,
                author: {
                    firstName: bookwork.author.firstName,
                    lastName: bookwork.author.lastName
                },
                publicationYear: bookwork.publicationYear
            }
        }
    )
    results = [results]
}

const showPage = pageOption => {
    pages.forEach(page => {
        page.classList.add("hidden")
    });
    pageLinks.forEach(pageLink => {
        pageLink.classList.remove("active");
    })
    d.querySelector(`.page-link.${pageOption}`).classList.add("active")
    d.querySelector(`.page.${pageOption}`).classList.remove("hidden")

    enablePreviousPageBtn()
}

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

        results = await res.json()

    } catch (ex) {
        const errorData = await ex.json(),
            errorResponse = {
                status: errorData.status,
                statusText: errorData.statusText,
                message: errorData.message,
                validationErrors: errorData.validationErrors
            }
        error = errorResponse
    }
}

const joinParamsToURL = (baseURL, params) => {
    let queryParams = Object.keys(params)
        .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
        .join('&')

    return `${baseURL}?${queryParams}`
}

const handleErrorMessages = currentPage => {
    if (error.status === 422) {
        handle422Exception(currentPage)
    }
}

const handle422Exception = currentPage => {
    error.validationErrors.forEach(er => {
        currentPage.querySelector(`.error_message.${er.field}`).textContent = er.message
    })
}

const cleanErrorMessages = page => {
    page.querySelectorAll(".error_message").forEach(el => {
        el.textContent = ""
    })
}

const showSearchResults = () => {
    modal.classList.remove("hidden")
    table.classList.remove("hidden")
    generaTableContent()

    if (operation = "search") {

        d.querySelector(".modal_btns_container .select_btn").classList.remove("hidden")

        if (table === authorsResultsTable) {
            selectResultBtn.textContent = "Select author";
        } else if (table === bookworksResultsTable) {
            selectResultBtn.textContent = "Select book work";
        } else if (table === bookEditionTable) {
            selectResultBtn.textContent = "Save new edition";
        }
    } else if (operation === "create") {
        d.querySelector(".modal_btns_container .create_btns").classList.remove("hidden")
    }

    changeOption();
    enableCloseModalBtn();
}

const generaTableContent = () => {
    if (table === authorsResultsTable) {
        generateAuthorsTableContent()
    } else if (table === bookworksResultsTable) {
        generateBookworksTableContent()
    } else if (table === bookEditionTable) {
        generateBookeditionsTableContent()
    }
}

const generateAuthorsTableContent = () => {
    for (let i = 0; i < results.length; i++) {

        let result = results[i];

        let newRow = d.createElement("tr");

        let firstName = d.createElement("td");
        firstName.textContent = result.firstName;

        let lastName = d.createElement("td");
        lastName.textContent = result.lastName;

        if (operation === "search") {
            let selectAuthor = d.createElement("td"),
                checkbox = d.createElement("input");
            checkbox.type = "checkbox";
            checkbox.name = `select-author`;
            checkbox.classList.add('result_option');
            checkbox.classList.add('author_result_option');
            checkbox.value = i;
            selectAuthor.appendChild(checkbox);
        }

        newRow.appendChild(firstName);
        newRow.appendChild(lastName);
        newRow.appendChild(selectAuthor);
        table.querySelector(".results_table_body").appendChild(newRow);
    }
}

const generateBookworksTableContent = () => {
    for (let i = 0; i < results.length; i++) {

        let result = results[i];

        let newRow = d.createElement("tr");

        let title = d.createElement("td");
        title.textContent = result.title;

        let bookAuthor = d.createElement("td");
        bookAuthor.textContent = `${author.firstName} ${author.lastName}`;

        let publicationYear = d.createElement("td");
        publicationYear.textContent = `${result.publicationYear ? result.publicationYear : "Unknown"}`;


        let selectBookwork
        if (operation === "search") {


            selectBookwork = d.createElement("td"),
                checkbox = d.createElement("input");
            checkbox.type = "checkbox";
            checkbox.name = `select-bookwork`;
            checkbox.classList.add('result_option');
            checkbox.classList.add('bookwork_result_option');
            checkbox.value = i;
            selectBookwork.appendChild(checkbox);
        }

        newRow.appendChild(title);
        newRow.appendChild(bookAuthor);
        newRow.appendChild(publicationYear);
        newRow.appendChild(selectBookwork);

        table.querySelector(".results_table_body").appendChild(newRow);
    }
}

const generateBookeditionsTableContent = () => {
    for (let i = 0; i < results.length; i++) {

        let result = results[i]

        let newRow = d.createElement("tr")

        let title = d.createElement("td")
        title.textContent = bookwork.title

        let bookAuthor = d.createElement("td")
        bookAuthor.textContent = `${author.firstName} ${author.lastName}`

        let isbn = d.createElement("td")
        isbn.textContent = result.isbn

        let editor = d.createElement("td")
        editor.textContent = result.editor

        let editionYear = d.createElement("td")
        editionYear.textContent = result.editor

        let language = d.createElement("td")
        language.textContent = result.language

        newRow.appendChild(title);
        newRow.appendChild(bookAuthor);
        newRow.appendChild(isbn);
        newRow.appendChild(editor);
        newRow.appendChild(editionYear);
        newRow.appendChild(language);
        newRow.appendChild(selectedBookedition);

        table.querySelector(".results_table_body").appendChild(newRow);
    }
}

const selectResult = () => {
    selectResultBtn.addEventListener("click", executeSelectResultBtnListener)
}

const executeSelectResultBtnListener = () => {
    if (resultsType === "author") {
        author = results[findSelectedResult()]
    } else if (resultsType === "bookwork") {
        bookwork = results[findSelectedResult()]
    }
    printSelectedResult(resultsType)
    closeModal()
    enableNextPateBtn()
    selectResultBtn.removeEventListener("click", executeSelectResultBtnListener)
}

const printSelectedResult = () => {
    if (resultsType === "author") {
        d.querySelectorAll(".selected_result_holder .selected_author").forEach(el => {
            el.textContent += `${author.firstName} ${author.lastName}`
        })
    } else if (resultsType === "bookwork") {
        d.querySelectorAll(".selected_result_holder .selected_bookwork").forEach(el => {
            el.textContent += `${bookwork.title}`
        })
    }
    results = "", resultsType = "";
}

const findSelectedResult = () => {
    const checkboxes = d.querySelectorAll('input.result_option');
    for (const checkbox of checkboxes) {
        if (checkbox.checked === true) {
            return checkbox.value
        }
    }
}

const enableNextPateBtn = () => {
    let currentPage = findCurrentPage(),
        nextPageBtn = currentPage.querySelector(".change_page_container .next_page")

    nextPageBtn.classList.remove("disabled")

    nextPageBtn.addEventListener("click", e => {
        showPage(nextPageBtn.classList[2])
    })

}

const enablePreviousPageBtn = () => {
    let currentPage = findCurrentPage(),
        previousPageBtn = currentPage.querySelector(".change_page_container .previous_page")

    if (previousPageBtn) {
        previousPageBtn.addEventListener("click", e => {
            showPage(previousPageBtn.classList[2])
        })
    }
}

const findCurrentPage = () => {
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

const changeOption = () => {
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

const enableCloseModalBtn = () => {
    d.addEventListener("click", e => {
        if (e.target === modal.querySelector(".close_symbol")) {
            closeModal()
        }
    })
}

const closeModal = () => {
    d.querySelectorAll(".results_table").forEach(table => {
        if (!table.classList.contains("hidden")) {
            table.querySelector(".results_table_body").innerHTML = ""
            table.classList.add("hidden")
        }
    })
    modal.classList.add("hidden")
    selectResultBtn.setAttribute("disabled", true)
}

showPage("author_page");
