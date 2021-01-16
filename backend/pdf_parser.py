import numpy
import paper_cv
import pdf2image


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

    return [Page(numpy.asarray(image)) for image in pdf2image.convert_from_path(path)]


def parse(path):
    pages = import_pages(path)

    for page in pages:
        paper_cv.detect_lines(page.image, 'wtf', debug=True)

        # Fetch the page # and pdf id from the cv thingie
        page.pdf_id = 69
        page.page = 69

    # Next let the user reorder it if they want to

    # Finally, assemble each of the pdfs and export


if __name__ == '__main__':
    parse("../test_data/test.pdf")
