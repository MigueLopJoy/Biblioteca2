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

    clearErrorMessages()
    clearFormData(e.target)

    if (currentPage.classList.contains("author_page")) {

        author = ""
        table = authorsResultsTable;
        resultsType = "author";
        deletePrintedReults()

        if (e.target === searchAuthorForm) {
            operation = "search"
            await getSearchAuthorResults(e.target)
        } else if (e.target === createAuthorForm) {
            operation = "create"
            await getCreateAuthorResults(e.target)
        }

    } else if (currentPage.classList.contains("bookwork_page")) {

        bookwork = ""
        table = bookworksResultsTable;
        resultsType = "bookwork";
        deletePrintedReults()

        if (e.target === searchBookworkForm) {
            operation = "search"
            await getSearchBookworkResults(e.target)
        } else if (e.target === createBookworkForm) {
            operation = "create"
            await getCreatehBookworkResults(e.target)
        }

    } else if (currentPage.classList.contains("edition_page")) {

        edition = ""
        resultsType = "edition";
        table = bookEditionTable;
        operation = "create"
        
        await getCreateBookeditionResults(e.target)
    }

    if (error) {
        handleErrorMessages()
        error = null
    } else {
        showSearchResults()
        enableModalActions()
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

const getEditAuthorResults = async editedFields => {
    await fetchRequest(
        "PUT",
        `http://localhost:8080/authors-catalog/edit-author/${results[0].idAuthor}`,
        {
            firstName: editedFields[0],
            lastName: editedFields[1]
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

const getEditBookworkResults = async editedFields => {
    await fetchRequest(
        "POST",
        `http://localhost:8080/bookworks-catalog/edit-bookwork/${results[0].idBookWork}`,
        {
            title: editedFields[0],
            author: {
                firstName: author.firstName,
                lastName: author.lastName
            },
            publicationYear: editedFields[2]
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

const getEditBookeditionResults = async editedFields => {
    await fetchRequest(
        "POST",
        `http://localhost:8080/general-catalog/save-bookedition/${results[0].idBookEdition}`,
        {
            isbn: editedFields[2],
            editor: editedFields[3],
            editionYear: editedFields[4],
            language: editedFields[5],
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

const handleErrorMessages = () => {
    if (error.status === 422) {
        handle422Exception()
    }
}

const handle422Exception = () => {
    error.validationErrors.forEach(er => {
        findCurrentPage().querySelector(`.error_message.${er.field}`).textContent = er.message
    })
}

const clearErrorMessages = () => {
    findCurrentPage().querySelectorAll(".error_message").forEach(el => {
        el.textContent = ""
    })
}

const showSearchResults = () => {
    modal.classList.remove("hidden")
    table.classList.remove("hidden")
    generaTableContent()

    if (operation === "search") {
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
}

const enableModalActions = () => {
    if (operation === "search") {
        enableOptionChangigng()
        enableCloseModalBtn()
        enableSelectResultBtn()
    } else if (operation === "create") {
        disableCloseModalBtn()
        enableCreateBtns()
    }
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
    if (operation === "search" && !table.querySelector("th.select_column")) {
        let selectColumn = d.createElement("th")
        selectColumn.textContent = "Select Autor"
        selectColumn.classList.add("select_column")
        table.querySelector("thead tr").appendChild(selectColumn)        
    }

    for (let i = 0; i < results.length; i++) {

        let result = results[i];

        let newRow = d.createElement("tr");

        let firstName = d.createElement("td");
        firstName.textContent = result.firstName;

        let lastName = d.createElement("td");
        lastName.textContent = result.lastName;

        let selectAuthor, checkbox
        if (operation === "search") {
            selectAuthor = d.createElement("td")
            checkbox = d.createElement("input")
            checkbox.type = "checkbox"
            checkbox.name = `select-author`
            checkbox.classList.add('result_option')
            checkbox.classList.add('author_result_option')
            checkbox.value = i;
            selectAuthor.appendChild(checkbox)
        }

        newRow.appendChild(firstName)
        newRow.appendChild(lastName)
        if (selectAuthor) {
            newRow.appendChild(selectAuthor)
        }
        table.querySelector(".results_table_body").appendChild(newRow)
    }
}

const generateBookworksTableContent = () => {
    
    if (operation === "search" && !table.querySelector("th.select_column")) {
        let selectColumn = d.createElement("th")
        selectColumn.textContent = "Select book work"
        selectColumn.classList.add("select_column")
        table.querySelector("thead tr").appendChild(selectColumn)
    }

    for (let i = 0; i < results.length; i++) {

        let result = results[i];

        let newRow = d.createElement("tr");

        let title = d.createElement("td");
        title.textContent = result.title;

        let bookAuthor = d.createElement("td");
        bookAuthor.textContent = `${author.firstName} ${author.lastName}`;

        let publicationYear = d.createElement("td");
        publicationYear.textContent = `${result.publicationYear ? result.publicationYear : "Unknown"}`;


        let selectBookwork, checkbox
        if (operation === "search") {
            selectBookwork = d.createElement("td"),
            checkbox = d.createElement("input")
            checkbox.type = "checkbox"
            checkbox.name = `select-bookwork`
            checkbox.classList.add('result_option')
            checkbox.classList.add('bookwork_result_option')
            checkbox.value = i
            selectBookwork.appendChild(checkbox)
        }

        newRow.appendChild(title)
        newRow.appendChild(bookAuthor)
        newRow.appendChild(publicationYear)
        if (selectBookwork) {
            newRow.appendChild(selectBookwork)
        }

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

const enableCreateBtns = () => {
    confirmBtn.addEventListener("click", executeConfirmBtnListener)
    editBtn.addEventListener("click", executeEditBtnListener)
}

const enableSelectResultBtn = () => {
    selectResultBtn.addEventListener("click", executeSelectResultBtnListener)
}

const executeConfirmBtnListener = () => {
    saveResult()
    endProcess()
    confirmBtn.removeEventListener("click", executeConfirmBtnListener)
}

const executeEditBtnListener = () => {
    saveResult()
    prepareEditonProcess()
    editBtn.removeEventListener("click", executeEditBtnListener)
}

const executeSelectResultBtnListener = () => {
    saveResult()
    endProcess()
    selectResultBtn.removeEventListener("click", executeSelectResultBtnListener)
}

const endProcess = () => {
    printSelectedResult()
    closeModal()
    enableNextPateBtn()
}

const saveResult = () => {
    if (resultsType === "author") {
        author = results[findSelectedResult()]
    } else if (resultsType === "bookwork") {
        bookwork = results[findSelectedResult()]
    } else if (resultsType === "edition") {
        newEdition = results[findSelectedResult()]
    }
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
}

const prepareEditonProcess = () => {

    const tbody = table.querySelector(".results_table_body"),
          cells = tbody.querySelectorAll("td")

    deletePrintedReults()

    if (resultsType === "author") {

        cells[0].innerHTML = `<input type="text" class="edition" value="${author.firstName}" >`
        cells[1].innerHTML = `<input type="text" class="edition"value="${author.lastName}" >`

        author = ""
    } else if (resultsType = "bookwork") {
        cells[0].innerHTML = `<input type="text" class="edition" value="${bookwork.title}" >`
        cells[2].innerHTML = `<input type="number" class="edition" value="${bookwork.publicationYear}" >`   

        bookwork = ""
    } else if (resultsType === "edition") {

        cells[2].innerHTML = `<input type="text" class="edition" value="${edition.isbn}" >`;
        cells[3].innerHTML = `<input type="text" class="edition" value="${edition.editor}" >`;
        cells[4].innerHTML = `<input type="number" class="edition" value="${edition.editionYear}" >`;
        cells[5].innerHTML = `<input type="text" class="edition" value="${edition.language}" >`;

        edition = ""
    }

    confirmBtn.removeEventListener("click", executeConfirmBtnListener)
    confirmBtn.addEventListener("click", confirmEdition)

}

const confirmEdition = async () => {

    const tbody = table.querySelector(".results_table_body"),
          editedFields = [...tbody.querySelectorAll("td input")].map(input => input.value)
          
          console.log(editedFields)

    if (resultsType === "author") {
        await getEditAuthorResults(editedFields)
    } else if (resultsType === "bookwork") {
        await getEditBookworkResults(editedFields)
    } else if (resultsType === "edition") {
        await getEditBookeditionResults(editedFields)
    }
    saveResult()
    endProcess()
    confirmBtn.removeEventListener("click", confirmEdition)
}

const deletePrintedReults = () => {
    if (resultsType === "author") {
        d.querySelectorAll(".selected_result_holder .selected_author").forEach(el => {
            el.textContent = "Author: "
        })
    } else if (resultsType === "bookwork") {
        d.querySelectorAll(".selected_result_holder .selected_bookwork").forEach(el => {
            el.textContent = "Book work: "
        })
    }
}

const clearFormData = form => {
    form.querySelectorAll("input").forEach(input => {
        if (input.type !== "submit") {
            input.value === ""
        }
    })
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

const enableCloseModalBtn = () => {
    let closeSymbol = modal.querySelector(".close_symbol")
    closeSymbol.classList.add("active")

    closeSymbol.addEventListener("click", e => {
        if (closeSymbol.classList.contains("active")) {
            closeModal()
        }
    })
}

const disableCloseModalBtn = () => {
    modal.querySelector(".close_symbol").classList.remove("active")
}

const closeModal = () => {
    table.querySelector(".results_table_body").innerHTML = ""        
    table.classList.add("hidden")
        
    modal.classList.add("hidden")
    selectResultBtn.setAttribute("disabled", true)

    if (operation === "search") {
        d.querySelector(".modal_btns_container .select_btn").classList.add("hidden")
    } else if (operation === "create") {

        d.querySelector(".modal_btns_container .create_btns").classList.add("hidden")
    }
    restartConfigurations()
}

const restartConfigurations = () => {
    results = "",
    resultsType = "", 
    operation = ""

    if (table.querySelector("th.select_column")) {
        table.querySelector("th.select_column").remove()
    }

    table = ""
}

showPage("author_page");
