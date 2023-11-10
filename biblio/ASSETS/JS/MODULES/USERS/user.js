import { fetchRequest } from "../requests.js";


const logout = async () => {

    localStorage.clear("jwtToken")

    await fetchRequest(
        "GET",
        "http://localhost:8080/users/logout"
    )

    window.location.replace('http://localhost/biblio/index.html');
}

export { logout }