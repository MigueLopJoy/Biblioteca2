const d = document,
    currentPage = d.getElementById("cataloging-section"),
    pageLinks = d.querySelectorAll(".page-link"),
    pages = d.querySelectorAll(".page"),
    searchAuthorForm = d.querySelector(".form.search_author_form"),
    createAuthorForm = d.querySelector(".form.create_author_form"),
    searchBookworkForm = d.querySelector(".form.search_bookwork_form"),
    createBookworkForm = d.querySelector(".form.create_authorbookwork_form"),
    authorsResultsTable = d.querySelector(".results-table.authors-results-table"),
    bookworksResultsTable = d.querySelector(".results-table.bookworks-results-table"),
    modal = d.getElementById("modal"),
    selectResultBtn = d.getElementById("select-result");

let author, bookwork;

pageLinks.forEach(pageLink => {
    pageLink.addEventListener("click", e => {
        e.preventDefault();
        showPage(e.target.name);
    });
});


d.addEventListener("submit", async e => {

    e.preventDefault();

    let results;
    if (e.target === searchAuthorForm || e.target === createAuthorForm) {
        if (e.target === searchAuthorForm) {

            results = await fetchRequest(
                "GET",
                joinParamsToURL(
                    "http://localhost:8080/authors-catalog/search-author",
                    {
                        author_name: e.target.author_name.value
                    }
                )
            );
        } else if (e.target === createAuthorForm) {

            results = await fetchRequest(
                "POST",
                "http://localhost:8080/authors-catalog/save-author",
                {
                    firstName: e.target.firstName.value,
                    lastName: e.target.lastName.value
                }
            );
        }
        console.log(results)
        showResults(authorsResultsTable, results);
    } else if (null) {

    }

})


d.addEventListener("click", e => {
    if (e.target === selectResultBtn) {

    }
})


const showPage = pageOption => {
    pages.forEach(page => {
        page.classList.remove("active");
    });
    pageLinks.forEach(pageLink => {
        pageLink.classList.remove("active");
    })
    d.querySelector(`.page-link.${pageOption}`).classList.toggle("active");
    document.querySelector(`.page.${pageOption}`).classList.add("active");
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

const showResults = (table, searchResults) => {
    modal.style.display = "block";
    table.style.display = "table";
    generaTableContent(table, searchResults);
}

const generaTableContent = (table, searchResults) => {

    for (let i = 0; i < searchResults.length; i++) {

        let result = searchResults[i],
            newRow;

        if (table === authorsResultsTable) {
            newRow = d.createElement("tr");

            let firstName = d.createElement("td");
            firstName.textContent = result.firstName;


            let lastName = d.createElement("td");
            lastName.textContent = result.lastName;

            let selectAuthor = d.createElement("td"),
                checkbox = d.createElement("input");
            checkbox.type = "checkbox";
            checkbox.name = `select-author`;
            checkbox.name = `select-result select-author`;
            checkbox.value = i;
            selectAuthor.appendChild(checkbox);

            newRow.appendChild(firstName);
            newRow.appendChild(lastName);
            newRow.appendChild(selectAuthor);

        } else if (table === bookworksResultsTable) {
            newRow = d.createElement("tr");

            let title = d.createElement("td");
            title.textContent = result.title;


            let author = d.createElement("td");
            author.textContent = `${result.author.firstName} ${result.author.lastName}`;

            let selectBookwork = d.createElement("td"),
                checkbox = d.createElement("input");
            checkbox.type = "checkbox";
            checkbox.name = `select-bookwork`;
            checkbox.name = `select-result select-bookwork`;
            checkbox.value = i;
            selectBookwork.appendChild(checkbox);

            newRow.appendChild(title);
            newRow.appendChild(author);
            newRow.appendChild(selectBookwork);
        }
        table.querySelector(".results-table-body").appendChild(newRow);
    }
}

const generateAuthorsTableContent = () => {

}

const generateBookworksTableContent = () => {

}

const selectResult = () => {
    const checkboxes = document.querySelectorAll('input[class="select-result"]');

    checkboxes.forEach(checkbox => {
        checkbox.addEventListener("change", () => {
            checkboxes.forEach(cb => {
                if (cb !== checkbox) {
                    cb.checked = false;
                }
            });
        });
    });
}


showPage("author-page");
