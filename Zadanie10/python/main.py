import turtle

# Określ reguły
rules = {"X": "F+[[X]-X]-F[-FX]+X", "F": "FF"}

# Określ początkowy stan systemu
state = "X"

# Określ liczbę iteracji
iterations = 5

# Wykonaj iteracje
for _ in range(iterations):
    state = "".join([rules.get(char, char) for char in state])

# Inicjalizacja żółwia
turtle.speed(0)
turtle.left(90)

# Ustal długość kroku i kąt
step_length = 5
angle = 25

# Wykonaj ruchy
stack = []
for char in state:
    if char == 'F':
        turtle.forward(step_length)
    elif char == '+':
        turtle.right(angle)
    elif char == '-':
        turtle.left(angle)
    elif char == '[':
        stack.append((turtle.position(), turtle.heading()))
    elif char == ']':
        position, heading = stack.pop()
        turtle.penup()
        turtle.setposition(position)
        turtle.setheading(heading)
        turtle.pendown()

# Zakończ rysowanie
turtle.done()
