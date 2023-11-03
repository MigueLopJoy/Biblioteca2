import {
    clearForms,
    setCreationValues,
    setSearchValues,
    showSearchResults,
    showCatalogCard
} from "./../modules_commons.js"

import {
    fetchRequest,
} from "./../requests.js"

import {
    handleErrorMessages,
    clearErrorMessages
} from "./../api_messages_handler.js"

const d = document

let reader, results, error, resultsType, operation, table, catalogCard

d.addEventListener("submit", async e => {
    e.preventDefault();

    if (d.getElementById("readers_section")) {
        sendReaderForm(undefined, e.target)
    }
})

const sendReaderForm = async (reader, form) => {
    error = undefined
    clearErrorMessages()

    if (!form) form = setFormInputsValues(reader)
    await runReaderProcess(form)

    if (error) {
        handleErrorMessages(error, form)
        clearForms()
    } else {
        if (operation === "search") {
            showSearchResults(table)
            setSearchValues(results, resultsType, table)
        } else if (operation === "create") {
            showCatalogCard(results, resultsType, catalogCard)
            setCreationValues(results.reader, resultsType, catalogCard)
        }
    }
}

const setFormInputsValues = reader => {
    let searchReaderForm = d.querySelector(".form.reader_form.search")
    searchReaderForm.reader_number.value = reader.readerNumber
    return searchReaderForm
}

const runReaderProcess = async form => {
    reader = ""
    resultsType = "reader"

    if (form.classList.contains("search")) {
        table = d.querySelector(".results_table.readers_results_table")
        operation = "search"
        results = await getReaders(form)
    } else if (form.classList.contains("create")) {
        catalogCard = d.querySelector(".catalog_card.reader_catalog_card")
        operation = "create"
        results = await createReader(form)
    }
}

const displayBirthYearRange = () => {
    let singleBirthYear = d.querySelector(".form .single_birth_year"),
        rangeBirthYear = d.querySelector(".form .range_birth_year"),
        genderSelect = d.querySelector(".form .reader_gender").parentNode

    singleBirthYear.classList.add("hidden")

    rangeBirthYear.classList.remove("d-none")
    rangeBirthYear.classList.add("d-flex")

    genderSelect.classList.remove("col-6")
    genderSelect.classList.add("col-4")
}

const getReaders = async form => {
    const rangeBirthYearContainer = form.querySelector(".range_birth_year"),
        genderSelect = form.querySelector(".reader_gender")

    try {
        results = await fetchRequest(
            "POST",
            "http://localhost:8080/readers/search-reader",
            {
                readerName: form.reader_name.value,
                readerNumber: form.reader_number.value,
                email: form.reader_email.value,
                phoneNumber: form.reader_phone_number.value,
                minBirthYear:
                    containerContainsClass(rangeBirthYearContainer) ?
                        form.birth_year.value : form.min_birth_year.value,
                maxBirthYear:
                    containerContainsClass(rangeBirthYearContainer) ?
                        form.birth_year.value : form.max_birth_year.value,
                gender: genderSelect.options[genderSelect.selectedIndex].value,
            }
        )
        return results
    } catch (ex) {
        error = ex
    }
}


const containerContainsClass = inputContainer =>
    inputContainer.classList.contains("d-none")


const createReader = async form => {
    const genderSelect = form.querySelector(".reader_gender")

    try {
        return await fetchRequest(
            "POST",
            "http://localhost:8080/readers/save-reader",
            {
                firstName: form.reader_firstname.value,
                lastName: form.reader_lastname.value,
                email: form.reader_email.value,
                phoneNumber: form.reader_phone_number.value,
                birthYear: form.birth_year.value,
                gender: genderSelect.options[genderSelect.selectedIndex].value,
            }
        )
    } catch (ex) {
        error = ex
    }
}

const editReader = async (idReader, editedFields) => {
    try {

        return await fetchRequest(
            "PUT",
            `http://localhost:8080/readers/edit-reader/${idReader}`,
            {
                originalReaderId: idReader,
                firstName: editedFields[0],
                lastName: editedFields[1],
                readerNumber: editedFields[2],
                email: editedFields[3],
                phoneNumber: editedFields[4],
                birthYear: editedFields[5],
                gender: editedFields[6],
            }
        )
    } catch (ex) {
        throw ex
    }
}

const deleteReader = async idReader => {
    try {
        return await fetchRequest(
            "DELETE",
            `http://localhost:8080/readers/delete-reader/${idReader}`
        )
    } catch (ex) {
        throw ex
    }
}

const generateReadersTableContent = () => {
    for (let i = 0; i < results.length; i++) {
        let result = results[i]

        let newRow = d.createElement("tr")
        newRow.classList.add("results_row")

        let readerName = d.createElement("td")
        readerName.textContent = `${result.firstName} ${result.lastName}`

        let readerNumber = d.createElement("td")
        readerNumber.textContent = result.readerNumber

        let birthYear = d.createElement("td")
        birthYear.textContent = result.birthYear

        let selectReader = d.createElement("td"),
            checkbox = d.createElement("input")
        checkbox.type = "checkbox"
        checkbox.name = `select_reader`
        checkbox.classList.add('result_option')
        checkbox.value = i;
        selectReader.appendChild(checkbox)

        newRow.appendChild(readerName)
        newRow.appendChild(readerNumber)
        newRow.appendChild(birthYear)
        newRow.appendChild(selectReader)
        table.querySelector(".results_table_body").appendChild(newRow)
    }
}

const generateReadersCatalogCard = async results => {
    let reader = results,
        catalogCard = d.querySelector(".catalog_card.reader_catalog_card")

    console.log(reader)

    catalogCard.classList.remove("hidden")

    console.log(catalogCard)

    catalogCard.querySelector(".reader_firstname").value = reader.firstName
    catalogCard.querySelector(".reader_lastname").value = reader.lastName
    catalogCard.querySelector(".reader_number").value = reader.readerNumber
    catalogCard.querySelector(".birth_year").value = reader.birthYear
    catalogCard.querySelector(".reader_gender").value = reader.gender
    catalogCard.querySelector(".reader_email").value = reader.email
    catalogCard.querySelector(".reader_phone_number").value = reader.phoneNumber

    return catalogCard
}

const getReader = () => {
    return reader
}

const getReaderRsults = () => {
    if (error) throw error
    return results
}

const setReaderValue = newReaderValue => {
    reader = newReaderValue
}

export {
    sendReaderForm,
    deleteReader,
    editReader,
    generateReadersTableContent,
    generateReadersCatalogCard,
    getReader,
    setReaderValue,
    getReaderRsults,
    displayBirthYearRange
}
