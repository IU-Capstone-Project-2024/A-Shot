import os

import numpy as np
import torch
from SuperGlobal.config import cfg
from tqdm import tqdm
import torch.nn.functional as F

from utils.src.image_grouping.datasetV2 import DataSet


class EmbeddingExtractor(torch.nn.Module):
	def __init__(self, original_model):
		super(EmbeddingExtractor, self).__init__()
		self.model = original_model

	def forward(self, tensor):
		scale_list = cfg.TEST.SCALE_LIST
		desc = self.model.extract_global_descriptor(tensor,True, True, True, scale_list)
		# if len(desc.shape) == 1:
		# 	desc.unsqueeze_(0)
		return desc.detach().cpu()
