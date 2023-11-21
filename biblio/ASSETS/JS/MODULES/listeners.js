import { loadContent } from "../display_pages.js"

import {
    closeModal,
    executeDeleteSymbolListener,
    confirmDeletion,
    executeSelectResultBtnListener,
    executeEditSymbolListener,
    executeConfirmCreationBtnListener,
    executeSearchRelatedObjectsListener,
    confirmEdition,
} from "./modules_commons.js"

import {
    displayAuthorSelectionTable,
    selectBookWorkAuthor
} from "./CATALOG/authors_catalog.js"

import {
    displayBookWorkSelectionTable,
    selectEditionBookWork
} from "./CATALOG/bookworks_catalog.js"

import {
    displayBookEditionSelectionTable,
    selectCopyBookEdition
} from "./CATALOG/bookeditions_catalog.js"

import {
    displayRegistrationNumberRange,
    displayRegistrationDateRange
} from "./CATALOG/bookcopies_catalog.js"

import {
    displayBirthYearRange,
} from "./USERS/readers.js"

import {
    logout,
    seeAccount
} from "./USERS/user.js"

import {
    displayLibrariesSelectionTable,
    selectEditionOrLibrarianLibrary
} from "./LIBRARY/libraries.js"

const d = document

d.addEventListener("click", async e => {

    if (e.target.matches(".close_symbol")) {
        if (e.target.classList.contains("active")) {
            closeModal()
        }
    }

    if (e.target.matches(".delete_symbol")) {
        executeDeleteSymbolListener()
    }

    if (e.target.matches(".edit_symbol")) {
        executeEditSymbolListener()
    }

    if (e.target.matches(".select_results_btn")) {
        if (!e.target.hasAttribute("disabled")) {
            let flux = e.target.classList.item(e.target.classList.length - 1)
            switch (flux) {
                case "select_bookwork_author":
                    selectBookWorkAuthor()
                    break
                case "select_edition_bookwork":
                    selectEditionBookWork()
                    break
                case "select_copy_bookedition":
                    selectCopyBookEdition()
                    break
                case "select_edition_library":
                case "select_librarian_library":
                    selectEditionOrLibrarianLibrary()
                    break
                default:
                    executeSelectResultBtnListener()
                    break
            }
        }
    }

    if (e.target.matches(".confirm_creation_btn")) {
        executeConfirmCreationBtnListener()
    }

    if (e.target.matches(".confirm_deletion_btn")) {
        confirmDeletion(e.target)
    }

    if (e.target.matches(".confirm_edition_btn")) {
        confirmEdition(e.target)
    }

    if (e.target.matches(".search_related_objects")) {
        if (e.target.classList.contains("active"))
            executeSearchRelatedObjectsListener()
    }

    if (e.target.matches(".search_bookwork_author")) {
        displayAuthorSelectionTable()
    }

    if (e.target.matches(".search_edition_bookwork")) {
        displayBookWorkSelectionTable()
    }

    if (e.target.matches(".search_edition_library") ||
        e.target.matches(".search_librarian_library")) {
        displayLibrariesSelectionTable()
    }

    if (e.target.matches(".search_copy_bookedition")) {
        displayBookEditionSelectionTable()
    }

    if (e.target.matches(".input-group-text.registration_number")) {
        displayRegistrationNumberRange()
    }

    if (e.target.matches(".input-group-text.registration_date")) {
        displayRegistrationDateRange()
    }

    if (e.target.matches(".input-group-text.birth_year")) {
        displayBirthYearRange()
    }

    if (e.target.matches("#user_icon")) {
        d.getElementById("user_popover").classList.toggle("active");
    }

    if (e.target.matches("#user_popover .close-btn")) {
        d.getElementById("user_popover").classList.remove("active");
    }

    if (e.target.matches("#user_popover .logout")) {
        logout()
    }
    if (e.target.matches("#user_popover .see_account")) {
        seeAccount()
    }

    if (e.target.matches(".page_link.search")) {
        toggleLinkActivation("search")

        if (d.querySelector(".bookeditions_page")) {
            await loadPageElementContent("PROGRAM/MODULES/CATALOG/PAGES/search_bookedition.html")
        } else if (d.querySelector(".bookcopies_page")) {
            await loadPageElementContent("PROGRAM/MODULES/CATALOG/PAGES/search_bookcopy.html")
        } else if (d.querySelector(".readers_page")) {
            await loadPageElementContent("PROGRAM/MODULES/USERS/PAGES/search_readers.html")
        } else if (d.querySelector(".librarians_page")) {
            await loadPageElementContent("PROGRAM/MODULES/USERS/PAGES/search_librarian.html")
        }
    } else if (e.target.matches(".page_link.create")) {
        toggleLinkActivation("create")

        if (d.querySelector(".bookeditions_page")) {
            await loadPageElementContent("PROGRAM/MODULES/CATALOG/PAGES/create_bookedition.html")
        } else if (d.querySelector(".bookcopies_page")) {
            await loadPageElementContent("PROGRAM/MODULES/CATALOG/PAGES/create_bookcopy.html")
        } else if (d.querySelector(".readers_page")) {
            await loadPageElementContent("PROGRAM/MODULES/USERS/PAGES/create_readers.html")
        } else if (d.querySelector(".librarians_page")) {
            await loadPageElementContent("PROGRAM/MODULES/USERS/PAGES/create_librarian.html")
        }
    }
})

d.addEventListener("change", e => {
    if (e.target.matches("input.result_option")) {
        const checkboxes = document.querySelectorAll('input.result_option')

        changeCheckBoxOption(checkboxes, e.target)
        toggleSelectResultBtnState(checkboxes)
    }
})

const loadPageElementContent = async url => {
    const pageElementContainer = d.querySelector(".page_element_container")
    await loadContent(url, pageElementContainer)
}

const toggleLinkActivation = operation => {
    const createLink = d.querySelector(".page_link.create"),
        searchLink = d.querySelector(".page_link.search")

    if (operation === "search") {
        if (createLink.classList.contains("enabled")) createLink.classList.remove("enabled")
        if (!searchLink.classList.contains("enabled")) searchLink.classList.add("enabled")
    } else if (operation === "create") {
        if (searchLink.classList.contains("enabled")) searchLink.classList.remove("enabled")
        if (!createLink.classList.contains("enabled")) createLink.classList.add("enabled")
    }
}

const changeCheckBoxOption = (checkboxes, target) => {
    for (let checkbox of checkboxes) {
        if (checkbox !== target) {
            checkbox.checked = false;
        }
    }
}

const toggleSelectResultBtnState = checkboxes => {
    const selectResultBtn = d.querySelector(".select_results_btn")

    let found = false,
        counter = 0

    do {
        let checkbox = checkboxes[counter]

        if (checkbox.checked) {
            found = true
        } else counter++
    } while (!found && counter < checkboxes.length)

    if (found) {
        if (selectResultBtn.hasAttribute("disabled")) {
            selectResultBtn.removeAttribute("disabled")
        }
    } else {
        if (!selectResultBtn.hasAttribute("disabled")) {
            selectResultBtn.setAttribute("disabled", true)
        }
    }
}