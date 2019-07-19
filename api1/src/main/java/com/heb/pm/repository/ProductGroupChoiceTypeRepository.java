package com.heb.pm.repository;

import com.heb.pm.entity.ProductGroupChoiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductGroupChoiceTypeRepository extends JpaRepository<ProductGroupChoiceType, String> {

    /**
     * Find the list of ProductGroupChoiceType by productGroupTypeCode.
     * @param productGroupTypeCode the productGroupTypeCode.
     * @return List<ProductGroupChoiceType>
     */
    List<ProductGroupChoiceType> findByKeyProductGroupTypeCode(String productGroupTypeCode);

	/**
     * Find the list of ProductGroupChoiceType by pickerSw.
     * @param pickerSwitch the productGroupTypeCode.
     * @return List<ProductGroupChoiceType>
     */
	List<ProductGroupChoiceType> findByPickerSwitch(boolean pickerSwitch);

	/**
	 * Find the list of ProductGroupChoiceType by productGroupTypeCode and picker flag.
	 * @param productGroupTypeCode the productGroupTypeCode.
	 * @param picker picker flag
	 * @return List<ProductGroupChoiceType>
	 */
	List<ProductGroupChoiceType> findByKeyProductGroupTypeCodeAndPickerSwitch(String productGroupTypeCode,Boolean picker);

	/**
	 * Get list ProductGroupChoiceType by choiceTypeCode.
	 *
	 * @param choiceTypeCode the code of ChoiceType.
	 * @return the list of ProductGroupChoiceType.
	 */
	List<ProductGroupChoiceType> findAllByChoiceTypeChoiceTypeCode(String choiceTypeCode);
}