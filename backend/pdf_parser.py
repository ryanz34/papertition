import pdf2image
import numpy

def import_images(path):
    """
    Imports the images at a path as a list of numpy matracies
    :param path:
    :return:
    """

    return [numpy.asarray(image) for image in pdf2image.convert_from_path(path)]

def parse(path):
    images = import_images(path)

    print(images)


if __name__ == '__main__':
    parse("../test_data/test.pdf")