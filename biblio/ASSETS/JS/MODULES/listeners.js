import {
    closeModal,
    executeDeleteSymbolListener,
    confirmDeletion,
    executeSelectResultBtnListener,
    executeEditSymbolListener,
    executeConfirmCreationBtnListener,
    confirmEdition,
} from "./modules_commons.js"

import {
    displayAuthorSelectionTable,
    selectBookWorkAuthor
} from "./CATALOG/bookworks_catalog.js"

import {
    displayRegistrationNumberRange,
    displayRegistrationDateRange
} from "./CATALOG/bookcopies_catalog.js"

import {
    displayYearOfBirthRange
} from "./USERS/readers.js"

const d = document

d.addEventListener("click", e => {

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
            let flux = e.target.classList[2]

            switch (flux) {
                case "select_bookwork_author":
                    selectBookWorkAuthor()
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
        executeSearchRelatedObjectsListener()
    }

    if (e.target.matches(".search_bookwork_author")) {
        displayAuthorSelectionTable()
    }

    if (e.target.matches(".select_author_btn")) {
        selectBookWorkAuthor()
    }

    if (e.target.matches(".input-group-text.registration_number")) {
        displayRegistrationNumberRange()
    }

    if (e.target.matches(".input-group-text.registration_date")) {
        displayRegistrationDateRange()
    }

    if (e.target.matches(".input-group-text.year_of_birth")) {
        displayYearOfBirthRange()
    }
})

d.addEventListener("change", e => {
    if (e.target.matches("input.result_option")) {
        const checkboxes = document.querySelectorAll('input.result_option')

        changeCheckBoxOption(checkboxes, e.target)
        toggleSelectResultBtnState(checkboxes)
    }
})

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