#!/usr/bin/env python3

"""ImageNet dataset."""

import os
import cv2
import numpy as np
import SuperGlobal.core.transforms as transforms
import torch.utils.data

import pickle as pkl

# Per-channel mean and SD values in BGR order
_MEAN = [0.406, 0.456, 0.485]
_SD = [0.225, 0.224, 0.229]


class DataSet(torch.utils.data.Dataset):
	"""Common dataset."""

	def __init__(self, data_path, dataset, files: list[str], scale_list):
		assert os.path.exists(
			data_path), "Data path '{}' not found".format(data_path)
		self._data_path, self._dataset, self._scale_list = data_path, dataset, scale_list
		self._construct_db(files)

	def _construct_db(self, files):
		self._db = []
		for file in files:
			im_path = os.path.join(
				self._data_path, self._dataset, "jpg", file)
			self._db.append({"im_path": im_path})



	def _prepare_im(self, im):
		"""Prepares the image for network input."""
		im = im.transpose([2, 0, 1])
		# [0, 255] -> [0, 1]
		im = im / 255.0
		# Color normalization
		im = transforms.color_norm(im, _MEAN, _SD)
		return im

	def __getitem__(self, index):
		# Load the image
		try:
			im = cv2.imread(self._db[index]["im_path"])

			# if self._split == "query":
			# 	bbx = self._db[index]["bbox"]
			# 	im = im[int(bbx[1]):int(bbx[3]), int(bbx[0]):int(bbx[2])]
			im_list = []

			for scale in self._scale_list:
				if scale == 1.0:
					im_np = im.astype(np.float32, copy=False)
					im_list.append(im_np)
				elif scale < 1.0:
					im_resize = cv2.resize(im, dsize=(0, 0), fx=scale, fy=scale, interpolation=cv2.INTER_AREA)
					im_np = im_resize.astype(np.float32, copy=False)
					im_list.append(im_np)
				elif scale > 1.0:
					im_resize = cv2.resize(im, dsize=(0, 0), fx=scale, fy=scale, interpolation=cv2.INTER_LINEAR)
					im_np = im_resize.astype(np.float32, copy=False)
					im_list.append(im_np)
				else:
					assert ()

		except Exception as e:
			print(e)
			print('error: ', self._db[index]["im_path"])

		for idx in range(len(im_list)):
			im_list[idx] = self._prepare_im(im_list[idx])
		return im_list

	def __len__(self):
		return len(self._db)
