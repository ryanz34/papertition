import numpy as np
import cv2
import math


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


def detectlines(path, outname, debug=False):
    gray = cv2.imread(path)
    edges = cv2.Canny(gray, 50, 150, apertureSize=3)

    if debug:
        cv2.imwrite(outname + 'edges-50-150.jpg', edges)

    minLineLength = gray.shape[1] * 0.6

    lines = cv2.HoughLinesP(image=edges, rho=1, theta=np.pi / 180, threshold=100, lines=np.array([]),
                            minLineLength=minLineLength, maxLineGap=300)

    a, b, c = lines.shape

    v = ((999999, 999999), (999999, 999999))
    h = ((999999, 999999), (999999, 999999))
    vdetect = False
    hdetect = False

    for i in range(a):

        if vertical(lines[i][0][0], lines[i][0][1], lines[i][0][2], lines[i][0][3], 0.005):

            if length(lines[i][0][0], lines[i][0][1], lines[i][0][2], lines[i][0][3]) > length(v[0][0], v[0][1], v[1][0], v[1][1]):

                vdetect = True
                v = (lines[i][0][0], lines[i][0][1]), (lines[i][0][2], lines[i][0][3])

        elif horizontal(lines[i][0][0], lines[i][0][1], lines[i][0][2], lines[i][0][3], 0.005):

            if max(lines[i][0][1], lines[i][0][1]) < max(h[0][1], h[1][1]):
                hdetect = True
                h = (lines[i][0][0], lines[i][0][1]), (lines[i][0][2], lines[i][0][3])

    if not vdetect or not hdetect:
        raise Exception("Cannot detect valid vertical or horizontal line")

    x = (v[0][0] + v[1][0]) // 2
    y = (h[0][1] + h[1][1]) // 2

    cropped = gray[1:y, 1:x]
    cv2.imwrite(outname + 'cropped.jpg', cropped)

    if debug:
        cv2.line(gray, v[0], v[1], (0, 0, 255), 4)
        cv2.line(gray, h[0], h[1], (0, 0, 255), 4)

        cv2.imwrite(outname + 'houghlines5.jpg', gray)


if __name__ == '__main__':
    detectlines('TData/k-2.jpg', 'k-2')
