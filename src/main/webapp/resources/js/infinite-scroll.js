document.addEventListener("DOMContentLoaded", function () {
    const grid = document.querySelector('.grid .row');

    imagesLoaded(grid, function () {
        new Masonry(grid, {
            itemSelector: '.col-md-4',
            percentPosition: true,
            columnWidth: '.col-md-4'
        });
    });
});

//TODO : fetch data to backend and append to grid