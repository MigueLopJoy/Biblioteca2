import { generateNewReaderTableContent } from "./readers_registering"

const showSearchResults = (operation, table) => {
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
        createBtnsContainer.classList.remove("hidden")
    }
}

const tableContainsClass = (table, className) => {
    return table.classList.contains(className)
}

const generaTableContent = table => {
    if (tableContainsClass(table, "registered_reader_table")) {
        generateNewReaderTableContent()
    }
}

const reasigneNewReaderValue = newReaderValue => {
    reader = newReaderValue
}

export { showSearchResults }
export { reasigneNewReaderValue }