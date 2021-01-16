import pdf2image

def parse(path):
    images = pdf2image.convert_from_path(path)

    print(images)

if __name__ == '__main__':
    parse("../test_data/test.pdf")