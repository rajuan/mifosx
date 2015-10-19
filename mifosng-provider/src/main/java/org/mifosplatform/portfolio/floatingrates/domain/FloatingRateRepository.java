package org.mifosplatform.portfolio.floatingrates.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface FloatingRateRepository extends JpaRepository<FloatingRate, Long>, JpaSpecificationExecutor<FloatingRate>  {

    @Query("from FloatingRate floatingRate where floatingRate.isBaseLendingRate = 1 and floatingRate.isActive = 1")
    FloatingRate retrieveBaseLendingRate();

}
