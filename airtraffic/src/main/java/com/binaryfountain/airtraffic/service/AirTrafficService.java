package com.binaryfountain.airtraffic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.binaryfountain.airtraffic.entity.AirCraft;
import com.binaryfountain.airtraffic.entity.AirCraft.CraftSize;
import com.binaryfountain.airtraffic.repository.AirCraftRepository;

@Service
@Transactional
public class AirTrafficService {

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private DBService dbService;
	private AtomicLong maxId = new AtomicLong();
	final static public int MAX_QUEUE_SIZE = 10000;

	@Autowired
	private RedisTemplate<Long, AirCraft> redisTemplate;

	// private static Logger logger =
	// LoggerFactory.getLogger(AirTrafficService.class);
	Semaphore read = new Semaphore(1);
	@Autowired
	private AirCraftRepository airCraftRepository;

	private Queue<Long> queue = new PriorityBlockingQueue<>();

	private long getKeyForQueue(AirCraft craft) {
		long mask = 0;
		// Emergency > VIP>Passenger >Cargo ;
		// based on the above priority, the small mask will be polled first
		switch (craft.getType()) {
		case Emergency:
			if (craft.getSize() == CraftSize.LARGE) {
				mask = 10000000000l;
			} else {
				mask = 20000000000l;
			}
			break;
		case VIP:
			if (craft.getSize() == CraftSize.LARGE) {
				mask = 30000000000l;
			} else {
				mask = 40000000000l;
			}
			break;
		case Passenger:
			if (craft.getSize() == CraftSize.LARGE) {
				mask = 50000000000l;
			} else {
				mask = 60000000000l;
			}
			break;
		case Cargo:
			if (craft.getSize() == CraftSize.LARGE) {
				mask = 70000000000l;
			} else {
				mask = 80000000000l;
			}
			break;
		}

		return mask + craft.getId();
	}

	public void initialize() {
		Iterable<AirCraft> crafts = airCraftRepository.findAll();
		for (AirCraft craft : crafts) {
			long key = getKeyForQueue(craft);
			queue.offer(key);
			redisTemplate.opsForValue().set(key, craft);
		}

		maxId.set(airCraftRepository.getMaxId(entityManager));
	}

	@Transactional
	public AirCraft enqueue(AirCraft craft) {
		if (craft.getType() == null) {
			throw new IllegalArgumentException(
					"Aircraft type value cannot be NULL or Empty. It must be one of:Emergency, VIP, Passenger, Cargo");
		}
		if (craft.getSize() == null) {
			throw new IllegalArgumentException(
					"Aircraft size value cannot be NULL or Empty. It must be one of: SMALL or LARGE");
		}
		// AirCraft craft = airCraftRepository.save(airCraft);
		craft.setId(maxId.incrementAndGet());
		// craft = airCraftRepository.save(craft);
		long key = getKeyForQueue(craft);

		queue.offer(key);
		redisTemplate.opsForValue().set(key, craft);

		dbService.insert(craft);
		return craft;
	}

	@Transactional
	public AirCraft dequeue() {
		if (queue.isEmpty()) {
			return null;
		}
		Long key = queue.poll();

		AirCraft craft = redisTemplate.opsForValue().get(key);
		redisTemplate.delete(key);
		if (craft != null) {
			dbService.remove(craft);
		}
		return craft;
	}

	@GetMapping(path = "/list")
	public @ResponseBody Iterable<AirCraft> findAll() {
		Queue<Long> temp = new PriorityQueue<>(queue);

		List<AirCraft> list = new ArrayList<>();
		while (!temp.isEmpty()) {
			list.add(redisTemplate.opsForValue().get(temp.poll()));
			if(list.size() > 100) {
				break;
			}
		}
		return list;
	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
		return new JedisConnectionFactory(config);
	}

	@Bean
	public RedisTemplate<Long, AirCraft> redisTemplate() {
		RedisTemplate<Long, AirCraft> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory());
		return template;
	}
}
