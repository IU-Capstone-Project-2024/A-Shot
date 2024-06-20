import torch
import os
from SuperGlobal.model.CVNet_Rerank_model import CVNet_Rerank
from SuperGlobal.config import cfg
import SuperGlobal.core.checkpoint as checkpoint
import torchvision.transforms as transforms

def setup_model():
    print("=> creating CVNet_Rerank model")
    model = CVNet_Rerank(101, cfg.MODEL.HEADS.REDUCTION_DIM, cfg.SupG.relup)
    if torch.cuda.is_available():
        print("Launch on CUDA")
        return model.cuda()
    print("Launch on CPU")
    return model.cpu()


def export_model_to_onnx(model, dummy_input, dummy_key_img, output_path):
    # Ensure the model is in evaluation mode
    model.eval()

    # Export the model
    print("Exporting to ONNX")
    torch.onnx.export(
        model,  # Model being run
        (dummy_input, dummy_key_img),
        output_path,
        export_params=True,
        opset_version=11,
        do_constant_folding=True,
        input_names=['input', 'key_img'],
        output_names=['output'],
        dynamic_axes={
            'input': {0: 'batch_size', 2: 'height', 3: 'width'},
            'key_img': {0: 'batch_size', 2: 'height', 3: 'width'},
            'output': {0: 'batch_size'}
        }  # Variable length axes
    )
    print(f"Model exported to {output_path}")


def main():
    model = setup_model()
    weights = "CVPR2022_CVNet_R50.pyth"
    checkp = os.path.join(os.getcwd(), "SuperGlobal", cfg.TEST.WEIGHTS, weights)
    checkpoint.load_checkpoint(checkp, model)

    # Set device
    device = 'cuda' if torch.cuda.is_available() else 'cpu'

    # Create dummy input tensors
    dummy_input = torch.randn(1, 3, 224, 224).to(device)
    dummy_key_img = torch.randn(1, 3, 224, 224).to(device)



    # Path to save the ONNX
    output_path = "checkpoints/CVNet_Rerank.onnx"

    export_model_to_onnx(model, dummy_input, dummy_key_img, output_path)


if __name__ == "__main__":
    main()