import glob
import os

import numpy as np
from PIL import Image
from torch.utils import data
from torchvision import transforms
import torchvision.transforms.functional as F


class ImageData(data.Dataset):
	def __init__(self, img_root, transform=None):
		self.img_paths = sorted(glob.glob(os.path.join(img_root, '*')))
		self.transform = transform

		if self.transform is None:
			self.transform = transforms.Compose([
				transforms.ToTensor(),
				transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),
			])

	def __getitem__(self, item):
		img_path = self.img_paths[item]
		name = os.path.basename(img_path)

		image = Image.open(img_path).convert("RGB")
		x = F.resize(image, [320, 320])
		x = self.transform(x)
		image = np.array(image)

		return name, image, x

	def __len__(self):
		return len(self.img_paths)
