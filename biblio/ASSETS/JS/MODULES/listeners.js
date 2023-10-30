import {
    closeModal,
    executeDeleteSymbolListener,
    confirmDeletion,
    executeSelectResultBtnListener,
    executeEditSymbolListener,
    executeConfirmCreationBtnListener,
    confirmEdition,
} from "./modules_commons.js"

import { bookWorkAuthorSelection } from "./CATALOG/bookworks_catalog.js"

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
            executeSelectResultBtnListener()
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
        console.log(e.target)
        bookWorkAuthorSelection()
    }
})

d.addEventListener("change", e => {
    if (e.target.matches("input.result_option")) {
        const checkboxes = document.querySelectorAll('input.result_option');

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