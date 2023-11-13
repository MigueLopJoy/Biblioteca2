import { fetchRequest } from "../requests.js"
import {
    displayReadersMainPage,
    displayLibrariansMainPage
} from "../../display_pages.js"


const logout = async () => {
    let response = await fetchRequest(
        "GET",
        "http://localhost:8080/test/get-headers"
    )

    localStorage.clear("jwtToken")
    console.log(response)
}

const seeAccount = async () => {
    let user = await fetchRequest(
        "GET",
        "http://localhost:8080/test/get-authorization-header",
    )
    console.log(user)
}

export {
    logout,
    seeAccount
}