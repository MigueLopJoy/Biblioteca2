import { loadContent } from "./../scripts.js";
import { loadJsFile } from "./../scripts.js";

const d = document,
    dropdownBTNs = d.getElementsByClassName("dropdown-btn");

for (let i = 0; i < dropdownBTNs.length; i++) {

    dropdownBTNs[i].addEventListener("click", e => {

        e.target.classList.toggle("active");

        let dropdownContent = e.target.nextElementSibling;
        if (dropdownContent.style.display === "block") {
            dropdownContent.style.display = "none";
        } else {
            dropdownContent.style.display = "block";
        }
    });
}

d.addEventListener("click", async e => {
    if (e.target.matches("#cataloging")) {
        await loadContent("./ASSETS/HTML/PROGRAM/CATALOG/cataloging.html", d.getElementById("main-content"))
        loadJsFile("./ASSETS/JS/MODULES/CATALOG/cataloging.js")
    }
})
