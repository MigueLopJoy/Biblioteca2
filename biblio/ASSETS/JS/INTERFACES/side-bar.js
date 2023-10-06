import { loadContent } from "./../scripts.js"
import { loadJsFiles } from "./../scripts.js"
import { showPage } from "./../MODULES/CATALOG/pages_navigation.js"
import { enableWindowNavLinkBtns } from "./../MODULES/CATALOG/pages_navigation.js"

const d = document,
    dropdownBTNs = d.getElementsByClassName("dropdown-btn")

for (let i = 0; i < dropdownBTNs.length; i++) {
    dropdownBTNs[i].addEventListener("click", e => {
        e.target.classList.toggle("active")

        let dropdownContent = e.target.nextElementSibling
        if (dropdownContent.style.display === "block") {
            dropdownContent.style.display = "none"
        } else {
            dropdownContent.style.display = "block"
        }
    });
}

d.addEventListener("click", async e => {
    if (e.target.matches("#cataloging")) {
        await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/cataloging.html", d.getElementById("main-content"))
        await loadJsFiles("./ASSETS/JS/MODULES/CATALOG/cataloging.js", "./ASSETS/JS/MODULES/CATALOG/catalog-commons.js")

        showPage("author_page")
        enableWindowNavLinkBtns()
    } else if (e.target.matches("#registering")) {
        await loadContent("./ASSETS/HTML/PROGRAM/MODULES/CATALOG/registering.html", d.getElementById("main-content"))
        await loadJsFiles("./ASSETS/JS/MODULES/CATALOG/registering.js", "./ASSETS/JS/MODULES/CATALOG/catalog-commons.js")
        showPage("bookedition_page")
        enableWindowNavLinkBtns()
    }
})
