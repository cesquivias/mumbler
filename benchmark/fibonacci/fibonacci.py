from time import time

start = int(time() * 1000)

def fibonacci(n):
    if n < 2:
        return 1
    else:
        return fibonacci(n - 1) + fibonacci(n - 2)

result = fibonacci(30);
end = int(time() * 1000)

print result
print 'computation time: %s' % (end - start)
