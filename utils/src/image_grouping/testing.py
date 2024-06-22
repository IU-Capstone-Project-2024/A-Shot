import argparse
import math
import os
import numpy as np
from matplotlib import image as mpimg
import torch
from tqdm import tqdm
import torch.nn.functional as F
import matplotlib.pyplot as plt

from SuperGlobal.modules.reranking.MDescAug import MDescAug
from SuperGlobal.modules.reranking.RerankwMDA import RerankwMDA

from SuperGlobal.model.CVNet_Rerank_model import CVNet_Rerank
from SuperGlobal.config import cfg
import SuperGlobal.core.checkpoint as checkpoint
from dataset import DataSet


# TODO file check
# TODO features check

def construct_dataloader(_DATA_DIR, dataset_name, files, scale_list):
	"""Constructs the data loader for the given dataset."""
	# Construct the dataset
	dataset = DataSet(_DATA_DIR, dataset_name, files, scale_list)
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


@torch.no_grad()
def extract_features(model, data_dir, dataset, files, scale_list_fix, gemp, rgem, sgem, scale_list):
	with torch.no_grad():
		test_loader = construct_dataloader(data_dir, dataset, files, scale_list_fix)

		img_feats = [[] for _ in range(len(scale_list_fix))]

		for im_list in tqdm(test_loader):
			for idx in range(len(im_list)):
				im_list[idx] = im_list[idx].cpu()

				desc = model.extract_global_descriptor(im_list[idx], gemp, rgem, sgem, scale_list)

				if len(desc.shape) == 1:
					desc.unsqueeze_(0)
				img_feats[idx].append(desc.detach().cpu())
		for idx in range(len(img_feats)):
			img_feats[idx] = torch.cat(img_feats[idx], dim=0)
			if len(img_feats[idx].shape) == 1:
				img_feats[idx].unsqueeze_(0)

		img_feats = img_feats[0]  # 6422 2048

		img_feats = F.normalize(img_feats, p=2, dim=1)
		img_feats = img_feats.cpu().numpy()

	return img_feats


@torch.no_grad()
def calculate_embs(model, data_dir, dataset, imgs: list[str]):
	scale_list = cfg.TEST.SCALE_LIST
	gemp = cfg.SupG.gemp
	rgem = cfg.SupG.rgem
	sgem = cfg.SupG.sgem

	emb_dir = os.path.join(data_dir, dataset, "embeddings.pt")
	if os.path.exists(emb_dir):
		print("Loading embeddings...")
		return torch.load(emb_dir)

	X = extract_features(model, data_dir, dataset, imgs, [1.0], gemp, rgem, sgem, scale_list)
	if torch.cuda.is_available():
		X = torch.tensor(X).cuda()
	else:
		X = torch.tensor(X).cpu()
	torch.save(X, emb_dir)
	return X


def show_all_similar(folder_path, similar_images, query_image):
	n = len(similar_images) + 1
	rows = 1
	cols = n
	if n > 2:
		rows = math.ceil(math.sqrt(n))
		cols = math.ceil(n / rows)

	# Create a figure and a set of subplots
	fig, axs = plt.subplots(rows, cols, figsize=(3 * cols, 3 * rows))

	# Ensure axs is always a 2D array
	if n == 1:
		axs = np.array([[axs]])
	elif n == 2:
		axs = np.array([axs])

	# Display query image
	img = mpimg.imread(folder_path + "/" + query_image)
	axs[0, 0].imshow(img)
	axs[0, 0].set_title(f'Query Image\n{query_image}')
	axs[0, 0].axis('off')

	# Display similar images
	for i, item in enumerate(similar_images.items()):
		image, prob = item
		img = mpimg.imread(folder_path + "/" + image)
		row = (i + 1) // cols
		col = (i + 1) % cols
		axs[row, col].imshow(img)
		axs[row, col].set_title(f'Similar Image {i + 1}:\n{image}\n Probability: {prob}')
		axs[row, col].axis('off')

	# Hide any empty subplots
	for i in range(n, rows * cols):
		row = (i + 0) // cols
		col = (i + 0) % cols
		axs[row, col].axis('off')

	# Adjust the layout
	plt.tight_layout()
	plt.show()


def custom_test_model(model: CVNet_Rerank, data_dir, dataset, qimg: str):
	# Idk What is that
	# ---------------------------------------------------#
	torch.backends.cudnn.benchmark = False
	model.eval()  # Set the module in evaluation mode
	state_dict = model.state_dict()
	model.load_state_dict(state_dict)
	# ---------------------------------------------------#

	# Calculate embeddings of all images
	images_dir = os.path.join(data_dir, dataset, "jpg")
	imgs = np.array([file for file in os.listdir(images_dir)])
	X = calculate_embs(model, data_dir, dataset, imgs)

	# Take embedding of query image
	qimg_id = np.where(imgs == qimg)[0]
	Q = X[qimg_id]

	# Calculate matrix of similarity
	sim = torch.matmul(X, Q.T).numpy()

	# Collect names with probs
	prob_dict = {img: prob[0] for img, prob in zip(imgs, sim)}
	prob_dict = dict(sorted(prob_dict.items(), key=lambda x: x[1], reverse=True))

	print("All images:")
	for k, v in prob_dict.items():
		print(f"{k}\t\t{v}")

	# Remove not similar
	threshold = 0.75
	prob_dict = {k: math.ceil(v * 10 ** 5) / 10 ** 5 for k, v in prob_dict.items() if v >= threshold and k != qimg}

	if len(prob_dict) == 0:
		print("No Similar Images!")
		return

	print("Similar images:")
	for k, v in prob_dict.items():
		print(f"{k}\t\t{v}")

	show_all_similar(images_dir, prob_dict, qimg)
	return


# TODO what is that?
# is_rerank = cfg.SupG.rerank,
# if is_rerank:
# MDescAug_obj = MDescAug()
# RerankwMDA_obj = RerankwMDA()
# 	rerank_dba_final, res_top1000_dba, ranks_trans_1000_pre, x_dba = MDescAug_obj(X, Q, ranks)
# 	ranks = RerankwMDA_obj(ranks, rerank_dba_final, res_top1000_dba, ranks_trans_1000_pre, x_dba)
# ranks = ranks.data.cpu().numpy()


def setup_model():
	print("=> creating CVNet_Rerank model")
	model = CVNet_Rerank(cfg.MODEL.DEPTH, cfg.MODEL.HEADS.REDUCTION_DIM, cfg.SupG.relup)
	if torch.cuda.is_available():
		print("Launch on CUDA")
		return model.cuda()
	print("Launch on CPU")
	return model.cpu()


def tester(qimg: str):
	model = setup_model()
	weights = "CVPR2022_CVNet_R50.pyth"
	checkp = os.path.join(os.getcwd(), "SuperGlobal", cfg.TEST.WEIGHTS, weights)
	checkpoint.load_checkpoint(checkp, model)

	data_dir = os.path.join(os.getcwd(), "datasets")
	custom_test_model(model, data_dir, "custom", qimg)


if __name__ == "__main__":
	parser = argparse.ArgumentParser(description='Find Similar Images')
	parser.add_argument('qimg', type=str, help='Query Image')
	args = parser.parse_args()
	tester(args.qimg)
