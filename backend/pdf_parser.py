import numpy
import paper_cv
import pdf2image
from google.cloud import vision
import os

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
        content = paper_cv.detect_lines(pages[p].image, 'wtf'+str(p), debug=True)
        image = vision.Image(content=content)

        response = client.document_text_detection(image=image)
        labels = response.text_annotations
        for l in labels:
            if l.description.strip().strip('\n') in {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}:
                pages[p].pdf_id = int(l.description.strip().strip('\n'))
                pages[p].page = int(l.description.strip().strip('\n'))
                break

    print(pages)

    # Next let the user reorder it if they want to

    # Finally, assemble each of the pdfs and export


if __name__ == '__main__':
    parse("../test_data/textbook.pdf")
