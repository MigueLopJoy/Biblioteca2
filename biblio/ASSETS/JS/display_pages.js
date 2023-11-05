const d = document

const loadContent = async (url, component) => {
    let res = await fetch(url),
        content = await res.text()
    component.innerHTML = content
}

const loadJsFiles = async (...sources) => {

    removeScriptsExcept(
        'http://localhost/biblio/ASSETS/JS/scripts.js',
        'http://localhost/biblio/ASSETS/JS/listeners.js',
        'https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js'
    )

    for (let src of sources) {
        const script = document.createElement("script")
        script.type = "module"
        script.src = `${src}`
        document.body.appendChild(script)
    }
}

const removeScriptsExcept = (...scriptsToKeep) => {
    const scripts = document.querySelectorAll("script")

    for (let script of scripts) {

        let keep = false
        let i = 0
        do {
            if (script.src == scriptsToKeep[i]) {
                keep = true
            } else {
                i++
            }
        } while (!keep && i < scriptsToKeep.length)

        if (!keep) {
            script.remove()
        }
        keep = false
        i = 0
    }
}


const displayCatalogingMainPage = async () => {
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/cataloging.html", d.getElementById("main-content"))
    await loadJsFiles(
        "./ASSETS/JS/MODULES/CATALOG/cataloging.js",
        "./ASSETS/JS/MODULES/modules_commons.js")
    EnableLinks("author_page")
}

const displayRegisteringMainPage = async () => {
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/registering.html", d.getElementById("main-content"))
    await loadJsFiles(
        "./ASSETS/JS/MODULES/CATALOG/registering.js",
        "./ASSETS/JS/MODULES/modules_commons.js")
}

const displayAuthorsCatalogMainPage = async () => {
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/authors_catalog.html", d.getElementById("main-content"))
    await loadJsFiles(
        "./ASSETS/JS/MODULES/CATALOG/authors_catalog.js",
        "./ASSETS/JS/MODULES/modules_commons.js")
}

const displayBookWorksCatalogMainPage = async () => {
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/bookworks_catalog.html", d.getElementById("main-content"))
    await loadJsFiles(
        "./ASSETS/JS/MODULES/CATALOG/bookworks_catalog.js",
        "./ASSETS/JS/MODULES/modules_commons.js")
}

const displayBookEditionsCatalogMainPage = async () => {
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/bookeditions_catalog.html", d.getElementById("main-content"))
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/PAGES/search_bookedition.html", d.querySelector(".page_element_container"))
    await loadJsFiles(
        "./ASSETS/JS/MODULES/CATALOG/bookeditions_catalog.js",
        "./ASSETS/JS/MODULES/modules_commons.js")
}

const displayBookCopiesCatalogMainPage = async () => {
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/bookcopies.html", d.getElementById("main-content"))
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/PAGES/search_bookcopy.html", d.querySelector(".page_element_container"))
    await loadJsFiles(
        "./ASSETS/JS/MODULES/CATALOG/bookcopies_catalog.js",
        "./ASSETS/JS/MODULES/modules_commons.js")
}

const displayReadersMainPage = async () => {
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/USERS/readers.html", d.getElementById("main-content"))
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/USERS/PAGES/search_readers.html", d.querySelector(".page_element_container"))
    await loadJsFiles(
        "./ASSETS/JS/MODULES/USERS/readers.js",
        "./ASSETS/JS/MODULES/modules_commons.js")
}

const displayLibrariansMainPage = async () => {
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/USERS/librarians.html", d.getElementById("main-content"))
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/USERS/PAGES/search_librarian.html", d.querySelector(".page_element_container"))
    await loadJsFiles(
        "./ASSETS/JS/MODULES/USERS/librarians.js",
        "./ASSETS/JS/MODULES/modules_commons.js")
}

export {
    displayCatalogingMainPage,
    displayRegisteringMainPage,
    displayAuthorsCatalogMainPage,
    displayBookWorksCatalogMainPage,
    displayBookEditionsCatalogMainPage,
    displayBookCopiesCatalogMainPage,
    displayReadersMainPage,
    displayLibrariansMainPage,
    loadContent
}