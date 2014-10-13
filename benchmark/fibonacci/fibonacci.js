var start = new Date().getTime();

function fibonacci(n) {
    if (n < 2) {
        return 1;
    } else {
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}

var result = fibonacci(30);
var end = new Date().getTime();

console.log(result);
console.log('computation time: ' + (end - start));
