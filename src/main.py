import tkinter as tk
import random
from collections import deque
import sys

seed = random.randrange(sys.maxsize)
print(hex(seed))

random.seed(seed)


CELL_SIZE = 25
GRID_WIDTH = 40
GRID_HEIGHT = 40

class MazeGenerator:
    def __init__(self, master):
        self.master = master
        self.canvas = tk.Canvas(master, width=CELL_SIZE*GRID_WIDTH, height=CELL_SIZE*GRID_HEIGHT, bg='white')
        self.canvas.pack()

        self.grid = [[{'visited': False, 'walls': {'top': True, 'bottom': True, 'left': True, 'right': True}}
                      for _ in range(GRID_WIDTH)] for _ in range(GRID_HEIGHT)]

        self.walls = []
        self.line_ids = {}  # (x, y, 'wall') -> line_id


        self.start_x = 0
        self.start_y = 0
        self.end_x = GRID_WIDTH - 1
        self.end_y = GRID_HEIGHT - 1

        self.grid[self.start_y][self.start_x]['visited'] = True
        self.add_walls(self.start_x, self.start_y)

        self.solution_path = []

        self.master.after(1, self.step)

    def add_walls(self, x, y):
        directions = [('top', (x, y-1)), ('bottom', (x, y+1)), ('left', (x-1, y)), ('right', (x+1, y))]
        for dir, (nx, ny) in directions:
            if 0 <= nx < GRID_WIDTH and 0 <= ny < GRID_HEIGHT:
                self.walls.append((x, y, dir))

    def step(self):
        if not self.walls:
            print("Maze generation complete")
            self.solve_maze()
            self.draw_grid()
            return

        wall = random.choice(self.walls)
        self.walls.remove(wall)

        x, y, direction = wall
        dx, dy = 0, 0
        if direction == 'top':
            dy = -1
        elif direction == 'bottom':
            dy = 1
        elif direction == 'left':
            dx = -1
        elif direction == 'right':
            dx = 1

        nx, ny = x + dx, y + dy

        if 0 <= nx < GRID_WIDTH and 0 <= ny < GRID_HEIGHT:
            if not self.grid[ny][nx]['visited']:
                self.grid[y][x]['walls'][direction] = False
                opposite = {'top': 'bottom', 'bottom': 'top', 'left': 'right', 'right': 'left'}
                self.grid[ny][nx]['walls'][opposite[direction]] = False

                self.grid[ny][nx]['visited'] = True
                self.add_walls(nx, ny)

        self.draw_grid()
        self.generate_maze()
        self.solve_maze()
        self.draw_grid()


    def solve_maze(self):
        #bfs
        queue = deque()
        queue.append((self.start_x, self.start_y))
        came_from = {}
        came_from[(self.start_x, self.start_y)] = None

        while queue:
            x, y = queue.popleft()

            if (x, y) == (self.end_x, self.end_y):
                break

            neighbors = []
            if not self.grid[y][x]['walls']['top']:
                neighbors.append((x, y-1))
            if not self.grid[y][x]['walls']['bottom']:
                neighbors.append((x, y+1))
            if not self.grid[y][x]['walls']['left']:
                neighbors.append((x-1, y))
            if not self.grid[y][x]['walls']['right']:
                neighbors.append((x+1, y))

            for nx, ny in neighbors:
                if (nx, ny) not in came_from:
                    queue.append((nx, ny))
                    came_from[(nx, ny)] = (x, y)

        # Reconstruct path
        path = []
        current = (self.end_x, self.end_y)
        while current is not None:
            path.append(current)
            current = came_from[current]

        path.reverse()
        self.solution_path = path

    def draw_grid(self):
        self.canvas.delete("all")
        line_ids = {}  # temp to replace self.line_ids
        for y in range(GRID_HEIGHT):
            for x in range(GRID_WIDTH):
                cell = self.grid[y][x]
                x1 = x * CELL_SIZE
                y1 = y * CELL_SIZE
                x2 = x1 + CELL_SIZE
                y2 = y1 + CELL_SIZE

                if cell['walls']['top']:
                    lid = self.canvas.create_line(x1, y1, x2, y1)
                    line_ids[(x, y, 'top')] = lid
                if cell['walls']['right']:
                    lid = self.canvas.create_line(x2, y1, x2, y2)
                    line_ids[(x, y, 'right')] = lid
                if cell['walls']['bottom']:
                    lid = self.canvas.create_line(x1, y2, x2, y2)
                    line_ids[(x, y, 'bottom')] = lid
                if cell['walls']['left']:
                    lid = self.canvas.create_line(x1, y1, x1, y2)
                    line_ids[(x, y, 'left')] = lid
                self.line_ids = line_ids


        # Highlight Start (green) and End (blue)
        sx1 = self.start_x * CELL_SIZE
        sy1 = self.start_y * CELL_SIZE
        sx2 = sx1 + CELL_SIZE
        sy2 = sy1 + CELL_SIZE
        self.canvas.create_rectangle(sx1+2, sy1+2, sx2-2, sy2-2, fill='green')

        ex1 = self.end_x * CELL_SIZE
        ey1 = self.end_y * CELL_SIZE
        ex2 = ex1 + CELL_SIZE
        ey2 = ey1 + CELL_SIZE
        self.canvas.create_rectangle(ex1+2, ey1+2, ex2-2, ey2-2, fill='blue')

        # Draw solution path (red line)
        if self.solution_path:
            for i in range(len(self.solution_path)-1):
                x1, y1 = self.solution_path[i]
                x2, y2 = self.solution_path[i+1]

                x1 = x1 * CELL_SIZE + CELL_SIZE // 2
                y1 = y1 * CELL_SIZE + CELL_SIZE // 2
                x2 = x2 * CELL_SIZE + CELL_SIZE // 2
                y2 = y2 * CELL_SIZE + CELL_SIZE // 2

                self.canvas.create_line(x1, y1, x2, y2, fill='red', width=3)

# Create window


    def on_mouse_move(self,event):
        x, y = event.x, event.y
        overlap_items = self.canvas.find_overlapping(x - 2, y - 2, x + 2, y + 2)

        # Reset all lines to black
        for line_id in self.line_ids.values():
            self.canvas.itemconfig(line_id, fill="black")

        # Highlight overlapping ones
        for lid in overlap_items:
            if lid in self.line_ids.values():
                self.canvas.itemconfig(lid, fill="red")

    def generate_maze(self):
        while self.walls:
            wall = random.choice(self.walls)
            self.walls.remove(wall)

            x, y, direction = wall
            dx, dy = 0, 0
            if direction == 'top':
                dy = -1
            elif direction == 'bottom':
                dy = 1
            elif direction == 'left':
                dx = -1
            elif direction == 'right':
                dx = 1

            nx, ny = x + dx, y + dy

            if 0 <= nx < GRID_WIDTH and 0 <= ny < GRID_HEIGHT:
                if not self.grid[ny][nx]['visited']:
                    self.grid[y][x]['walls'][direction] = False
                    opposite = {'top': 'bottom', 'bottom': 'top', 'left': 'right', 'right':'left'}
                    self.grid[ny][nx]['walls'][opposite[direction]] = False

                    self.grid[ny][nx]['visited'] = True
                    self.add_walls(nx, ny)

# Bind mouse movement



root = tk.Tk()
root.title("Prim's Maze Generator and Solver")
root.configure(cursor="dot")
maze = MazeGenerator(root)
maze.canvas.bind("<Motion>", maze.on_mouse_move)

root.mainloop()
