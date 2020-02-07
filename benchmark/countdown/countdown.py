from time import time

def countdown(n):
    if n < 101:
        if n < 1:
            return 0
        else:
            return countdown(n - 1)
    else:
        countdown(100)
        return countdown(n - 100)

countdown(10000)
countdown(10000)
countdown(10000)
countdown(10000)

start = int(time() * 1000)
countdown(10000)
end = int(time() * 1000)
print 'computation time: %s' % (end - start)
