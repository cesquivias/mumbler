from time import time

def fibonacci(n):
    if n < 2:
        return 1
    else:
        return fibonacci(n - 1) + fibonacci(n - 2)

fibonacci(30)
fibonacci(30)
fibonacci(30)
fibonacci(30)
fibonacci(30)
fibonacci(30)
fibonacci(30)
fibonacci(30)
fibonacci(30)
fibonacci(30)
start = int(time() * 1000)
result = fibonacci(30)
end = int(time() * 1000)

print result
print 'computation time: %s' % (end - start)
