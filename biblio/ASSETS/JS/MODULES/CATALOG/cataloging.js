const d = document,
    currentPage = d.getElementById("cataloging-section"),
    pageLinks = d.querySelectorAll(".page-link"),
    pages = d.querySelectorAll(".page"),
    searchAuthorForm = d.querySelector(".form.search_author_form"),
    createAuthorForm = d.querySelector(".form.create_author_form"),
    searchBookworkForm = d.querySelector(".form.search_bookwork_form"),
    createBookworkForm = d.querySelector(".form.create_authorbookwork_form"),
    modal = d.getElementById("modal"),
    resultsTable = d.getElementById("results-table"),
    template = d.getElementById("template"),
    fragment = d.createDocumentFragment();

const authorTHead = 
    `
        <thead>
            <th>Author id</td>
            <th>First name</th>
            <th>Last name</th>
        </thead>
    `,


    bookworkTHead = 
    `
        <thead>
            <th>Book work id</th>
            <th>Title</th>
            <th>Author</th>
            <th>Publication Year</th>
        </thead>
    `;




pageLinks.forEach(pageLink => {
    pageLink.addEventListener("click", e => {
        e.preventDefault();
        showPage(e.target.name);
    });
});

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

d.addEventListener("submit", async e => {

    e.preventDefault();

    let results;
    if (e.target === searchAuthorForm) {

        results = fetchRequest(
            "GET",
            joinParamsToURL(
                "http://localhost:8080/authors-catalog/search-author",
                {
                    author_name: e.target.author_name.value
                }
            )
        );
    } else if (e.target === createAuthorForm) {

        results = fetchRequest(
            "POST",
            "http://localhost:8080/authors-catalog/save-author",
            {
                firstName: e.target.firstName.value,
                lastName: e.target.lastName.value
            }
        );
    }
    showResults(authorTHead, results);
})


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

const showResults = (thead, searchResults) => {
    modal.style.display = "flex";
    modal.style.alignItems = "center";
    modal.style.justifyContent = "center";
    generaTableContent(thead, searchResults);
}


const generaTableContent = (thead, searchResults) => {

    let tableContent = `${thead}<tbody>`;


    for (let i = 0; i < searchResults.length; i++) {
        tableContent += '<tr>';

        Object.keys(searchResults[i])
            .map(key => {
                console.log(searchResults[i])
                console.log(searchResults[key])
                console.log(searchResults[i][key])

                tableContent += 
                    `
                    <td>
                        ${searchResults[i][key]}
                    </td>
                    `;
            });
        tableContent += '</tr>';
    }

    tableContent += '</tbody>';

    resultsTable.innerHTML = tableContent;

}

const selectResult = () => {

}


showPage("author-page");
