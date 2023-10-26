import { 
  displayCatalogingMainPage,
  displayRegisteringMainPage,
  displayReadersRegisteringMainPage,
  displayBrowseAuthorsCatalogMainPage,
  displayBrowseBookWorksCatalogMainPage 
} from "./MODULES/CATALOG/display_pages.js"

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
  } else if (e.target.matches("#b_authors_catalog")) {
    displayBrowseAuthorsCatalogMainPage()
  } else if (e.target.matches("#b_bookworks_catalog")) {
    displayBrowseBookWorksCatalogMainPage()
  } else if (e.target.matches("#b_bookeditions_catalog")) {
    displayBrowseBookWorksCatalogMainPage()
  } 
})


