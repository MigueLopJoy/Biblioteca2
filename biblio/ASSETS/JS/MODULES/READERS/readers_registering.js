import { findCurrentPage } from "./../modules_commons.js"
import { fetchRequest } from "./../modules_commons.js"
import { handleErrorMessages } from "./../modules_commons.js"
import { clearErrorMessages } from "./../modules_commons.js"
import { clearFormsData } from "./../modules_commons.js"

import { showSearchResults } from "./readers_commons.js"

const d = document,
    registeredReaderTable = d.querySelector(".results_table.registered_reader_table")

let newReader, results, error, table, resultsType, operation

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("readers_registering_section")) {
        let currentPage = findCurrentPage()

        clearErrorMessages()

        if (currentPage.classList.contains("readers_registering_page")) {
            await runCreateNewReaderProcess(e.target)
        }

        if (error) {
            handleErrorMessages(error, e.target)
            error = null
            clearFormsData()
        } else {
            console.log(operation, table)
            showSearchResults(operation, table)
            enableModalActions(results, resultsType, operation, table)
        }
    }
})

const runCreateNewReaderProcess = async form => {
    console.log(form)
    console.log(registeredReaderTable)
    newReader = ""
    table = registeredReaderTable
    resultsType = "newReader"
    operation = "create"
    await getCreateReaderResults(form)
    console.log(results)
}

const getCreateReaderResults = async form => {
    try {
        console.log(
            {
                firstName: form.firstname.value.trim(),
                lastName: form.lastname.value.trim(),
                email: form.email.value.trim(),
                phoneNumber: form.phone_number.value.trim(),
                dateOfBirth: form.birth_date.value.trim(),
                gender: form.gender.value.trim()
            }
        )
        results = [await fetchRequest(
            "POST",
            "http://localhost:8080/readers/save-reader",
            {
                firstName: form.firstname.value.trim(),
                lastName: form.lastname.value.trim(),
                dateOfBirth: form.birth_date.value.trim(),
                gender: form.gender.value.trim(),
                email: form.email.value.trim(),
                phoneNumber: form.phone_number.value.trim(),
            }
        )]
    } catch (ex) {
        error = ex
    }
}

const getEditNewReaderResults = async editedFields => {
    try {
        results = [await fetchRequest(
            "PUT",
            `http://localhost:8080/readers/edit-reader/${results[0].idReader}`,
            {
                originalReaderId: results[0].idReader,
                firstName: editedFields[0],
                lastName: editedFields[1],
                dateOfBirth: editedFields[2],
                gender: editedFields[3],
                email: editedFields[4],
                phoneNumber: editedFields[5],
                readerNumber: editedFields[6]
            }
        )]
        return results
    } catch (ex) {
        throw ex
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
    cells[0].innerHTML = `<input type="text" class="edition" value="${newReader.firstName}" >`
    cells[1].innerHTML = `<input type="text" class="edition"value="${newReader.lastName}" >`
    cells[2].innerHTML = `<input type="text" class="edition" value="${newReader.dateOfBirth}" >`
    cells[3].innerHTML = `<input type="text" class="edition"value="${newReader.gender}" >`
    cells[4].innerHTML = `<input type="text" class="edition" value="${newReader.email}" >`
    cells[5].innerHTML = `<input type="text" class="edition"value="${newReader.phoneNumber}" >`
}

const getNewReader = () => {
    return
}

const reasigneNewReaderValue = newReaderValue => {
    newReader = newReaderValue
}

export { generateNewReaderTableContent }
export { getEditNewReaderResults }
export { prepareNewReaderEditionProcess }
export { getNewReader }
export { reasigneNewReaderValue }