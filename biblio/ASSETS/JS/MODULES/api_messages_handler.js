const d = document

const handleErrorMessages = (error, layer) => {

    layer = selectCorrectLayer(layer)

    if (error.status === 422) {
        error.validationErrors.forEach(er => {
            let validationErrorMessage = layer.querySelector(`.error_message.${er.field}`)
            validationErrorMessage.classList.add("active")
            validationErrorMessage.textContent = er.message
        })
    } else {
        let generalErrorMessage = layer.querySelector('.error_message.general_error')
        generalErrorMessage.classList.add("active")
        generalErrorMessage.textContent = error.message
    }
}

const selectCorrectLayer = layer => {
    return layer.parentNode.classList.contains("auxiliar_element_container") ?
        d.querySelector(".page_element_container .form") :
        layer
}

const clearErrorMessages = () => {
    d.querySelectorAll(".error_message").forEach(er => {
        if (er.classList.contains("active")) {
            er.classList.remove("active")
        }
        er.textContent = ""
    })
}

const displaySuccessMessage = results => {
    const successMessage = d.querySelector(".success_message")
    successMessage.classList.add("active")
    if (results.successMessage) {
        successMessage.textContent = results.successMessage
    } else hiddeSuccessMessage()
}

const hiddeSuccessMessage = () => {
    const successMessage = d.querySelector(".success_message")
    successMessage.textContent = ""
    if (successMessage.classList.contains("active")) successMessage.classList.remove("active")
}

export {
    handleErrorMessages,
    clearErrorMessages,
    displaySuccessMessage,
    hiddeSuccessMessage
}