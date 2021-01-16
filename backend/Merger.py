from PIL import Image
import sys


class PDFPackage:
    def __init__(self, name):
        self.name = name
        self.imgs = []

    def append(self, path):
        self.imgs.append(Image.open(path))

    def save(self, dir):
        if len(self.imgs) > 0:
            start = self.imgs[0]

            additional = []

            for j in range(1, len(self.imgs)):
                additional.append(self.imgs[j])

            start.save(dir+self.name+'.pdf', "PDF", resolution=100.0, save_all=True, append_images=additional)


args = sys.argv
current = ''

for i in range(1, len(args)):
    if len(args[i]) > 0 and args[i][0] == '-':
        if current != '':
            current.save('out/')

        name = args[i].strip('-')
        print(name)
        current = PDFPackage(name)

    else:
        current.append(args[i])

current.save('out/')
