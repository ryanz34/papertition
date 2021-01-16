import numpy as np
import cv2
import math

gray = cv2.imread('TData/k-2.jpg')
edges = cv2.Canny(gray, 50, 150, apertureSize=3)
cv2.imwrite('edges-50-150.jpg', edges)

minLineLength = gray.shape[1] * 0.6

lines = cv2.HoughLinesP(image=edges, rho=1, theta=np.pi / 180, threshold=100, lines=np.array([]),
                        minLineLength=minLineLength, maxLineGap=300)

a, b, c = lines.shape


def vertical(x1, y1, x2, y2, threshold):
    dx = abs(x1 - x2)
    dy = abs(y1 - y2)

    return dy > 0 and (dx / dy) < threshold


def horizontal(x1, y1, x2, y2, threshold):
    dx = abs(x1 - x2)
    dy = abs(y1 - y2)

    return dx > 0 and (dy / dx) < threshold


def length(x1, y1, x2, y2):
    dx = abs(x1 - x2)
    dy = abs(y1 - y2)

    return math.hypot(dx, dy)


v = ((999999, 999999), (999999, 999999))
h = ((999999, 999999), (999999, 999999))
for i in range(a):

    if vertical(lines[i][0][0], lines[i][0][1], lines[i][0][2], lines[i][0][3], 0.005):

        if length(lines[i][0][0], lines[i][0][1], lines[i][0][2], lines[i][0][3]) > length(v[0][0], v[0][1], v[1][0],
                                                                                           v[1][1]):
            v = (lines[i][0][0], lines[i][0][1]), (lines[i][0][2], lines[i][0][3])

    elif horizontal(lines[i][0][0], lines[i][0][1], lines[i][0][2], lines[i][0][3], 0.005):

        if max(lines[i][0][1], lines[i][0][1]) < max(h[0][1], h[1][1]):
            h = (lines[i][0][0], lines[i][0][1]), (lines[i][0][2], lines[i][0][3])

x = (v[0][0] + v[1][0]) // 2
y = (h[0][1] + h[1][1]) // 2

cropped = gray[1:y, 1:x]
cv2.imwrite('cropped.jpg', cropped)


cv2.line(gray, v[0], v[1], (0, 0, 255), 4)
cv2.line(gray, h[0], h[1], (0, 0, 255), 4)

cv2.imwrite('houghlines5.jpg', gray)
