package com.heb.pm.productDetails.product.OnlineAttributes;

import com.heb.pm.entity.Attribute;
import com.heb.pm.entity.DynamicAttribute;
import com.heb.pm.entity.EntityAttribute;
import com.heb.pm.entity.EntityAttributeCode;
import com.heb.pm.entity.TagsAndSpecsAttribute;
import com.heb.pm.entity.TagsAndSpecsAttributeUpdate;
import com.heb.pm.entity.TagsAndSpecsValue;
import com.heb.pm.repository.DynamicAttributeRepository;
import com.heb.pm.repository.EntityAttributeCodeRepository;
import com.heb.pm.repository.EntityAttributeRepository;
import com.heb.pm.ws.ProductAttributeManagementServiceClient;
import org.omg.DynamicAny.DynAny;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * A service for processing tags and specs attributes requests
 * @author s753601
 * @version 2.14.0
 */
@Service
public class TagsAndSpecsService {

	private String PRODUCT_TYPE = "PROD";
	private long TAGS_AND_SPECS_ENTITY_ID=16L;

	@Autowired
	DynamicAttributeRepository dynamicAttributeRepository;

	@Autowired
	EntityAttributeCodeRepository entityAttributeCodeRepository;

	@Autowired
	EntityAttributeRepository entityAttributeRepository;

	@Autowired
	ProductAttributeManagementServiceClient productAttributeManagementServiceClient;

	@Value("${app.sourceSystemId}")
	private int DefaultSourceSystemCode;

	/**
	 * This method will get the list of Dynamic attributes that will then be converted to a list of Tags and Specs
	 * Attributes
	 * @param prodId the product identification number
	 * @return
	 */
	public List<TagsAndSpecsAttribute> getTagsAndSpecs(Long prodId){
		List<DynamicAttribute> dynamicAttributes = this.dynamicAttributeRepository.findByKeyKeyAndKeyKeyTypeAndKeySourceSystemOrderByKeyAttributeId(prodId, PRODUCT_TYPE, DefaultSourceSystemCode);
		return createTagsAndSpecsAttributes(dynamicAttributes, prodId);
	}

	/**
	 * This method will take a list of dynamic attributes and convert them to Tags and Specst attributes
	 * @param dynamicAttributes
	 * @param prodId
	 * @return
	 */
	private List<TagsAndSpecsAttribute> createTagsAndSpecsAttributes(List<DynamicAttribute> dynamicAttributes, Long prodId){
		ArrayList<TagsAndSpecsAttribute> tagsAndSpecsAttributes = new ArrayList<>();
		long currentAttribute = -1;
		TagsAndSpecsAttribute currentTagsAndSpecs = new TagsAndSpecsAttribute();
		List<TagsAndSpecsValue> values= new ArrayList<>();
		for (DynamicAttribute attribute: dynamicAttributes) {
			if(currentAttribute != attribute.getKey().getAttributeId()){
				if(currentAttribute !=-1){
					currentTagsAndSpecs.setValues(values);
					tagsAndSpecsAttributes.add(currentTagsAndSpecs);
					currentTagsAndSpecs=new TagsAndSpecsAttribute();
				}
				currentAttribute = attribute.getKey().getAttributeId();
				currentTagsAndSpecs.setAttributeId(attribute.getKey().getAttributeId());
				currentTagsAndSpecs.setAttributeName(attribute.getAttribute().getAttributeName());
				currentTagsAndSpecs.setMultiValueFlag(attribute.getAttribute().getMultipleValueSwitch());
				currentTagsAndSpecs.setProdId(prodId);
				if(attribute.getAttributeCodeId() == null){
					values = new ArrayList<>();
					currentTagsAndSpecs.setAlternativeAttributeCodeText(attribute.getTextValue());
				} else {
					values = new ArrayList<>();
					values=getTagsAndSpecsValues(attribute.getKey().getAttributeId());
				}
			}
			selectAttributeCode(values, attribute.getAttributeCodeId());
		}
		currentTagsAndSpecs.setValues(values);
		tagsAndSpecsAttributes.add(currentTagsAndSpecs);
		return tagsAndSpecsAttributes;
	}

	/**
	 * This method will take and attribute Id and generate a list of all possilble attribute codes tied to that
	 * attribute
	 * @param attributeId the parent attribute identification number
	 * @return
	 */
	private List<TagsAndSpecsValue> getTagsAndSpecsValues(int attributeId){
		ArrayList<TagsAndSpecsValue> values = new ArrayList<>();
		List<EntityAttributeCode> entityAttributeCodes = this.entityAttributeCodeRepository.findByKeyAttributeIdAndKeyEntityId(Long.valueOf(attributeId), TAGS_AND_SPECS_ENTITY_ID);
		for (EntityAttributeCode entityAttributeCode: entityAttributeCodes) {
			TagsAndSpecsValue currentValue = new TagsAndSpecsValue();
			currentValue.setAttributeCodeId(entityAttributeCode.getKey().getAttributeCodeId());
			currentValue.setName(entityAttributeCode.getAttributeCode().getAttributeValueText());
			currentValue.setSelected(false);
			values.add(currentValue);
		}
		return values;
	}

	/**
	 * This method will search the list of attribute codes and select the one whose code matches the current attribute
	 * code id
	 * @param values  the list of possible attribute codes
	 * @param attributeCode the attribute code being selected
	 * @return
	 */
	private List<TagsAndSpecsValue> selectAttributeCode(List<TagsAndSpecsValue> values, Long attributeCode){
		if(attributeCode != null){
			for (TagsAndSpecsValue value: values) {
				if(value.getAttributeCodeId() == attributeCode){
					value.setSelected(true);
				}
			}
		}
		return values;
	}

	/**
	 * This method gets all of the possible tags and specs options
	 * @return
	 */
	public List<EntityAttribute> getTagsAndSpecsOptions(){
		List<EntityAttribute> tagsAndSpecsOptions = this.entityAttributeRepository.GetTagsAndSpecsAttributes();
		return tagsAndSpecsOptions;
	}

	/**
	 * This method returns all of the possible tags and specs values for an attribute
	 * @return
	 */
	public List<EntityAttributeCode> getTagsAndSpecsValues(){
		List<EntityAttributeCode> tagsAndSpecsVaues = this.entityAttributeCodeRepository.findByKeyEntityId(TAGS_AND_SPECS_ENTITY_ID);
		return tagsAndSpecsVaues;
	}

	/**
	 * This method will take a list of process all of the tags and specs updates
	 * @param updates the updates to a products tags and specs
	 * @param userId the user requesting the change
	 */
	public void updateTagsAndSpecs(TagsAndSpecsAttributeUpdate updates, String userId){
		if(updates.getUpdates().size()>0) {
			this.productAttributeManagementServiceClient.updateTagsAndSpecsAttribute(updates.getUpdates(), userId);
		}
		if(updates.getDeletes().size()>0) {
			for(TagsAndSpecsAttribute attribute:updates.getDeletes()){
				for (TagsAndSpecsValue value: attribute.getValues()){
					value.setSelected(false);
				}
			}
			this.productAttributeManagementServiceClient.updateTagsAndSpecsAttribute(updates.getDeletes(), userId);
		}
	}
}
