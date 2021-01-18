import risar
import time as t
file_name = "../Java/log.txt"

start = t.time()

for line in open(file_name):
    line = line.strip().split()
    range_start = int(line[5])
    range_end = int(line[7])
    time = int(line[10])
    number_of_prime_numbers = int(line[13])
    print(f"{range_start}-{range_end}, {time}, {number_of_prime_numbers}")

end = t.time()

print(f"Finished in {(end-start):.2f} seconds")



