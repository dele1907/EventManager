document.getElementById('program-video').addEventListener('click', function() {
    var video = document.getElementById('program-video');
    if (video.paused) {
        video.play();
    } else {
        video.pause();
    }
});

