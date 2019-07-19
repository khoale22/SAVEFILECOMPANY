/*
 *   ngTableDirective.js
 *
 *   This code, with minor customisation, is the sample code provided in the site
 *   http://ng-table.com/#/editing/demo-inline by it's author Vitalii Savchuk (https://github.com/esvit)
 */

/**
 * Ng Table Directives used to track Table Row/cell changes.
 *
 * @author vn40486
 * @since 2.1.0
 */
(function() {
	angular.module("productMaintenanceUiApp").directive("ngTrackedTable", ngTrackedTable);

	ngTrackedTable.$inject = [];

	/**
	 * ngTrackedTable function definition.
	 *
	 * @returns {{restrict: string, priority: number, require: string, controller: ngTrackedTableController}}
	 */
	function ngTrackedTable() {
		return {
			restrict: "A",
			priority: -1,
			require: "ngForm",
			controller: ngTrackedTableController
		};
	}

	ngTrackedTableController.$inject = ["$scope", "$parse", "$attrs", "$element"];

	/**
	 * ngTrackedTable controller defintion.
	 * @param $scope	object that refers to the application model.
	 * @param $parse	converts Angular expression into a function.
	 * @param $attrs	directive attributes.
	 * @param $element	raw dom element wrapper.
	 */
	function ngTrackedTableController($scope, $parse, $attrs, $element) {
		var self = this;
		var tableForm = $element.controller("form");
		var dirtyCellsByRow = [];
		var invalidCellsByRow = [];

		init();

		/**
		 * Initialize directive.
		 */
		function init() {
			var setter = $parse($attrs.ngTrackedTable).assign;
			setter($scope, self);
			$scope.$on("$destroy", function() {
				setter(null);
			});

			self.reset = reset;
			self.isCellDirty = isCellDirty;
			self.setCellDirty = setCellDirty;
			self.setCellInvalid = setCellInvalid;
			self.untrack = untrack;
		}

		/**
		 * Used to get cells of a specific  row.
		 * @param row	a row in the data table.
		 * @param cellsByRow	cells of a row in a data table.
		 * @returns {*}
		 */
		function getCellsForRow(row, cellsByRow) {
			return _.find(cellsByRow, function(entry) {
				return entry.row === row;
			})
		}

		/**
		 * Returns boolean over the cell's original content modified state.
		 * @param row	a row in the data table.
		 * @param cell	a cell of the row in the data table.
		 * @returns {*|boolean}	true if the cell's content is modified; false otherwise.
		 */
		function isCellDirty(row, cell) {
			var rowCells = getCellsForRow(row, dirtyCellsByRow);
			return rowCells && rowCells.cells.indexOf(cell) !== -1;
		}

		/**
		 * Used to reset a row to its original/initial state before it was modified recently.
		 */
		function reset() {
			dirtyCellsByRow = [];
			invalidCellsByRow = [];
			setInvalid(false);
		}

		/**
		 * Set a specific cell cell status as Dirty/Modified/not.
		 * @param row
		 * @param cell
		 * @param isDirty	boolean true or false.
		 */
		function setCellDirty(row, cell, isDirty) {
			setCellStatus(row, cell, isDirty, dirtyCellsByRow);
		}

		/**
		 * Sets the status of cell as invalid or not, thereby used to highlight the row to valid input.
		 * @param row
		 * @param cell
		 * @param isInvalid	boolean of row/cell invalid status.
		 */
		function setCellInvalid(row, cell, isInvalid) {
			setCellStatus(row, cell, isInvalid, invalidCellsByRow);
			setInvalid(invalidCellsByRow.length > 0);
		}

		/**
		 * Sets value of a specific cell.
		 * @param row
		 * @param cell
		 * @param value	new value to be set.
		 * @param cellsByRow	cells of a selected row.
		 */
		function setCellStatus(row, cell, value, cellsByRow) {
			var rowCells = getCellsForRow(row, cellsByRow);
			if (!rowCells && !value) {
				return;
			}

			if (value) {
				if (!rowCells) {
					rowCells = {
						row: row,
						cells: []
					};
					cellsByRow.push(rowCells);
				}
				if (rowCells.cells.indexOf(cell) === -1) {
					rowCells.cells.push(cell);
				}
			} else {
				_.remove(rowCells.cells, function(item) {
					return cell === item;
				});
				if (rowCells.cells.length === 0) {
					_.remove(cellsByRow, function(item) {
						return rowCells === item;
					});
				}
			}
		}

		/**
		 * Used to set table status as invalid for not valid data in it's content.
		 * @param isInvalid
		 */
		function setInvalid(isInvalid) {
			self.$invalid = isInvalid;
			self.$valid = !isInvalid;
		}

		function untrack(row) {
			_.remove(invalidCellsByRow, function(item) {
				return item.row === row;
			});
			_.remove(dirtyCellsByRow, function(item) {
				return item.row === row;
			});
			setInvalid(invalidCellsByRow.length > 0);
		}
	}
})();

