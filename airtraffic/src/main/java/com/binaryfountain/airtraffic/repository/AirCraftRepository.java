package com.binaryfountain.airtraffic.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.binaryfountain.airtraffic.entity.AirCraft;
import com.binaryfountain.airtraffic.entity.AirCraft.CraftType;

@RepositoryRestResource(collectionResourceRel = "aircraft", path = "aircraft")
public interface AirCraftRepository extends PagingAndSortingRepository<AirCraft, Long> {

	public Page<AirCraft> findAll(Pageable pageable);

	List<AirCraft> findAllByType(CraftType type, Pageable pageable);


	
	public default long getMaxId(EntityManager entityManager) {

        String queryStr = "select max(id) from  aircraft";
        try {
            Query query = entityManager.createNativeQuery(queryStr);
            BigInteger obj = (BigInteger)query.getSingleResult();
            return obj.longValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
