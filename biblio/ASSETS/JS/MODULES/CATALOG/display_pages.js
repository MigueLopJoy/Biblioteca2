const d = document

const loadContent = async (url, component) => {
    try {
        let res = await fetch(url),
            content = await res.text()
        component.innerHTML = content
    } catch (error) {
        console.error("Failed to load content:", error)
    }
}

const loadJsFiles = async (...sources) => {

    removeScriptsExcept('http://localhost/biblio/ASSETS/JS/scripts.js')
    console.log(document.querySelectorAll("script"))

    for (let src of sources) {
        const timestamp = new Date().getTime(),
            script = document.createElement("script")
        script.type = "module"
        script.src = `${src}?${timestamp}`
        document.body.appendChild(script)
    }
    console.log(document.querySelectorAll("script"))
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
    await loadJsFiles("./ASSETS/JS/MODULES/CATALOG/cataloging.js", "./ASSETS/JS/MODULES/CATALOG/catalog-commons.js")
    showPageAndEnableLinks("author_page")
}

const displayRegisteringMainPage = async () => {
    await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/registering.html", d.getElementById("main-content"))
    await loadJsFiles("./ASSETS/JS/MODULES/CATALOG/registering.js", "./ASSETS/JS/MODULES/CATALOG/catalog-commons.js")
    showPageAndEnableLinks("bookedition_page")
}

const showPageAndEnableLinks = page => {
    showPage(page)
    enableWindowNavLinkBtns()
}

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
}

export { displayCatalogingMainPage }
export { displayRegisteringMainPage }
export { enableWindowNavLinkBtns }
export { loadContent }
export { showPage }