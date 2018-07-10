
def gener(n):
    num = 0
    while num < n:
        yield str(num) + "a"
        num += 1


print(gener(10))