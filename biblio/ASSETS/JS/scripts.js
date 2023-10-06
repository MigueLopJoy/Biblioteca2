const d = document,
  sideBar = d.getElementById("sidebar");

d.addEventListener("DOMContentLoaded", async e => {
  try {
    await loadContent("./ASSETS/HTML/PROGRAM/side-bar.html", sideBar);
    loadJsFiles("./ASSETS/JS/INTERFACES/side-bar.js");
  } catch (error) {
    console.error("Failed to load side-bar content:", error);
  }
})

const loadContent = async (url, component) => {
  try {
    let res = await fetch(url),
      content = await res.text()
    component.innerHTML = content
  } catch (error) {
    console.error("Failed to load content:", error)
  }
}

const loadJsFiles = async (...sources) => {

  removeScriptsExcept('http://localhost/biblio/ASSETS/JS/scripts.js', 'http://localhost/biblio/ASSETS/JS/INTERFACES/side-bar.js')

  for (let src of sources) {
    let script = document.createElement("script")
    script.type = "module"
    script.src = src
    document.body.appendChild(script)
  }
}

function removeScriptsExcept(...scriptsToKeep) {
  const scripts = document.querySelectorAll("script")

  for (let script of scripts) {

    let keep = false
    let i = 0
    do {
      if (script.src == scriptsToKeep[i]) {
        keep = true
      } else {
        i++
      }
    } while (!keep && i < scriptsToKeep.length)

    if (!keep) {
      script.remove()
    }
    keep = false
    i = 0
  }
}

export { loadContent };

export { loadJsFiles };