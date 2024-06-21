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
	if (image.channels() != mean.size() || image.channels() != stddev.size()) {
		throw std::invalid_argument("mean and stddev values should be provided for each channel");
	}

	std::vector<Magick::Image> channels;
	channels.reserve(image.channels());
	Magick::separateImages(&channels, image, MagickCore::ChannelType::AllChannels);

	tensor.clear();
	tensor.reserve(image.channels() * image.rows() * image.columns());

	for (size_t i = 0; i < image.channels(); ++i) {
		float channel_mean = mean[i];
		float channel_stddev = stddev[i];
		const Magick::Image &channel = channels[i];

		for (ssize_t y = 0; y < channel.rows(); ++y) {
			const Magick::Quantum *pixels = channel.getConstPixels(0, y, channel.columns(), 1);
			for (ssize_t x = 0; x < channel.columns(); ++x) {
				const Magick::Quantum pixel = *pixels++;
				// FIXME: QuantumRange instead of 65535.0f
				float value = ((float) pixel / 65535.0f - channel_mean) / channel_stddev;
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
	size_t channels_num = std::min(channels.size(), tensor.size() / (width * height));
	if (channels_num == 0) {
		throw std::invalid_argument("invalid sizes provided");
	}

	MagickCore::StorageType storage_type = Magick::StorageType::FloatPixel;
	if (channels_num > 1) {
		std::vector<Magick::Image> components;
		components.reserve(channels_num);

		for (size_t i = 0; i < channels_num; ++i) {
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
