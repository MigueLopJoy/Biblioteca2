const d = document

const loadContent = async (url, component) => {
    let res = await fetch(url),
        content = await res.text()
    component.innerHTML = content
}

const loadJsFiles = async (...sources) => {

    removeScriptsExcept(
        'http://localhost/biblio/ASSETS/JS/scripts.js',
        "https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
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
        "./ASSETS/JS/MODULES/CATALOG/catalog-commons.js",
        "./ASSETS/JS/MODULES/modules_commons.js")
    showPageAndEnableLinks("author_page")
}

const displayRegisteringMainPage = async () => {
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/registering.html", d.getElementById("main-content"))
    await loadJsFiles(
        "./ASSETS/JS/MODULES/CATALOG/registering.js",
        "./ASSETS/JS/MODULES/CATALOG/catalog-commons.js",
        "./ASSETS/JS/MODULES/modules_commons.js")
    showPageAndEnableLinks("bookedition_page")
}

const displayReadersRegisteringMainPage = async () => {
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/READERS/readers_registering.html", d.getElementById("main-content"))
    await loadJsFiles(
        "./ASSETS/JS/MODULES/READERS/readers_registering.js",
        "./ASSETS/JS/MODULES/READERS/readers_commons.js",
        "./ASSETS/JS/MODULES/modules_commons.js")
    showPageAndEnableLinks("readers_registering_page")
}

const displayBrowseAuthorsCatalogMainPage = async () => {
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/BROWSE/authors_catalog.html", d.getElementById("main-content"))
    await loadJsFiles(
        "./ASSETS/JS/MODULES/CATALOG/BROWSE/authors_catalog.js",
        "./ASSETS/JS/MODULES/CATALOG/CATALOG-COMMONS.js",
        "./ASSETS/JS/MODULES/modules_commons.js")
    showPageAndEnableLinks("b_authors_page")
}

const showPageAndEnableLinks = page => {
    showPage(page)
    enableWindowNavLinkBtns()
}

const enableWindowNavLinkBtns = () => {
    const pageLinks = d.querySelectorAll(".page_link")

    if (pageLinks) {
        pageLinks.forEach(pageLink => {
            pageLink.addEventListener("click", e => {
                if (e.target.classList.contains("enabled")) {
                    e.preventDefault();
                    showPage(e.target.classList[1])
                }
            })
        })
    }
}

const showPage = pageOption => {
    const pages = d.querySelectorAll(".page"),
        pageLinks = d.querySelectorAll(".page_link")

    if (pages) {
        pages.forEach(page => {
            page.classList.add("hidden")
        })

        d.querySelector(`.page.${pageOption}`).classList.remove("hidden")
    }

    if (pageLinks.length > 0) {
        pageLinks.forEach(pageLink => {
            pageLink.classList.remove("active");
        })
        d.querySelector(`.page_link.${pageOption}`).classList.add("active")
    }
}

export { displayCatalogingMainPage }
export { displayRegisteringMainPage }
export { displayReadersRegisteringMainPage }
export { displayBrowseAuthorsCatalogMainPage }
export { enableWindowNavLinkBtns }
export { loadContent }
export { showPage }