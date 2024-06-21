//
// Created by a on 6/21/24.
//

#ifndef CORE_PIPE_HH
#define CORE_PIPE_HH


#include <queue>
#include <mutex>
#include <condition_variable>

template<typename T>
class Pipe {
private:
	bool open;
	size_t capacity;
	std::queue<T> queue;
	std::mutex mutex;
	std::condition_variable consumer_cond;
	std::condition_variable producer_cond;

public:
	explicit Pipe(size_t capacity) :
		capacity(capacity),
		open(true) {

	}

	bool sink(T &item) {
		std::unique_lock<std::mutex> lock(mutex);
		for (; open;) {
			if (queue.empty()) {
				consumer_cond.wait(lock);
				continue;
			}
			item = std::move(queue.front());
			queue.pop();
			return true;
		}
		return false;
	}

	bool flush(T &&item) {
		std::unique_lock<std::mutex> lock(mutex);
		for (; open;) {
			if (queue.size() > capacity) {
				producer_cond.wait(lock);
				continue;
			}
			queue.push(item);
			consumer_cond.notify_one();
			return true;
		}
		return false;
	}

	void close() {
		std::unique_lock<std::mutex> lock(mutex);
		if (open) {
			open = false;
			consumer_cond.notify_all();
			producer_cond.notify_all();
		}
	}

	~Pipe() {
		close();
	}
};

#endif //CORE_PIPE_HH
