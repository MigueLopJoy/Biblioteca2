import { fetchRequest } from "../requests.js"
import {
    displayReadersMainPage,
    displayLibrariansMainPage
} from "../../display_pages.js"


const logout = async () => {
    let response = await fetchRequest(
        "GET",
        "http://localhost:8080/users/logout"
    )

    localStorage.clear("jwtToken")
    console.log(response)
}

const seeAccount = async () => {
    let user = await fetchRequest(
        "GET",
        "http://localhost:8080/users/me",
    )
    console.log(user)
}

export {
    logout,
    seeAccount
}