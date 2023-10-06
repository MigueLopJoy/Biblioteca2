const d = document

const enableWindowNavLinkBtns = () => {
    const pageLinks = d.querySelectorAll(".page_link")

    pageLinks.forEach(pageLink => {
        pageLink.addEventListener("click", e => {
            if (e.target.classList.contains("enabled")) {
                e.preventDefault();
                showPage(e.target.classList[1]);
            }
        });
    })
}

const showPage = pageOption => {
    const pages = d.querySelectorAll(".page"),
        pageLinks = d.querySelectorAll(".page_link")

    pages.forEach(page => {
        page.classList.add("hidden")
    });
    pageLinks.forEach(pageLink => {
        pageLink.classList.remove("active");
    })
    d.querySelector(`.page_link.${pageOption}`).classList.add("active")
    d.querySelector(`.page.${pageOption}`).classList.remove("hidden")
}

export { enableWindowNavLinkBtns }
export { showPage }