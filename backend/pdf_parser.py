import numpy
import paper_cv
import pdf2image
from google.cloud import vision
import os
import math

os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = os.path.abspath("papertition-c318e0fafafa.json")

class Page:
    def __init__(self, image):
        self.image = image
        self.pdf_id = -1
        self.page = -1

    def __repr__(self):
        return "PDF ID: %s Page: %s" % (str(self.pdf_id), str(self.page))

    def __str__(self):
        return self.__repr__

class Text:
    def __init__(self, bounds, num):
        self.bounds = [(v.x, v.y) for v in bounds]
        self.num = num
        self.x, self.y = self.compute_avg()
        self.dist = self.compute_dist()

    def compute_avg(self):
        min_x = self.bounds[0][0]
        min_y = self.bounds[0][1]

        for v in range(1, len(self.bounds)):
            min_x = min(min_x, self.bounds[v][0])
            min_y = min(min_y, self.bounds[v][1])

        return min_x, min_y

    def compute_dist(self):
        return math.hypot(self.x, self.y)

def dist(num : Text):
    return num.dist

def import_pages(path):
    """
    Imports the images at a path as a list of Page objects
    :param path:
    :return:
    """

    return [Page(numpy.asarray(image)) for image in pdf2image.convert_from_path(path)]


def parse(path):
    pages = import_pages(path)

    client = vision.ImageAnnotatorClient()

    for p in range(len(pages)):
        content = paper_cv.detect_lines(pages[p].image, 'out/wtf'+str(p), debug=False)
        image = vision.Image(content=content)

        response = client.document_text_detection(image=image)
        labels = response.text_annotations
        numbers = []

        for l in labels:
            if l.description.strip() in {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}:
                numbers.append(Text(l.bounding_poly.vertices, int(l.description.strip())))

        if len(numbers) > 0:
            pid = numbers[0]
            page = numbers[0]

            for n in numbers:
                pid = min(pid, n, key=dist)
                page = max(page, n, key=dist)

            pages[p].pdf_id = pid.num
            pages[p].page = page.num

    print(pages)

    # Next let the user reorder it if they want to

    # Finally, assemble each of the pdfs and export


if __name__ == '__main__':
    parse("../test_data/real_test_case.pdf")
