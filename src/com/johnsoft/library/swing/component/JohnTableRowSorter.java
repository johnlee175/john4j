package com.johnsoft.library.swing.component;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableStringConverter;

@SuppressWarnings("rawtypes")
public abstract class JohnTableRowSorter<M extends TableModel> extends RowSorter<M>
{
	/**
	 * Whether or not we resort on TableModelEvent.UPDATEs.
	 */
	private boolean sortsOnUpdates;

	/**
	 * View (JTable) -> model.
	 */
	private Row[] viewToModel;

	/**
	 * model -> view (JTable)
	 */
	private int[] modelToView;

	/**
	 * Comparators specified by column.
	 */
	private Comparator[] comparators;

	/**
	 * Whether or not the specified column is sortable, by column.
	 */
	private boolean[] isSortable;

	/**
	 * Cached SortKeys for the current sort.
	 */
	private SortKey[] cachedSortKeys;

	/**
	 * Cached comparators for the current sort
	 */
	private Comparator[] sortComparators;

	/**
	 * Developer supplied Filter.
	 */
	private RowFilter<? super M, Integer> filter;

	/**
	 * Value passed to the filter. The same instance is passed to the filter for
	 * different rows.
	 */
	private FilterEntry filterEntry;

	/**
	 * The sort keys.
	 */
	private List<SortKey> sortKeys;

	/**
	 * Whether or not to use getStringValueAt. This is indexed by column.
	 */
	private boolean[] useToString;

	/**
	 * Indicates the contents are sorted. This is used if getSortsOnUpdates is
	 * false and an update event is received.
	 */
	private boolean sorted;

	/**
	 * Maximum number of sort keys.
	 */
	private int maxSortKeys;

	/**
	 * Provides access to the data we're sorting/filtering.
	 */
	private TableRowSorterModelWrapper modelWrapper;

	/**
	 * Size of the model. This is used to enforce error checking within the
	 * table changed notification methods (such as rowsInserted).
	 */
	private int modelRowCount;
	
	 /**
     * Comparator that uses compareTo on the contents.
     */
    private static final Comparator COMPARABLE_COMPARATOR =
            new ComparableComparator();

    /**
     * Underlying model.
     */
    private M tableModel;

    /**
     * For toString conversions.
     */
    private TableStringConverter stringConverter;
    
    /**
     * If true: one cell value is null, another cell value isn't, the null value is bigger
     */
    private boolean isNullBigger;
	
	public JohnTableRowSorter() 
	{
        this(null);
	}

	public JohnTableRowSorter(M model) 
	{
		sortKeys = Collections.emptyList();
		maxSortKeys = 3;
        setModel(model);
	}

	protected final void setModelWrapper(TableRowSorterModelWrapper modelWrapper)
	{
		if (modelWrapper == null)
		{
			throw new IllegalArgumentException("modelWrapper most be non-null");
		}
		TableRowSorterModelWrapper last = this.modelWrapper;
		this.modelWrapper = modelWrapper;
		if (last != null)
		{
			modelStructureChanged();
		} else
		{
			// If last is null, we're in the constructor. If we're in
			// the constructor we don't want to call to overridable methods.
			modelRowCount = getModelWrapper().getRowCount();
		}
	}

	protected final TableRowSorterModelWrapper getModelWrapper()
	{
		return modelWrapper;
	}

	public final M getModel()
	{
		return getModelWrapper().getModel();
	}
	
    public void setModel(M model) {
        tableModel = model;
        setModelWrapper(new TableRowSorterModelWrapper());
    }
    
    public void setStringConverter(TableStringConverter stringConverter) {
        this.stringConverter = stringConverter;
    }
    
    public TableStringConverter getStringConverter() {
        return stringConverter;
    }

	public void setSortable(int column, boolean sortable)
	{
		checkColumn(column);
		if (isSortable == null)
		{
			isSortable = new boolean[getModelWrapper().getColumnCount()];
			for (int i = isSortable.length - 1; i >= 0; i--)
			{
				isSortable[i] = true;
			}
		}
		isSortable[column] = sortable;
	}

	public boolean isSortable(int column)
	{
		checkColumn(column);
		return (isSortable == null) ? true : isSortable[column];
	}

