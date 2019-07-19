package com.heb.pm.productHierarchy;

import com.heb.pm.CoreEntityManager;
import com.heb.pm.CoreTransactional;
import com.heb.pm.entity.*;
import com.heb.pm.repository.*;
import com.heb.util.jpa.LazyObjectResolver;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Holds all business logic related to Department.
 *
 * @author m314029
 * @since 2.6.0
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ProductHierarchyUtils productHierarchyUtils;

    @Autowired
    private ClassCommodityRepository classCommodityRepository;

    @Autowired
    private BdmRepository bdmRepository;

    @Autowired
    private ItemClassRepository itemClassRepository;

    @Autowired
    private SubDepartmentRepository subDepartmentRepository;
    @Autowired
    @CoreEntityManager
    private EntityManager entityManager;

    @Autowired
    private ProductHierarchyPredicateBuilder productHierarchyPredicateBuilder;

    private LazyObjectResolver<Department> resolver = new DepartmentResolver();

    private LazyObjectResolver<ClassCommodity> classCommodityResolver = new ClassCommodityResolver();

    private static final String BDM_SEARCH_TYPE = "BDM";

    private static final String PRODUCT_HIERARCHY_SEARCH_TYPE = "Product Hierarchy";

    private static final String BLANK_CHARACTER = " ";

    private static final String BDM_CODE_FORMAT = "%s   ";

    private static final String NO_SEARCH_TYPE_IS_SELECTED_MESSAGE = "No search type is selected.";

    /**
     * Call the department repository to find all departments, then resolve the list;
     *
     * @return the list of all departments
     */
    @Cacheable(value = "productHierarchyCache")
    @CoreTransactional
    public List<Department> findAll() {
        List<Department> departments = this.departmentRepository.findAll();
        departments.forEach(
                department -> this.productHierarchyUtils.extrapolateItemClassListOfSubDepartmentList(
                        department.getSubDepartmentList()));
        departments.forEach(department -> this.resolver.fetch(department));
        return departments;
    }

    /**
     * This method filters the product hierarchy by a search string by product hierarchy display name or bdm display name.
     *
     * @param trimmedUpperCaseSearchString Search string already trimmed and upper case.
     * @param searchType the Search type.
     * @return List of departments that contains any level matching the search query and all levels above.
     */
    public List<Department> findHierarchyLevelsByRegularExpression(String trimmedUpperCaseSearchString, String searchType) {
        //If user search by product hierarchy.
        if (searchType.equals(PRODUCT_HIERARCHY_SEARCH_TYPE)) {
            return findProductHierarchyByKeyword(trimmedUpperCaseSearchString);
        }
        //If user search by BDM.
        if (searchType.equals(BDM_SEARCH_TYPE)) {
            return findHierarchyLevelsByBdmDisplayName(trimmedUpperCaseSearchString);
        }
        throw new IllegalArgumentException(NO_SEARCH_TYPE_IS_SELECTED_MESSAGE);
    }

    /**
     * This method filters the product hierarchy by a search string by bdm display name. Find all of bdms has display name
     * contains search string. Then find all of commodities belong to list of bdms.
     * Then create new hierarchies contains department,subdepartment,item classes that contains all of commodities belong to list of bdms.
     *
     * @param trimmedUpperCaseSearchString Search string already trimmed and upper case.
     * @return List of departments that contains all of level contains commodities matching search query.
     */
    public List<Department> findHierarchyLevelsByBdmDisplayName(String trimmedUpperCaseSearchString) {
        List<ClassCommodity> classCommodityList = new ArrayList<>();
        List<ItemClass> itemClassList = new ArrayList<>();
        List<SubDepartment> subDepartmentList = new ArrayList<>();
        List<Department> departmentList = new ArrayList<>();
        //find all bdm has display name contains search string.
        List<Bdm> bdms = bdmRepository.findBdmBySearchString(trimmedUpperCaseSearchString);
        if (!bdms.isEmpty()) {
            //find all commodities has bdm belong to bdms.
            for (Bdm bdm : bdms) {
                List<ClassCommodity> classCommodities = classCommodityRepository.findByBdmCode(String.format(BDM_CODE_FORMAT, bdm.getBdmCode()));
                if (!classCommodities.isEmpty()) {
                    classCommodityList.addAll(classCommodities);
                }
            }
            if (!classCommodityList.isEmpty()) {
                List<Integer> classCodes = new ArrayList<>();
                for (ClassCommodity classCommodity : classCommodityList) {
                    //Get SubCommodityList of Commodity.
                    this.classCommodityResolver.fetch(classCommodity);
                    // Find all class code ids that contains commodities belong to bdms.
                    if (classCodes.indexOf(classCommodity.getKey().getClassCode()) == -1) {
                        classCodes.add(classCommodity.getKey().getClassCode());
                    }
                }
                if (!classCodes.isEmpty()) {
                    List<SubDepartmentKey> subDepartmentKeys = new ArrayList<>();
                    for (Integer classCode : classCodes) {
                        ItemClass itemClass = itemClassRepository.findOne(classCode);
                        if (itemClass != null) {
                            if (itemClass.getCommodityList() != null && !itemClass.getCommodityList().isEmpty()) {
                                //If commodity of item class is exist in commodities belong to bdms
                                // then add commodity into new commodity list.
                                //Set new commodity list for item class.
                                itemClass.setCommodityList(this.findCommoditiesMatched(itemClass.getCommodityList(), classCommodityList));
                                itemClassList.add(itemClass);
                            }
                            //Find all Subdepartment keys of Subdepartments contains itemclasses.
                            SubDepartmentKey subDepartmentKey = new SubDepartmentKey();
                            this.setValueOfDepartmentKey(subDepartmentKey, itemClass.getDepartmentId(), itemClass.getSubDepartmentId());
                            if (subDepartmentKeys.indexOf(subDepartmentKey) == -1) {
                                subDepartmentKeys.add(subDepartmentKey);
                            }
                        }
                    }

                    if (!subDepartmentKeys.isEmpty()) {
                        List<SubDepartmentKey> departmentKeys = new ArrayList<>();
                        for (SubDepartmentKey subDepartmentKey : subDepartmentKeys) {
                            SubDepartment subDepartment = subDepartmentRepository.findOne(subDepartmentKey);
                            if (subDepartment != null) {
                                //Set new item class list into subDepartment.
                                //If item class of subdepartment is exist in itemclass list.
                                // then add item class into new item class  list.
                                subDepartment.setItemClasses(this.findItemClassesMatched(itemClassRepository.findByDepartmentIdAndSubDepartmentId(
                                        Integer.parseInt(subDepartmentKey.getDepartment()),
                                        subDepartmentKey.getSubDepartment()), itemClassList));
                                subDepartmentList.add(subDepartment);
                                //find all department key of department contains subDepartment list.
                                SubDepartmentKey departmentKey = new SubDepartmentKey();
                                departmentKey.setDepartment(subDepartmentKey.getDepartment());
                                departmentKey.setSubDepartment(BLANK_CHARACTER);
                                if (departmentKeys.indexOf(departmentKey) == -1) {
                                    departmentKeys.add(departmentKey);
                                }
                            }
                        }
                        if (!departmentKeys.isEmpty()) {
                            //Sort department keys.
                            departmentKeys.sort((o1, o2) -> {
                                if (o1.getDepartment().equals(o2.getDepartment()))
                                    return 0;
                                return Integer.parseInt(o1.getDepartment()) < Integer.parseInt(o2.getDepartment()) ? -1 : 1;
                            });
                            //If subdepartment of department is exist in subdepartment list.
                            // then add subdepartment into new subDepartment list.
                            for (SubDepartmentKey departmentKey : departmentKeys) {
                                Department department = departmentRepository.findOne(departmentKey);
                                if (department != null) {
                                    //Set new SubDepartment List into Department.
                                    department.setSubDepartmentList(this.findSubDepartmentMatched(department.getSubDepartmentList(), subDepartmentList));
                                    departmentList.add(department);
                                }
                            }
                        }
                    }
                }
            }
        }
        return departmentList;
    }

    /**
     * Find list of element are exist in both commodity list.
     *
     * @param commodities   the commodity list.
     * @param classCommodityMatches the commodity list.
     * @return List of commodity.
     */
    private List<ClassCommodity> findCommoditiesMatched(List<ClassCommodity> commodities, List<ClassCommodity> classCommodityMatches) {
        List<ClassCommodity> classCommoditiesMatched = new ArrayList<>();
        for (ClassCommodity classCommodity : commodities) {
            for (ClassCommodity classCommodityMatch : classCommodityMatches) {
                if (classCommodity.getKey().equals(classCommodityMatch.getKey())) {
                    classCommoditiesMatched.add(classCommodity);
                    break;
                }
            }
        }
        return classCommoditiesMatched;
    }

    /**
     * Find list of element are exist in both itemclasses list.
     *
     * @param itemClasses   the itemclasses list.
     * @param itemClassMatches the itemclasses list.
     * @return list of itemclass.
     */
    private List<ItemClass> findItemClassesMatched(List<ItemClass> itemClasses, List<ItemClass> itemClassMatches) {
        List<ItemClass> itemClassesMatched = new ArrayList<>();
        for (ItemClass itemClass : itemClasses) {
            for (ItemClass itemClassMatch : itemClassMatches) {
                if (itemClass.getItemClassCode().equals(itemClassMatch.getItemClassCode())) {
                    itemClassesMatched.add(itemClass);
                    break;
                }
            }
        }
        return itemClassesMatched;
    }

    /**
     * Find list of element are exist in both subdepartment list.
     *
     * @param subDepartments    the subDepartment List.
     * @param subDepartmentsMatches the subDepartment List.
     * @return list of subdepartment.
     */
    private List<SubDepartment> findSubDepartmentMatched(List<SubDepartment> subDepartments, List<SubDepartment> subDepartmentsMatches) {
        List<SubDepartment> subDepartmentsMatched = new ArrayList<>();
        for (SubDepartment subDepartment : subDepartments) {
            for (SubDepartment subDepartmentMatch : subDepartmentsMatches) {
                if (subDepartment.getKey().equals(subDepartmentMatch.getKey())) {
                    subDepartmentsMatched.add(subDepartment);
                    break;
                }
            }
        }
        return subDepartmentsMatched;
    }

    /**
     * Set value for department key.
     *
     * @param subDepartmentKey the subDepartmentKey.
     * @param departmentId the department id.
     * @param subDepartment the subDepartment.
     */
    private void setValueOfDepartmentKey(SubDepartmentKey subDepartmentKey, Integer departmentId, String subDepartment) {
        if (departmentId < 10) {
            subDepartmentKey.setDepartment('0' + departmentId.toString());
            subDepartmentKey.setSubDepartment(subDepartment.trim());
        } else {
            subDepartmentKey.setDepartment(departmentId.toString());
            subDepartmentKey.setSubDepartment(subDepartment.trim());
        }
    }

    /**
     * Find product hierarchy by keyword.
     * @param keyword the keyword to search
     * @return the list of departments.
     */
    private List<Department> findProductHierarchyByKeyword(String keyword) {
        ProductHierarchyTreeBuilder productHierarchyBuilder = new ProductHierarchyTreeBuilder();
        // Search sub commodity by keyword.
        List<SubCommodity> subCommodityResults = this.findSubCommodityByKeyword(keyword);
        // Build sub commodities tree and it it into search results
        subCommodityResults.forEach((subCommodity) -> {
            if (StringUtils.isNotBlank(subCommodity.getCommodityMaster().getItemClassMaster().getSubDepartmentId())) {
                subCommodity.getCommodityMaster().getItemClassMaster().setSubDepartmentMaster(getSubDepartmentByDeptCodeAndSubDeptCode(subCommodity.getCommodityMaster().getItemClassMaster().getDepartmentId(),
                        subCommodity.getCommodityMaster().getItemClassMaster().getSubDepartmentId()));
                productHierarchyBuilder.build(subCommodity);
            }
        });
        // Search class commodity by keyword and exclude class commodity keys
        List<ClassCommodity> classCommodities = this.findClassCommodityByKeyword(keyword, productHierarchyBuilder.getExcludeClsCommodityKeys());
        // Build commodity tree and it into search results.
        classCommodities.forEach((classCommodity) -> {
            if (StringUtils.isNotBlank(classCommodity.getItemClassMaster().getSubDepartmentId())) {
                classCommodity.getItemClassMaster().setSubDepartmentMaster(getSubDepartmentByDeptCodeAndSubDeptCode(classCommodity.getItemClassMaster().getDepartmentId(),
                        classCommodity.getItemClassMaster().getSubDepartmentId()));
                productHierarchyBuilder.build(classCommodity);
            }
        });
        // Search item class by keyword and exclude item class ids.
        List<ItemClass> itemClassesResults = this.findItemClassByKeyword(keyword, productHierarchyBuilder.getExcludeItemClassIds());
        // Build item class tree and it into search results.
        itemClassesResults.forEach((itemClass) -> {
            if (StringUtils.isNotBlank(itemClass.getSubDepartmentId())) {
                itemClass.setSubDepartmentMaster(getSubDepartmentByDeptCodeAndSubDeptCode(itemClass.getDepartmentId(), itemClass.getSubDepartmentId()));
                productHierarchyBuilder.build(itemClass);
            }
        });
        // Search department by keyword and exclude sub departments
        List<SubDepartment> subDepartmentResults = this.findSubDepartmentByKeyword(keyword, productHierarchyBuilder.getExcludeSubDepartmentKeys());
        // Build sub department tree and add it into search results.
        subDepartmentResults.forEach((subDept) -> {
            productHierarchyBuilder.build(subDept);
        });

        // Search department by keyword and exclude department.
        List<Department> departments = this.findDepartmentByKeyword(keyword, productHierarchyBuilder.getExcludeDepartmentKeys());
        // Build department tree and add it into search results.
        departments.forEach((dept) -> productHierarchyBuilder.build(dept));
        List<Department> searchResults = productHierarchyBuilder.getSearchResults();
        Collections.sort(searchResults, Comparator.comparing(o -> o.getKey().getDepartment()));
        return searchResults;
    }

    /**
     * Find sub commodity by keyword.
     *
     * @param keyword the keyword to search.
     * @return the list of sub commodities.
     */
    private List<SubCommodity> findSubCommodityByKeyword(String keyword) {
        // Get the criteriaBuilder needed to build the query.
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        // Builds the criteria for the main query
        CriteriaQuery<SubCommodity> queryBuilder = criteriaBuilder.createQuery(SubCommodity.class);
        // Select from sub commodity.
        Root<SubCommodity> root = queryBuilder.from(SubCommodity.class);
        // Build the where clause
        Predicate predicate = this.productHierarchyPredicateBuilder.buildSubCommodityPredicate(
                root, queryBuilder, criteriaBuilder, keyword);
        queryBuilder.where(predicate);
        // Execute the query.
        return this.entityManager.createQuery(queryBuilder).getResultList();
    }

    /**
     * Find commodity by keyword and the exclude list of class commodity keys that doesn't want to include them in search results.
     *
     * @param keyword                 the keyword to search.
     * @param excludeClsCommodityKeys the list of class commodity keys that doesn't want to includes them in search results.
     * @return the list of class commodities.
     */
    private List<ClassCommodity> findClassCommodityByKeyword(String keyword, List<ClassCommodityKey> excludeClsCommodityKeys) {
        // Get the criteriaBuilder needed to build the query.
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        // Builds the criteria for the main query
        CriteriaQuery<ClassCommodity> queryBuilder = criteriaBuilder.createQuery(ClassCommodity.class);
        // Select from class commodity.
        Root<ClassCommodity> root = queryBuilder.from(ClassCommodity.class);
        // Build the where clause
        Predicate predicate = this.productHierarchyPredicateBuilder.buildClassCommodityPredicate(
                root, queryBuilder, criteriaBuilder, keyword, excludeClsCommodityKeys);
        queryBuilder.where(predicate);
        // Execute the query.
        return this.entityManager.createQuery(queryBuilder).getResultList();
    }

    /**
     * Find item class by keyword and the exclude list of item class ids that doesn't want to includes them in search results.
     *
     * @param keyword             the keyword to search.
     * @param excludeItemClassIds the exclude list of item class ids that doesn't want to includes them in search results.
     * @return the list of item classes.
     */
    private List<ItemClass> findItemClassByKeyword(String keyword, List<Integer> excludeItemClassIds) {
        // Get the criteriaBuilder needed to build the query.
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        // Builds the criteria for the main query
        CriteriaQuery<ItemClass> queryBuilder = criteriaBuilder.createQuery(ItemClass.class);
        // Select from item class.
        Root<ItemClass> root = queryBuilder.from(ItemClass.class);
        // Build the where clause
        Predicate predicate = this.productHierarchyPredicateBuilder.buildItemClassSpecification(
                root, queryBuilder, criteriaBuilder, keyword, excludeItemClassIds);
        queryBuilder.where(predicate);
        // Execute the query.
        return this.entityManager.createQuery(queryBuilder).getResultList();
    }

    /**
     * Find sub department by keyword and the list of exclude sub department keys that doesn't want to includes them in search results.
     *
     * @param keyword            the keyword to search.
     * @param excludeSubDeptKeys the list of sub department keys that doesn't want to includes them in search results.
     * @return the list of sub departments
     */
    private List<SubDepartment> findSubDepartmentByKeyword(String keyword, List<SubDepartmentKey> excludeSubDeptKeys) {
        // Get the criteriaBuilder needed to build the query.
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        // Builds the criteria for the main query
        CriteriaQuery<SubDepartment> queryBuilder = criteriaBuilder.createQuery(SubDepartment.class);
        // Select from sub department.
        Root<SubDepartment> root = queryBuilder.from(SubDepartment.class);
        // Build the where clause
        Predicate predicate = this.productHierarchyPredicateBuilder.buildSubDeptSpecification(
                root, queryBuilder, criteriaBuilder, keyword, excludeSubDeptKeys);
        queryBuilder.where(predicate);
        // Execute the query.
        return this.entityManager.createQuery(queryBuilder).getResultList();
    }

    /**
     * Get the sub department by department code and sub department code.
     *
     * @param departmentCode    the department code.
     * @param subDepartmentCode the sub department code.
     * @return the sub department.
     */
    private SubDepartment getSubDepartmentByDeptCodeAndSubDeptCode(Integer departmentCode, String subDepartmentCode) {
        // Get the criteriaBuilder needed to build the query.
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        // Builds the criteria for the main query
        CriteriaQuery<SubDepartment> queryBuilder = criteriaBuilder.createQuery(SubDepartment.class);
        // Select from sub department.
        Root<SubDepartment> root = queryBuilder.from(SubDepartment.class);
        // Build the where clause
        Predicate predicate = this.productHierarchyPredicateBuilder.buildSubDeptSpecification(
                root, queryBuilder, criteriaBuilder, departmentCode, subDepartmentCode);
        queryBuilder.where(predicate);
        // Execute the query.
        return this.entityManager.createQuery(queryBuilder).getSingleResult();
    }

    /**
     * Find department by keyword and the list of exclude dept keys that doesn't want to includes them in search results.
     *
     * @param keyword         the keyword to search.
     * @param excludeDeptKeys the list of dept keys that doesn't want to includes them in search results.
     * @return the list of departments
     */
    private List<Department> findDepartmentByKeyword(String keyword, List<SubDepartmentKey> excludeDeptKeys) {
        // Get the criteriaBuilder needed to build the query.
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        // Builds the criteria for the main query
        CriteriaQuery<Department> queryBuilder = criteriaBuilder.createQuery(Department.class);
        // Select from department.
        Root<Department> root = queryBuilder.from(Department.class);
        // Build the where clause
        Predicate predicate = this.productHierarchyPredicateBuilder.buildDepartmentSpecification(
                root, queryBuilder, criteriaBuilder, keyword, excludeDeptKeys);
        queryBuilder.where(predicate);
        // Execute the query.
        return this.entityManager.createQuery(queryBuilder).getResultList();
    }
}
