package com.heb.pm.scaleManagement;

import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.util.controller.StreamingExportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
//Ingredient Not In Statements Starts
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Value;
import com.heb.pm.CoreEntityManager;
import com.heb.pm.CoreTransactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
//Ingredient Not In Statements Ends
/**
 * Service that generates reports for the scale system.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
public class ReportsService {

	private static final Logger logger = LoggerFactory.getLogger(ReportsService.class);

	private static final String INGREDIENT_REPORT_HEADER= "\"UPC\",\"PLU\",\"Description Line One\",\"Description Line Two\",\"Ingredient Statement Number\",\"Ingredients\"";
	private static final String INGREDIENT_EXPORT_FORMAT = "\"%d\",\"%d\",\"%s\",\"%s\",\"%d\",\"%s\"";
	private static final String NLEA2016_REPORT_HEADER = "\"Nutrient Statement Number\",\"UPC\",\"English Line 1\",\"English Line 2\",\"Department\"";
	private static final String NLEA2016_EXPORT_FORMAT = "\"%d\",\"%s\",\"%s\",\"%s\",\"%s\"";
	private static final String LOG_MESSAGE_FETCHING_PAGE = "Fetching page %d";
	private static final int PAGE_SIZE = 100;
	private static final int NUMBER_10 = 10;
	private static final int NUMBER_ZERO = 0;
	private static final String STRING_0 = "0";
	private static final String BLANK = " ";
	private static final String ITEM_CODE_TYPE_CONTR = "CONTR";
	private static final String STATEMENT_NOT_TIED_TO_UPC_MESSAGE = "Statement not tied to UPC";
	// Ingredient Not In Statements Starts
	@Value("${oracle.region}")
	private String databaseSchema;
	
	@Autowired
	@CoreEntityManager
	EntityManager entityManager;

	 //	Start - Changes are done for Ingredient not used in statements by Paranthaman,Dhayanithi
	private static final String INGREDIENT_NOT_IN_STATEMENTS_REPORT_HEADER = "\"INGR CODE\",\"SOI FLAG\",\"INGREDIENT DESCRIPTION\"";
	private static final String INGREDIENT_NOT_IN_STATEMENTS_EXPORT_FORMAT = "\"%s\",\"%s\",\"%s\"";
	public static final String DATE_FORMAT_NOW = "MM/dd/yy";


	private static final String INGREDIENT_NOT_IN_STATEMENTS_SQL = "SELECT INGREDIENT_CODE AS \"INGR CODE\", SOI_FLAG AS \"SOI FLAG\" \n" + 
	    		"      ,INGREDIENT_DESC AS \"INGREDIENT DESCRIPTION\" \n" + 
	    		" FROM   EMD.SL_INGREDIENT SI \n" + 
	    		" WHERE  ( NOT EXISTS (SELECT 1 \n" + 
	    		"                        FROM   EMD.SL_INGRD_STMT_DTL DTL \n" + 
	    		"                        WHERE  DTL.PD_INGRD_CD = SI.INGREDIENT_CODE) \n" + 
	    		"        AND NOT EXISTS (SELECT 1 \n" + 
	    		"                        FROM   EMD.SL_SOI_INGREDIENT SOI \n" + 
	    		"                        WHERE  SOI.INGREDIENT_CODE = SI.INGREDIENT_CODE) ) ";
		
	 //	End - Changes are done for Ingredient not used in statements by Paranthaman,Dhayanithi

	private static List<Long> invalidIngredientStatements;

	static {
		ReportsService.invalidIngredientStatements = new LinkedList<>();
		ReportsService.invalidIngredientStatements.add(0l);
		ReportsService.invalidIngredientStatements.add(9999l);
	}

	@Autowired
	private ScaleUpcRepository scaleUpcRepository;

	@Autowired
	private NutrientStatementHeaderRepository nutrientStatementHeaderRepository;

	@Autowired
	private NutrientsService nutrientsService;

	@Autowired
	private ItemMasterRepository itemMasterRepository;

	@Autowired
	private SubDepartmentRepository subDepartmentRepository;



	/**
	 * Streams a report of UPCs whose ingredient statements match a supplied pattern.
	 *
	 * @param outputStream The output stream to write the report to.
	 * @param ingredientPattern The pattern to match in the ingredient statement.
	 */
	public void streamIngredientReport(ServletOutputStream outputStream, String ingredientPattern){

		// Print out the header
		try {
			outputStream.println(ReportsService.INGREDIENT_REPORT_HEADER);
			outputStream.flush();
		} catch (IOException e) {
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}

		// Build the object that will match the user's requested pattern.
		Pattern pattern = Pattern.compile(ingredientPattern, Pattern.CASE_INSENSITIVE);

		int page = 0;
		int sinceLastPrint = 0;
		boolean dataFound;
		// Will pull all the UPCs with valid ingredient statements in pages of 100.
		do {
			dataFound = false;

			//If the program has not output to the file in 50 iterations, output "loading" to mitigate timeout issues.
			try {
				if (sinceLastPrint >= 50) {
					outputStream.flush();
					outputStream.println("Loading...");
					sinceLastPrint = 0;
				}
			}catch (IOException e){
				throw new StreamingExportException(e.getMessage(), e.getCause());
			}
			sinceLastPrint++;

			// Get the next page of data.
			ReportsService.logger.debug(String.format("Fetching page %d", page));

			Pageable pageRequest = new PageRequest(page++, ReportsService.PAGE_SIZE);
			List<ScaleUpc> scaleUpcs =
					scaleUpcRepository.findByIngredientStatementNotIn(
							ReportsService.invalidIngredientStatements, pageRequest);

			// After the last one is pulled, the list will be empty.
			if (!scaleUpcs.isEmpty()) {
				dataFound = true;
				for (ScaleUpc scaleUpc : scaleUpcs) {
					String ingredientStatement = scaleUpc.getIngredientStatementHeader().getIngredientsText();
					// If the ingredient statement matches the user's supplied pattern, print it out to the
					// output stream.
					if (pattern.matcher(ingredientStatement).find()) {
						try {
							outputStream.println(String.format(ReportsService.INGREDIENT_EXPORT_FORMAT,
									scaleUpc.getUpc(), scaleUpc.getPlu(), scaleUpc.getEnglishDescriptionOne(),
									scaleUpc.getEnglishDescriptionTwo(), scaleUpc.getIngredientStatement(),
									ingredientStatement));
							sinceLastPrint = 0;
						} catch (IOException e) {
							throw new StreamingExportException(e.getMessage(), e.getCause());
						}
					}
				}
			}
		} while (dataFound);
	}

	/**
	 * Streams a report of nutrient statements that do not contain a corresponding 2016 statement .
	 *
	 * @param outputStream The output stream to write the report to.
	 */
	public void streamNLEA2016Report(ServletOutputStream outputStream) {
		// Print out the header
		try {
			outputStream.println(ReportsService.NLEA2016_REPORT_HEADER);
			outputStream.flush();
		} catch (IOException e) {
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
		int page = 0;
		boolean dataFound;
		// Will pull all the nutrient statements are valid in pages of 100.
		do {
			dataFound = false;
			// Get the next page of data.
			ReportsService.logger.debug(String.format(LOG_MESSAGE_FETCHING_PAGE, page));
			Pageable pageRequest = new PageRequest(page++, ReportsService.PAGE_SIZE,NutrientStatementHeader.getDefaultSort());
			List<NutrientStatementHeader> nutrientStatementHeaders =
					this.nutrientStatementHeaderRepository.findByStatementMaintenanceSwitchNot(NutrientStatementHeader.DELETE_MAINT_SW, pageRequest);
			if (!nutrientStatementHeaders.isEmpty()) {
				dataFound = true;
				for (NutrientStatementHeader nutrientStatementHeader : nutrientStatementHeaders) {
					// If nutrient statement not contain a corresponding 2016 statement.
					if (!nutrientsService.isNLEA16NutrientStatementExists(nutrientStatementHeader.getNutrientStatementNumber())) {
						try {
							//If that statement is tied to a UPC.
							if (nutrientStatementHeader.getScaleUpc() != null) {
								outputStream.println(String.format(ReportsService.NLEA2016_EXPORT_FORMAT,
										nutrientStatementHeader.getNutrientStatementNumber(), String.valueOf(nutrientStatementHeader.getScaleUpc().getUpc()),
										nutrientStatementHeader.getScaleUpc().getEnglishDescriptionOne(),
										nutrientStatementHeader.getScaleUpc().getEnglishDescriptionTwo(), getDepartment(nutrientStatementHeader.getScaleUpc().getUpc())));
							} else {
								outputStream.println(String.format(ReportsService.NLEA2016_EXPORT_FORMAT,
										nutrientStatementHeader.getNutrientStatementNumber(), STATEMENT_NOT_TIED_TO_UPC_MESSAGE, BLANK, BLANK, BLANK));
							}
						} catch (IOException e) {
							throw new StreamingExportException(e.getMessage(), e.getCause());
						}
					}
				}
			}
		} while (dataFound);
	}

	 //	Start - Changes are done for Ingredient not used in statements by Paranthaman,Dhayanithi
	/**
	 * Streams a report of nutrient statements that do not contain a corresponding 2016 statement .
	 *
	 * @param outputStream The output stream to write the report to.
	 */
	public void streamIngredientsNotInStatementsReport(ServletOutputStream outputStream) {
		// Print out the header
		try {
		    outputStream.println("          H. E. BUTT GROCERY COMPANY");
		    outputStream.println("INGREDIENTS NOT USED IN INGREDIENT STATEMENTS");
		    outputStream.println(("         Report Date :").concat(reportDate()));
			outputStream.println(ReportsService.INGREDIENT_NOT_IN_STATEMENTS_REPORT_HEADER);
			outputStream.flush();
		} catch (IOException e) {
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
		int page = 0;
			Pageable pageRequest = new PageRequest(page++, ReportsService.PAGE_SIZE,Ingredient.getDefaultSort());
			String sql = String.format(ReportsService.INGREDIENT_NOT_IN_STATEMENTS_SQL,
					this.databaseSchema);
			ReportsService.logger.debug(sql);
			Query q = entityManager.createNativeQuery(sql);
			List<Object[]> ingredientHeadersList = q.getResultList();
			ReportsService.logger.debug(String.valueOf(ingredientHeadersList.size()));
			List<Ingredient> ingredientHeaders = new ArrayList<Ingredient>();
			ingredientHeadersList.stream().forEach((record) -> {
				Ingredient ingredientHeadersLst = new Ingredient();
				ingredientHeadersLst.setIngredientCode(record[0].toString());
				String soiFlg = (record[1].toString());
				ingredientHeadersLst.setMaintFunction(record[1].toString());
				ingredientHeadersLst.setIngredientDescription(record[2].toString());
				ingredientHeaders.add(ingredientHeadersLst);
		});

			if (!ingredientHeaders.isEmpty()) {
				for (Ingredient ingredientHeader : ingredientHeaders) {
						try {
							if (ingredientHeader.getIngredientCode() != null) {
								outputStream.println(String.format(ReportsService.INGREDIENT_NOT_IN_STATEMENTS_EXPORT_FORMAT,
										ingredientHeader.getIngredientCode(),
										ingredientHeader.getMaintFunction(),
										ingredientHeader.getIngredientDescription()));
							}
						} catch (IOException e) {
							throw new StreamingExportException(e.getMessage(), e.getCause());
						}
					
				}
			}
		
	}
    //	End - Changes are done for Ingredient not used in statements by Paranthaman,Dhayanithi
	
	// Ingredient Not In Statements Ends
	/**
	 * Get department for scale upc.
	 * @param upc the upc id.
	 * @return department display name.
	 */
	private String getDepartment(Long upc) {
			ItemMasterKey itemMasterKey = new ItemMasterKey();
			itemMasterKey.setItemCode(upc);
			itemMasterKey.setItemType(ItemMasterKey.DSD);
			SubDepartment subDepartment;
			ItemMaster itemMaster = itemMasterRepository.findOne(itemMasterKey);
			if (itemMaster != null) {
				subDepartment = subDepartmentRepository.findOne(subDepartmentKeyHandle(itemMaster));
					if (subDepartment != null) {
						return subDepartment.getDisplayName();
					}
			}
			else {
				itemMasterKey.setItemType(ItemMasterKey.WAREHOUSE);
				itemMaster = itemMasterRepository.findOne(itemMasterKey);
				if (itemMaster != null) {
					subDepartment = subDepartmentRepository.findOne(subDepartmentKeyHandle(itemMaster));
					if (subDepartment != null) {
						return subDepartment.getDisplayName();
					}
				}
				else {
					itemMasterKey.setItemType(ITEM_CODE_TYPE_CONTR);
					itemMaster = itemMasterRepository.findOne(itemMasterKey);
					if (itemMaster != null) {
						subDepartment = subDepartmentRepository.findOne(subDepartmentKeyHandle(itemMaster));
						if (subDepartment != null) {
							return subDepartment.getDisplayName();
						}
					}
				}
			}
		return BLANK;
	}

	/**
	 * Handle SubDepartmentKey.
	 * @param itemMaster the item master.
	 * @return SubDepartmentKey.
	 */
	private SubDepartmentKey subDepartmentKeyHandle (ItemMaster itemMaster) {
		SubDepartmentKey subDepartmentKey = new SubDepartmentKey();
		if (itemMaster.getDepartmentIdOne() != null && itemMaster.getDepartmentIdOne()!=NUMBER_ZERO  && itemMaster.getSubDepartmentIdOne() != null) {
			setValueOfDepartmentKey(subDepartmentKey,itemMaster.getDepartmentIdOne(),itemMaster.getSubDepartmentIdOne());
		}
		else if (itemMaster.getDepartmentIdTwo() != null && itemMaster.getDepartmentIdTwo()!=NUMBER_ZERO  && itemMaster.getSubDepartmentIdTwo() != null){
			setValueOfDepartmentKey(subDepartmentKey,itemMaster.getDepartmentIdTwo(),itemMaster.getSubDepartmentIdTwo());
		}
		else if (itemMaster.getDepartmentIdThree() != null && itemMaster.getDepartmentIdThree()!=NUMBER_ZERO  && itemMaster.getSubDepartmentIdThree() != null){
			setValueOfDepartmentKey(subDepartmentKey,itemMaster.getDepartmentIdThree(),itemMaster.getSubDepartmentIdThree());
		}
		else if (itemMaster.getDepartmentIdFour() != null && itemMaster.getDepartmentIdFour()!=NUMBER_ZERO  && itemMaster.getSubDepartmentIdFour() != null){
			setValueOfDepartmentKey(subDepartmentKey,itemMaster.getDepartmentIdFour(),itemMaster.getSubDepartmentIdFour());
		}
		return subDepartmentKey;
	}
    //	Start - Changes are done for Ingredient not used in statements by Paranthaman,Dhayanithi
    /**
	 * Get report Date.
	 * @param .
	 */
    public static String reportDate() {
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
    	return sdf.format(cal.getTime());
    }
	//	End - Changes are done for Ingredient not used in statements by Paranthaman,Dhayanithi

	/**
	 * Set value for Departmanet Key.
	 * @param subDepartmentKey the subDepartMentKey.
	 */
	private void setValueOfDepartmentKey (SubDepartmentKey subDepartmentKey, Integer departmentId, String subDepartment) {
		if (departmentId / NUMBER_10 == NUMBER_ZERO) {
			subDepartmentKey.setDepartment(STRING_0 + departmentId.toString());
			subDepartmentKey.setSubDepartment(subDepartment.trim());
		} else {
			subDepartmentKey.setDepartment(departmentId.toString());
			subDepartmentKey.setSubDepartment(subDepartment.trim());
		}
	}
}
