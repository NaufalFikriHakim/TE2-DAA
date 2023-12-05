import random

# Number of elements
def generate_tc(file_name, number):
    random_numbers = [random.randint(1, 100) for _ in range(number)]

    with open(file_name, "w") as file:
        for random_number in random_numbers:
            file.write(str(random_number) + "\n")

generate_tc("10elemen.txt", 10)
generate_tc("40elemen.txt", 40)
generate_tc("80elemen.txt", 80)


