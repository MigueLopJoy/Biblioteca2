import { fetchRequest } from "../requests.js"
import { 
    displayReadersMainPage,
    displayLibrariansMainPage 
} from "../../display_pages.js"


const logout = async () => {
    await fetchRequest(
        "POST",
        "http://localhost:8080/users/logout"
    )

    localStorage.clear("jwtToken")

    window.location.replace('http://localhost/biblio');
}

const seeAccount = async () => {
    let user = await fetchRequest(
        "POST",
        "http://localhost:8080/users/get-connected-user",
    )
    console.log(user)
}

export { 
    logout,
    seeAccount 
}