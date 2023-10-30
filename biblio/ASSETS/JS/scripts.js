import {
  displayCatalogingMainPage,
  displayRegisteringMainPage,
  displayAuthorsCatalogMainPage,
  displayBookWorksCatalogMainPage,
  displayBookEditionsCatalogMainPage
} from "./display_pages.js"

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
    displayCatalogingMainPage()
  } else if (e.target.matches("#registering")) {
    displayRegisteringMainPage()
  } else if (e.target.matches("#search_readers")) {
    displaySearchReadersMainPage()
  } else if (e.target.matches("#readers_registering")) {
    displayReadersRegisteringMainPage()
  } else if (e.target.matches("#authors_catalog")) {
    displayAuthorsCatalogMainPage()
  } else if (e.target.matches("#bookworks_catalog")) {
    displayBookWorksCatalogMainPage()
  } else if (e.target.matches("#bookeditions_catalog")) {
    displayBookEditionsCatalogMainPage()
  } else if (e.target.matches("#bookcopies")) {
    displayBookCopiesCatalogMainPage()
  }
})


