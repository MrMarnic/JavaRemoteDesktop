function onMonClick(e) {
    const index = e.getAttribute("data-mon-index");
    location.href = "http://" + location.host + "/desktop?monIndex=" + index;
}