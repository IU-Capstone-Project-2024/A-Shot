import onnxruntime
import os
import torch
from tqdm import tqdm

from SuperGlobal.model.CVNet_Rerank_model import CVNet_Rerank
from SuperGlobal.config import cfg
import SuperGlobal.core.checkpoint as checkpoint
from ModelWrapper import EmbeddingExtractor
from utils.src.image_grouping.datasetV2 import DataSet


def construct_dataloader(path):
	"""Constructs the data loader for the given dataset."""
	# Construct the dataset
	dataset = DataSet(path, [1.0])
	# Create a loader
	loader = torch.utils.data.DataLoader(
		dataset,
		batch_size=1,
		shuffle=False,
		sampler=None,
		num_workers=4,
		pin_memory=False,
		drop_last=False,
	)
	return loader


def start():
	model = CVNet_Rerank(cfg.MODEL.DEPTH, cfg.MODEL.HEADS.REDUCTION_DIM, cfg.SupG.relup).cpu()
	weights = "CVPR2022_CVNet_R50.pyth"
	checkp = os.path.join(os.getcwd(), "SuperGlobal", cfg.TEST.WEIGHTS, weights)
	checkpoint.load_checkpoint(checkp, model)

	wrapped_model = EmbeddingExtractor(model)

	loader = construct_dataloader("datasets/custom/test")

	torch.tensor([]).cpu()

	# for im_list in loader:
	# 	tensor = im_list[0].cpu()

	tensor = torch.randn(1, 3, 500, 500).cpu()

	prog = torch.onnx.dynamo_export(wrapped_model, tensor)
	prog.save("checkpoints/WrapperONNX.onnx")


def load_model():
	sess = onnxruntime.InferenceSession("embedding_extractor.onnx")

	data_path = "datasets/custom/jpg"
	input_name = sess.get_inputs()[0].name
	output_name = sess.get_outputs()[0].name

	out = sess.run([output_name], {input_name: data_path})[0]
	print(out)


def f():
	from torch.fx import symbolic_trace

	model = CVNet_Rerank(cfg.MODEL.DEPTH, cfg.MODEL.HEADS.REDUCTION_DIM, cfg.SupG.relup).cpu()
	weights = "CVPR2022_CVNet_R50.pyth"
	checkp = os.path.join(os.getcwd(), "SuperGlobal", cfg.TEST.WEIGHTS, weights)
	checkpoint.load_checkpoint(checkp, model)

	wrapped_model = EmbeddingExtractor(model)

	traced_model = symbolic_trace(wrapped_model)

	# Print the graph
	print(traced_model.graph)


if __name__ == "__main__":
	# f()
	start()
