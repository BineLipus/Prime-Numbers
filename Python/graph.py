import risar
import time as t
file_name = "../Java/log.txt"

start = t.time()
data = []
for line in open(file_name):
    line = line.strip().split()
    data.append([int(line[5]), int(line[7]), int(line[10]), int(line[13])])
    print(data[-1])


end = t.time()

print(f"Finished in {(end-start):.2f} seconds")

max_time = max([x[2] for x in data])
stepX = (data[-1][1] // 100000) // risar.maxX
stepY = risar.maxY / max_time

last_y = risar.maxY
for x in range(risar.maxX):
    try:
        risar.crta(x - 1, last_y, x - 1, risar.maxY - data[x*stepX][2] * stepY, risar.rdeca)
        last_y = risar.maxY - data[x*stepX][2]
    except Exception:
        raise Exception
        a = 1

risar.stoj()





