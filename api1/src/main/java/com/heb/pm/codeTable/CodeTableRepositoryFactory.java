package com.heb.pm.codeTable;

import com.heb.pm.codeTable.factory.CodeTableFrontEnd;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Constructs repositories to use for code table CRUD operations.
 *
 * @author m314029
 * @since 2.21.0
 */
@Service
public class CodeTableRepositoryFactory implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(CodeTableRepositoryFactory.class);

	// logs
	private static final String CODE_TABLE_FOUND_MESSAGE = "Code table repository: %s will be used for code table: %s.";

	// errors
	private static final String UNKNOWN_CODE_TABLE_ERROR = "%s is an unknown code table. Please contact " +
			"production support.";

	// code table constants
	private static final String PACKAGING_TYPE_TABLE = "PKG_TYP";
	private static final String CODE_DATE_TYPE_TABLE = "CODE_DATED";
	private static final String PACK_CONFIGURATION_TABLE = "PK_CFG";
	private static final String STATE_TABLE = "STATE";
	private static final String MASTER_PACK_MATERIAL_TABLE = "MST_PK_MATRL";

	private static final String A100873_CLOTHING_SZ = "A100873_CLOTHING_SZ";
	private static final String A102101_FSV_IMPRTER_OF_FD = "A102101_FSV_IMPRTER_OF_FD";
	private static final String A101204_GLOVE_SZ = "A101204_GLOVE_SZ";
	private static final String A101919_LID_SZ = "A101919_LID_SZ";
	private static final String A102148_PIZZA_PORTN = "A102148_PIZZA_PORTN";
	private static final String A102248_PRIM_MDSE_LOC = "A102248_PRIM_MDSE_LOC";
	private static final String A101660_SELL_UNT_PKG_MATRL = "A101660_SELL_UNT_PKG_MATRL";
	private static final String A102102_RGLTR_OVSIGHT = "A102102_RGLTR_OVSIGHT";
	private static final String A101601_SELL_UNT_PKG_TYP = "A101601_SELL_UNT_PKG_TYP";
	private static final String A102349_SHEET_PAN_SZ = "A102349_SHEET_PAN_SZ";
	private static final String A102375_SHEET_SZ = "A102375_SHEET_SZ";
	private static final String A100698_TRNG_PANTS_SZ = "A100698_TRNG_PANTS_SZ";


	private Map<String, JpaRepository> codeTableRepositories;

	@Autowired
	private PackagingTypeRepository packagingTypeRepository;

	@Autowired
	private PackConfigurationRepository packConfigurationRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private CodeDateTypeRepository codeDateTypeRepository;

	@Autowired
	private MasterPackMaterialRepository masterPackMaterialRepository;

	@Autowired
	private ClothingSizeRepository clothingSizeRepository;

	@Autowired
	private FsvImporterOfFoodRepository fsvImporterOfFoodRepository;

	@Autowired
	private GloveSizeRepository gloveSizeRepository;

	@Autowired
	private LidSizeRepository lidSizeRepository;

	@Autowired
	private PizzaPortionRepository pizzaPortionRepository;

	@Autowired
	private PrimaryMerchandisingLocationRepository primaryMerchandisingLocationRepository;

	@Autowired
	private SellingUnitPackageTypeRepository sellingUnitPackageTypeRepository;

	@Autowired
	private RegulatoryOversightRepository regulatoryOversightRepository;

	@Autowired
	private SellingUnitPackageMaterialRepository sellingUnitPackageMaterialRepository;

	@Autowired
	private SheetPanSizeRepository sheetPanSizeRepository;

	@Autowired
	private TrainingPantsSizeRepository trainingPantsSizeRepository;

	@Autowired
	private SheetSizeRepository sheetSizeRepository;
	/**
	 * Getter for a repository linked to the given table name.
	 *
	 * @param tableName Table name to get the repository for.
	 * @return Repository to be used for CRUD operations.
	 */
	public <T extends CodeTable> JpaRepository<T, String> getRepository(String tableName){
		JpaRepository<T, String> toReturn = codeTableRepositories.get(tableName);

		if(toReturn != null) {
			logger.info(String.format(CODE_TABLE_FOUND_MESSAGE, toReturn.getClass().getInterfaces()[0], tableName));
			return toReturn;
		} else {
			logger.error(String.format(UNKNOWN_CODE_TABLE_ERROR, tableName));
			throw new IllegalArgumentException(
					String.format(UNKNOWN_CODE_TABLE_ERROR, tableName));
		}
	}

	/**
	 * After class is built, builds map of table name => repository to use.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.codeTableRepositories = new HashMap<>();
		codeTableRepositories.put(PACKAGING_TYPE_TABLE, this.packagingTypeRepository);
		codeTableRepositories.put(CODE_DATE_TYPE_TABLE, this.codeDateTypeRepository);
		codeTableRepositories.put(PACK_CONFIGURATION_TABLE, this.packConfigurationRepository);
		codeTableRepositories.put(STATE_TABLE, this.stateRepository);
        codeTableRepositories.put(MASTER_PACK_MATERIAL_TABLE, this.masterPackMaterialRepository);
		codeTableRepositories.put(A100873_CLOTHING_SZ, this.clothingSizeRepository);
		codeTableRepositories.put(A102101_FSV_IMPRTER_OF_FD, this.fsvImporterOfFoodRepository);
		codeTableRepositories.put(A101204_GLOVE_SZ, this.gloveSizeRepository);
		codeTableRepositories.put(A101919_LID_SZ, this.lidSizeRepository);
		codeTableRepositories.put(A102148_PIZZA_PORTN, this.pizzaPortionRepository);
		codeTableRepositories.put(A102248_PRIM_MDSE_LOC, this.primaryMerchandisingLocationRepository);
		codeTableRepositories.put(A101660_SELL_UNT_PKG_MATRL, this.sellingUnitPackageMaterialRepository);
		codeTableRepositories.put(A102102_RGLTR_OVSIGHT, this.regulatoryOversightRepository);
		codeTableRepositories.put(A101601_SELL_UNT_PKG_TYP, this.sellingUnitPackageTypeRepository);
		codeTableRepositories.put(A102349_SHEET_PAN_SZ, this.sheetPanSizeRepository);
		codeTableRepositories.put(A102375_SHEET_SZ, this.sheetSizeRepository);
		codeTableRepositories.put(A100698_TRNG_PANTS_SZ, this.trainingPantsSizeRepository);
	}

	/**
	 * Converts a list of code table front end objects to a list of the respective entities (based off table name).
	 *
	 * @param tableName the tableName.
	 * @param entities the entities.
	 * @return returns a list of the respective code table entities based off the table name.
	 */
	public List<? extends CodeTable> toEntities(String tableName, List<CodeTableFrontEnd> entities) {
		switch (tableName) {
			case PACKAGING_TYPE_TABLE: {
				return entities.stream().map(PackagingType::new).collect(Collectors.toList());
			}
			case CODE_DATE_TYPE_TABLE: {
				return entities.stream().map(CodeDateType::new).collect(Collectors.toList());
			}
			case PACK_CONFIGURATION_TABLE: {
				return entities.stream().map(PackConfiguration::new).collect(Collectors.toList());
			}
			case STATE_TABLE: {
				return entities.stream().map(State::new).collect(Collectors.toList());
			}
			case MASTER_PACK_MATERIAL_TABLE: {
				return entities.stream().map(MasterPackMaterial::new).collect(Collectors.toList());
			}
			case A100873_CLOTHING_SZ: {
				return entities.stream().map(ClothingSize::new).collect(Collectors.toList());
			}
			case A102101_FSV_IMPRTER_OF_FD: {
				return entities.stream().map(FsvImporterOfFood::new).collect(Collectors.toList());
			}
			case A101204_GLOVE_SZ: {
				return entities.stream().map(GloveSize::new).collect(Collectors.toList());
			}
			case A101919_LID_SZ: {
				return entities.stream().map(LidSize::new).collect(Collectors.toList());
			}
			case A102148_PIZZA_PORTN: {
				return entities.stream().map(PizzaPortion::new).collect(Collectors.toList());
			}
			case A102248_PRIM_MDSE_LOC: {
				return entities.stream().map(PrimaryMerchandisingLocation::new).collect(Collectors.toList());
			}
			case A101660_SELL_UNT_PKG_MATRL: {
				return entities.stream().map(SellingUnitPackageMaterial::new).collect(Collectors.toList());
			}
			case A102102_RGLTR_OVSIGHT: {
				return entities.stream().map(RegulatoryOversight::new).collect(Collectors.toList());
			}
			case A101601_SELL_UNT_PKG_TYP: {
				return entities.stream().map(SellingUnitPackageType::new).collect(Collectors.toList());
			}
			case A102349_SHEET_PAN_SZ: {
				return entities.stream().map(SheetPanSize::new).collect(Collectors.toList());
			}
			case A102375_SHEET_SZ: {
				return entities.stream().map(SheetSize::new).collect(Collectors.toList());
			}
			case A100698_TRNG_PANTS_SZ: {
				return entities.stream().map(TrainingPantsSize::new).collect(Collectors.toList());
			}
			default: {
				logger.error(String.format(UNKNOWN_CODE_TABLE_ERROR, tableName));
				return null;
			}
		}
	}
}
