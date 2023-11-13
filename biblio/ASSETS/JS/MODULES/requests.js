const fetchRequest = async (method, url, bodyContent) => {
    try {
        let options = {
            method: method,
            headers: {
                "content-type": "application/json; charset=utf-8",
                "authorization": "Bearer " + localStorage.getItem("jwtToken")
            },
            body: bodyContent ? JSON.stringify(bodyContent) : null
        }

        console.log("Request Options:", options);

        let res = await fetch(url, options)

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