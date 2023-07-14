const d = document;
const mainContent = d.getElementById("main-content");
const sideBar = d.getElementById("sidebar");
const fragment = d.createDocumentFragment();

d.addEventListener("DOMContentLoaded", async (e) => {
  try {
    await loadContent("./ASSETS/HTML/LOGIN/login.html", mainContent);
  } catch (error) {
    console.error("Failed to load side-bar content:", error);
  }
});

d.addEventListener("click", e => {
  if (e.target.matches("#search")) {
    searchBook();
  } else if (e.target.matches("#save")) {
    saveBook();
  } else if (e.target.matches("#search-again")) {
    loadContent("./ASSETS/HTML/BOOKS/search.html", mainContent);
  } else if (e.target.matches(".menu-btn")) {
    e.target.nextElementSibling.classList.toggle("active");
  } else if (e.target.matches("#search-book")) {
    loadContent("./ASSETS/HTML/BOOKS/search.html", mainContent);
  } else if (e.target.matches("#save-book")) {
    loadContent("./ASSETS/HTML/BOOKS/save.html", mainContent);
  } else if (e.target.matches("#start-register")) {
    loadContent("./ASSETS/HTML/LOGIN/sign-in-library-data.html", mainContent);
  } else if (e.target.matches(".sub-option.continue-option > a")) {
    if (allInputsFilledIn()) {
      loadContent("./ASSETS/HTML/LOGIN/sign-in-person-in-charge-data.html", mainContent);
    }
  } else if (e.target.matches(".sub-option.back-option > a")) {
    loadContent("./ASSETS/HTML/LOGIN/sign-in-library-data.html", mainContent);
  }
});

const loadContent = async (url, component) => {
  try {
    const response = await fetch(url);
    const content = await response.text();
    component.innerHTML = content;
  } catch (error) {
    console.error("Failed to load content:", error);
  }
}

const searchBook = async () => {
  const bookCode = d.getElementById("book-code").value;
  const title = d.getElementById("title").value;
  const authorFirstName = d.getElementById("author-firstname").value;
  const authorLastName = d.getElementById("author-lastname").value;
  const publicationYear = d.getElementById("publication-year").value;
  const publisher = d.getElementById("publisher").value;

  try {
    const reqBody = {
      bookCode,
      title,
      authorDTO: {
        firstName: authorFirstName,
        lastNames: authorLastName,
      },
      publicationYear,
      publisher,
    };

    const options = {
      method: "POST",
      headers: {
        "Content-type": "application/json",
      },
      body: JSON.stringify(reqBody),
    }

    const res = await fetch("http://localhost:8080/books/search", options);
    const json = await res.json();

    if (!res.ok) {
      throw { status: res.status, statusText: res.statusText };
    }

    await loadContent("./ASSETS/HTML/BOOKS/results.html", mainContent);
    const template = d.getElementById("template");

    console.log(json);

    updateTable(json, template);
  } catch (err) {
    const message = err.statusText || "An error occurred";
    d.getElementById("form").insertAdjacentHTML(
      "afterend",
      `<p><b>Error ${err.status}: ${message}</b></p>`
    );
  }
};



const updateTable = (data, template) => {
  data.forEach((el) => {
    const clone = d.importNode(template.content, true);
    clone.querySelector(".title").textContent = el.title;
    clone.querySelector(".author").textContent = `${el.authorDTO.firstName} ${el.authorDTO.lastNames}`;
    clone.querySelector(".publication-year").textContent = el.publicationYear;
    clone.querySelector(".publisher").textContent = el.publisher;
    fragment.appendChild(clone);
  });

  const table = d.querySelector("table");
  table.querySelector("tbody").appendChild(fragment);
};

const showSuccessMessage = () => {

  $resultContainer = document.createElement("DIV"),
    $resultMessage = document.createElement("P"),

    $resultContainer.setAttribute("id", "result-container");

  $resultMessage.textContent = "Book stored successfully";

  $resultContainer.appendChild($resultMessage);

  d.querySelector(".container").appendChild($resultContainer);

  setTimeout(() => {
    $resultContainer.remove();
  }, 3000);
};

const getData = () => {
  const title = d.getElementById("title").value;
  const authorFirstName = d.getElementById("author-firstname").value;
  const authorLastName = d.getElementById("author-lastname").value;
  const publicationYear = d.getElementById("publication-year").value;
  const publisher = d.getElementById("publisher").value;

}

const allInputsFilledIn = () => {
  const form = document.getElementById("form"),
    inputs = form.querySelectorAll("input")

  for (let input of inputs) {
    console.log(input.value)
    if (input.value === "") {
      return false
    }
  }
  return true
}

