import os
import numpy as np
import onnxruntime
import torchvision.transforms as transforms
from PIL import Image


def test_onnx():
	images_dir = os.path.join(os.getcwd(), "datasets", "custom", "jpg")

	input_img = os.path.join(images_dir, "A1.jpg")
	key_img = os.path.join(images_dir, "car.jpg")

	input_img = Image.open(input_img)
	key_img = Image.open(key_img)

	t = transforms.Compose([
		transforms.Resize((512, 512)),
		transforms.ToTensor(),
		transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
	])

	input_img = t(input_img).unsqueeze(0).numpy()
	key_img = t(key_img).unsqueeze(0).numpy()

	session = onnxruntime.InferenceSession("./checkpoints/CVNet_Rerank.onnx")
	out = session.run(None, {"input": input_img, "key_img": key_img})
	print(out[0])


if __name__ == "__main__":
	test_onnx()
