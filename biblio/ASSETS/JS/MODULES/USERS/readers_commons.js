import { generateNewReaderTableContent } from "./readers_registering.js"

const d = document,
    selectBtnContainer = d.querySelector(".modal_btns_container .select_btn"),
    createBtnContainer = d.querySelector(".modal_btns_container .create_btn"),
    selectResultBtn = d.querySelector(".modal_btns_container .select_result_btn")

const tableContainsClass = (table, className) => {
    return table.classList.contains(className)
}

const showSearchResults = (operation, table) => {

    console.log(operation, table)

    const modal = d.getElementById("modal")

    modal.classList.remove("hidden")
    table.classList.remove("hidden")

    generaTableContent(table)

    if (operation === "search") {
        selectBtnContainer.classList.remove("hidden")

        if (tableContainsClass(table, "class")) {
            selectResultBtn.textContent = "btn text content"
        }
    } else if (operation === "create") {
        createBtnContainer.classList.remove("hidden")
    }
}

const generaTableContent = table => {
    if (tableContainsClass(table, "registered_reader_table")) {
        generateNewReaderTableContent()
    }
}

export { showSearchResults }