	public void setSortKeys(List<? extends SortKey> sortKeys)
	{
		List<SortKey> old = this.sortKeys;
		if (sortKeys != null && sortKeys.size() > 0)
		{
			int max = getModelWrapper().getColumnCount();
			for (SortKey key : sortKeys)
			{
				if (key == null || key.getColumn() < 0
						|| key.getColumn() >= max)
				{
					throw new IllegalArgumentException("Invalid SortKey");
				}
			}
			this.sortKeys = Collections
					.unmodifiableList(new ArrayList<SortKey>(sortKeys));
		} else
		{
			this.sortKeys = Collections.emptyList();
		}
		if (!this.sortKeys.equals(old))
		{
			fireSortOrderChanged();
			if (viewToModel == null)
			{
				// Currently unsorted, use sort so that internal fields
				// are correctly set.
				sort();
			} else
			{
				sortExistingData();
			}
		}
	}

	public List<? extends SortKey> getSortKeys()
	{
		return sortKeys;
	}

	public void setMaxSortKeys(int max)
	{
		if (max < 1)
		{
			throw new IllegalArgumentException("Invalid max");
		}
		maxSortKeys = max;
	}

	public int getMaxSortKeys()
	{
		return maxSortKeys;
	}

	public void setSortsOnUpdates(boolean sortsOnUpdates)
	{
		this.sortsOnUpdates = sortsOnUpdates;
	}

	public boolean getSortsOnUpdates()
	{
		return sortsOnUpdates;
	}

	public void setRowFilter(RowFilter<? super M, Integer> filter)
	{
		this.filter = filter;
		sort();
	}

	public RowFilter<? super M, Integer> getRowFilter()
	{
		return filter;
	}

	public void toggleSortOrder(int column)
	{
		checkColumn(column);
		if (isSortable(column))
		{
			List<SortKey> keys = new ArrayList<SortKey>(getSortKeys());
			SortKey sortKey;
			int sortIndex;
			for (sortIndex = keys.size() - 1; sortIndex >= 0; sortIndex--)
			{
				if (keys.get(sortIndex).getColumn() == column)
				{
					break;
				}
			}
			if (sortIndex == -1)
			{
				// Key doesn't exist
				sortKey = new SortKey(column, SortOrder.ASCENDING);
				keys.add(0, sortKey);
			} else if (sortIndex == 0)
			{
				// It's the primary sorting key, toggle it
				keys.set(0, toggle(keys.get(0)));
			} else
			{
				// It's not the first, but was sorted on, remove old
				// entry, insert as first with ascending.
				keys.remove(sortIndex);
				keys.add(0, new SortKey(column, SortOrder.ASCENDING));
			}
			if (keys.size() > getMaxSortKeys())
			{
				keys = keys.subList(0, getMaxSortKeys());
			}
			setSortKeys(keys);
		}
	}

	private SortKey toggle(SortKey key)
	{
		if (key.getSortOrder() == SortOrder.ASCENDING)
		{
			return new SortKey(key.getColumn(), SortOrder.DESCENDING);
		}
		return new SortKey(key.getColumn(), SortOrder.ASCENDING);
	}

	public int convertRowIndexToView(int index)
	{
		if (modelToView == null)
		{
			if (index < 0 || index >= getModelWrapper().getRowCount())
			{
				throw new IndexOutOfBoundsException("Invalid index");
			}
			return index;
		}
		return modelToView[index];
	}

	public int convertRowIndexToModel(int index)
	{
		if (viewToModel == null)
		{
			if (index < 0 || index >= getModelWrapper().getRowCount())
			{
				throw new IndexOutOfBoundsException("Invalid index");
			}
			return index;
		}
		return viewToModel[index].modelIndex;
	}

	private boolean isUnsorted()
	{
		List<? extends SortKey> keys = getSortKeys();
		int keySize = keys.size();
		return (keySize == 0 || keys.get(0).getSortOrder() == SortOrder.UNSORTED);
	}

	/**
	 * Sorts the existing filtered data. This should only be used if the filter
	 * hasn't changed.
	 */
	private void sortExistingData()
	{
		int[] lastViewToModel = getViewToModelAsInts(viewToModel);

		updateUseToString();
		cacheSortKeys(getSortKeys());

		if (isUnsorted())
		{
			if (getRowFilter() == null)
			{
				viewToModel = null;
				modelToView = null;
			} else
			{
				int included = 0;
				for (int i = 0; i < modelToView.length; i++)
				{
					if (modelToView[i] != -1)
					{
						viewToModel[included].modelIndex = i;
						modelToView[i] = included++;
					}
				}
			}
		} else
		{
			// sort the data
			Arrays.sort(viewToModel);

			// Update the modelToView array
			setModelToViewFromViewToModel(false);
		}
		fireRowSorterChanged(lastViewToModel);
	}

