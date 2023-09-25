const d = document,
    currentPage = d.getElementById("cataloging-section"),
    pageLinks = document.querySelectorAll(".page-link"),
    pages = document.querySelectorAll(".page"),
    searchAuthorForm = document.getElementById("search_author_form"),
    createAuthorForm = document.getElementById("create_author_form");


pageLinks.forEach(pageLink => {
    pageLink.addEventListener("click", e => {
        e.preventDefault();
        showPage(e.target.name);
    });
});


d.addEventListener("submit", async e => {

    e.preventDefault();

    if (e.target === createAuthorForm) {

        fetchRequest(
            "POST",
            {
                firstName: e.target.firstName.value,
                lastName: e.target.lastName.value
            },
            "http://localhost:8080/authors-catalog/save-author"
        );

    } else if (e.target === searchAuthorForm) {

        fetchGetRequest(
            "http://localhost:8080/authors-catalog/search-author",
            e.target.author_name.value);
    }
})


const fetchRequest = async (method, bodyContent, url) => {

    try {
        let options = {
            method: method,
            headers: {
                "Content-type": "application/json; charset=utf-8"
            },
            body: JSON.stringify(
                bodyContent
            )
        },
            res = await fetch(url, options),
            json = await res.json();

        if (!res.ok) throw { status: res.status, statusText: res.statusText };

        console.log(json)

    } catch (error) {
        console.log(error)
    }
}

const fetchGetRequest = async (url, params) => {

    try {
        let options = {
            method: "GET",
            headers: {
                "Content-type": "application/json; charset=utf-8"
            }
        },

            res = await fetch(
                getRequestURL(url, params),
                options),

            json = await res.json();

        if (!res.ok) throw { status: res.status, statusText: res.statusText };

        console.log(json)

    } catch (error) {
        console.log(error)
    }
}

const getRequestURL = (baseURL, params) => {
    let queryParams = Object.keys(params)
        .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(queryParams[key])}`)
        .join('&'),

        completeURL = `${baseURL}?${queryParams}`;

    return completeURL;
}

const showPage = pageId => {
    pages.forEach(page => {
        page.classList.remove("active");
    });
    pageLinks.forEach(pageLink => {
        pageLink.classList.remove("active");
    })
    d.getElementsByName(pageId)[0].classList.toggle("active");
    document.getElementById(pageId).classList.add("active");
}


showPage("author-page");

