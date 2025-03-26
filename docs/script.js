const videoElements = document.getElementsByTagName('video');

for (const element of videoElements) {
    element.addEventListener('click', function() {
        if (element.paused) {
            element.play();
        } else {
            element.pause();
        }
    });
}