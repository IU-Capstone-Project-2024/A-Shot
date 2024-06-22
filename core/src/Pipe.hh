//
// Created by a on 6/21/24.
//

#ifndef CORE_PIPE_HH
#define CORE_PIPE_HH


#include <queue>
#include <mutex>
#include <condition_variable>

enum PipeResult {
	PIPE_OK = 0,
	PIPE_CLOSED = 1,
	PIPE_CLOGGED = 2,
};

template<typename T>
class Drain {
public:
	virtual PipeResult flush(T &&item, bool block) = 0;

	virtual void dry() = 0;
};

template<typename T>
class Exhaust {
public:
	virtual PipeResult suck(T &item, bool block) = 0;

	virtual void plug() = 0;
};

template<typename T>
class Pipe :
	public Drain<T>,
	public Exhaust<T> {
private:
	size_t capacity;
	std::queue<T> queue;

	std::mutex mutex;
	std::condition_variable head_cond;
	std::condition_variable tail_cond;

	bool head_open;
	bool tail_open;

	class Head : Drain<T> {
	private:
		Pipe<T> &pipe;

	public:
		explicit Head(Pipe<T> &pipe) : pipe(pipe) {}

		void dry() override {
			std::unique_lock<std::mutex> lock(pipe.mutex);
			if (pipe.head_open) {
				pipe.head_open = false;
				pipe.head_cond.notify_all();
				pipe.tail_cond.notify_all();
			}
		}

		PipeResult flush(T &&item, bool block) override {
			std::unique_lock<std::mutex> lock(pipe.mutex);

			for (; pipe.head_open && pipe.tail_open;) {
				if (pipe.queue.size() >= pipe.capacity) {
					if (!block) {
						return PipeResult::PIPE_CLOGGED;
					}

					pipe.head_cond.wait(lock);
					continue;
				}

				pipe.queue.push(std::move(item));
				pipe.tail_cond.notify_one();
				return PipeResult::PIPE_OK;
			}

			pipe.head_open = false;
			return PipeResult::PIPE_CLOSED;
		}
	};

	class Tail : Exhaust<T> {
	private:
		Pipe<T> &pipe;

	public:
		explicit Tail(Pipe<T> &pipe) : pipe(pipe) {}

		void plug() override {
			std::unique_lock<std::mutex> lock(pipe.mutex);
			if (pipe.tail_open) {
				pipe.tail_open = false;
				pipe.tail_cond.notify_all();
				pipe.head_cond.notify_all();
			}
		}

		PipeResult suck(T &item, bool block) override {
			std::unique_lock<std::mutex> lock(pipe.mutex);

			for (; pipe.tail_open && (pipe.head_open || !pipe.queue.empty());) {
				if (pipe.queue.empty()) {
					if (!block) {
						return PipeResult::PIPE_CLOGGED;
					}

					pipe.tail_cond.wait(lock);
					continue;
				}

				item = std::move(pipe.queue.front());
				pipe.queue.pop();
				pipe.head_cond.notify_one();
				return PipeResult::PIPE_OK;
			}

			pipe.tail_open = false;
			return PipeResult::PIPE_CLOSED;
		}
	};

	Head head;
	Tail tail;

public:
	explicit Pipe(size_t capacity) :
		capacity(capacity),

		tail_open(true),
		head_open(true),

		head(*this),
		tail(*this) {
	}

	PipeResult flush(T &&item, bool block) override {
		return head.flush(std::move(item), block);
	}

	PipeResult suck(T &item, bool block) override {
		return tail.suck(item, block);
	}

	void dry() override {
		head.dry();
	}

	void plug() override {
		tail.plug();
	}
};

#endif //CORE_PIPE_HH
