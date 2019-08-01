package com.millward.repositories;

import java.util.Date;
import java.util.List;

import com.millward.entities.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Integer> {

    @Query(value = "select p from Price p join fetch p.company where p.transactionTime in (select max(p2.transactionTime) from Price p2 where p2.company.id = p.company.id)")
    List<Price> findLatest();

    @Query(value = "select p from Price p join fetch p.company where p.transactionTime in (select max(p2.transactionTime) from Price p2 where p2.company.id = p.company.id and p2.transactionTime < :midnight)")
    List<Price> findLatestBefore(@Param("midnight") Date midnight);

    Price findTop1ByCompanyIdOrderByTransactionTime(Long companyId);
}
