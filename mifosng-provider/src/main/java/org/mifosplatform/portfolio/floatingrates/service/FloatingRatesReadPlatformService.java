package org.mifosplatform.portfolio.floatingrates.service;

import java.util.List;

import org.mifosplatform.portfolio.floatingrates.data.FloatingRateData;
import org.mifosplatform.portfolio.floatingrates.data.InterestRatePeriodData;

public interface FloatingRatesReadPlatformService {

    List<FloatingRateData> retrieveAll();

    List<FloatingRateData> retrieveLookupActive();

    FloatingRateData retrieveOne(Long floatingRateId);

    List<FloatingRateData> retrieveAllActive();

    FloatingRateData retrieveBaseLendingRate();

    List<InterestRatePeriodData> retrieveInterestRatePeriods(Long floatingRateId);

}
