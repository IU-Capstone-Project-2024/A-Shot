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
class Closeable {
public:
	virtual void close() = 0;
};

template<typename T>
class DrainPipe : public virtual Closeable<T> {
public:
	virtual PipeResult flush(T &&item, bool block) = 0;
};

template<typename T>
class SupplyPipe : public virtual Closeable<T> {
public:
	virtual PipeResult sink(T &item, bool block) = 0;
};

template<typename T>
class Pipe :
	public DrainPipe<T>,
	public SupplyPipe<T>,
	public virtual Closeable<T> {
private:
	size_t capacity;
	std::queue<T> queue;
	std::mutex mutex;
	std::condition_variable supply_cond;
	std::condition_variable drain_cond;
	bool supply_open;
	bool drain_open;

	class Drain : DrainPipe<T> {
	private:
		Pipe<T> &pipe;

	public:
		explicit Drain(Pipe<T> &pipe) : pipe(pipe) {}

		void close() override {
			std::unique_lock<std::mutex> lock(pipe.mutex);
			if (pipe.drain_open) {
				pipe.drain_open = false;
				pipe.supply_cond.notify_all();
				pipe.drain_cond.notify_all();
			}
		}

		PipeResult flush(T &&item, bool block) override {
			std::unique_lock<std::mutex> lock(pipe.mutex);

			for (; pipe.drain_open && (pipe.supply_open || !pipe.queue.empty());) {
				if (pipe.queue.size() > pipe.capacity) {
					if (!block) {
						return PipeResult::PIPE_CLOGGED;
					}

					pipe.drain_cond.wait(lock);
					continue;
				}

				pipe.queue.push(std::move(item));
				pipe.supply_cond.notify_one();
				return PipeResult::PIPE_OK;
			}

			pipe.drain_open = false;
			return PipeResult::PIPE_CLOSED;
		}
	};

	class Supply : SupplyPipe<T> {
	private:
		Pipe<T> &pipe;

	public:
		explicit Supply(Pipe<T> &pipe) : pipe(pipe) {}

		void close() override {
			std::unique_lock<std::mutex> lock(pipe.mutex);
			if (pipe.supply_open) {
				pipe.supply_open = false;
				pipe.supply_cond.notify_all();
				pipe.drain_cond.notify_all();
			}
		}

		PipeResult sink(T &item, bool block) override {
			std::unique_lock<std::mutex> lock(pipe.mutex);

			for (; pipe.supply_open && pipe.drain_open;) {
				if (pipe.queue.empty()) {
					if (!block) {
						return PipeResult::PIPE_CLOGGED;
					}

					pipe.supply_cond.wait(lock);
					continue;
				}

				item = std::move(pipe.queue.front());
				pipe.queue.pop();
				pipe.drain_cond.notify_one();
				return PipeResult::PIPE_OK;
			}

			pipe.supply_open = false;
			return PipeResult::PIPE_CLOSED;
		}
	};

	Drain drain;
	Supply supply;

public:
	explicit Pipe(size_t capacity) :
		capacity(capacity),

		drain_open(true),
		supply_open(true),

		drain(*this),
		supply(*this) {
	}

	void close() override {
		supply.close();
		drain.close();
	}

	PipeResult flush(T &&item, bool block) override {
		return drain.flush(std::move(item), block);
	}

	PipeResult sink(T &item, bool block) override {
		return supply.sink(item, block);
	}
};

#endif //CORE_PIPE_HH