	public void sort()
	{
		sorted = true;
		int[] lastViewToModel = getViewToModelAsInts(viewToModel);
		updateUseToString();
		if (isUnsorted())
		{
			// Unsorted
			cachedSortKeys = new SortKey[0];
			if (getRowFilter() == null)
			{
				// No filter & unsorted
				if (viewToModel != null)
				{
					// sorted -> unsorted
					viewToModel = null;
					modelToView = null;
				} else
				{
					// unsorted -> unsorted
					// No need to do anything.
					return;
				}
			} else
			{
				// There is filter, reset mappings
				initializeFilteredMapping();
			}
		} else
		{
			cacheSortKeys(getSortKeys());

			if (getRowFilter() != null)
			{
				initializeFilteredMapping();
			} else
			{
				createModelToView(getModelWrapper().getRowCount());
				createViewToModel(getModelWrapper().getRowCount());
			}

			// sort them
			Arrays.sort(viewToModel);

			// Update the modelToView array
			setModelToViewFromViewToModel(false);
		}
		fireRowSorterChanged(lastViewToModel);
	}

	/**
	 * Updates the useToString mapping before a sort.
	 */
	private void updateUseToString()
	{
		int i = getModelWrapper().getColumnCount();
		if (useToString == null || useToString.length != i)
		{
			useToString = new boolean[i];
		}
		for (--i; i >= 0; i--)
		{
			useToString[i] = useToString(i);
		}
	}

	/**
	 * Resets the viewToModel and modelToView mappings based on the current
	 * Filter.
	 */
	private void initializeFilteredMapping()
	{
		int rowCount = getModelWrapper().getRowCount();
		int i, j;
		int excludedCount = 0;

		// Update model -> view
		createModelToView(rowCount);
		for (i = 0; i < rowCount; i++)
		{
			if (include(i))
			{
				modelToView[i] = i - excludedCount;
			} else
			{
				modelToView[i] = -1;
				excludedCount++;
			}
		}

		// Update view -> model
		createViewToModel(rowCount - excludedCount);
		for (i = 0, j = 0; i < rowCount; i++)
		{
			if (modelToView[i] != -1)
			{
				viewToModel[j++].modelIndex = i;
			}
		}
	}

	/**
	 * Makes sure the modelToView array is of size rowCount.
	 */
	private void createModelToView(int rowCount)
	{
		if (modelToView == null || modelToView.length != rowCount)
		{
			modelToView = new int[rowCount];
		}
	}

	/**
	 * Resets the viewToModel array to be of size rowCount.
	 */
	private void createViewToModel(int rowCount)
	{
		int recreateFrom = 0;
		if (viewToModel != null)
		{
			recreateFrom = Math.min(rowCount, viewToModel.length);
			if (viewToModel.length != rowCount)
			{
				Row[] oldViewToModel = viewToModel;
				viewToModel = new Row[rowCount];
				System.arraycopy(oldViewToModel, 0, viewToModel, 0,
						recreateFrom);
			}
		} else
		{
			viewToModel = new Row[rowCount];
		}
		int i;
		for (i = 0; i < recreateFrom; i++)
		{
			viewToModel[i].modelIndex = i;
		}
		for (i = recreateFrom; i < rowCount; i++)
		{
			viewToModel[i] = new Row(this, i);
		}
	}

	/**
	 * Caches the sort keys before a sort.
	 */
	private void cacheSortKeys(List<? extends SortKey> keys)
	{
		int keySize = keys.size();
		sortComparators = new Comparator[keySize];
		for (int i = 0; i < keySize; i++)
		{
			sortComparators[i] = getComparator0(keys.get(i).getColumn());
		}
		cachedSortKeys = keys.toArray(new SortKey[keySize]);
	}

	protected boolean useToString(int column) {
        Comparator comparator = superGetComparator(column);
        if (comparator != null) {
            return false;
        }
        Class columnClass = getModel().getColumnClass(column);
        if (columnClass == String.class) {
            return false;
        }
        if (Comparable.class.isAssignableFrom(columnClass)) {
            return false;
        }
        return true;
    }

