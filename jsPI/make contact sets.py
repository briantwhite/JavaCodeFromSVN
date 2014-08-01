from tkinter import *
import math
import sys
import colorsys

itemRadius = 10.0
worldSize = 20

#make lists of conformations and their contact sets (don't count 1 touching 2)
# report as list of directions (0..5) and contact pairs "2,4" etc

# a pretty-print routine to make a picture of the configuration
# items is list of coordinate pairs (tuples)
def ShowItems(items):
    
# first, find boundaries
    minX = sys.float_info.max
    minY = sys.float_info.max
    maxX = -sys.float_info.max
    maxY = -sys.float_info.max
    
    for item in items:
        u = item[0] * itemRadius * 2
        v = item[1] * itemRadius * 2
        center_x = float(u) + float(v)/2.0
        center_y = -float(v)*math.sqrt(3)/2.0
        x = center_x - itemRadius
        if (x < minX):
            minX = x
        y = center_y - itemRadius
        if (y < minY):
            minY = y
        x = center_x + itemRadius
        if (x > maxX):
            maxX = x
        y = center_y + itemRadius
        if (y > maxY):
            maxY = y

    xSize = maxX - minX
    ySize = maxY - minY 
    master = Tk()
    w = Canvas(master, width=(xSize + itemRadius), height=(ySize + itemRadius))
    w.pack()

    xOffset = minX - itemRadius
    yOffset = minY - itemRadius

    for item in items:
        u = item[0] * itemRadius * 2
        v = item[1] * itemRadius * 2
        center_x = float(u) + float(v)/2.0
        center_y = -float(v)*math.sqrt(3)/2.0
        w.create_oval(center_x - itemRadius - xOffset, center_y - itemRadius - yOffset,
                      center_x + itemRadius - xOffset, center_y + itemRadius - yOffset,
                      fill="yellow")
        w.create_text(center_x - xOffset, center_y - yOffset, text=item[2], fill="black")


def ShowWorld(world):
    allItems = [];
    for u in range(worldSize):
        for v in range(worldSize):
            if (world[u][v] != -1):
                allItems.append((u,v,world[u][v]))
    return allItems

# used for direction changing
ou = [-1, -1, 0, 1, 1, 0]
ov = [0, 1, 1, 0, -1, -1]

# given a direction
#   0 = west
#   1 = northwest
#   2 = northeast
#   3 = east
#   4 = southeast
#   5 = southwest
# give a (du, dv) tuple to add to existing u, v to get new coordinates
def getDirectionIncrement(direction):
    return (ou[direction], ov[direction])



# set up world of u,v coordinates as in paper
# if an item is placed in a cell, the index of that item (0 thru (n-1)) is put in that cell
# empty cells are filled with -1

#number of items in chain
NumItems = 4
currentItemIndex = 0

# recursive placement function
def tryToPlaceItem(NumItems, itemIndex, u, v, world, directionList):
    # if you've successfully reached the end of the chain, you're done
    if (itemIndex == NumItems):
        print(directionList[1:])
#        ShowItems(ShowWorld(world))
#        print("\tremoving item at %d, %d"% (u,v))
        World[u][v] = -1
        return 

    # otherwise, try to place the item
    for d in range (6):
        delta = getDirectionIncrement(d)
#        print("item %d; trying direction %d - new location %d,%d" % (itemIndex, d, u + delta[0], v + delta[1]))
        # see if cell is empty
        if (World[u + delta[0]][v + delta[1]] == -1):
            World[u + delta[0]][v + delta[1]] = itemIndex
            directionList[itemIndex] = d
#            print("\t success; placed item %d at %d,%d" % (itemIndex, u + delta[0], v + delta[1]))
            tryToPlaceItem(NumItems, itemIndex + 1, u + delta[0], v + delta[1], world, directionList)
#        else:
#            print("\t fail")

#    print("\t opt of choices; need to back up")
#    print("\t removing item from %d,%d" % (u,v))
    World[u][v] = -1


# clear world and place first two items
World = [[-1 for x in range(worldSize)] for x in range(worldSize)]
directionList = [-1 for x in range(NumItems)]

#place the first item - there's only one way to do it
u = 10
v = 10
World[u][v] = currentItemIndex
currentItemIndex += 1

#place the second item - to the east of the first one
firstDirection = 3
directionList[currentItemIndex] = firstDirection
delta = getDirectionIncrement(firstDirection)
u = u + delta[0]
v = v + delta[1]
World[u][v] = currentItemIndex
currentItemIndex += 1

tryToPlaceItem(NumItems, currentItemIndex, u, v, World, directionList)
