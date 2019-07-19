package com.heb.pm.customHierarchy;

/**
 * @author m314029
 * @since
 */
public class CustomHierarchyParentSearchCriteria {
	private Long hierarchyParentId;
	private String hierarchyContext;
	private boolean firstSearch;
	private int page;
	private int pageSize;

	public Long getHierarchyParentId() {
		return hierarchyParentId;
	}

	public void setHierarchyParentId(Long hierarchyParentId) {
		this.hierarchyParentId = hierarchyParentId;
	}

	public String getHierarchyContext() {
		return hierarchyContext;
	}

	public void setHierarchyContext(String hierarchyContext) {
		this.hierarchyContext = hierarchyContext;
	}

	public boolean isFirstSearch() {
		return firstSearch;
	}

	public void setFirstSearch(boolean firstSearch) {
		this.firstSearch = firstSearch;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Returns a String representation of the object.
	 *
	 * @return A String representation of the object.
	 */
	@Override
	public String toString() {
		return "CustomHierarchyParentSearchCriteria{" +
				"hierarchyParentId=" + hierarchyParentId +
				", hierarchyContext='" + hierarchyContext + '\'' +
				", firstSearch=" + firstSearch +
				", page=" + page +
				", pageSize=" + pageSize +
				'}';
	}
}
