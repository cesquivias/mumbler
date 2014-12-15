function fib(n) {
    if (n < 2) {
        return 1;
    } else {
        return fib(n - 2) + fib(n -1);
    }
}

function main() {
    start = nanoTime();
    println(fib(30));
    end = nanoTime();
    println("computation time: " + (end - start) / 1000);
}
