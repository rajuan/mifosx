package org.mifosplatform.portfolio.floatingrates.service;

import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;

public interface FloatingRateWritePlatformService {

	public CommandProcessingResult createFloatingRate(JsonCommand command);

	public CommandProcessingResult updateFloatingRate(JsonCommand command);
}
