import { findCurrentPage } from "./../modules_commons.js"
import { fetchRequest } from "./../modules_commons.js"
import { joinParamsToURL } from "./../modules_commons.js"
import { handleErrorMessages } from "./../modules_commons.js"
import { clearErrorMessages } from "./../modules_commons.js"

import { showSearchResults } from "./readers_commons.js"

const d = document,
    registeredReaderTable = d.querySelector(".results_table.registered_reader_table")

let newReader, results, error, table, resultsType, operation

d.addEventListener("submit", async e => {
    e.preventDefault();

        let currentPage = findCurrentPage()

        clearErrorMessages()

        if (currentPage.classList.contains("author_page")) {
            await runCreateNewReaderProcess(e.target)
        } 

        if (error) {
            handleErrorMessages(error, e.target)
            error = null
            clearFormsData()
        } else {
            showSearchResults(operation, table)
            enableModalActions(results, resultsType, operation, table)
        }
})

const runCreateNewReaderProcess = async () => {
    reader = ""
    table = registeredReaderTable
    resultsType = "newReader"
    clearPrintedReults(resultsType)

    if (form.classList.contains("author_form") &&
        form.classList.contains("search")
    ) {
        operation = "search"
        await getCreateReaderResults(form)
    } 
}

const getCreateReaderResults = async form => {
    try {
        results = [await fetchRequest(
            "POST",
            "http://localhost:8080/readers/save-reader",
            {
                firstName: form.firstName.value.trim(),
                lastName: form.lastName.value.trim(),
                email: form.email.value.trim(),
                phoneNumber: form.phoneNumber.value.trim(),
                dateOfBirth: form.dateOfBirth.value.trim(),
                gender: form.gender.value.trim()
            }
        )]
    } catch (ex) {
        error = ex
    }
}

const generateNewReaderTableContent = () => {
    for (let i = 0; i < results.length; i++) {

        let result = results[i]

        let newRow = d.createElement("tr")
        newRow.classList.add("results_row")

        let firstName = d.createElement("td")
        firstName.textContent = result.firstName

        let lastName = d.createElement("td")
        lastName.textContent = result.lastName

        let dateOfBirth = d.createElement("td")
        dateOfBirth.textContent = result.dateOfBirth

        let gender = d.createElement("td")
        gender.textContent = result.gender

        let email = d.createElement("td")
        email.textContent = result.email

        let phoneNumber = d.createElement("td")
        phoneNumber.textContent = result.phoneNumber
    


        newRow.appendChild(firstName)
        newRow.appendChild(lastName)
        newRow.appendChild(dateOfBirth)
        newRow.appendChild(gender)
        newRow.appendChild(email)
        newRow.appendChild(phoneNumber)

        let tableBody = table.querySelector(".results_table_body")

        if (tableBody.firstChild) {
            tableBody.insertBefore(newRow, tableBody.firstChild)

            let errorMessageTd = tableBody.querySelector(".error_message_row > td")
            errorMessageTd.setAttribute("colspan", 6)
        } else {
            tableBody.appendChild(newRow)
        }
    }
}

const prepareNewReaderEditionProcess = cells => {
    cells[0].innerHTML = `<input type="text" class="newReader" value="${newReader.firstName}" >`
    cells[1].innerHTML = `<input type="text" class="newReader"value="${newReader.lastName}" >`
    cells[2].innerHTML = `<input type="text" class="newReader" value="${newReader.dateOfBirth}" >`
    cells[3].innerHTML = `<input type="text" class="newReader"value="${newReader.gender}" >`
    cells[4].innerHTML = `<input type="text" class="newReader" value="${newReader.email}" >`
    cells[5].innerHTML = `<input type="text" class="newReader"value="${newReader.phoneNumber}" >`
}



const getNewReader = () => {
    return newReader
}


const reasigneNewReaderValue = newReaderValue => {
    newReader = newReaderValue
}

export { generateNewReaderTableContent }
export { prepareNewReaderEditionProcess }
export { getNewReader }
export { reasigneNewReaderValue }