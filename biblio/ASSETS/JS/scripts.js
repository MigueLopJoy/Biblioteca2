const d = document,
  sideBar = d.getElementById("sidebar");

d.addEventListener("DOMContentLoaded", async e => {
  try {
    await loadContent("./ASSETS/HTML/PROGRAM/side-bar.html", sideBar);
    loadJsFile("./ASSETS/JS/INTERFACES/side-bar.js");
  } catch (error) {
    console.error("Failed to load side-bar content:", error);
  }
})

const loadContent = async (url, component) => {
  try {
    let response = await fetch(url),
      content = await response.text();
    component.innerHTML = content;
  } catch (error) {
    console.error("Failed to load content:", error);
  }
}

const loadJsFile = async src => {
  let script = document.createElement("script")
  script.type = "module";
  script.src = src;
  document.body.appendChild(script);
}


export { loadContent };

export { loadJsFile };