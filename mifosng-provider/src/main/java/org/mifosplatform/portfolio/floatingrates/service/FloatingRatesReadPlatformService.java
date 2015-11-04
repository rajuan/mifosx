package org.mifosplatform.portfolio.floatingrates.service;

import java.util.List;

import org.mifosplatform.portfolio.floatingrates.data.InterestRatePeriodData;
import org.mifosplatform.portfolio.floatingrates.data.FloatingRateData;

public interface FloatingRatesReadPlatformService {

	List<FloatingRateData> retrieveAll();

	FloatingRateData retrieveOne(Long floatingRateId);
	
	List<FloatingRateData> retrieveAllActive();
	
	List<FloatingRateData> retrieveLookupActive();

	List<InterestRatePeriodData> retrieveInterestRatePeriods(Long floatingRateId);

}
