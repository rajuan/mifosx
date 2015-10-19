package org.mifosplatform.portfolio.floatingrates.service;

import java.util.List;

import org.mifosplatform.portfolio.floatingrates.data.FloatingRateData;

public interface FloatingRatesReadPlatformService {

	List<FloatingRateData> retrieveAll();

	FloatingRateData retrieveOne(Long floatingRateId);

}
