import numpy
import paper_cv
import pdf2image
from google.cloud import vision


class Page:
    def __init__(self, image):
        self.image = image
        self.pdf_id = -1
        self.page = -1


def import_pages(path):
    """
    Imports the images at a path as a list of Page objects
    :param path:
    :return:
    """

    return [Page(numpy.asarray(image)) for image in pdf2image.convert_from_path(path, poppler_path="c:\\Users\\ryan\\PycharmProjects\\papertition\\poppler-20.12.1\\Library\\bin")]


def parse(path):
    pages = import_pages(path)[:1]

    client = vision.ImageAnnotatorClient()

    for page in pages:
        content = paper_cv.detect_lines(page.image, 'wtf', debug=True)

        image = vision.Image(content=content)

        response = client.document_text_detection(image=image)
        labels = response.text_annotations
        for l in labels:
            if l.description.strip().strip('\n') in {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'}:
                page.pdf_id = int(l.description.strip().strip('\n'))
                page.page = int(l.description.strip().strip('\n'))
                break

    # Next let the user reorder it if they want to

    # Finally, assemble each of the pdfs and export


if __name__ == '__main__':
    parse("../test_data/k.pdf")
