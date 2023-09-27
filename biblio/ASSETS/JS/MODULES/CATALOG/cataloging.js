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
    selectResultBtn = d.querySelector(".select_result");

let author, bookwork;

pageLinks.forEach(pageLink => {
    pageLink.addEventListener("click", e => {
        e.preventDefault();
        showPage(e.target.classList[1]);
    });
});

d.addEventListener("submit", async e => {

    e.preventDefault();

    let results, table, resultsType;

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
                ISBN: e.target.isbn.value,
                editor: e.target.editor_name.value,
                editionYear: e.target.edition_year.value,
                language: e.target.edition_language.value,
                bookwork: bookwork
            }
        )]
    }
    showSearchResults(table, results);
    selectResult(results, resultsType);
})

const showPage = pageOption => {
    pages.forEach(page => {
        page.classList.remove("active");
    });
    pageLinks.forEach(pageLink => {
        pageLink.classList.remove("active");
    })
    d.querySelector(`.page-link.${pageOption}`).classList.add("active");
    d.querySelector(`.page.${pageOption}`).classList.add("active");
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

const showSearchResults = (table, searchResults) => {
    modal.style.display = "block";
    table.style.display = "table";
    generaTableContent(table, searchResults);

    if (table === authorsResultsTable) {
        selectResultBtn.textContent = "Select author";
    } else if (table === bookworksResultsTable) {
        selectResultBtn.textContent = "Select book work";
    } else if (table === bookEditionTable) {
        selectResultBtn.textContent = "Save new edition";
    }
    changeOption();
}

const generaTableContent = (table, searchResults) => {
    if (table === authorsResultsTable) {
        generateAuthorsTableContent(table, searchResults)
    } else if (table === bookworksResultsTable) {
        generateBookworksTableContent(table, searchResults)
    }
}

const generateAuthorsTableContent = (table, searchResults) => {
    for (let i = 0; i < searchResults.length; i++) {

        let result = searchResults[i];

        let newRow = d.createElement("tr");

        let firstName = d.createElement("td");
        firstName.textContent = result.firstName;

        let lastName = d.createElement("td");
        lastName.textContent = result.lastName;

        let selectAuthor = d.createElement("td"),
            checkbox = d.createElement("input");
        checkbox.type = "checkbox";
        checkbox.name = `select-author`;
        checkbox.classList.add('select-result');
        checkbox.classList.add('select-author');
        checkbox.value = i;
        selectAuthor.appendChild(checkbox);

        newRow.appendChild(firstName);
        newRow.appendChild(lastName);
        newRow.appendChild(selectAuthor);
        table.querySelector(".results_table_body").appendChild(newRow);
    }
}

const generateBookworksTableContent = (table, searchResults) => {
    for (let i = 0; i < searchResults.length; i++) {

        let result = searchResults[i];

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
        checkbox.classList.add('select-result');
        checkbox.classList.add('select-bookwork');
        checkbox.value = i;
        selectBookwork.appendChild(checkbox);

        newRow.appendChild(title);
        newRow.appendChild(bookAuthor);
        newRow.appendChild(publicationYear);
        newRow.appendChild(selectBookwork);
        table.querySelector(".results_table_body").appendChild(newRow);
    }
}

const selectResult = (searchResults, resultType) => {
    d.addEventListener("click", e => {
        if (e.target === selectResultBtn) {
            if (resultType === "author") {
                author = searchResults[findSelectedResult()];
                printSelectedResult(resultType)
            } else if (resultType === "bookwork") {
                bookwork = searchResults[findSelectedResult()];
                printSelectedResult(resultType)
                bookworksResultsTable.style.display = "none";
            }
            modal.style.display = "none";
        }
    });
}

const printSelectedResult = (resultType) => {
    if (resultType === "author") {
        d.querySelectorAll(".selected-result-holder .selected-author").forEach(el => {
            el.textContent = `${author.firstName} ${author.lastName}`;
            authorsResultsTable.style.display = "none";
        })
    } else if (resultType === "bookwork") {
        d.querySelectorAll(".selected-result-holder .selected-bookwork").forEach(el => {
            el.textContent = `${bookwork.title}`;
            authorsResultsTable.style.display = "none";
        })
    }
}

const findSelectedResult = () => {
    const checkboxes = d.querySelectorAll('input.select-result');
    for (const checkbox of checkboxes) {
        if (checkbox.checked === true) {
            return checkbox.value
        }
    }
    return null
}

d.addEventListener("change", e => {
    if (e.target === d.querySelector("input[type='checkbox'].select-result")) {
        changeBtnState(document.querySelectorAll('input[class="select-result"]'));
    }
})

const changeOption = () => {
    const checkboxes = document.querySelectorAll('input[class="select-result"]');

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener("change", () => {
            checkboxes.forEach(cb => {
                if (cb !== checkbox) {
                    cb.checked = false;
                }
            });
            changeBtnState(checkbox);
        });
    });
}

const changeBtnState = checkbox => {
    const onceAtLeastSelected = [...checkboxes].some(checkbox => checkbox.checked);
    selectResultBtn.disabled = !onceAtLeastSelected;
}

showPage("author-page");
