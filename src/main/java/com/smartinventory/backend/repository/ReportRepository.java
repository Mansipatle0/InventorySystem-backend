package com.smartinventory.backend.repository;

import com.smartinventory.backend.dto.MonthlySalesRowDto;
import com.smartinventory.backend.dto.ProductWiseHistoryDto;

import com.smartinventory.backend.entity.Sale;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportRepository extends CrudRepository<Sale, Long> {

	@Query("""
		    SELECT new com.smartinventory.backend.dto.MonthlySalesRowDto(
		        MONTH(s.saleDate),

		        COUNT(s.id),

		        SUM(s.itemsSold),

		        SUM(s.grandTotal)
		    )
		    FROM (
		        SELECT 
		            s.id as id,
		            s.saleDate as saleDate,
		            s.grandTotal as grandTotal,
		            (SELECT SUM(si.quantity) 
		             FROM com.smartinventory.backend.entity.SaleItem si 
		             WHERE si.sale.id = s.id) as itemsSold
		        FROM Sale s
		        WHERE YEAR(s.saleDate) = :year
		          AND s.status = 'COMPLETED'
		    ) s
		    GROUP BY MONTH(s.saleDate)
		    ORDER BY MONTH(s.saleDate)
		    """)
		List<MonthlySalesRowDto> getMonthlySales(int year);
	
	@Query("""
		    SELECT MONTH(s.saleDate) AS month,
		           COUNT(s.id) AS bills,
		           SUM(s.grandTotal) AS revenue
		    FROM Sale s
		    WHERE YEAR(s.saleDate) = :year
		      AND s.status = 'COMPLETED'
		    GROUP BY MONTH(s.saleDate)
		    ORDER BY MONTH(s.saleDate)
		""")
		List<Object[]> getYearlySummaryRaw(int year);

		
		@Query(
			    value = """
			        SELECT 
			            p.sku_code AS code,
			            p.name AS name,
			            p.category AS category,
			            SUM(i.quantity) AS qtySold,
			            SUM(i.line_total) AS revenue
			        FROM sale_items i
			        JOIN sales s ON s.id = i.sale_id
			        JOIN products p ON p.id = i.product_id
			        WHERE s.status = 'COMPLETED'
			          AND s.sale_date >= DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY)
			        GROUP BY p.id, p.sku_code, p.name, p.category
			        ORDER BY qtySold DESC
			    """,
			    nativeQuery = true
			)
			List<Object[]> getFastMovers();

		@Query(
			    value = """
			        SELECT 
			            p.sku_code AS code,
			            p.name AS name,
			            p.category AS category,
			            SUM(i.quantity) AS qtySold,
			            SUM(i.line_total) AS revenue
			        FROM sale_items i
			        JOIN sales s ON s.id = i.sale_id
			        JOIN products p ON p.id = i.product_id
			        WHERE s.status = 'COMPLETED'
			          AND s.sale_date >= DATE_SUB(CURRENT_DATE(), INTERVAL 30 DAY)
			        GROUP BY p.id, p.sku_code, p.name, p.category
			        ORDER BY qtySold ASC
			    """,
			    nativeQuery = true
			)
			List<Object[]> getSlowMovers();

			@Query("""
				    SELECT new com.smartinventory.backend.dto.ProductWiseHistoryDto(
				        p.skuCode,
				        p.name,
				        p.category,
				        SUM(si.quantity),
				        SUM(si.lineTotal),
				        MAX(s.createdAt)
				    )
				    FROM SaleItem si
				    JOIN si.product p
				    JOIN si.sale s
				    WHERE s.status = 'COMPLETED'
				    GROUP BY p.skuCode, p.name, p.category
				    ORDER BY p.name
				""")
				List<ProductWiseHistoryDto> getProductWiseHistory();




}
