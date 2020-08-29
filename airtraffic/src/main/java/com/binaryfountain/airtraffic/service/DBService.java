package com.binaryfountain.airtraffic.service;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.binaryfountain.airtraffic.entity.AirCraft;
import com.binaryfountain.airtraffic.repository.AirCraftRepository;

/*
 * This class responsible for temporally  keep the enqueued 
 */
@Service
@Transactional
public class DBService {
	
	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private AirCraftRepository airCraftRepository;

	private Queue<AirCraft> insertQueue = new LinkedBlockingQueue<>();
	private Queue<AirCraft> removeQueue = new LinkedBlockingQueue<>();

	public DBService() {

		InsertCraftScheduledService ics = new InsertCraftScheduledService(insertQueue);
		Thread insertTH = new Thread(() -> {
			try {
				Thread.sleep(2 * 1000);// delay the thread;
			} catch (InterruptedException ie) {
				//
			}
			ics.flushPeriodic();
		});
		insertTH.start();

		RemoveCraftScheduledService rcs = new RemoveCraftScheduledService(removeQueue);
		Thread removeTH = new Thread(() -> {
			try {
				Thread.sleep(2 * 1000);// delay the thread;
			} catch (InterruptedException ie) {
				//
			}
			rcs.flushPeriodic();
		});
		removeTH.start();
	}

	public void insert(AirCraft airCraft) {
		insertQueue.add(airCraft);
	}

	public void remove(AirCraft airCraft) {
		removeQueue.add(airCraft);
	}

	@Transactional
	private class InsertCraftScheduledService {
		Queue<AirCraft> insertQueue;

		public InsertCraftScheduledService(Queue<AirCraft> insertQueue) {
			this.insertQueue = insertQueue;

		}

		private long getSleepInterval() {
			return 1000;
		}

		public void flushPeriodic() {
			while (true) {
				synchronized (this) {
					try {
						wait(getSleepInterval());
					} catch (InterruptedException e) {
						// must have been woken up early!
					}
				}

				Queue<AirCraft> queue = null;
				if (!insertQueue.isEmpty()) {
					queue = new LinkedList<>(insertQueue);
					insertQueue.clear();
				}

				if (queue != null && !queue.isEmpty()) {
					airCraftRepository.saveAll(queue);
					// AirCraft craft = queue.poll();
					// craft = airCraftRepository.save(craft);
				}
			}
		}
	}

	@Transactional
	private class RemoveCraftScheduledService {
		Queue<AirCraft> removeQueue;

		public RemoveCraftScheduledService(Queue<AirCraft> removeQueue) {
			this.removeQueue = removeQueue;

		}

		private long getSleepInterval() {
			return 1000;
		}

		public void flushPeriodic() {
			while (true) {
				synchronized (this) {
					try {
						wait(getSleepInterval());
					} catch (InterruptedException e) {
						// must have been woken up early!
					}
				}

				Queue<AirCraft> queue = null;
				if (!removeQueue.isEmpty()) {
					queue = new LinkedList<>(removeQueue);
					removeQueue.clear();
				}

				if (queue != null && !queue.isEmpty()) {
					airCraftRepository.deleteAll(queue);
					// AirCraft craft = queue.poll();
					// craft = airCraftRepository.save(craft);
				}
			}
		}
	}
}
