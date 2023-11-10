const fetchRequest = async (method, url, bodyContent) => {
    try {
        console.log("Authorization: " + "Bearer " + localStorage.getItem("jwtToken"))
        let options = {
            method: method,
            headers: {
                "Content-type": "application/json; charset=utf-8",
                "Authorization": "Bearer " + localStorage.getItem("jwtToken")
            },
            body: bodyContent ? JSON.stringify(bodyContent) : null
        },
            res = await fetch(url, options)

        if (!res.ok) throw res

        return await res.json()

    } catch (ex) {
        console.log(ex)
        const errorData = await ex.json(),
            errorResponse = {
                status: errorData.status,
                statusText: errorData.statusText,
                message: errorData.message,
                validationErrors: errorData.validationErrors
            }
        throw errorResponse
    }
}

const joinParamsToURL = (baseURL, params) => {
    let queryParams = Object.keys(params)
        .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
        .join('&')

    return `${baseURL}?${queryParams}`
}

export {
    fetchRequest,
    joinParamsToURL
}