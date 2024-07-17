package util

import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.sqrt

class DBSCAN(
	private val eps: Double,
	private val minPts: Int,
) {
	private val UNCLASSIFIED = 0
	private val NOISE = -1

	fun cluster(data: List<ByteArray>): List<Int> {
		val labels = IntArray(data.size) { UNCLASSIFIED }

		var clusterId = 0
		for (pointIndex in data.indices) {
			if (labels[pointIndex] != UNCLASSIFIED) continue

			val neighbors = regionQuery(data, pointIndex)
			if (neighbors.size < minPts) {
				labels[pointIndex] = NOISE
				continue
			}

			clusterId++
			expandCluster(data, labels, pointIndex, neighbors, clusterId)
		}

		return labels.toList()
	}

	private fun expandCluster(
		data: List<ByteArray>,
		labels: IntArray,
		pointIndex: Int,
		neighbors: MutableList<Int>,
		clusterId: Int
	) {
		labels[pointIndex] = clusterId

		var i = 0
		while (i < neighbors.size) {
			val neighborIndex = neighbors[i]
			if (labels[neighborIndex] == NOISE) labels[neighborIndex] = clusterId

			if (labels[neighborIndex] != UNCLASSIFIED) {
				i++
				continue
			}

			labels[neighborIndex] = clusterId

			val newNeighbors = regionQuery(data, neighborIndex)
			if (newNeighbors.size >= minPts) {
				neighbors.addAll(newNeighbors)
			}

			i++
		}
	}

	private fun regionQuery(
		data: List<ByteArray>,
		pointIndex: Int
	): MutableList<Int> {
		val neighbors = mutableListOf<Int>()
		for (i in data.indices) {
			if (i == pointIndex) continue
			if (cosineSimilarity(data[pointIndex], data[i]) >= eps) {
				neighbors.add(i)
			}
		}
		return neighbors
	}


	private fun cosineSimilarity(a: ByteArray, b: ByteArray): Double {
		val aBuf = ByteBuffer.wrap(a).order(ByteOrder.nativeOrder()).asFloatBuffer()
		val bBuf = ByteBuffer.wrap(b).order(ByteOrder.nativeOrder()).asFloatBuffer()

		val aArr = FloatArray(aBuf.limit())
		val bArr = FloatArray(bBuf.limit())

		aBuf[aArr, 0, aBuf.limit()]
		bBuf[bArr, 0, bBuf.limit()]

		val dotProduct = aArr.zip(bArr).sumOf { (it.first * it.second).toDouble() }
		val normA = sqrt(aArr.sumOf { (it * it).toDouble() })
		val normB = sqrt(bArr.sumOf { (it * it).toDouble() })
		return dotProduct / (normA * normB)
	}
}