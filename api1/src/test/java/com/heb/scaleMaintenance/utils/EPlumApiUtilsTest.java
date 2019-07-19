package com.heb.scaleMaintenance.utils;

import com.heb.scaleMaintenance.entity.*;
import com.heb.scaleMaintenance.model.ScaleMaintenanceLoadParameters;
import com.heb.scaleMaintenance.model.ScaleMaintenanceNutrient;
import com.heb.scaleMaintenance.model.ScaleMaintenanceNutrientStatement;
import com.heb.scaleMaintenance.model.ScaleMaintenanceProduct;
import com.heb.scaleMaintenance.service.ScaleMaintenanceAuthorizeRetailService;
import org.mockito.Mockito;
import testSupport.CommonMocks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for EPlumAPIUtils class.
 *
 * @author m314029
 * @since 2.17.8
 */
public class EPlumApiUtilsTest {

	private EPlumApiUtils ePlumApiUtils;

	private Map<Long, ScaleMaintenanceUpc> getCurrentUpcMap() {
		return new HashMap<>();
	}

	private ScaleMaintenanceAuthorizeRetailService getScaleMaintenanceAuthorizationService() {
		ScaleMaintenanceAuthorizeRetailService service = Mockito.mock(ScaleMaintenanceAuthorizeRetailService.class);
		Mockito.when(service.getAuthorizedByTransactionIdAndStoreAndNoMessage(Mockito.anyLong(), Mockito.anyInt()))
				.thenReturn(this.getScaleMaintenanceAuthorizationRetails());
		return service;
	}

	private List<ScaleMaintenanceAuthorizeRetail> getScaleMaintenanceAuthorizationRetails() {
		List<ScaleMaintenanceAuthorizeRetail> toReturn = new ArrayList<>();
		ScaleMaintenanceAuthorizeRetailKey key = new ScaleMaintenanceAuthorizeRetailKey()
				.setStore(838)
				.setTransactionId(1L)
				.setUpc(4011L);
		ScaleMaintenanceAuthorizeRetail currentAuthorizeRetail = new ScaleMaintenanceAuthorizeRetail()
				.setKey(key)
				.setAuthorized(true)
				.setRetail(1.49)
				.setByCountQuantity(2)
				.setWeighed(true);
		toReturn.add(currentAuthorizeRetail);
		key = new ScaleMaintenanceAuthorizeRetailKey()
				.setStore(838)
				.setTransactionId(1L)
				.setUpc(4012L);
		currentAuthorizeRetail = new ScaleMaintenanceAuthorizeRetail()
				.setKey(key)
				.setAuthorized(true)
				.setRetail(1.49)
				.setByCountQuantity(2)
				.setWeighed(false);
		toReturn.add(currentAuthorizeRetail);
		return toReturn;
	}

	private List<ScaleMaintenanceUpc> getCurrentUpcs() {
		List<ScaleMaintenanceUpc> toReturn = new ArrayList<>();
		ScaleMaintenanceUpcKey key = new ScaleMaintenanceUpcKey()
				.setTransactionId(1L)
				.setUpc(4011L);
		ScaleMaintenanceUpc scaleMaintenanceUpc = new ScaleMaintenanceUpc()
				.setKey(key)
				.setJsonVersion("1.0.0")
				.setScaleProductAsJson(this.getScaleProduct(key.getUpc()));
		toReturn.add(scaleMaintenanceUpc);
		key = new ScaleMaintenanceUpcKey()
				.setTransactionId(1L)
				.setUpc(4012L);
		scaleMaintenanceUpc = new ScaleMaintenanceUpc()
				.setKey(key)
				.setJsonVersion("1.0.0")
				.setScaleProductAsJson(this.getScaleProduct(key.getUpc()));
		toReturn.add(scaleMaintenanceUpc);
		return toReturn;

	}

	private ScaleMaintenanceProduct getScaleProduct(Long upc) {
		return new ScaleMaintenanceProduct()
				.setUpc(upc)
				.setPlu(upc)
				.setNutrientStatementCode(upc)
				.setNutrientStatement(this.getNutrientStatement())
				.setIngredientStatement(248L)
				.setIngredientText("white flour(corn starch, sugar, yeast)")
				.setDepartment("09")
				.setSubDepartment("F")
				.setActionCode(1L)
				.setVitaminA(this.getScaleMaintenanceNutrient())
				.setVitaminC(this.getScaleMaintenanceNutrient())
				.setCalcium(this.getScaleMaintenanceNutrient())
				.setIron(this.getScaleMaintenanceNutrient())
				.setCalories(this.getScaleMaintenanceNutrient())
				.setCaloriesFromFat(this.getScaleMaintenanceNutrient())
				.setTotalFat(this.getScaleMaintenanceNutrient())
				.setSaturatedFat(this.getScaleMaintenanceNutrient())
				.setCholesterol(this.getScaleMaintenanceNutrient())
				.setTotalCarbohydrates(this.getScaleMaintenanceNutrient())
				.setDietaryFiber(this.getScaleMaintenanceNutrient())
				.setSodium(this.getScaleMaintenanceNutrient())
				.setSugar(this.getScaleMaintenanceNutrient())
				.setProtein(this.getScaleMaintenanceNutrient())
				.setPotassium(this.getScaleMaintenanceNutrient())
				.setTransFat(this.getScaleMaintenanceNutrient())
				.setSugarAlcohol(this.getScaleMaintenanceNutrient());
	}

	private ScaleMaintenanceNutrient getScaleMaintenanceNutrient() {
		return new ScaleMaintenanceNutrient()
				.setPercent(15L)
				.setValue(12.0);
	}

	private ScaleMaintenanceNutrientStatement getNutrientStatement() {
		return new ScaleMaintenanceNutrientStatement()
				.setMeasureQuantity(1.1)
				.setMetricQuantity(15)
				.setServingsPerContainer(12)
				.setUomCommonCode("EACH")
				.setUomMetricCode("g");
	}

	private Integer getTestStore() {
		return 838;
	}

	private Long getEPlumBatchNumber() {
		return 1L;
	}

	private ScaleMaintenanceTracking getScaleMaintenanceTracking() {
		return new ScaleMaintenanceTracking()
				.setCreateTime(LocalDateTime.now())
				.setJsonVersion("1.0.0")
				.setLoadParametersAsJson(this.getLoadParameters())
				.setScaleTransactionTypeCode(ScaleTransactionType.Code.LOAD.getId())
				.setStatusCode(Status.Code.IN_PROGRESS.getId())
				.setUserId(CommonMocks.getUserInfo().getUserId())
				.setTransactionId(1L);
	}

	private ScaleMaintenanceLoadParameters getLoadParameters() {
		return new ScaleMaintenanceLoadParameters()
				.setStores("838")
				.setUpcs("4011,4012");
	}
}
