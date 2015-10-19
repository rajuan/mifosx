package org.mifosplatform.portfolio.floatingrates.serialization;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;
import org.mifosplatform.infrastructure.core.data.ApiParameterError;
import org.mifosplatform.infrastructure.core.data.DataValidatorBuilder;
import org.mifosplatform.infrastructure.core.exception.PlatformApiDataValidationException;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.core.service.DateUtils;
import org.mifosplatform.portfolio.floatingrates.domain.FloatingRate;
import org.mifosplatform.portfolio.floatingrates.domain.FloatingRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

@Component
public class FloatingRateDataValidator {
	
	private final Set<String> supportedParametersForFloatingRates = new HashSet<>(Arrays.asList("name",
			"isBaseLendingRate", "isActive", "ratePeriods"));
	private final Set<String> supportedParametersForFloatingRatePeriods = new HashSet<>(Arrays.asList("fromDate", 
			"interestRate", "interestRatePeriodEnum", "isDifferentialToBaseLendingRate","locale", "dateFormat"));
	
	private final FromJsonHelper fromApiJsonHelper;
	private final FloatingRateRepository floatingRateRepository;

	@Autowired
	public FloatingRateDataValidator(final FromJsonHelper fromApiJsonHelper,
			final FloatingRateRepository floatingRateRepository) {
        this.fromApiJsonHelper = fromApiJsonHelper;
        this.floatingRateRepository = floatingRateRepository;
    }

	public void validateForCreate(String json) {
		final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, this.supportedParametersForFloatingRates);
        
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("floatingrate");

        final JsonElement element = this.fromApiJsonHelper.parse(json);
        
        final String name = this.fromApiJsonHelper.extractStringNamed("name", element);
        baseDataValidator.reset().parameter("name").value(name).notBlank().notExceedingLengthOf(200);
        
        if(this.fromApiJsonHelper.parameterExists("isBaseLendingRate", element)){
        	final Boolean isBaseLendingRate = this.fromApiJsonHelper.extractBooleanNamed("isBaseLendingRate", element);
        	
        	baseDataValidator.reset().parameter("isBaseLendingRate").value(isBaseLendingRate).notNull();
        	if(isBaseLendingRate == null){
        		baseDataValidator.reset().parameter("isBaseLendingRate").trueOrFalseRequired(false);
        	}else if(isBaseLendingRate){
        		FloatingRate baseLendingRate = this.floatingRateRepository.retrieveBaseLendingRate();
        		if(baseLendingRate != null){
        			baseDataValidator.reset().parameter("isBaseLendingRate").value(isBaseLendingRate)
        				.failWithCode("baselendingrate.duplicate", "Base Lending Rate already exists");
        		}
        	}
        }
        
        if(this.fromApiJsonHelper.parameterExists("isActive", element)){
        	final Boolean isActive = this.fromApiJsonHelper.extractBooleanNamed("isActive", element);
        	if(isActive == null){
            	baseDataValidator.reset().parameter("isActive").trueOrFalseRequired(false);
        	}
        }
        