	/**
	 * Refreshes the modelToView mapping from that of viewToModel. If
	 * <code>unsetFirst</code> is true, all indices in modelToView are first set
	 * to -1.
	 */
	private void setModelToViewFromViewToModel(boolean unsetFirst)
	{
		int i;
		if (unsetFirst)
		{
			for (i = modelToView.length - 1; i >= 0; i--)
			{
				modelToView[i] = -1;
			}
		}
		for (i = viewToModel.length - 1; i >= 0; i--)
		{
			modelToView[viewToModel[i].modelIndex] = i;
		}
	}

	private int[] getViewToModelAsInts(Row[] viewToModel)
	{
		if (viewToModel != null)
		{
			int[] viewToModelI = new int[viewToModel.length];
			for (int i = viewToModel.length - 1; i >= 0; i--)
			{
				viewToModelI[i] = viewToModel[i].modelIndex;
			}
			return viewToModelI;
		}
		return new int[0];
	}

	public void setComparator(int column, Comparator<?> comparator)
	{
		checkColumn(column);
		if (comparators == null)
		{
			comparators = new Comparator[getModelWrapper().getColumnCount()];
		}
		comparators[column] = comparator;
	}

	private Comparator<?> superGetComparator(int column)
	{
		checkColumn(column);
		if (comparators != null)
		{
			return comparators[column];
		}
		return null;
	}
	
	public Comparator<?> getComparator(int column) {
        Comparator comparator = superGetComparator(column);
        if (comparator != null) {
            return comparator;
        }
        Class columnClass = getModel().getColumnClass(column);
        if (columnClass == String.class) {
            return Collator.getInstance();
        }
        if (Comparable.class.isAssignableFrom(columnClass)) {
            return COMPARABLE_COMPARATOR;
        }
        return Collator.getInstance();
    }

	// Returns the Comparator to use during sorting. Where as
	// getComparator() may return null, this will never return null.
	private Comparator getComparator0(int column)
	{
		Comparator comparator = getComparator(column);
		if (comparator != null)
		{
			return comparator;
		}
		// This should be ok as useToString(column) should have returned
		// true in this case.
		return Collator.getInstance();
	}

	private RowFilter.Entry<M, Integer> getFilterEntry(int modelIndex)
	{
		if (filterEntry == null)
		{
			filterEntry = new FilterEntry();
		}
		filterEntry.modelIndex = modelIndex;
		return filterEntry;
	}

	public int getViewRowCount()
	{
		if (viewToModel != null)
		{
			// When filtering this may differ from
			// getModelWrapper().getRowCount()
			return viewToModel.length;
		}
		return getModelWrapper().getRowCount();
	}

	public int getModelRowCount()
	{
		return getModelWrapper().getRowCount();
	}

	private void allChanged()
	{
		modelToView = null;
		viewToModel = null;
		comparators = null;
		isSortable = null;
		if (isUnsorted())
		{
			// Keys are already empty, to force a resort we have to
			// call sort
			sort();
		} else
		{
			setSortKeys(null);
		}
	}

	public void modelStructureChanged()
	{
		allChanged();
		modelRowCount = getModelWrapper().getRowCount();
	}

	public void allRowsChanged()
	{
		modelRowCount = getModelWrapper().getRowCount();
		sort();
	}

	public void rowsInserted(int firstRow, int endRow)
	{
		checkAgainstModel(firstRow, endRow);
		int newModelRowCount = getModelWrapper().getRowCount();
		if (endRow >= newModelRowCount)
		{
			throw new IndexOutOfBoundsException("Invalid range");
		}
		modelRowCount = newModelRowCount;
		if (shouldOptimizeChange(firstRow, endRow))
		{
			rowsInserted0(firstRow, endRow);
		}
	}

	public void rowsDeleted(int firstRow, int endRow)
	{
		checkAgainstModel(firstRow, endRow);
		if (firstRow >= modelRowCount || endRow >= modelRowCount)
		{
			throw new IndexOutOfBoundsException("Invalid range");
		}
		modelRowCount = getModelWrapper().getRowCount();
		if (shouldOptimizeChange(firstRow, endRow))
		{
			rowsDeleted0(firstRow, endRow);
		}
	}

