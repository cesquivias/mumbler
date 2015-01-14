function countdown(n) {
    if (n < 101) {
        if (n > 0) {
            countdown(n - 1);
        }
    } else {
        countdown(100);
        countdown(n - 100);
    }
}

function main() {
    countdown(10000);
    countdown(10000);
    countdown(10000);
    countdown(10000);

    start = nanoTime();
    countdown(10000);
    end = nanoTime();
    println("computation time: " + (end - start) / 1000000);
}
