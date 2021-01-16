import math

import cv2
import numpy as np


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


def detect_lines(img, outname, debug=False, vhthreshold=0.05, hgap=200):

    lab = cv2.cvtColor(img, cv2.COLOR_BGR2LAB)

    l, a, b = cv2.split(lab)

    clahe = cv2.createCLAHE(clipLimit=3.0, tileGridSize=(8, 8))
    cl = clahe.apply(l)

    limg = cv2.merge((cl, a, b))

    gray = cv2.cvtColor(limg, cv2.COLOR_LAB2BGR)

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

        if vertical(lines[i][0][0], lines[i][0][1], lines[i][0][2], lines[i][0][3], vhthreshold):

            if lines[i][0][0] > hgap and length(lines[i][0][0], lines[i][0][1], lines[i][0][2], lines[i][0][3]) > length(v[0][0], v[0][1], v[1][0], v[1][1]):

                vdetect = True
                v = (lines[i][0][0], lines[i][0][1]), (lines[i][0][2], lines[i][0][3])

        elif horizontal(lines[i][0][0], lines[i][0][1], lines[i][0][2], lines[i][0][3], vhthreshold):

            if max(lines[i][0][1], lines[i][0][1]) < max(h[0][1], h[1][1]):
                hdetect = True
                h = (lines[i][0][0], lines[i][0][1]), (lines[i][0][2], lines[i][0][3])

    if not vdetect or not hdetect:
        raise Exception("Cannot detect valid vertical or horizontal line")

    x = (v[0][0] + v[1][0]) // 2
    y = (h[0][1] + h[1][1]) // 2

    cropped = gray[1:y, 1:x]
    if debug:
        cv2.imwrite(outname + 'cropped.jpg', cropped)

    if debug:
        cv2.line(gray, v[0], v[1], (0, 0, 255), 4)
        cv2.line(gray, h[0], h[1], (0, 0, 255), 4)

        cv2.imwrite(outname + 'houghlines5.jpg', gray)

    retval, buffer = cv2.imencode('.png', cropped)
    imgbytes = buffer.tobytes()

    return imgbytes


if __name__ == '__main__':
    detect_lines(cv2.imread('TData/k-2.jpg'), 'k-2', debug=True)
