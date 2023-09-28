const d = document,
    currentPage = d.getElementById("cataloging-section"),
    pageLinks = d.querySelectorAll(".page-link"),
    pages = d.querySelectorAll(".page"),
    searchAuthorForm = d.querySelector(".form.search_author_form"),
    createAuthorForm = d.querySelector(".form.create_author_form"),
    searchBookworkForm = d.querySelector(".form.search_bookwork_form"),
    createBookworkForm = d.querySelector(".form.create_bookwork_form"),
    createBookEditionForm = d.querySelector(".form.create_edition_form"),
    authorsResultsTable = d.querySelector(".results_table.authors_results_table"),
    bookworksResultsTable = d.querySelector(".results_table.bookworks_results_table"),
    bookEditionTable = d.querySelector(".results_table.edition_results_table"),
    modal = d.getElementById("modal"),
    selectResultBtn = d.querySelector(".select_result_btn");

let author, bookwork, newEdition, results, resultsType;

pageLinks.forEach(pageLink => {
    pageLink.addEventListener("click", e => {
        e.preventDefault();
        showPage(e.target.classList[1]);
    });
});

d.addEventListener("submit", async e => {

    e.preventDefault();

    let table;

    if (e.target === searchAuthorForm || e.target === createAuthorForm) {

        table = authorsResultsTable;
        resultsType = "author";

        if (e.target === searchAuthorForm) {
            results = await fetchRequest(
                "GET",
                joinParamsToURL(
                    "http://localhost:8080/authors-catalog/search-author",
                    {
                        author_name: e.target.author_name.value
                    }
                )
            )
        } else if (e.target === createAuthorForm) {

            results = [await fetchRequest(
                "POST",
                "http://localhost:8080/authors-catalog/save-author",
                {
                    firstName: e.target.firstName.value,
                    lastName: e.target.lastName.value
                }
            )]
        }

    } else if (e.target === searchBookworkForm || e.target === createBookworkForm) {

        table = bookworksResultsTable;
        resultsType = "bookwork";

        if (e.target === searchBookworkForm) {
            results = await fetchRequest(
                "POST",
                "http://localhost:8080/bookworks-catalog/search-bookwork",
                {
                    title: e.target.title.value,
                    author: `${author.firstName} ${author.lastName}`
                }
            )
        } else if (e.target === createBookworkForm) {

            results = [await fetchRequest(
                "POST",
                "http://localhost:8080/bookworks-catalog/save-bookwork",
                {
                    title: e.target.title.value,
                    author: {
                        firstName: author.firstName,
                        lastName: author.lastName
                    },
                    publicationYear: e.target.publication_year.value
                }
            )]
        }
    } else if (e.target === createBookEditionForm) {

        resultsType = "edition";
        table = bookEditionTable;

        results = [await fetchRequest(
            "POST",
            "http://localhost:8080/general-catalog/save-bookedition",
            {
                isbn: e.target.isbn.value,
                editor: e.target.editor_name.value,
                editionYear: e.target.edition_year.value,
                language: e.target.edition_language.value,
                bookwork: bookwork
            }
        )]
    }
    showSearchResults(table);
    selectResult();
})

const showPage = pageOption => {
    pages.forEach(page => {
        page.classList.add("hidden")
    });
    pageLinks.forEach(pageLink => {
        pageLink.classList.remove("active");
    })
    d.querySelector(`.page-link.${pageOption}`).classList.add("active");
    d.querySelector(`.page.${pageOption}`).classList.remove("hidden");
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
            res = await fetch(url, options),
            json = await res.json();

        if (!res.ok) throw { status: res.status, statusText: res.statusText };

        console.log(json)
        return json;

    } catch (error) {
        console.log(error)
    }
}

const joinParamsToURL = (baseURL, params) => {
    let queryParams = Object.keys(params)
        .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
        .join('&');

    return `${baseURL}?${queryParams}`;
}

const showSearchResults = table => {
    modal.classList.remove("hidden")
    table.classList.remove("hidden")
    generaTableContent(table)

    if (table === authorsResultsTable) {
        selectResultBtn.textContent = "Select author";
    } else if (table === bookworksResultsTable) {
        selectResultBtn.textContent = "Select book work";
    } else if (table === bookEditionTable) {
        selectResultBtn.textContent = "Save new edition";
    }

    changeOption();
    enableCloseModalBtn();
}

const generaTableContent = (table) => {
    if (table === authorsResultsTable) {
        generateAuthorsTableContent(table)
    } else if (table === bookworksResultsTable) {
        generateBookworksTableContent(table)
    }
}

const generateAuthorsTableContent = (table) => {
    for (let i = 0; i < results.length; i++) {

        let result = results[i];

        let newRow = d.createElement("tr");

        let firstName = d.createElement("td");
        firstName.textContent = result.firstName;

        let lastName = d.createElement("td");
        lastName.textContent = result.lastName;

        let selectAuthor = d.createElement("td"),
            checkbox = d.createElement("input");
        checkbox.type = "checkbox";
        checkbox.name = `select-author`;
        checkbox.classList.add('result_option');
        checkbox.classList.add('author_result_option');
        checkbox.value = i;
        selectAuthor.appendChild(checkbox);

        newRow.appendChild(firstName);
        newRow.appendChild(lastName);
        newRow.appendChild(selectAuthor);
        table.querySelector(".results_table_body").appendChild(newRow);
    }
}

const generateBookworksTableContent = table => {
    for (let i = 0; i < results.length; i++) {

        let result = results[i];

        let newRow = d.createElement("tr");

        let title = d.createElement("td");
        title.textContent = result.title;

        let bookAuthor = d.createElement("td");
        bookAuthor.textContent = `${author.firstName} ${author.lastName}`;

        let publicationYear = d.createElement("td");
        publicationYear.textContent = `${result.publicationYear}`;

        let selectBookwork = d.createElement("td"),
            checkbox = d.createElement("input");
        checkbox.type = "checkbox";
        checkbox.name = `select-bookwork`;
        checkbox.classList.add('result_option');
        checkbox.classList.add('bookwork_result_option');
        checkbox.value = i;
        selectBookwork.appendChild(checkbox);

        newRow.appendChild(title);
        newRow.appendChild(bookAuthor);
        newRow.appendChild(publicationYear);
        newRow.appendChild(selectBookwork);
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
    enableNextPageBtn()
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

const enableNextPageBtn = () => {
    let nextStepBtn,
        i = 0,
        nextStepBtns = d.querySelectorAll(".nextstep_container span"),
        found = false,
        page, btn;

    nextStepBtns.forEach(btn => {
        btn.classList.add("disabled")
    })


    do {
        if (!pages[i].classList.contains("hidden")) {
            page = pages[i]
            found = true
        } else {
            i++
        }
    } while (!found && i < pages.length)

    if (found) {
        i = 0
        found = false
    }

    do {
        btn = nextStepBtns[i]

        if (page.classList[1] === btn.classList[1]) {
            nextStepBtn = btn
            found = true
        } else {
            i++
        }
    } while (!found && i < nextStepBtns.length)

    nextStepBtn.classList.remove("disabled")

    nextStepBtn.addEventListener("click", e => {
        showPage(nextStepBtn.classList[2])
    })
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