	public void rowsUpdated(int firstRow, int endRow)
	{
		checkAgainstModel(firstRow, endRow);
		if (firstRow >= modelRowCount || endRow >= modelRowCount)
		{
			throw new IndexOutOfBoundsException("Invalid range");
		}
		if (getSortsOnUpdates())
		{
			if (shouldOptimizeChange(firstRow, endRow))
			{
				rowsUpdated0(firstRow, endRow);
			}
		} else
		{
			sorted = false;
		}
	}

	public void rowsUpdated(int firstRow, int endRow, int column)
	{
		checkColumn(column);
		rowsUpdated(firstRow, endRow);
	}

	private void checkAgainstModel(int firstRow, int endRow)
	{
		if (firstRow > endRow || firstRow < 0 || endRow < 0
				|| firstRow > modelRowCount)
		{
			throw new IndexOutOfBoundsException("Invalid range");
		}
	}

	/**
	 * Returns true if the specified row should be included.
	 */
	private boolean include(int row)
	{
		RowFilter<? super M, Integer> filter = getRowFilter();
		if (filter != null)
		{
			return filter.include(getFilterEntry(row));
		}
		// null filter, always include the row.
		return true;
	}

	@SuppressWarnings("unchecked")
	private int compare(int model1, int model2)
	{
		int column;
		SortOrder sortOrder;
		Object v1, v2;
		int result;

		for (int counter = 0; counter < cachedSortKeys.length; counter++)
		{
			column = cachedSortKeys[counter].getColumn();
			sortOrder = cachedSortKeys[counter].getSortOrder();
			if (sortOrder == SortOrder.UNSORTED)
			{
				result = model1 - model2;
			} else
			{
				// v1 != null && v2 != null
				if (useToString[column])
				{
					v1 = getModelWrapper().getStringValueAt(model1, column);
					v2 = getModelWrapper().getStringValueAt(model2, column);
				} else
				{
					v1 = getModelWrapper().getValueAt(model1, column);
					v2 = getModelWrapper().getValueAt(model2, column);
				}
				// Treat nulls as < then non-null
				if (v1 == null)
				{
					if (v2 == null)
					{
						result = 0;
					} else
					{
						result = isNullBigger ? 1 : -1;
					}
				} else if (v2 == null)
				{
					result = isNullBigger ? -1 : 1;
				} else
				{
					result = sortComparators[counter].compare(v1, v2);
				}
				if (sortOrder == SortOrder.DESCENDING)
				{
					result *= -1;
				}
			}
			if (result != 0)
			{
				return result;
			}
		}
		// If we get here, they're equal. Fallback to model order.
		return model1 - model2;
	}

	/**
	 * Whether not we are filtering/sorting.
	 */
	private boolean isTransformed()
	{
		return (viewToModel != null);
	}

	/**
	 * Insets new set of entries.
	 * 
	 * @param toAdd
	 *            the Rows to add, sorted
	 * @param current
	 *            the array to insert the items into
	 */
	private void insertInOrder(List<Row> toAdd, Row[] current)
	{
		int last = 0;
		int index;
		int max = toAdd.size();
		for (int i = 0; i < max; i++)
		{
			index = Arrays.binarySearch(current, toAdd.get(i));
			if (index < 0)
			{
				index = -1 - index;
			}
			System.arraycopy(current, last, viewToModel, last + i, index - last);
			viewToModel[index + i] = toAdd.get(i);
			last = index;
		}
		System.arraycopy(current, last, viewToModel, last + max, current.length
				- last);
	}

	/**
	 * Returns true if we should try and optimize the processing of the
	 * <code>TableModelEvent</code>. If this returns false, assume the event was
	 * dealt with and no further processing needs to happen.
	 */
	private boolean shouldOptimizeChange(int firstRow, int lastRow)
	{
		if (!isTransformed())
		{
			// Not transformed, nothing to do.
			return false;
		}
		if (!sorted || (lastRow - firstRow) > viewToModel.length / 10)
		{
			// We either weren't sorted, or to much changed, sort it all
			sort();
			return false;
		}
		return true;
	}

