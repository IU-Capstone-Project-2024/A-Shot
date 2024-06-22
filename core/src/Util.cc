//
// Created by a on 6/21/24.
//

#include "Util.hh"

void util::MagickToTensor(
	Magick::Image &image,
	const std::vector<float> &mean,
	const std::vector<float> &stddev,
	std::vector<float> &tensor
) {
	size_t c = image.channels();
	size_t w = image.columns();
	size_t h = image.rows();

	if (c != mean.size() || c != stddev.size()) {
		throw std::invalid_argument("mean and stddev values should be provided for each channel");
	}

	std::vector<Magick::Image> channels;
	channels.reserve(c);
	Magick::separateImages(&channels, image, MagickCore::ChannelType::AllChannels);

	tensor.clear();
	tensor.reserve(c * w * h);

	for (size_t i = 0; i < image.channels(); ++i) {
		float channel_mean = mean[i];
		float channel_stddev = stddev[i];
		const Magick::Image &channel = channels[i];

		for (ssize_t y = 0; y < h; ++y) {
			const Magick::Quantum *pixels = channel.getConstPixels(0, y, w, 1);
			for (ssize_t x = 0; x < w; ++x) {
				const Magick::Quantum pixel = *pixels++;
				// FIXME: QuantumRange instead of 65535.0f
				float value = ((float) pixel / 255.0f - channel_mean) / channel_stddev;
				tensor.emplace_back(value);
			}
		}
	}
}

void util::TensorToMagick(
	const std::vector<float> &tensor,
	size_t width,
	size_t height,
	const std::string &channels,
	Magick::Image &image,
	Magick::ColorspaceType colorspace
) {
	size_t c = std::min(channels.size(), tensor.size() / (width * height));
	if (c == 0) {
		throw std::invalid_argument("invalid sizes provided");
	}

	MagickCore::StorageType storage_type = Magick::StorageType::FloatPixel;
	if (c > 1) {
		std::vector<Magick::Image> components;
		components.reserve(c);

		for (size_t i = 0; i < c; ++i) {
			size_t off = i * width * height;
			Magick::Image channel(width, height, std::string(1, channels[i]), storage_type, &tensor[off]);
			components.emplace_back(channel);
		}

		MagickCore::ChannelType channel_type = MagickCore::ChannelType::AllChannels;
		Magick::combineImages(&image, components.begin(), components.end(), channel_type, colorspace);
	}
	else {
		image.read(width, height, std::string(1, channels[0]), storage_type, tensor.data());
	}
}
