import numpy as np
import matplotlib.pyplot as plt

# Constants
A = 10
B = 28
C = 8/3
dt = 0.01
t0 = 0
tf = 100  # Final time

# Initial conditions
x0 = y0 = z0 = 1

# System of differential equations
def lorenz(xyz):
    x, y, z = xyz
    return np.array([A*(y-x), -x*z + B*x - y, x*y - C*z])

# Time array
t = np.arange(t0, tf, dt)

# Solve using Euler method
def euler():
    xyz = np.zeros((3, len(t)))
    xyz[:, 0] = x0, y0, z0
    for i in range(len(t)-1):
        xyz[:, i+1] = xyz[:, i] + dt*lorenz(xyz[:, i])
    return xyz

# Solve using Midpoint method
def midpoint():
    xyz = np.zeros((3, len(t)))
    xyz[:, 0] = x0, y0, z0
    for i in range(len(t)-1):
        k1 = dt*lorenz(xyz[:, i])
        k2 = dt*lorenz(xyz[:, i] + 0.5*k1)
        xyz[:, i+1] = xyz[:, i] + k2
    return xyz

# Solve using RK4 method
def rk4():
    xyz = np.zeros((3, len(t)))
    xyz[:, 0] = x0, y0, z0
    for i in range(len(t)-1):
        k1 = dt*lorenz(xyz[:, i])
        k2 = dt*lorenz(xyz[:, i] + 0.5*k1)
        k3 = dt*lorenz(xyz[:, i] + 0.5*k2)
        k4 = dt*lorenz(xyz[:, i] + k3)
        xyz[:, i+1] = xyz[:, i] + (k1 + 2*k2 + 2*k3 + k4) / 6
    return xyz

# Ask the user for the method
method = input("Which method do you want to use? Options: Euler, Midpoint, RK4 or All: ")

# Plot based on the user's choice
if method.lower() == 'euler':
    xyz = euler()
    plt.plot(xyz[0, :], xyz[2, :], label='Euler', linewidth=0.5)
elif method.lower() == 'midpoint':
    xyz = midpoint()
    plt.plot(xyz[0, :], xyz[2, :], label='Midpoint', linewidth=0.5)
elif method.lower() == 'rk4':
    xyz = rk4()
    plt.plot(xyz[0, :], xyz[2, :], label='RK4', linewidth=0.5)
elif method.lower() == 'all':
    xyz = euler()
    plt.plot(xyz[0, :], xyz[2, :], label='Euler', linewidth=0.5)
    xyz = midpoint()
    plt.plot(xyz[0, :], xyz[2, :], label='Midpoint', linewidth=0.5)
    xyz = rk4()
    plt.plot(xyz[0, :], xyz[2, :], label='RK4', linewidth=0.5)
else:
    print("Invalid method. Please choose either Euler, Midpoint, RK4, or All.")
    exit()

plt.xlabel('x')
plt.ylabel('z')
plt.title('Lorenz Attractor')
plt.legend()
plt.show()