        validateRatePeriods(baseDataValidator, element);
        
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}
	
	private void validateRatePeriods(DataValidatorBuilder baseDataValidator, JsonElement element) {
        if(this.fromApiJsonHelper.parameterExists("ratePeriods", element)){
        	final JsonArray ratePeriods = this.fromApiJsonHelper.extractJsonArrayNamed("ratePeriods", element);
        	baseDataValidator.reset().parameter("ratePeriods").value(ratePeriods).notBlank().jsonArrayNotEmpty();
        	
        	if(ratePeriods != null){
        		List<LocalDate> fromDates = new ArrayList<>();
        		for(int i=0; i < ratePeriods.size(); i++){
        			final JsonElement ratePeriod = ratePeriods.get(i);
        			
        			this.fromApiJsonHelper.checkForUnsupportedParameters(ratePeriod.getAsJsonObject(), this.supportedParametersForFloatingRatePeriods);

        			final LocalDate fromDate = this.fromApiJsonHelper.extractLocalDateNamed("fromDate", ratePeriod);
        	        baseDataValidator.reset().parameter("fromDate").value(fromDate).notBlank().validateDateAfter(DateUtils.getLocalDateOfTenant().plusDays(1));
        	        if(fromDate != null){
        	        	fromDates.add(fromDate);
        	        }
        	        
    	            final BigDecimal interestRatePerPeriod = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("interestRate", ratePeriod);
    	            baseDataValidator.reset().parameter("interestRate").value(interestRatePerPeriod).notNull().zeroOrPositiveAmount();

    	            final Integer interestRateFrequencyType = this.fromApiJsonHelper.extractIntegerNamed("interestRatePeriodEnum", ratePeriod,
    	                    Locale.getDefault());
    	            baseDataValidator.reset().parameter("interestRatePeriodEnum").value(interestRateFrequencyType).notNull().inMinMaxRange(0, 3);
    	            
    	            if(this.fromApiJsonHelper.parameterExists("isDifferentialToBaseLendingRate", ratePeriod)){
    	            	final Boolean isDifferentialToBaseLendingRate = this.fromApiJsonHelper.extractBooleanNamed("isDifferentialToBaseLendingRate", ratePeriod);
    	            	if(isDifferentialToBaseLendingRate == null){
        	            	baseDataValidator.reset().parameter("isDifferentialToBaseLendingRate").trueOrFalseRequired(false);
    	            	} else if(isDifferentialToBaseLendingRate){
    	            		FloatingRate baseLendingRate = this.floatingRateRepository.retrieveBaseLendingRate();
    	            		if(baseLendingRate != null){
    	            			baseDataValidator.reset().parameter("isDifferentialToBaseLendingRate").value(isDifferentialToBaseLendingRate)
    	            				.failWithCode("isDifferentialToBaseLendingRate.no.baselending.rate.defined", "Base Lending Rate doesn't exists");
    	            		}

    	            	}
    	            }
        		}
        		Set<LocalDate> uniqueFromDates = new HashSet<>(fromDates);
        		if(fromDates.size() != uniqueFromDates.size()){
        			baseDataValidator.reset().parameter("fromDate").failWithCode("multiple.same.date", 
        					"More than one entry in ratePeriods have same fromDate.");
        		}
        		
        	}
        }
	}

	public void validateForUpdate(String json) {
		final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, this.supportedParametersForFloatingRates);
        
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("floatingrate");

        final JsonElement element = this.fromApiJsonHelper.parse(json);
        
        if(this.fromApiJsonHelper.parameterExists("name", element)){
            final String name = this.fromApiJsonHelper.extractStringNamed("name", element);
            baseDataValidator.reset().parameter("name").value(name).notBlank().notExceedingLengthOf(200);
        }
        
        if(this.fromApiJsonHelper.parameterExists("isBaseLendingRate", element)){
        	final Boolean isBaseLendingRate = this.fromApiJsonHelper.extractBooleanNamed("isBaseLendingRate", element);
        	
        	baseDataValidator.reset().parameter("isBaseLendingRate").value(isBaseLendingRate).notNull();
        	if(isBaseLendingRate == null){
        		baseDataValidator.reset().parameter("isBaseLendingRate").trueOrFalseRequired(false);
        	}else if(isBaseLendingRate){
        		FloatingRate baseLendingRate = this.floatingRateRepository.retrieveBaseLendingRate();
        		if(baseLendingRate != null){
        			baseDataValidator.reset().parameter("isBaseLendingRate").value(isBaseLendingRate)
        				.failWithCode("baselendingrate.duplicate", "Base Lending Rate already exists");
        		}
        	}
        }
        
        if(this.fromApiJsonHelper.parameterExists("isActive", element)){
        	final Boolean isActive = this.fromApiJsonHelper.extractBooleanNamed("isActive", element);
        	if(isActive == null){
            	baseDataValidator.reset().parameter("isActive").trueOrFalseRequired(false);
        	}
        }
        
        validateRatePeriods(baseDataValidator, element);
        
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
	}

	private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) { throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist",
                "Validation errors exist.", dataValidationErrors); }
    }

}