	private void rowsInserted0(int firstRow, int lastRow)
	{
		int[] oldViewToModel = getViewToModelAsInts(viewToModel);
		int i;
		int delta = (lastRow - firstRow) + 1;
		List<Row> added = new ArrayList<Row>(delta);

		// Build the list of Rows to add into added
		for (i = firstRow; i <= lastRow; i++)
		{
			if (include(i))
			{
				added.add(new Row(this, i));
			}
		}

		// Adjust the model index of rows after the effected region
		int viewIndex;
		for (i = modelToView.length - 1; i >= firstRow; i--)
		{
			viewIndex = modelToView[i];
			if (viewIndex != -1)
			{
				viewToModel[viewIndex].modelIndex += delta;
			}
		}

		// Insert newly added rows into viewToModel
		if (added.size() > 0)
		{
			Collections.sort(added);
			Row[] lastViewToModel = viewToModel;
			viewToModel = new Row[viewToModel.length + added.size()];
			insertInOrder(added, lastViewToModel);
		}

		// Update modelToView
		createModelToView(getModelWrapper().getRowCount());
		setModelToViewFromViewToModel(true);

		// Notify of change
		fireRowSorterChanged(oldViewToModel);
	}

	private void rowsDeleted0(int firstRow, int lastRow)
	{
		int[] oldViewToModel = getViewToModelAsInts(viewToModel);
		int removedFromView = 0;
		int i;
		int viewIndex;

		// Figure out how many visible rows are going to be effected.
		for (i = firstRow; i <= lastRow; i++)
		{
			viewIndex = modelToView[i];
			if (viewIndex != -1)
			{
				removedFromView++;
				viewToModel[viewIndex] = null;
			}
		}

		// Update the model index of rows after the effected region
		int delta = lastRow - firstRow + 1;
		for (i = modelToView.length - 1; i > lastRow; i--)
		{
			viewIndex = modelToView[i];
			if (viewIndex != -1)
			{
				viewToModel[viewIndex].modelIndex -= delta;
			}
		}

		// Then patch up the viewToModel array
		if (removedFromView > 0)
		{
			Row[] newViewToModel = new Row[viewToModel.length - removedFromView];
			int newIndex = 0;
			int last = 0;
			for (i = 0; i < viewToModel.length; i++)
			{
				if (viewToModel[i] == null)
				{
					System.arraycopy(viewToModel, last, newViewToModel,
							newIndex, i - last);
					newIndex += (i - last);
					last = i + 1;
				}
			}
			System.arraycopy(viewToModel, last, newViewToModel, newIndex,
					viewToModel.length - last);
			viewToModel = newViewToModel;
		}

		// Update the modelToView mapping
		createModelToView(getModelWrapper().getRowCount());
		setModelToViewFromViewToModel(true);

		// And notify of change
		fireRowSorterChanged(oldViewToModel);
	}

	private void rowsUpdated0(int firstRow, int lastRow)
	{
		int[] oldViewToModel = getViewToModelAsInts(viewToModel);
		int i, j;
		int delta = lastRow - firstRow + 1;
		int modelIndex;

		if (getRowFilter() == null)
		{
			// Sorting only:

			// Remove the effected rows
			Row[] updated = new Row[delta];
			for (j = 0, i = firstRow; i <= lastRow; i++, j++)
			{
				updated[j] = viewToModel[modelToView[i]];
			}

			// Sort the update rows
			Arrays.sort(updated);

			// Build the intermediary array: the array of
			// viewToModel without the effected rows.
			Row[] intermediary = new Row[viewToModel.length - delta];
			for (i = 0, j = 0; i < viewToModel.length; i++)
			{
				modelIndex = viewToModel[i].modelIndex;
				if (modelIndex < firstRow || modelIndex > lastRow)
				{
					intermediary[j++] = viewToModel[i];
				}
			}

			// Build the new viewToModel
			insertInOrder(Arrays.asList(updated), intermediary);

			// Update modelToView
			setModelToViewFromViewToModel(false);
		} else
		{
			// Sorting & filtering.

			// Remove the effected rows, adding them to updated and setting
			// modelToView to -2 for any rows that were not filtered out
			List<Row> updated = new ArrayList<Row>(delta);
			int newlyVisible = 0;
			int newlyHidden = 0;
			int effected = 0;
			for (i = firstRow; i <= lastRow; i++)
			{
				if (modelToView[i] == -1)
				{
					// This row was filtered out
					if (include(i))
					{
						// No longer filtered
						updated.add(new Row(this, i));
						newlyVisible++;
					}
				} else
				{
					// This row was visible, make sure it should still be
					// visible.
					if (!include(i))
					{
						newlyHidden++;
					} else
					{
						updated.add(viewToModel[modelToView[i]]);
					}
					modelToView[i] = -2;
					effected++;
				}
			}

			// Sort the updated rows
			Collections.sort(updated);

			// Build the intermediary array: the array of
			// viewToModel without the updated rows.
			Row[] intermediary = new Row[viewToModel.length - effected];
			for (i = 0, j = 0; i < viewToModel.length; i++)
			{
				modelIndex = viewToModel[i].modelIndex;
				if (modelToView[modelIndex] != -2)
				{
					intermediary[j++] = viewToModel[i];
				}
			}

			// Recreate viewToModel, if necessary
			if (newlyVisible != newlyHidden)
			{
				viewToModel = new Row[viewToModel.length + newlyVisible
						- newlyHidden];
			}

			// Rebuild the new viewToModel array
			insertInOrder(updated, intermediary);

			// Update modelToView
			setModelToViewFromViewToModel(true);
		}
		// And finally fire a sort event.
		fireRowSorterChanged(oldViewToModel);
	}

