package org.mifosplatform.portfolio.floatingrates.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.mifosplatform.infrastructure.core.service.RoutingDataSource;
import org.mifosplatform.portfolio.floatingrates.data.FloatingRateData;
import org.mifosplatform.portfolio.floatingrates.data.FloatingRatePeriodData;
import org.mifosplatform.portfolio.floatingrates.exception.FloatingRateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class FloatingRatesReadPlatformServiceImpl implements
		FloatingRatesReadPlatformService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FloatingRatesReadPlatformServiceImpl(final RoutingDataSource dataSource){
    	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	@Override
	public List<FloatingRateData> retrieveAll() {
		FloatingRateRowMapper rateMapper = new FloatingRateRowMapper(false);
		final String sql = "select " + rateMapper.schema();
		return this.jdbcTemplate.query(sql, rateMapper);
	}

	@Override
	public FloatingRateData retrieveOne(final Long floatingRateId) {
		try{
			FloatingRateRowMapper rateMapper = new FloatingRateRowMapper(true);
			final String sql = "select " + rateMapper.schema() 
					+ " where rate.id = ?";
			return this.jdbcTemplate.queryForObject(sql, rateMapper, new Object[] { floatingRateId});
		} catch (final EmptyResultDataAccessException e){
			throw new FloatingRateNotFoundException(floatingRateId);
		}
	}
	
	
	private final class FloatingRateRowMapper implements RowMapper<FloatingRateData> {
		private final boolean addRatePeriods;
		
		private final StringBuilder sqlQuery = new StringBuilder()
			.append("rate.id as id, ")
			.append("rate.name as name, ")
			.append("rate.is_base_lending_rate as isBaseLendingRate, ")
			.append("rate.is_active as isActive, ")
			.append("crappu.username as createdBy, ")
			.append("rate.created_date as createdOn, ")
			.append("moappu.username as modifiedBy, ")
			.append("rate.lastmodified_date as modifiedOn ")
			.append("FROM m_floating_rates as rate ")
			.append("LEFT JOIN m_appuser as crappu on rate.createdby_id = crappu.id ")
			.append("LEFT JOIN m_appuser as moappu on rate.lastmodifiedby_id = moappu.id ");
		
		public FloatingRateRowMapper(final boolean addRatePeriods) {
			this.addRatePeriods = addRatePeriods;
		}

		@Override
		public FloatingRateData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum)
				throws SQLException {
			final Long id = rs.getLong("id");
			final String name = rs.getString("name");
			final boolean isBaseLendingRate = rs.getBoolean("isBaseLendingRate");
			final boolean isActive = rs.getBoolean("isActive");
			final String createdBy = rs.getString("createdBy");
			final Date createdOn = rs.getDate("createdOn");
			final String modifiedBy = rs.getString("modifiedBy");
			final Date modifiedOn = rs.getDate("modifiedOn");
			List<FloatingRatePeriodData> ratePeriods = null;
			if(addRatePeriods){
				FloatingRatePeriodRowMapper ratePeriodMapper = new FloatingRatePeriodRowMapper();
				final String sql = "select " + ratePeriodMapper.schema()
						+ " where period.floating_rates_id = ? ";
				ratePeriods = jdbcTemplate.query(sql, ratePeriodMapper, new Object[] {id});
			}
			return new FloatingRateData(id, name, isBaseLendingRate, isActive, 
					createdBy, createdOn, modifiedBy, modifiedOn, ratePeriods);
		}
		
		public String schema(){
			return sqlQuery.toString();
		}
	}

	private final class FloatingRatePeriodRowMapper implements RowMapper<FloatingRatePeriodData> {
		
		private final StringBuilder sqlQuery = new StringBuilder()
			.append("period.id as id, ")
			.append("period.from_date as fromDate, ")
			.append("period.interest_rate as interestRate, ")
			.append("period.interest_rate_period_enum as interestRatePeriodEnum, ")
			.append("period.is_differential_to_base_lending_rate as isDifferentialToBaseLendingRate, ")
			.append("period.is_active as isActive, ")
			.append("crappu.username as createdBy, ")
			.append("period.created_date as createdOn, ")
			.append("moappu.username as modifiedBy, ")
			.append("period.lastmodified_date as modifiedOn ")
			.append("FROM m_floating_rates_periods as period ")
			.append("LEFT JOIN m_appuser as crappu on period.createdby_id = crappu.id ")
			.append("LEFT JOIN m_appuser as moappu on period.lastmodifiedby_id = moappu.id ");
		
		@Override
		public FloatingRatePeriodData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum)
				throws SQLException {
			final Long id = rs.getLong("id");
			final Date fromDate = rs.getDate("fromDate");
			final BigDecimal interestRate = rs.getBigDecimal("interestRate");
			final int interestRatePeriodEnum = rs.getInt("interestRatePeriodEnum");
			final boolean isDifferentialToBaseLendingRate = rs.getBoolean("isDifferentialToBaseLendingRate");
			final boolean isActive = rs.getBoolean("isActive");
			final String createdBy = rs.getString("createdBy");
			final Date createdOn = rs.getDate("createdOn");
			final String modifiedBy = rs.getString("modifiedBy");
			final Date modifiedOn = rs.getDate("modifiedOn");
			return new FloatingRatePeriodData(id, fromDate, interestRate, interestRatePeriodEnum, 
					isDifferentialToBaseLendingRate, isActive, createdBy, createdOn, modifiedBy, modifiedOn);
		}
		
		public String schema(){
			return sqlQuery.toString();
		}
	}
}
