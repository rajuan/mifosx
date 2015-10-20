package org.mifosplatform.portfolio.loanproduct.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.mifosplatform.portfolio.floatingrates.domain.FloatingRate;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "m_product_loan_floating_rates")
public class LoanProductFloatingRates  extends AbstractPersistable<Long> {

    @OneToOne
    @JoinColumn(name = "loan_product_id", nullable = false)
	private LoanProduct loanProduct;
	
    @ManyToOne
    @JoinColumn(name = "floating_rates_id", nullable = false)
	private FloatingRate floatingRate;
    
    @Column(name = "interest_rate_differential", nullable = false)
	private BigDecimal interestRateDifferential;
    
    @Column(name = "min_differential_lending_rate", nullable = false)
	private BigDecimal minDifferentialLendingRate;
    
    @Column(name = "default_differential_lending_rate", nullable = false)
	private BigDecimal defaultDifferentialLendingRate;
    
    @Column(name = "max_differential_lending_rate", nullable = false)
	private BigDecimal maxDifferentialLendingRate;
    
    @Column(name = "is_floating_interest_rate_calculation_allowed", nullable = false)
	private boolean isFloatingInterestRateCalculationAllowed;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "createdby_id", nullable = false)
	private AppUser createdBy;
	
    @ManyToOne(optional = true)
    @JoinColumn(name = "lastmodifiedby_id", nullable = false)
	private AppUser modifiedBy;
	
	@Column(name = "created_date", nullable = false)
	private Date createdOn;
	
	@Column(name = "lastmodified_date", nullable = false)
	private Date modifiedOn;

	public LoanProductFloatingRates(){
		
	}
	public LoanProductFloatingRates(FloatingRate floatingRate, BigDecimal interestRateDifferential, 
            BigDecimal minDifferentialLendingRate, BigDecimal maxDifferentialLendingRate, BigDecimal defaultDifferentialLendingRate, 
            Boolean isFloatingInterestRateCalculationAllowed){
		this.floatingRate = floatingRate;
		this.interestRateDifferential = interestRateDifferential;
		this.minDifferentialLendingRate = minDifferentialLendingRate;
		this.maxDifferentialLendingRate = maxDifferentialLendingRate;
		this.defaultDifferentialLendingRate = defaultDifferentialLendingRate;
		this.isFloatingInterestRateCalculationAllowed = isFloatingInterestRateCalculationAllowed;
	}

	public LoanProduct getLoanProduct() {
		return this.loanProduct;
	}

	public FloatingRate getFloatingRate() {
		return this.floatingRate;
	}

	public BigDecimal getInterestRateDifferential() {
		return this.interestRateDifferential;
	}

	public BigDecimal getMinDifferentialLendingRate() {
		return this.minDifferentialLendingRate;
	}

	public BigDecimal getDefaultDifferentialLendingRate() {
		return this.defaultDifferentialLendingRate;
	}

	public BigDecimal getMaxDifferentialLendingRate() {
		return this.maxDifferentialLendingRate;
	}

	public boolean isFloatingInterestRateCalculationAllowed() {
		return this.isFloatingInterestRateCalculationAllowed;
	}

	public AppUser getCreatedBy() {
		return this.createdBy;
	}

	public AppUser getModifiedBy() {
		return this.modifiedBy;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public Date getModifiedOn() {
		return this.modifiedOn;
	}
	
	
}