	private void checkColumn(int column)
	{
		if (column < 0 || column >= getModelWrapper().getColumnCount())
		{
			throw new IndexOutOfBoundsException(
					"column beyond range of TableModel");
		}
	}

	/**
	 * RowFilter.Entry implementation that delegates to the ModelWrapper.
	 * getFilterEntry(int) creates the single instance of this that is passed to
	 * the Filter. Only call getFilterEntry(int) to get the instance.
	 */
	private class FilterEntry extends RowFilter.Entry<M, Integer>
	{
		/**
		 * The index into the model, set in getFilterEntry
		 */
		int modelIndex;

		public M getModel()
		{
			return getModelWrapper().getModel();
		}

		public int getValueCount()
		{
			return getModelWrapper().getColumnCount();
		}

		public Object getValue(int index)
		{
			return getModelWrapper().getValueAt(modelIndex, index);
		}

		public String getStringValue(int index)
		{
			return getModelWrapper().getStringValueAt(modelIndex, index);
		}

		public Integer getIdentifier()
		{
			return getModelWrapper().getIdentifier(modelIndex);
		}
	}

	/**
	 * Row is used to handle the actual sorting by way of Comparable. It will
	 * use the sortKeys to do the actual comparison.
	 */
	// NOTE: this class is static so that it can be placed in an array
	private static class Row implements Comparable<Row>
	{
		private JohnTableRowSorter sorter;
		int modelIndex;

		public Row(JohnTableRowSorter sorter, int index)
		{
			this.sorter = sorter;
			modelIndex = index;
		}

		public int compareTo(Row o)
		{
			return sorter.compare(modelIndex, o.modelIndex);
		}
	}
	
	/**
     * Implementation of DefaultRowSorter.ModelWrapper that delegates to a
     * TableModel.
     */
    private class TableRowSorterModelWrapper {
    	
        protected TableRowSorterModelWrapper() {
        }
        
        public M getModel() {
            return tableModel;
        }

        public int getColumnCount() {
            return (tableModel == null) ? 0 : tableModel.getColumnCount();
        }

        public int getRowCount() {
            return (tableModel == null) ? 0 : tableModel.getRowCount();
        }

        public Object getValueAt(int row, int column) {
            return tableModel.getValueAt(row, column);
        }

        public String getStringValueAt(int row, int column) {
            TableStringConverter converter = getStringConverter();
            if (converter != null) {
                // Use the converter
                String value = converter.toString(
                        tableModel, row, column);
                if (value != null) {
                    return value;
                }
                return "";
            }

            // No converter, use getValueAt followed by toString
            Object o = getValueAt(row, column);
            if (o == null) {
                return "";
            }
            String string = o.toString();
            if (string == null) {
                return "";
            }
            return string;
        }

        public Integer getIdentifier(int index) {
            return index;
        }
    }


    private static class ComparableComparator implements Comparator {
        @SuppressWarnings("unchecked")
        public int compare(Object o1, Object o2) {
            return ((Comparable)o1).compareTo(o2);
        }
    }
    
    public boolean isNullBigger()
	{
		return isNullBigger;
	}
    
    public void setNullBigger(boolean isNullBigger)
	{
		this.isNullBigger = isNullBigger;
	}
}