/**
 * Track row level changes.
 */
(function() {
	angular.module("productMaintenanceUiApp").directive("ngTrackedTableRow", ngTrackedTableRow);

	ngTrackedTableRow.$inject = [];

	function ngTrackedTableRow() {
		return {
			restrict: "A",
			priority: -1,
			require: ["^ngTrackedTable", "ngForm"],
			controller: ngTrackedTableRowController
		};
	}

	ngTrackedTableRowController.$inject = ["$attrs", "$element", "$parse", "$scope"];

	/**
	 * ngTrackedTable row controller definition.
	 * @param $attrs	row directive attributes.
	 * @param $element	raw DOM element wrapper.
	 * @param $parse converts Angular expression into a function.
	 * @param $scope object that refers to the application model.
	 */
	function ngTrackedTableRowController($attrs, $element, $parse, $scope) {
		var self = this;
		var row = $parse($attrs.ngTrackedTableRow)($scope);
		var rowFormCtrl = $element.controller("form");
		var trackedTableCtrl = $element.controller("ngTrackedTable");

		self.isCellDirty = isCellDirty;
		self.setCellDirty = setCellDirty;
		self.setCellInvalid = setCellInvalid;

		function isCellDirty(cell) {
			return trackedTableCtrl.isCellDirty(row, cell);
		}

		function setCellDirty(cell, isDirty) {
			trackedTableCtrl.setCellDirty(row, cell, isDirty)
		}

		function setCellInvalid(cell, isInvalid) {
			trackedTableCtrl.setCellInvalid(row, cell, isInvalid)
		}
	}
})();

/**
 * Track Cell/column changes.
 */
(function() {
	angular.module("productMaintenanceUiApp").directive("ngTrackedTableCell", ngTrackedTableCell);

	ngTrackedTableCell.$inject = [];

	function ngTrackedTableCell() {
		return {
			restrict: "A",
			priority: -1,
			scope: true,
			require: ["^ngTrackedTableRow", "ngForm"],
			controller: ngTrackedTableCellController
		};
	}

	ngTrackedTableCellController.$inject = ["$attrs", "$element", "$scope"];

	/**
	 * ngTrackedTable cell controller definition.
	 * @param $attrs cell directive attributes.
	 * @param $element raw DOM element wrapper.
	 * @param $scope object that refers to the application model.
	 */
	function ngTrackedTableCellController($attrs, $element, $scope) {
		var self = this;
		var cellFormCtrl = $element.controller("form");
		var cellName = cellFormCtrl.$name;
		var trackedTableRowCtrl = $element.controller("ngTrackedTableRow");

		if (trackedTableRowCtrl.isCellDirty(cellName)) {
			cellFormCtrl.$setDirty();
		} else {
			cellFormCtrl.$setPristine();
		}
		// note: we don't have to force setting validaty as angular will run validations
		// when we page back to a row that contains invalid data

		$scope.$watch(function() {
			return cellFormCtrl.$dirty;
		}, function(newValue, oldValue) {
			if (newValue === oldValue) return;

			trackedTableRowCtrl.setCellDirty(cellName, newValue);
		});

		$scope.$watch(function() {
			return cellFormCtrl.$invalid;
		}, function(newValue, oldValue) {
			if (newValue === oldValue) return;

			trackedTableRowCtrl.setCellInvalid(cellName, newValue);
		});
	}
})();
